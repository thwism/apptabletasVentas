package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.ArrayList;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

public class Transaccion_IT_Detalle extends AppCompatActivity {
    private static final int LIMITE=100;
    private TextView fe,he,trans,vendedor,cedula,nombres,direccion,telefono,mail,estado,referencia,fm,alterno,total;
    private TextView obs;
    private TextView forma, banco, ncheque, ncuenta, titular, fvenci;
    private TableLayout tabla, tablaresumen;
    private TableRow.LayoutParams layoutFila,layoutCeldas;
    private CardView contenedorObservacion,contenedorCheque;
    private Resources resources;
    private TextView [] facturas,asignado,codigo,monto,cobrado,letra,saldo;
    private int total_filas;
    private ArrayList<Resumen> cobrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion__it__detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        cargarDatos(getIntent().getStringExtra("transid"));
        cargarDetalles(getIntent().getStringExtra("transid"));
        calcularTotalCobro();
    }

    public void inicializarComponentes(){
        total_filas=0;
        cobrados = new ArrayList<>();
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
        total= (TextView)findViewById(R.id.totalCobro);
        estado= (TextView)findViewById(R.id.infoTransEstado);
        referencia= (TextView)findViewById(R.id.infoTransReferencia);
        fm= (TextView)findViewById(R.id.detalleCobroFM);
        contenedorObservacion=(CardView) findViewById(R.id.contenedorObservaciones);
        contenedorCheque=(CardView) findViewById(R.id.contenedorCheque);
        forma = (TextView)findViewById(R.id.detalleCobroDocs);
        banco = (TextView)findViewById(R.id.detalleChequeBanco);
        ncheque = (TextView)findViewById(R.id.detalleChequeNCH);
        ncuenta = (TextView)findViewById(R.id.detalleChequeNC);
        fvenci = (TextView)findViewById(R.id.detalleChequeVenci);
        titular = (TextView)findViewById(R.id.detalleChequeTitular);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,35,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas.setMargins(5,5,5,5);
        resources=this.getResources();
        facturas=new TextView[LIMITE];
        asignado=new TextView[LIMITE];
        codigo=new TextView[LIMITE];
        letra=new TextView[LIMITE];
        monto=new TextView[LIMITE];
        cobrado= new TextView[LIMITE];
        saldo= new TextView[LIMITE];
        tabla= (TableLayout)findViewById(R.id.detalleCobroTableLayout);
        tablaresumen= (TableLayout)findViewById(R.id.detalleCobroContenedorTablaResumen);
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
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(Transaccion_IT_Detalle.this);
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
    public void mostrarContenedorCheque() {
        if (contenedorCheque.getVisibility() == View.GONE) {
            animar(false, contenedorCheque);
            contenedorCheque.setVisibility(View.VISIBLE);
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
        ArrayList<String> cobrados= new ArrayList<>();
        TableRow fila;
        int pst=0;
        total_filas=cursor.getCount();
        if(cursor.moveToFirst()){
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                facturas[pst]= new TextView(this);
                facturas[pst].setMaxLines(1);
                facturas[pst].setGravity(Gravity.LEFT);
                facturas[pst].setLayoutParams(layoutCeldas);
                facturas[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)));

                if(!cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)).contains("INTERES")){
                    if(!cobrados.contains(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)))){
                        cobrados.add(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)));
                    }
                }

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
                monto[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_valor)));

                cobrado[pst]= new TextView(this);
                cobrado[pst].setMaxLines(1);
                cobrado[pst].setGravity(Gravity.CENTER);
                cobrado[pst].setLayoutParams(layoutCeldas);
                cobrado[pst].setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_pagado)));

                saldo[pst]= new TextView(this);
                saldo[pst].setMaxLines(1);
                saldo[pst].setGravity(Gravity.CENTER);
                saldo[pst].setLayoutParams(layoutCeldas);
                saldo[pst].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor))-cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_pagado))));

                if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));
                switch (cursor.getInt(cursor.getColumnIndex(PCKardex.FIELD_forma_pago))){
                    case 0:
                        forma.setText(getString(R.string.efectivo));
                        break;
                    case 1:
                        forma.setText(getString(R.string.cheque));
                        banco.setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_banco_id)));
                        ncheque.setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_numero_cheque)));
                        ncuenta.setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_numero_cuenta)));
                        fvenci.setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_pago_fecha_vencimiento)));
                        titular.setText(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_titular)));
                        mostrarContenedorCheque();
                        break;
                    case 2:
                        forma.setText(getString(R.string.retencion));
                        break;
                }
                fila.setPadding(10, 0, 10, 0);
                fila.addView(facturas[pst]);
                fila.addView(asignado[pst]);
                fila.addView(codigo[pst]);
                fila.addView(letra[pst]);
                fila.addView(monto[pst]);
                fila.addView(cobrado[pst]);
                fila.addView(saldo[pst]);
                tabla.addView(fila);
                pst++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
        /*Generar informacion resumida de las facturas cobradas*/
        //GenerarInformacionResumida genResumen= new GenerarInformacionResumida();
        //genResumen.execute(cobrados);
    }
    /*Tarea Asincrona para generar arreglo resumen*/
    private class GenerarInformacionResumida extends AsyncTask<ArrayList<String>,Integer,ArrayList<Resumen>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(Transaccion_IT_Detalle.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Generando Informacion Resumida");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0]);
        }

        @Override
        protected ArrayList<Resumen> doInBackground(ArrayList<String>... params) {
            ArrayList<Resumen> result= new ArrayList<>();
            DBSistemaGestion helper= new DBSistemaGestion(Transaccion_IT_Detalle.this);
            try
            {
                for(String fact: params[0]){
                    Cursor cursor= helper.consultarSaldoFactura(fact);
                    if(cursor.moveToFirst()){
                        double debe = cursor.getDouble(0);
                        double haber = cursor.getDouble(1);
                        double saldo = debe-haber;
                        Resumen res = new Resumen();
                        res.setFactura(fact);
                        res.setMonto(redondearNumero(debe));
                        res.setPagado(redondearNumero(haber));
                        res.setSaldo(redondearNumero(saldo));
                        result.add(res);
                    }
                    cursor.close();
                    //Thread.sleep(1000);
                }
            } catch(Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
            }
            helper.close();
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<Resumen> s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.size()>0){
                    cobrados.clear();
                    cobrados = s;
                    cargarResumenDeuda();
                    Toast.makeText(Transaccion_IT_Detalle.this,"Informacion generada correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /*fin tarea asincrona*/
    public void cargarResumenDeuda(){
        TableRow fila;
        for (Resumen resumen: cobrados){

            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            TextView fact = new TextView(this);
            fact.setMaxLines(1);
            fact.setGravity(Gravity.LEFT);
            fact.setLayoutParams(layoutCeldas);
            fact.setText(resumen.getFactura());

            TextView mont = new TextView(this);
            mont.setMaxLines(1);
            mont.setGravity(Gravity.CENTER);
            mont.setLayoutParams(layoutCeldas);
            mont.setText(resumen.getMonto());

            TextView pago = new TextView(this);
            pago.setMaxLines(1);
            pago.setGravity(Gravity.CENTER);
            pago.setLayoutParams(layoutCeldas);
            pago.setText(resumen.getPagado());

            TextView sald = new TextView(this);
            sald.setMaxLines(1);
            sald.setGravity(Gravity.CENTER);
            sald.setLayoutParams(layoutCeldas);
            sald.setText(resumen.getSaldo());

            fila.setPadding(10, 0, 10, 0);
            fila.addView(fact);
            fila.addView(mont);
            fila.addView(pago);
            fila.addView(sald);
            tablaresumen.addView(fila);
        }
    }

    public void calcularTotalCobro(){
        double total_trans= 0.d;
        for(int i=0;i<total_filas; i++){
            total_trans+=Double.parseDouble(cobrado[i].getText().toString());
        }
        total.setText(redondearNumero(total_trans));
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
        Intent intent = new Intent(Transaccion_IT_Detalle.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
    class Resumen{
        private String factura;
        private String monto;
        private String pagado;
        private String saldo;

        public Resumen() {
        }

        public String getFactura() {
            return factura;
        }

        public void setFactura(String factura) {
            this.factura = factura;
        }

        public String getMonto() {
            return monto;
        }

        public void setMonto(String monto) {
            this.monto = monto;
        }

        public String getPagado() {
            return pagado;
        }

        public void setPagado(String pagado) {
            this.pagado = pagado;
        }

        public String getSaldo() {
            return saldo;
        }

        public void setSaldo(String saldo) {
            this.saldo = saldo;
        }

        @Override
        public String toString() {
            return "Resumen{" +
                    "factura='" + factura + '\'' +
                    ", monto='" + monto + '\'' +
                    ", pagado='" + pagado + '\'' +
                    ", saldo='" + saldo + '\'' +
                    '}';
        }
    }
}
