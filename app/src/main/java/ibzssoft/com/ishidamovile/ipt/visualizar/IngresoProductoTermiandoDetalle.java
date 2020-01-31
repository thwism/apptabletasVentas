package ibzssoft.com.ishidamovile.ipt.visualizar;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
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
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

public class IngresoProductoTermiandoDetalle extends AppCompatActivity {
    private TextView fe,he,bod,trans,precio,vendedor,obs,fm,estado,referenciaTittle,referencia;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion,layoutCeldaNro;
    private CardView contenedorObservacion,contenedorAdicionales;
    private TableLayout tabla;
    private TextView [] descripciones,cantidades;
    private Resources resources;
    private static final int LIMITE=100;
    private String  cli_id;
    private int decimales;
    private boolean conf_observacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ing_pro_term_detalle);
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
        this.cargarPreferencias();
        this.cargarDatos(getIntent().getStringExtra("transid"));
        this.agregarFilasTabla(getIntent().getStringExtra("transid"));
    }
    public void cargarPreferencias(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(IngresoProductoTermiandoDetalle.this);
        this.conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
        String dec= ext.get(getString(R.string.key_empresa_decimales),"2");
        this.decimales = Integer.parseInt(dec);
    }
    public void inicializarComponentes(){
        fe = (TextView)findViewById(R.id.infoTransFE);
        he = (TextView)findViewById(R.id.infoTransHE);
        bod= (TextView)findViewById(R.id.infoTransBodega);
        trans= (TextView)findViewById(R.id.infoTransTrans);
        precio= (TextView)findViewById(R.id.infoTransPrecio);
        vendedor= (TextView)findViewById(R.id.infoTransVendedor);
        obs= (TextView)findViewById(R.id.infoTransObs);
        fm= (TextView)findViewById(R.id.infoTransFM);
        estado= (TextView)findViewById(R.id.infoTransEstado);
        contenedorObservacion=(CardView)findViewById(R.id.infoTransObservaciones);
        contenedorAdicionales=(CardView)findViewById(R.id.contenedorAdicionales);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,40,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaNro= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.infoTransTableLayout);
        cantidades=new TextView[LIMITE];descripciones=new TextView[LIMITE];
        referenciaTittle = (TextView) findViewById(R.id.formItemTxt84t);
        referencia = (TextView) findViewById(R.id.infoTransReferencia);
        resources=this.getResources();
    }

    public void cargarDatos(String id_trans){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.obtenerTransaccion(id_trans);
        if(cursor.moveToFirst()){
            cli_id = cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovcli));
            //Date fecha = new ParseDates().changeStringToDateSimpleFormat(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            fe.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            he.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)));
            vendedor.setText(cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
            fm.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado)));
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
                if(this.conf_observacion)mostrarContenedorObservaciones();
                if (cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")) {
                    precio.setText(getString(R.string.info_precio_pcgrupo_enabled));
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(IngresoProductoTermiandoDetalle.this);
                    int conf_num_pcg = ext.configuracionPreciosGrupos();
                    if (precio.getText().toString().equals(getString(R.string.info_precio_pcgrupo_disable)) != true) {
                        cargarPrecio(cli_id, conf_num_pcg);
                    }
                } else {
                    precio.setText(getString(R.string.info_precio_pcgrupo_disable));
                }
            }
            cursor1.close();
        }
        cursor.close();
        helper.close();
    }

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



//                fila.setPadding(5, 5, 5, 5);

                fila.addView(nro);
                fila.addView(descripciones[pst]);
                fila.addView(cantidades[pst]);
                tabla.addView(fila);
                pst++;
            }while (cursor.moveToNext());
        }
    }

    public String redondearNumero(double numero) {
        int dec =this.decimales;
        String clave = "";
        switch (dec){
            case 2:clave = "0.00";break;
            case 3:clave = "0.000";break;
            case 4:clave = "0.0000";break;
            case 5:clave = "0.00000";break;
        }
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
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
        Intent intent = new Intent(IngresoProductoTermiandoDetalle.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
