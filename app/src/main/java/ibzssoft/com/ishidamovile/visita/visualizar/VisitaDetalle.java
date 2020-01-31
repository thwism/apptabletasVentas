package ibzssoft.com.ishidamovile.visita.visualizar;

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

import java.text.DecimalFormat;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

public class VisitaDetalle extends AppCompatActivity {
    private static final int LIMITE=100;
    private TextView fe,he,trans,vendedor,cedula,nombres,direccion,telefono,mail,estado,referencia,fm,alterno;
    private TextView obs,fecha,hora,contesta,relacion;
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila,layoutCeldas;
    private CardView contenedorObservacion;
    private Resources resources;
    private TextView [] facturas,asignado,codigo,monto,letra,saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion_vst_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        cargarDatos(getIntent().getStringExtra("transid"));
        cargarDetalles(getIntent().getStringExtra("transid"));
    }

    public void inicializarComponentes(){
        fe = (TextView)findViewById(R.id.infoTransFE);
        he = (TextView)findViewById(R.id.infoTransHE);
        trans= (TextView)findViewById(R.id.detalleCobroCodTrans);
        vendedor= (TextView)findViewById(R.id.detalleCobroVendedor);
        cedula= (TextView)findViewById(R.id.detalleCobroCI_RUC);
        nombres= (TextView)findViewById(R.id.detalleCobroNombres);
        direccion= (TextView)findViewById(R.id.detalleCobroDireccion);
        telefono= (TextView)findViewById(R.id.detalleCobroTelefono);
        mail= (TextView)findViewById(R.id.detalleCobroCorreo);
        alterno= (TextView)findViewById(R.id.detalleCobroNombreAlt);
        obs= (TextView) findViewById(R.id.txtdetalleCobroObservacion);
        fecha= (TextView) findViewById(R.id.fecha);
        hora= (TextView) findViewById(R.id.hora);
        contesta= (TextView) findViewById(R.id.contesta);
        relacion= (TextView) findViewById(R.id.relacion);

        estado= (TextView)findViewById(R.id.infoTransEstado);
        referencia= (TextView)findViewById(R.id.infoTransReferencia);
        fm= (TextView)findViewById(R.id.detalleCobroFM);
        contenedorObservacion=(CardView) findViewById(R.id.contenedorObservaciones);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,35,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas.setMargins(5,5,5,5);
        resources=this.getResources();
        facturas=new TextView[LIMITE];
        asignado=new TextView[LIMITE];
        codigo=new TextView[LIMITE];
        letra=new TextView[LIMITE];
        monto=new TextView[LIMITE];
        saldo= new TextView[LIMITE];
        tabla= (TableLayout)findViewById(R.id.detalleCobroTableLayout);
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
            fe.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            he.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)));
            fm.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado)));
            vendedor.setText(cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
            if(cursor.getInt(cursor.getColumnIndex(Transaccion.FIELD_band_enviado))!=0){
                estado.setText("Enviado");
                estado.setTextColor(getResources().getColor(R.color.successfull));
                referencia.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_referencia)));
            }else{

            }
            Cursor cursor1=helper.consultarGNTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            if(cursor1.moveToFirst()){
                trans.setText(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_codtrans)));
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(VisitaDetalle.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                if(conf_observacion)mostrarContenedorObservaciones();
                cargarObservaciones(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_descripcion)));
            }
            cursor1.close();
        }
        cursor.close();
        helper.close();
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

    public void cargarDetalles(String id_trans){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarPCKardexTransaccion(id_trans);
        TableRow fila;
        int pst=0;
        if(cursor.moveToFirst()){
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                facturas[pst]= new TextView(this);
                facturas[pst].setMaxLines(1);
                facturas[pst].setGravity(Gravity.LEFT);
                facturas[pst].setLayoutParams(layoutCeldas);
                facturas[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)));

                asignado[pst]= new TextView(this);
                asignado[pst].setMaxLines(1);
                asignado[pst].setGravity(Gravity.CENTER);
                asignado[pst].setLayoutParams(layoutCeldas);
                asignado[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_idasignado)));

                codigo[pst]= new TextView(this);
                codigo[pst].setMaxLines(1);
                codigo[pst].setGravity(Gravity.CENTER);
                codigo[pst].setLayoutParams(layoutCeldas);
                codigo[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_doc)));

                letra[pst]= new TextView(this);
                letra[pst].setMaxLines(1);
                letra[pst].setGravity(Gravity.CENTER);
                letra[pst].setLayoutParams(layoutCeldas);
                letra[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_ordencuota)));

                monto[pst]= new TextView(this);
                monto[pst].setMaxLines(1);
                monto[pst].setGravity(Gravity.CENTER);
                monto[pst].setLayoutParams(layoutCeldas);
                monto[pst].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor))));


                saldo[pst]= new TextView(this);
                saldo[pst].setMaxLines(1);
                saldo[pst].setGravity(Gravity.CENTER);
                saldo[pst].setLayoutParams(layoutCeldas);
                saldo[pst].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor))-cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_pagado))));

                if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));
                fila.setPadding(10, 0, 10, 0);
                fila.addView(facturas[pst]);
                fila.addView(asignado[pst]);
                fila.addView(codigo[pst]);
                fila.addView(letra[pst]);
                fila.addView(monto[pst]);
                fila.addView(saldo[pst]);
                tabla.addView(fila);
                pst++;

            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
    }


    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }

    public void cargarObservaciones(String datos){
        //observacion,solitante,tiempo entrega,forma,validez,atencion
        System.out.println("Observaciones Cargadas: "+datos);
        String data[]=datos.split(";");
        try{
            if(data[0].isEmpty()!=true) obs.setText(data[0]);
            if(data[12].isEmpty()!=true) fecha.setText(data[12]);
            if(data[13].isEmpty()!=true) hora.setText(data[13]);
            if(data[14].isEmpty()!=true) contesta.setText(data[14]);
            if(data[15].isEmpty()!=true) relacion.setText(data[14]);
        }catch(Exception e){
            e.printStackTrace();
        }
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
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
