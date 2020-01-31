package ibzssoft.com.ishidamovile;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

public class VisitaDetalle extends AppCompatActivity {
    private TextView fe,he,bod,trans,precio,vendedor,cedula,nombres,direccion,telefono,mail,cli_obs,obs,subtotal_12,subtotal_0,subtotal_total,total_iva,total_trans,fm,estado,alterno,referenciaTittle,referencia;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion,layoutCeldaNro;
    private CardView contenedorObservacion,contenedorAdicionales;
    private TableLayout tabla;
    private TextView [] descripciones,cantidades,precios,descuentos,totales;
    private Resources resources;
    private int[]iva;
    private static final int LIMITE=100;
    private double PORC_IVA=0.0;
    private int filas=0;
    private int pcgrupo=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/**
 * Inicializar componentes
 */
        inicializarComponentes();
        /***
         * Cargar datos a la vista
         */
        cargarDatos(getIntent().getStringExtra("trans_id"));
        agregarFilasTabla(getIntent().getStringExtra("trans_id"));
        cargarTotales();
    }
    public void inicializarComponentes(){
        PORC_IVA = new Double(getIntent().getStringExtra("impuestos"))/100;
        System.out.println("Porcentaje Detalle: "+PORC_IVA);
        fe = (TextView)findViewById(R.id.infoTransFE);
        he = (TextView)findViewById(R.id.infoTransHE);
        bod= (TextView)findViewById(R.id.infoTransBodega);
        trans= (TextView)findViewById(R.id.infoTransTrans);
        precio= (TextView)findViewById(R.id.infoTransPrecio);
        vendedor= (TextView)findViewById(R.id.infoTransVendedor);
        cedula= (TextView)findViewById(R.id.infoTransCedula);
        nombres= (TextView)findViewById(R.id.infoTransNombres);
        direccion= (TextView)findViewById(R.id.infoTransDireccion);
        telefono= (TextView)findViewById(R.id.infoTransTelefono);
        mail= (TextView)findViewById(R.id.infoTransCorreo);
        cli_obs= (TextView)findViewById(R.id.infoTransObservation);
        alterno= (TextView)findViewById(R.id.infoTransAlterno);
        obs= (TextView)findViewById(R.id.infoTransObs);
        fm= (TextView)findViewById(R.id.infoTransFM);
        estado= (TextView)findViewById(R.id.infoTransEstado);
        subtotal_0= (TextView)findViewById(R.id.infoTransSub0);
        subtotal_total= (TextView)findViewById(R.id.infoTransSubtotal);
        total_iva= (TextView)findViewById(R.id.infoTransIVA);
        total_trans= (TextView)findViewById(R.id.infoTransTotal);
        contenedorObservacion=(CardView)findViewById(R.id.infoTransObservaciones);
        contenedorAdicionales=(CardView)findViewById(R.id.contenedorAdicionales);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,40,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaNro= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.infoTransTableLayout);
        cantidades=new TextView[LIMITE];descripciones=new TextView[LIMITE];descuentos=new TextView[LIMITE];totales=new TextView[LIMITE];precios=new TextView[LIMITE];iva=new int[LIMITE];
        subtotal_12=(TextView)findViewById(R.id.infoTransSub12);
        referenciaTittle = (TextView) findViewById(R.id.formItemTxt84t);
        referencia = (TextView) findViewById(R.id.infoTransReferencia);
        resources=this.getResources();
    }

    public void cargarDatos(String id_trans){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.obtenerTransaccion(id_trans);
        if(cursor.moveToFirst()){
            cedula.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)));
            nombres.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)));
            alterno.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)));
            direccion.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion1)));
            telefono.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono1)));
            mail.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_email)));
            cli_obs.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_observacion)));
            //Date fecha = new ParseDates().changeStringToDateSimpleFormat(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            fe.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            he.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)));
            vendedor.setText(cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
            fm.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado)));
            if(cedula.getText().toString().equals("9999999999999"))mostrarContenedorAdicionales();
            if(cursor.getInt(cursor.getColumnIndex(Transaccion.FIELD_band_enviado))!=0){
                estado.setText("Enviado");
                estado.setTextColor(getResources().getColor(R.color.successfull));
                referencia.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_referencia)));
            }else{
                referenciaTittle.setVisibility(View.GONE);
                referencia.setVisibility(View.GONE);
            }
            Cursor cursor1=helper.consultarGNTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            if(cursor1.moveToFirst()){
                Cursor cursor3=helper.consultarBodega(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                if(cursor3.moveToFirst()){
                    bod.setText(cursor3.getString(cursor3.getColumnIndex(Bodega.FIELD_codbodega)));
                }
                cursor3.close();
                trans.setText(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_codtrans)));
                cargarObservaciones(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_descripcion)));
                    /*Configuracion de observaciones y precios*/
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(VisitaDetalle.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                System.out.println("Configuracion obserbaciones: "+conf_observacion);
                if(conf_observacion)mostrarContenedorObservaciones();
                if(!cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")){
                    precio.setText(R.string.info_precio_pcgrupo_disable);
                    pcgrupo=0;
                }else{
                    precio.setText(R.string.info_precio_pcgrupo_enabled);;
                    pcgrupo=1;
                    cargarPrecio(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovcli)),ext.configuracionPreciosGrupos() );
                }
            }
            cursor1.close();
        }
        cursor.close();
        helper.close();
    }

    public void cargarObservaciones(String datos){
        //observacion,solitante,tiempo entrega,forma,validez,atencion
        System.out.println("Observaciones Cargadas: "+datos);
        String data[]=datos.split(";");
        try{
            if(data[0].isEmpty()!=true) obs.setText(data[0]);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void mostrarContenedorObservaciones(){
        if(contenedorObservacion.getVisibility()==View.GONE){
            animar(false,contenedorObservacion);
            contenedorObservacion.setVisibility(View.VISIBLE);
        }
    }
    public void mostrarContenedorAdicionales(){
        if(contenedorAdicionales.getVisibility()==View.GONE){
            animar(false,contenedorAdicionales);
            contenedorAdicionales.setVisibility(View.VISIBLE);
        }
    }

    private void animar(boolean mostrar,CardView layout)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        }
        else
        {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layout.setLayoutAnimation(controller);
        layout.startAnimation(animation);
    }

    /***
     * Agregar filas al resumen
     */
    public void agregarFilasTabla(String id_trans){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarIVKardex(id_trans);
        filas=cursor.getCount();

        int pst=0;
        if(cursor.moveToFirst()){
            do{

                TableRow fila= new TableRow(this);
                fila.setLayoutParams(layoutFila);
                if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));
                if(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id))!=null){
                    fila.setBackgroundColor(getResources().getColor(R.color.colorInfo));
                }
                layoutCeldaNro.span=1;
                TextView nro = new TextView(this);
                nro.setTextSize(14);
                nro.setText(String.valueOf(pst+1));
                nro.setTextColor(getResources().getColor(R.color.textColorPrimary));
                nro.setLayoutParams(layoutCeldaNro);
                nro.setGravity(Gravity.CENTER);
                nro.setPadding(5, 5, 5, 5);

                layoutCeldaDescripcion.span=5;
                descripciones[pst]= new TextView(this);
                descripciones[pst].setEnabled(false);
                descripciones[pst].setFocusable(false);
                descripciones[pst].setTextSize(14);
                descripciones[pst].setMaxLines(1);
                descripciones[pst].setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                descripciones[pst].setGravity(Gravity.LEFT);
                descripciones[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                descripciones[pst].setLayoutParams(layoutCeldaDescripcion);
                descripciones[pst].setPadding(5, 5, 5, 5);


                cantidades[pst]= new TextView(this);
                cantidades[pst].setTextSize(14);
                cantidades[pst].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_cantidad)));
                cantidades[pst].setEnabled(false);
                cantidades[pst].setFocusable(false);
                cantidades[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                cantidades[pst].setLayoutParams(layoutCeldas);
                cantidades[pst].setGravity(Gravity.CENTER);
                cantidades[pst].setPadding(5, 5, 5, 5);

                precios[pst]= new TextView(this);
                precios[pst].setTextSize(14);
                precios[pst].setFocusable(false);
                precios[pst].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total)));
                precios[pst].setEnabled(false);
                precios[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                precios[pst].setLayoutParams(layoutCeldas);
                precios[pst].setGravity(Gravity.CENTER);
                precios[pst].setPadding(5, 5, 5, 5);

                descuentos[pst]= new TextView(this);
                descuentos[pst].setFocusable(false);
                descuentos[pst].setTextSize(14);
                descuentos[pst].setEnabled(false);
                descuentos[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                descuentos[pst].setLayoutParams(layoutCeldas);
                descuentos[pst].setGravity(Gravity.CENTER);
                descuentos[pst].setPadding(5, 5, 5, 5);
                descuentos[pst].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_descuento)));

                totales[pst]= new TextView(this);
                totales[pst].setTextSize(14);
                totales[pst].setFocusable(false);
                totales[pst].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total))*cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_cantidad))));
                totales[pst].setEnabled(false);
                totales[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                totales[pst].setLayoutParams(layoutCeldas);
                totales[pst].setGravity(Gravity.CENTER);
                totales[pst].setPadding(5, 5, 5, 5);

                iva[pst]=cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva));


