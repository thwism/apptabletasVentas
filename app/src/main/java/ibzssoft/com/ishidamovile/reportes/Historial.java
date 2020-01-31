package ibzssoft.com.ishidamovile.reportes;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Detalle;
import ibzssoft.com.storage.DBSistemaGestion;

public class Historial extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener
{
    private TextView date1;
    private TextView date2;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private TextView cedula,nombres,alterno;
    private ArrayList<Detalle> items = new ArrayList<Detalle>();
    private static final int LIMIT = 200;
    private TextView [] codigo,descripciones,cant,transacciones,fechas,can_total,precios;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion;
    private TableLayout tabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        System.out.println("Cliente recibido: "+getIntent().getStringExtra("cliente"));
        cedula = (TextView)findViewById(R.id.historialCedula);
        nombres = (TextView)findViewById(R.id.historialNombres);
        alterno = (TextView)findViewById(R.id.historialAlterno);
        cargarDatos(getIntent().getStringExtra("cliente"));
        codigo = new TextView[LIMIT];descripciones = new TextView[LIMIT];cant= new TextView[LIMIT];
        transacciones= new TextView[LIMIT];fechas= new TextView[LIMIT];
        can_total= new TextView[LIMIT];precios= new TextView[LIMIT];
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.historialTableLayout);
        date1 = (TextView)findViewById(R.id.fecha_inicio);
        date2 = (TextView)findViewById(R.id.fecha_fin);
        loadDates();
        Button buscar = (Button)findViewById(R.id.date_button);
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Historial.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setTitle("Fecha Inicial");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Historial.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setTitle("Fecha Final");
                dpd.show(getFragmentManager(), "Datepickerdialog2");
            }
        });
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarFechaMenor(date1.getText().toString(),date2.getText().toString())){
                    cargarPreferenciasConexion();
                    RecibirHistorialTask recibirHistorialTask =new RecibirHistorialTask();
                    recibirHistorialTask.execute(ip,port,url,ws,date1.getText().toString(),date2.getText().toString(),getIntent().getStringExtra("cliente"));
                }
            }
        });
    }

    public void cargarDatos(String cliente){
        try{
            DBSistemaGestion helper = new DBSistemaGestion(this);
            Cursor cursor = helper.obtenerCliente(cliente);
            if(cursor.moveToFirst()){
                cedula.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)));
                nombres.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)));
                alterno.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)));
            }
            cursor.close();
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(Historial.this);
        ip=extraerConfiguraciones.get(getString(R.string.key_conf_ip),getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(getString(R.string.key_conf_port),getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(getString(R.string.key_conf_url),getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(getString(R.string.key_ws_historial),getString(R.string.pref_ws_historial));
    }

    public boolean validarFechaMenor(String fecha_inicial,String fecha_final){
        try{
            Date fecha_fin = new ParseDates().changeStringToDateSimpleFormat1(fecha_final);
            Date fecha_inicio= new ParseDates().changeStringToDateSimpleFormat1(fecha_inicial);
            if(fecha_fin.before(fecha_inicio)){
                new AlertDialog.Builder(this).setTitle("Advertencia")
                        .setMessage("El rango de fechas para la busqueda se encuentra mal establecido")
                        .setNeutralButton("Cerrar", null).show();
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onResume(){
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        DatePickerDialog dpd2 = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog2");
        if(dpd != null) dpd.setOnDateSetListener(this);
        if(dpd2 != null) dpd2.setOnDateSetListener(this);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        if(view.getTag().equals("Datepickerdialog")){
            date1.setText(date);
        }else if(view.getTag().equals("Datepickerdialog2")){
            date2.setText(date);
        }
    }

    public ArrayList<Detalle> ordenarLista(final ArrayList<Detalle> detalles, int campo){
        switch (campo){
            case 1:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return lhs.getDescripcion().compareTo(rhs.getDescripcion());
                    }
                });
                break;
            case 2:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return Double.compare(lhs.getCantidad(),rhs.getCantidad());
                    }
                });
                break;
            case 3:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return lhs.getTransaccion().compareTo(rhs.getTransaccion());
                    }
                });
                break;
            case 4:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return new ParseDates().changeStringToDateSimpleFormat2(lhs.getFecha()).compareTo(new ParseDates().changeStringToDateSimpleFormat2(rhs.getFecha()));
                    }
                });
                break;
            case 5:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return Double.compare(lhs.getCantidad_total(),rhs.getCantidad_total());
                    }
                });
                break;
            case 6:
                Collections.sort(detalles, new Comparator<Detalle>() {
                    @Override
                    public int compare(Detalle lhs, Detalle rhs) {
                        return Double.compare(lhs.getPrecio(),rhs.getPrecio());
                    }
                });
                break;
            default:
                break;
        }
        return detalles;
    }
    public void encerarArreglos(){
        for(int i=0;i<LIMIT;i++){
            codigo[i]=null;descripciones[i]=null;cant[i]=null;
            transacciones[i]=null;fechas[i]=null;
            can_total[i]=null;precios[i]=null;
        }
        this.cleanTable(tabla);
    }
    private void cleanTable(TableLayout table) {
        int childCount = table.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }


    public void agregarFilasTabla(ArrayList<Detalle> detalles){
        encerarArreglos();
        int pst=0;
        TableRow fila;
        if(!detalles.isEmpty()){
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);
                if ( pst % 2 == 0) fila.setBackgroundColor(this.getResources().getColor(R.color.dividerColor));
                layoutCeldas.span = 1;
                codigo[pst]= new TextView(this);
                codigo[pst].setTextSize(12);
                codigo[pst].setMaxLines(1);
                codigo[pst].setText(detalles.get(pst).getCodigo());
                codigo[pst].setEnabled(false);
                codigo[pst].setFocusable(false);
                codigo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                codigo[pst].setLayoutParams(layoutCeldas);
                codigo[pst].setGravity(Gravity.LEFT);
                codigo[pst].setPadding(5, 5, 5, 5);

                layoutCeldaDescripcion.span=2;
                descripciones[pst]= new TextView(this);
                descripciones[pst].setEnabled(false);
                descripciones[pst].setFocusable(false);
                descripciones[pst].setTextSize(12);
                descripciones[pst].setText(detalles.get(pst).getDescripcion());
                descripciones[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                descripciones[pst].setLayoutParams(layoutCeldaDescripcion);
                descripciones[pst].setGravity(Gravity.LEFT);
                descripciones[pst].setPadding(5, 5, 5, 5);

                layoutCeldas.span = 1;
                cant[pst]= new TextView(this);
                cant[pst].setTextSize(12);
                cant[pst].setText(String.valueOf(detalles.get(pst).getCantidad()));
                cant[pst].setEnabled(false);
                cant[pst].setFocusable(false);
                cant[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                cant[pst].setLayoutParams(layoutCeldas);
                cant[pst].setGravity(Gravity.CENTER);
                cant[pst].setPadding(5, 5, 5, 5);


                transacciones[pst]= new TextView(this);
                transacciones[pst].setTextSize(12);
                transacciones[pst].setMaxLines(1);
                transacciones[pst].setFocusable(false);
                transacciones[pst].setEnabled(false);
                transacciones[pst].setText(detalles.get(pst).getTransaccion());
                transacciones[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                transacciones[pst].setLayoutParams(layoutCeldas);
                transacciones[pst].setGravity(Gravity.CENTER);
                transacciones[pst].setPadding(5, 5, 5, 5);

                fechas[pst]= new TextView(this);
                fechas[pst].setTextSize(12);
                fechas[pst].setFocusable(false);
                fechas[pst].setText(new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimpleFormat(detalles.get(pst).getFecha())));
                fechas[pst].setEnabled(false);
                fechas[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                fechas[pst].setLayoutParams(layoutCeldas);
                fechas[pst].setGravity(Gravity.CENTER);
                fechas[pst].setPadding(5, 5, 5, 5);

                can_total[pst]= new TextView(this);
                can_total[pst].setTextSize(12);
                can_total[pst].setFocusable(false);
                can_total[pst].setText(String.valueOf(detalles.get(pst).getCantidad_total()));
                can_total[pst].setEnabled(false);
                can_total[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                can_total[pst].setLayoutParams(layoutCeldas);
                can_total[pst].setGravity(Gravity.CENTER);
                can_total[pst].setPadding(5, 5, 5, 5);

                precios[pst]= new TextView(this);
                precios[pst].setTextSize(12);
                precios[pst].setFocusable(false);
                precios[pst].setText(String.valueOf(detalles.get(pst).getPrecio()));
                precios[pst].setEnabled(false);
                precios[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                precios[pst].setLayoutParams(layoutCeldas);
                precios[pst].setGravity(Gravity.CENTER);
                precios[pst].setPadding(5, 5, 5, 5);

                fila.setPadding(2,0, 2, 0);
                fila.addView(codigo[pst]);
                fila.addView(descripciones[pst]);
                fila.addView(cant[pst]);
                fila.addView(transacciones[pst]);
                fila.addView(fechas[pst]);
                fila.addView(precios[pst]);
                fila.addView(can_total[pst]);
                tabla.addView(fila);
                pst++;
            }while (pst<detalles.size());
        }else{
            System.out.println("Tama;o de la tabla: "+detalles.size());
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);
            TextView info= new TextView(this);
            info.setText("NO HAY RESULTADOS");
            info.setLayoutParams(layoutCeldas);
            info.setGravity(Gravity.CENTER);
            fila.setPadding(10, 20, 10, 20);
            fila.addView(info);
            tabla.addView(fila);
        }
    }

    public void agregarFilaVacia(){
        encerarArreglos();
        layoutCeldas.span=5;
        TableRow fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        TextView info= new TextView(this);
        info.setText("NO HAY RESULTADOS");
        info.setLayoutParams(layoutCeldas);
        info.setGravity(Gravity.CENTER);
        fila.addView(info);
        tabla.addView(fila);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    //cargar fechas por defecto
    public void loadDates(){
        date2.setText(new ParseDates().changeDateToStringSimple1(new Date()));
        date1.setText(new ParseDates().changeDateToStringSimple1(new ParseDates().sumarRestarDiasYear(new Date(),-1)));
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                        onBackPressed();
                    return true;
                case R.id.setting_desc:
                    ordenarLista(items,1);
                    agregarFilasTabla(items);
                    Toast ts = Toast.makeText(Historial.this, "Ordenando por descripcion", Toast.LENGTH_LONG);
                    ts.show();
                    return true;

                case R.id.setting_cant:
                    ordenarLista(items,2);
                    agregarFilasTabla(items);
                        Toast ts2 = Toast.makeText(Historial.this, "Ordenando por cantidad", Toast.LENGTH_LONG);
                        ts2.show();
                    return true;
                case R.id.setting_NTrans:
                    ordenarLista(items,3);
                    agregarFilasTabla(items);
                    Toast ts3 = Toast.makeText(Historial.this, "Ordenando por transaccion", Toast.LENGTH_LONG);
                    ts3.show();
                    return true;
                case R.id.setting_fecha:
                    ordenarLista(items,4);
                    agregarFilasTabla(items);
                    Toast ts4 = Toast.makeText(Historial.this, "Ordenando por fecha", Toast.LENGTH_LONG);
                    ts4.show();
                    return true;
                case R.id.setting_CTot:
                    ordenarLista(items,5);
                    agregarFilasTabla(items);
                    Toast ts5 = Toast.makeText(Historial.this, "Ordenando por cantidad total", Toast.LENGTH_LONG);
                    ts5.show();
                    return true;
                case R.id.setting_PU:
                    ordenarLista(items,6);
                    agregarFilasTabla(items);
                    Toast ts6 = Toast.makeText(Historial.this, "Ordenando por precio unitario", Toast.LENGTH_LONG);
                    ts6.show();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    //Clase para descargar informacion de compras
    private class RecibirHistorialTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(Historial.this);
            progress.setTitle("Descargando Historial");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            String ip_param = params[0];
            String port_param = params[1];
            String url_param = params[2];
            String ws_param = params[3];
            String fecha1 = params[4].replace("/","-");
            String fecha2 = params[5].replace("/","-");
            String client = params[6];
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            System.out.println("Consulta historial: "+"http://" +ip_param+ ":" + port_param+ url_param+ ws_param +"/"+client+"/"+ fecha1 + "/" + fecha2);
            HttpGet del = new HttpGet("http://" +ip_param+ ":" + port_param+ url_param+ ws_param +"/"+client+"/"+ fecha1 + "/" + fecha2);
            del.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson = new Gson();
                items.clear();
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Detalle per = gson.fromJson(obj.toString(), Detalle.class);
                    items.add(per);
                }
                Thread.sleep(100);
                result = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                result =false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (progress.isShowing()) {
                progress.dismiss();
                if(s){
                    if(items.size()>0){
                        agregarFilasTabla(items);
                    }else{
                        agregarFilaVacia();
                    }
                }else{
                    Toast ts = Toast.makeText(Historial.this, "No se puede descargar el historial", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }
    }
}