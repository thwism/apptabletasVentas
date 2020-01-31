package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Detalle;
import ibzssoft.com.modelo.Promedio;
import ibzssoft.com.storage.DBSistemaGestion;

public class ReportePromedios extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener{

    private TextView date1;
    private TextView date2;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private TextView cedula,nombres,alterno;
    private ArrayList<Promedio> items = new ArrayList<Promedio>();
    private static final int LIMIT = 500;
    private TextView [] transaccion,emision,plazo,vencimiento,valor,pago,saldo,mora,credito;
    private TableRow.LayoutParams layoutFila,layoutCeldas;
    private TableLayout tabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_promedios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        System.out.println("Cliente recibido: "+getIntent().getStringExtra("cliente"));
        cedula = (TextView)findViewById(R.id.promedioCedula);
        nombres = (TextView)findViewById(R.id.promedioNombres);
        alterno = (TextView)findViewById(R.id.promedioAlterno);
        cargarDatos(getIntent().getStringExtra("cliente"));
        transaccion = new TextView[LIMIT];emision= new TextView[LIMIT];plazo= new TextView[LIMIT];
        vencimiento= new TextView[LIMIT];valor= new TextView[LIMIT];
        pago= new TextView[LIMIT];saldo= new TextView[LIMIT];mora= new TextView[LIMIT];credito= new TextView[LIMIT];
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.promedioTableLayout);
        date1 = (TextView)findViewById(R.id.prom_fecha_inicio);
        date2 = (TextView)findViewById(R.id.prom_fecha_fin);
        loadDates();
        Button buscar = (Button)findViewById(R.id.prom_date_button);
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReportePromedios.this,
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
                        ReportePromedios.this,
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
                    RecibirPromediosTask recibirPromediosTask= new RecibirPromediosTask();
                    recibirPromediosTask.execute(ip,port,url,ws,date1.getText().toString(),date2.getText().toString(),getIntent().getStringExtra("cliente"));
                    //llamar funcion asincrona
                }
            }
        });
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(ReportePromedios.this);
        ip=extraerConfiguraciones.get(getString(R.string.key_conf_ip),getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(getString(R.string.key_conf_port),getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(getString(R.string.key_conf_url),getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(getString(R.string.key_ws_promedios),getString(R.string.pref_ws_promedios));
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
    //cargar fechas por defecto
    public void loadDates(){
        date2.setText(new ParseDates().changeDateToStringSimple1(new Date()));
        date1.setText(new ParseDates().changeDateToStringSimple1(new ParseDates().sumarRestarDiasYear(new Date(),-1)));
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
    public void encerarArreglos(){
        for(int i=0;i<LIMIT;i++){
            transaccion[i]=null;emision[i]=null;plazo[i]=null;
            vencimiento[i]=null;valor[i]=null;
            pago[i]=null;saldo[i]=null;
            mora[i]=null;credito[i]=null;
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
    public ArrayList<Promedio> ordenarLista(final ArrayList<Promedio> detalles, int campo){
        switch (campo){
            case 0:
                Collections.sort(detalles, new Comparator<Promedio>() {
                    @Override
                    public int compare(Promedio lhs, Promedio rhs) {
                        return lhs.getTransaccion().compareTo(rhs.getTransaccion());
                    }
                });
                break;
            case 1:
                Collections.sort(detalles, new Comparator<Promedio>() {
                    @Override
                    public int compare(Promedio lhs, Promedio rhs) {
                        return new ParseDates().changeStringToDateSimpleFormat2(rhs.getFecha_emision()).compareTo(new ParseDates().changeStringToDateSimpleFormat2(lhs.getFecha_emision()));
                    }
                });
                break;
            case 2:
                Collections.sort(detalles, new Comparator<Promedio>() {
                    @Override
                    public int compare(Promedio lhs, Promedio rhs) {
                        return new ParseDates().changeStringToDateSimpleFormat2(rhs.getFecha_vencimiento()).compareTo(new ParseDates().changeStringToDateSimpleFormat2(lhs.getFecha_vencimiento()));
                    }
                });
                break;
            case 3:
                Collections.sort(detalles, new Comparator<Promedio>() {
                    @Override
                    public int compare(Promedio lhs, Promedio rhs) {
                        return new ParseDates().changeStringToDateSimpleFormat2(rhs.getFecha_pago()).compareTo(new ParseDates().changeStringToDateSimpleFormat2(lhs.getFecha_pago()));
                    }
                });
                break;
            default:
                break;
        }
        return detalles;
    }
    public void agregarFilaVacia(){
        encerarArreglos();
        layoutCeldas.span=5;//***-
        TableRow fila = new TableRow(this);
        fila.setLayoutParams(layoutFila);
        TextView info= new TextView(this);
        info.setText("NO HAY RESULTADOS");
        info.setLayoutParams(layoutCeldas);
        info.setGravity(Gravity.CENTER);
        fila.addView(info);
        tabla.addView(fila);
    }
    public void agregarFilasTabla(ArrayList<Promedio> promedios){
        encerarArreglos();
        int pst=0;
        TableRow fila;
        if(!promedios.isEmpty()){
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);
                if ( pst % 2 == 0) fila.setBackgroundColor(this.getResources().getColor(R.color.dividerColor));
                layoutCeldas.span = 1;

                TextView nro = new TextView(this);
                nro.setTextSize(12);
                nro.setText(String.valueOf(pst+1));
                nro.setTextColor(getResources().getColor(R.color.textColorPrimary));
                nro.setLayoutParams(layoutCeldas);
                nro.setGravity(Gravity.CENTER);
                nro.setPadding(5, 5, 5, 5);

                transaccion[pst]= new TextView(this);
                transaccion[pst].setTextSize(12);
                transaccion[pst].setMaxLines(1);
                transaccion[pst].setText(promedios.get(pst).getTransaccion());
                transaccion[pst].setEnabled(false);
                transaccion[pst].setFocusable(false);
                transaccion[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                transaccion[pst].setLayoutParams(layoutCeldas);
                transaccion[pst].setGravity(Gravity.CENTER);
                transaccion[pst].setPadding(5, 5, 5, 5);


                emision[pst]= new TextView(this);
                emision[pst].setEnabled(false);
                emision[pst].setFocusable(false);
                emision[pst].setTextSize(12);
                emision[pst].setText(new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimpleFormat(promedios.get(pst).getFecha_emision())));
                emision[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                emision[pst].setLayoutParams(layoutCeldas);
                emision[pst].setGravity(Gravity.LEFT);
                emision[pst].setPadding(5, 5, 5, 5);

                layoutCeldas.span = 1;
                plazo[pst]= new TextView(this);
                plazo[pst].setTextSize(12);
                plazo[pst].setText(String.valueOf(promedios.get(pst).getPlazo()));
                plazo[pst].setEnabled(false);
                plazo[pst].setFocusable(false);
                plazo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                plazo[pst].setLayoutParams(layoutCeldas);
                plazo[pst].setGravity(Gravity.CENTER);
                plazo[pst].setPadding(5, 5, 5, 5);


                vencimiento[pst]= new TextView(this);
                vencimiento[pst].setTextSize(12);
                vencimiento[pst].setMaxLines(1);
                vencimiento[pst].setFocusable(false);
                vencimiento[pst].setEnabled(false);
                vencimiento[pst].setText(new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimpleFormat(promedios.get(pst).getFecha_vencimiento())));
                vencimiento[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                vencimiento[pst].setLayoutParams(layoutCeldas);
                vencimiento[pst].setGravity(Gravity.CENTER);
                vencimiento[pst].setPadding(5, 5, 5, 5);

                valor[pst]= new TextView(this);
                valor[pst].setTextSize(12);
                valor[pst].setFocusable(false);
                valor[pst].setText(String.valueOf(promedios.get(pst).getValor()));
                valor[pst].setEnabled(false);
                valor[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                valor[pst].setLayoutParams(layoutCeldas);
                valor[pst].setGravity(Gravity.CENTER);
                valor[pst].setPadding(5, 5, 5, 5);

                pago[pst]= new TextView(this);
                pago[pst].setTextSize(12);
                pago[pst].setFocusable(false);
                if(promedios.get(pst).getFecha_pago()!=null){
                    pago[pst].setText(new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimpleFormat(promedios.get(pst).getFecha_pago())));
                }
                pago[pst].setEnabled(false);
                pago[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                pago[pst].setLayoutParams(layoutCeldas);
                pago[pst].setGravity(Gravity.CENTER);
                pago[pst].setPadding(5, 5, 5, 5);

                saldo[pst]= new TextView(this);
                saldo[pst].setTextSize(12);
                saldo[pst].setFocusable(false);
                saldo[pst].setText(String.valueOf(promedios.get(pst).getSaldo()));
                saldo[pst].setEnabled(false);
                saldo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                saldo[pst].setLayoutParams(layoutCeldas);
                saldo[pst].setGravity(Gravity.CENTER);
                saldo[pst].setPadding(5, 5, 5, 5);

                mora[pst]= new TextView(this);
                mora[pst].setTextSize(12);
                mora[pst].setFocusable(false);
                mora[pst].setText(String.valueOf(promedios.get(pst).getDias_mora()));
                mora[pst].setEnabled(false);
                mora[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                mora[pst].setLayoutParams(layoutCeldas);
                mora[pst].setGravity(Gravity.CENTER);
                mora[pst].setPadding(5, 5, 5, 5);

                credito[pst]= new TextView(this);
                credito[pst].setTextSize(12);
                credito[pst].setFocusable(false);
                credito[pst].setText(String.valueOf(promedios.get(pst).getDias_credito()));
                credito[pst].setEnabled(false);
                credito[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                credito[pst].setLayoutParams(layoutCeldas);
                credito[pst].setGravity(Gravity.CENTER);
                credito[pst].setPadding(5, 5, 5, 5);

                fila.setPadding(2,0, 2, 0);
                fila.addView(nro);
                fila.addView(transaccion[pst]);
                fila.addView(emision[pst]);
                fila.addView(plazo[pst]);
                fila.addView(vencimiento[pst]);
                fila.addView(valor[pst]);
                fila.addView(pago[pst]);
                fila.addView(saldo[pst]);
                fila.addView(mora[pst]);
                fila.addView(credito[pst]);
                tabla.addView(fila);
                pst++;
            }while (pst<promedios.size());
        }else{
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);
            TextView info= new TextView(this);
            info.setText("NO HAY RESULTADOS");
            info.setLayoutParams(layoutCeldas);
            info.setGravity(Gravity.CENTER);
            fila.addView(info);
            tabla.addView(fila);
        }
    }
/*
tarea asincrona recibir promedios
 */
private class RecibirPromediosTask extends AsyncTask<String,Integer,Boolean> {
    private ProgressDialog progress;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress=new ProgressDialog(ReportePromedios.this);
        progress.setTitle("Descargando Promedio de Cobros");
        progress.setMessage("Espere...");
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        String ip_param=params[0];
        String port_param=params[1];
        String url_param=params[2];
        String ws_param=params[3];
        String fecha1 = params[4].replace("/","-");
        String fecha2 = params[5].replace("/","-");
        String cliente = params[6];
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 9000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 9000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet del = new HttpGet("http://" + ip_param + ":" + port_param + url_param +ws_param +"/" + cliente+ "/" + fecha1 + "/" + fecha2);
        del.setHeader("content-type", "application/json");
        try
        {
            HttpResponse resp = httpClient.execute(del);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);
            Gson gson= new Gson();
            items.clear();
            for(int i=0; i<respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                Promedio per=gson.fromJson(obj.toString(), Promedio.class);
                items.add(per);
                System.out.println("Promedio Recibido: "+per.toString());
            }
            Thread.sleep(100);
            result = true;
        }
        catch(Exception ex)
        {
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
                Toast ts = Toast.makeText(ReportePromedios.this, "No se puede descargar la informacion", Toast.LENGTH_LONG);
                ts.show();
            }
        }
    }
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.setting_trans:
                ordenarLista(items,0);
                agregarFilasTabla(items);
                Toast ts0 = Toast.makeText(ReportePromedios.this, "Ordenando por fecha de emision", Toast.LENGTH_LONG);
                ts0.show();
                return true;
            case R.id.setting_fe:
                ordenarLista(items,1);
                agregarFilasTabla(items);
                Toast ts = Toast.makeText(ReportePromedios.this, "Ordenando por fecha de emision", Toast.LENGTH_LONG);
                ts.show();
                return true;

            case R.id.setting_fv:
                ordenarLista(items,2);
                agregarFilasTabla(items);
                Toast ts2 = Toast.makeText(ReportePromedios.this, "Ordenando por fecha de vencimiento", Toast.LENGTH_LONG);
                ts2.show();
                return true;
            case R.id.setting_fp:
                ordenarLista(items,3);
                agregarFilasTabla(items);
                Toast ts3 = Toast.makeText(ReportePromedios.this, "Ordenando por fecha de pago", Toast.LENGTH_LONG);
                ts3.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_promedios, menu);
        return true;
    }
}