//                fila.setPadding(5, 5, 5, 5);

                fila.addView(nro);
                fila.addView(descripciones[pst]);
                fila.addView(cantidades[pst]);
                fila.addView(precios[pst]);
                fila.addView(descuentos[pst]);
                fila.addView(totales[pst]);
                tabla.addView(fila);
                pst++;
            }while (cursor.moveToNext());
        }
    }

    /*public double cargarPreciosSeleccion(String id_item, int num_precio){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarItem(id_item);
        double result=0.0;
        if(cursor.moveToFirst()){
            switch (num_precio){
                case 0:
                    result = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1));
                    break;
                case 1:
                    result = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2));
                    break;
                case 2:
                    result = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3));
                    break;
                case 3:
                    result = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4));
                    break;
                default:
                    break;
            }
        }
        helper.close();
        return result;
    }*/

    public void cargarTotales(){
        double subtotal12=0.0;
        double subtotal0=0.0;
        double impuestos=0.0;
        double subtotal=0.0;
        double total=0.0;

        for(int i=0; i<filas; i++){
            if(iva[i]!=0){
                subtotal12+=Double.parseDouble(totales[i].getText().toString());
            }else{
                subtotal0+=Double.parseDouble(totales[i].getText().toString());
            }
        }
        subtotal_12.setText(redondearNumero(subtotal12));
        subtotal_0.setText(redondearNumero(subtotal0));
        subtotal=(subtotal12+subtotal0);
        impuestos=(subtotal12*PORC_IVA);
        total=(subtotal0+subtotal12+impuestos);
        subtotal_total.setText(redondearNumero(subtotal));
        total_iva.setText(redondearNumero(impuestos));
        total_trans.setText(redondearNumero(total));
    }
    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }

    /*Cargar precio por PCGRUPO*/
    public boolean cargarPrecio(String cliente, int numgrupo) {
        try {
            DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
            Cursor cursor1 = helper.consultarPCGrupo(numgrupo, cliente);
            if (cursor1.moveToFirst()) {
                System.out.println("Precio a ser cargado: " + cursor1.getString(1));
                precio.setText(obtenerNumeroPrecio(cursor1.getString(1)));
            } else {
                Toast ts = Toast.makeText(getApplicationContext(), R.string.info_no_load_price, Toast.LENGTH_SHORT);
                ts.show();
            }
            helper.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String obtenerNumeroPrecio(String inf_price) {
        String price = "";
        for (int i = 0; i < inf_price.length(); i++) {
            System.out.println("Cadenas comparadas: " + inf_price.substring(i, i + 1));
            if (inf_price.substring(i, i + 1).equals("1")) {
                price += String.valueOf(i + 1) + ",";
            }
        }
        price = price.substring(0, price.length() - 1);
        System.out.println("Resultado final: " + price);
        return price;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VisitaDetalle.this, MainActivity.class);
        intent.putExtra("posicion", getIntent().getIntExtra("posicion", 0));
        startActivity(intent);
        finish();
    }

}
