package ibzssoft.com.ishidamovile.cobro.crear;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.adaptadores.Validaciones;
import ibzssoft.com.adaptadores.ValidateReferencia;
import ibzssoft.com.enviar.IVKardex_Serialize_Envio;
import ibzssoft.com.enviar.PKardex_Envio;
import ibzssoft.com.enviar.Transaccion_Serialize_Envio;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.imprimir.PrinterCommands;
import ibzssoft.com.ishidamovile.imprimir.Utils;
import ibzssoft.com.modelo.Banco;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.cobros.CobradoFactura;
import ibzssoft.com.modelo.cobros.DocumentoCobrado;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.recibir.RecibirFacturas;
import ibzssoft.com.storage.DBSistemaGestion;

public class CobroY extends AppCompatActivity implements View.OnClickListener {
    private static final int LIMIT= 200;
    private AutoCompleteTextView autoCompleteCliente;
    private TextView codTrans,emiTrans,impTrans,tipoTrans,vendeTrans,cliCI,clinombre,cliComercial,cliDir,cliTel,cliEmail;
    private CardView layoutContenedorObs;
    private TextView [] filaDocs,filaCodigo;
    private TextView [] filaFE,filaFV,filaPL,filaDV,filaValor,filaVC,filaSaldo,filaSaldoInt,filaInt,filaLetra,filaFP;
    private CheckBox [] seleccion;
    private TextView [] filaCobro;
    private TextView [] indice;
    private Resources resources;
    private TableLayout tabla, tabla_facts;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaCobro;
    private String [] insPckID;
    private String [] formas;
    private String [] idvendedor;
    private String [] idcobrador;
    private boolean [] cobrado;
    private TextView total_cobro;
    private EditText observacion;
    /*Campos forma de pago*/
    private RadioButton efectivo,cheque,retencion;
    private CardView msgCheque,msgBanco,contentBanco,contentCheque,msgRetencion,contentRetencion;
    private EditText textNroCheque,textNroCuenta,textTitular,textFechaVenci;
    private AutoCompleteTextView autoCompleteBanco;
    private Button guardarCobroDialog;
    /*Campos para la retencion*/
    private EditText textRetencionIVABase,textRetencionIVA,textRetencionRenta,textRetencionRentaBase,textNEstab,textNPunto,textSecuencial,textAutorizacion,textFechaCaducidad;
    private Button guardarCobro, calcularCobro;
    private EditText montorecibido;
    private  String cli_identificador,banco_identificador;

    private int conf_tipo_envio;
    private boolean conf_calc_int;
    private boolean conf_cronologico;
    private boolean conf_impresion_obligatoria;
    private String nombre_impresora;
    private double tasa_mora=0;
    private double dias_gracia=0;
    private int count = 0;
    private double recibido = 0.0;
    //private TextView saldo_restante;
    //private TextView tot_interes,tot_cuota;
    //private TextView saldo_inicial;
    private String nombreempresa;
    private String direccionempresa;

    private TextView tot_saldo,tot_impuestos,tot_sld_imp;
    /*
        Variables para imprimir
    */
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    /*
        Variable global para documentos cobrados
     */
    private ArrayList<DocumentoCobrado> documentos_cobrados;
    private int  conf_modo_busqueda;

    private String vendedorid,usuarioid;
    /**
     * Variables para saldos de facturas
     **/
    private TextView[] fact, debe, haber, saldo_fact;
    private CheckBox[] opc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobro_y);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.cargarPreferencias();
        this.inicializarComponentes();
    }
    public void cargarPreferencias(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(this);
        this.usuarioid = ext.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
        this.vendedorid = ext.get(getString(R.string.key_act_ven),getString(R.string.pref_act_ven_default));
    }

    public void inicializarComponentes() {
        resources=this.getResources();
        autoCompleteCliente = (AutoCompleteTextView) findViewById(R.id.autoCompleteCliente);
        tot_saldo = (TextView)findViewById(R.id.tot_saldo);
        tot_impuestos = (TextView)findViewById(R.id.tot_impuestos);
        tot_sld_imp= (TextView)findViewById(R.id.tot_sld_imp);
        observacion = (EditText)findViewById(R.id.txtnuevoCobroObservacion);
        codTrans=(TextView)findViewById(R.id.nuevoCobroCodTrans);
        emiTrans=(TextView)findViewById(R.id.nuevoCobroFecha);
        impTrans=(TextView)findViewById(R.id.nuevoCobroImpresora);
        tipoTrans=(TextView)findViewById(R.id.nuevoCobroTipo);
        vendeTrans=(TextView)findViewById(R.id.nuevoCobroVendedor);
        cliCI=(TextView)findViewById(R.id.nuevoCobroCI_RUC);
        clinombre=(TextView)findViewById(R.id.nuevoCobroNombres);
        cliComercial=(TextView)findViewById(R.id.nuevoCobroNombreAlt);
        cliDir=(TextView)findViewById(R.id.nuevoCobroDireccion);
        cliTel=(TextView)findViewById(R.id.nuevoCobroTelefono);
        cliEmail=(TextView)findViewById(R.id.nuevoCobroCorreo);
        layoutContenedorObs=(CardView) findViewById(R.id.contenedorObservaciones);
        //saldo_restante= (TextView)findViewById(R.id.saldo_restante);

        total_cobro= (TextView)findViewById(R.id.totalCobro);
        filaDocs = new  TextView [LIMIT];
        filaFP = new  TextView [LIMIT];
        filaCodigo = new  TextView [LIMIT];
        filaLetra= new  TextView [LIMIT];
        cobrado = new boolean[LIMIT];
        insPckID = new String [LIMIT];
        formas = new String [LIMIT];
        idvendedor= new String [LIMIT];
        idcobrador = new String [LIMIT];
        seleccion=new  CheckBox[LIMIT];
        filaFE= new  TextView [LIMIT];filaFV= new  TextView [LIMIT];filaPL= new  TextView [LIMIT];filaDV= new  TextView [LIMIT];filaValor= new  TextView [LIMIT];filaVC= new  TextView [LIMIT];/*filaSVencer= new  TextView [LIMIT];filaVencido=new  TextView [LIMIT];*/filaSaldo=new  TextView [LIMIT];filaSaldoInt=new  TextView [LIMIT];filaInt=new  TextView [LIMIT];
        filaCobro=new  TextView[LIMIT];
        indice=new  TextView[LIMIT];

        cobrado = new boolean[LIMIT];
        documentos_cobrados = new ArrayList<>();
        /**
         * Variables para saldos x factura
         */
        fact=new  TextView[LIMIT]; debe=new  TextView[LIMIT];haber=new  TextView[LIMIT];saldo_fact=new  TextView[LIMIT];opc=new  CheckBox[LIMIT];
        /**
         * campos opciones de pago
         */
        cli_identificador="";
        banco_identificador="";
        tabla= (TableLayout)findViewById(R.id.nuevoCobroTableLayout);
        tabla_facts= (TableLayout)findViewById(R.id.tablaSaldosFacturas);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas.span=1;
        layoutCeldaCobro = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaCobro.span=2;

        calcularCobro=(Button)findViewById(R.id.cobrar);
        guardarCobro=(Button)findViewById(R.id.guardar);
        montorecibido=(EditText) findViewById(R.id.montorecibido);
        montorecibido.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL );

        AutoCompleteAdapter adapter = this.new AutoCompleteAdapter(getApplicationContext());
        autoCompleteCliente.setAdapter(adapter);
        autoCompleteCliente.setDropDownWidth(800);
        autoCompleteCliente.setOnItemClickListener(adapter);

        conf_calc_int=false;
        conf_tipo_envio=0;
        conf_cronologico=false;
        conf_impresion_obligatoria=false;
        nombre_impresora="";
        recibido = 0.0;
        this.conf_modo_busqueda= 0;

        cargarConfiguracionCobro();
        CargarConfiguracionTransaccionTask taskHeadTransaction= new CargarConfiguracionTransaccionTask();
        taskHeadTransaction.execute();
        cargarPreferenciasEmpresa();

        calcularCobro.setEnabled(false);
        guardarCobro.setEnabled(false);

        calcularCobro.setOnClickListener(this);
        guardarCobro.setOnClickListener(this);
    }

    public void cargarConfiguracionCobro(){
        ExtraerConfiguraciones ext= new ExtraerConfiguraciones(CobroY.this);
        nombre_impresora=ext.get(getString(R.string.key_nombre_impresora),"MTP-II");
        impTrans.setText(nombre_impresora);
        conf_calc_int = ext.getBoolean(getString(R.string.key_conf_calcula_interes),true);
        String conf = ext.get(getString(R.string.key_conf_tipo_envio_cobro),"0");
        conf_impresion_obligatoria = ext.getBoolean(getString(R.string.key_conf_impresion_obligatoria),true);
        conf_tipo_envio = Integer.parseInt(conf);
        conf_cronologico= ext.getBoolean(getString(R.string.key_conf_orden_cronologico),true);
        String tm = ext.get(getString(R.string.key_conf_tasa_mora),"0");
        tasa_mora = Double.parseDouble(tm);
        String dg = ext.get(getString(R.string.key_conf_dias_gracia_mora),"0");
        dias_gracia = Integer.parseInt(dg);
    }
    /**
       *Adaptador para autocomplete cliente
     **/
    class AutoCompleteAdapter extends CursorAdapter implements AdapterView.OnItemClickListener{

        DBSistemaGestion dbSistemaGestion;
        public AutoCompleteAdapter(Context context) {
            super(CobroY.this,null);
            dbSistemaGestion=new DBSistemaGestion(context);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

            Cursor cursor = null;
            try {
                cursor = dbSistemaGestion.buscarClientes(
                        (constraint != null ? constraint.toString() : "@@@@"), getIntent().getStringArrayExtra("accesos"), conf_modo_busqueda);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final int columnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc);
            final int columnIndex2 = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre);
            final String str = cursor.getString(columnIndex)+" --> "+cursor.getString(columnIndex2);
            return str;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int itemColumnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc);
            final int descColumnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre);
            final int descAlterno = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombrealterno);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            text1.setText(cursor.getString(itemColumnIndex));
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            text2.setText(cursor.getString(descColumnIndex));
            TextView text3 = (TextView) view.findViewById(R.id.text3);
            text3.setText(cursor.getString(descAlterno));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.fila_autocomplete_item, parent, false);
            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            String idCliente = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_idprovcli));
            String itemNombre = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre));
            String itemAlterno= cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_nombrealterno));
            String itemCI = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc));
            String itemDir = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_direccion1));
            String itemTel = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_telefono1));
            String itemCor = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_email));
            cli_identificador=idCliente;
            cliCI.setText(itemCI);
            clinombre.setText(itemNombre);
            cliComercial.setText(itemAlterno);
            cliDir.setText(itemDir);
            cliTel.setText(itemTel);
            cliEmail.setText(itemCor);
            autoCompleteCliente.setText("");
            autoCompleteCliente.setEnabled(false);
            autoCompleteCliente.setFocusable(false);
            if(conf_calc_int)agregarFilasIniciales(idCliente);
            else{
                tasa_mora = 0.0;
                agregarFilasIniciales(idCliente);
            }
            agregarFilasSaldoFacturas(idCliente);
        }
    }//Fin adaptador autocomplete Cliente
    /*
        METODOS PARA AGREGAR FILAS A LA VISTA
     */
    public void agregarFilasCobradas(ArrayList<DocumentoCobrado> documentos){
        TableRow fila;
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                insPckID[count]= documentos.get(count).getPckid();

                formas[count]= documentos.get(count).getForma();

                idvendedor[count]= documentos.get(count).getIdvendedor();
                idcobrador[count]= documentos.get(count).getIdcobrador();

                indice[count] = new TextView(this);
                indice[count].setText(String.valueOf(count+1));
                indice[count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                indice[count].setLayoutParams(layoutCeldas);
                indice[count].setGravity(Gravity.CENTER);
                indice[count].setEnabled(false);
                indice[count].setTextSize(13);
                indice[count].setPadding(5, 5, 5, 5);

                filaDocs[count]= new TextView(this);
                filaDocs[count].setMaxLines(1);
                filaDocs[count].setGravity(Gravity.LEFT);
                filaDocs[count].setLayoutParams(layoutCeldaCobro);
                filaDocs[count].setText(documentos.get(count).getDocumento());
                filaDocs[count].setTextSize(13);
                filaDocs[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaDocs[count].setPadding(5, 5, 5, 5);



                filaCodigo[count]= new TextView(this);
                filaCodigo[count].setMaxLines(1);
                filaCodigo[count].setGravity(Gravity.LEFT);
                filaCodigo[count].setLayoutParams(layoutCeldas);
                filaCodigo[count].setText(documentos.get(count).getCodigo());
                filaCodigo[count].setTextSize(13);
                filaCodigo[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaCodigo[count].setPadding(5, 5, 5, 5);

                filaLetra[count]= new TextView(this);
                filaLetra[count].setMaxLines(1);
                filaLetra[count].setGravity(Gravity.CENTER);
                filaLetra[count].setLayoutParams(layoutCeldas);
                filaLetra[count].setText(documentos.get(count).getLetra());
                filaLetra[count].setTextSize(13);
                filaLetra[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaLetra[count].setPadding(5, 5, 5, 5);

                filaFE[count]= new TextView(this);
                filaFE[count].setMaxLines(1);
                layoutCeldas.span=1;
                filaFE[count].setGravity(Gravity.CENTER);
                filaFE[count].setLayoutParams(layoutCeldas);
                filaFE[count].setText(documentos.get(count).getEmision());
                filaFE[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaFE[count].setTextSize(13);
                filaFE[count].setPadding(5, 5, 5, 5);

                filaFV[count]= new TextView(this);
                filaFV[count].setMaxLines(1);
                layoutCeldas.span=1;
                filaFV[count].setLayoutParams(layoutCeldas);
                filaFV[count].setGravity(Gravity.CENTER);
                filaFV[count].setText(documentos.get(count).getVencimiento());
                filaFV[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaFV[count].setTextSize(13);
                filaFV[count].setPadding(5, 5, 5, 5);

                filaPL[count]= new TextView(this);
                filaPL[count].setMaxLines(1);
                filaPL[count].setGravity(Gravity.CENTER);
                filaPL[count].setLayoutParams(layoutCeldas);
                filaPL[count].setText(String.valueOf(documentos.get(count).getPlazo()));
                filaPL[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaPL[count].setTextSize(13);
                filaPL[count].setPadding(5, 5, 5, 5);


                filaDV[count] = new TextView(this);
                filaDV[count].setMaxLines(1);
                filaDV[count].setTextColor(resources.getColor(R.color.colorPrimary));
                filaDV[count].setGravity(Gravity.CENTER);
                filaDV[count].setLayoutParams(layoutCeldas);
                filaDV[count].setText(String.valueOf(documentos.get(count).getDv()));
                filaDV[count].setTextSize(13);
                filaDV[count].setPadding(5, 5, 5, 5);

                filaFP[count] = new TextView(this);
                filaFP[count].setMaxLines(1);
                filaFP[count].setTextColor(resources.getColor(R.color.colorPrimary));
                filaFP[count].setGravity(Gravity.CENTER);
                filaFP[count].setLayoutParams(layoutCeldas);
                filaFP[count].setText(String.valueOf(documentos.get(count).getUltpago()));
                filaFP[count].setTextSize(13);
                filaFP[count].setPadding(5, 5, 5, 5);

                filaValor[count] = new TextView(this);
                filaValor[count].setMaxLines(1);
                filaValor[count].setGravity(Gravity.CENTER);
                filaValor[count].setLayoutParams(layoutCeldas);
                filaValor[count].setText(redondearNumero(documentos.get(count).getValor()));
                filaValor[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaValor[count].setTextSize(13);
                filaValor[count].setPadding(5, 5, 5, 5);

                filaVC[count] = new TextView(this);
                filaVC[count].setMaxLines(1);
                filaVC[count].setGravity(Gravity.CENTER);
                filaVC[count].setLayoutParams(layoutCeldas);
                filaVC[count].setText(redondearNumero(documentos.get(count).getPagado()));
                filaVC[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaVC[count].setTextSize(13);
                filaVC[count].setPadding(5, 5, 5, 5);

                filaSaldo[count] = new TextView(this);
                filaSaldo[count].setMaxLines(1);
                filaSaldo[count].setGravity(Gravity.CENTER);
                filaSaldo[count].setLayoutParams(layoutCeldas);
                filaSaldo[count].setTypeface(null, Typeface.BOLD);
                filaSaldo[count].setText(redondearNumero(documentos.get(count).getSaldo()));
                filaSaldo[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaSaldo[count].setTextSize(13);
                filaSaldo[count].setPadding(5, 5, 5, 5);

                filaCobro[count]= new TextView(this);
                filaCobro[count].setMaxLines(1);
                filaCobro[count].setTextSize(13);
                filaCobro[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaCobro[count].setEnabled(false);
                filaCobro[count].setPadding(5, 5, 5, 5);
                filaCobro[count].setText("0.00");
                filaCobro[count].setGravity(Gravity.CENTER);
                filaCobro[count].setLayoutParams(layoutCeldas);

                filaInt[count]= new TextView(this);
                filaInt[count].setMaxLines(1);
                filaInt[count].setTextSize(13);
                filaInt[count].setTextColor(getResources().getColor(R.color.colorPrimary));
                filaInt[count].setEnabled(false);
                filaInt[count].setPadding(5, 5, 5, 5);filaInt[count].setText("0.0");
                filaInt[count].setGravity(Gravity.CENTER);
                filaInt[count].setLayoutParams(layoutCeldas);

                filaSaldoInt[count] = new TextView(this);
                filaSaldoInt[count].setMaxLines(1);
                filaSaldoInt[count].setGravity(Gravity.CENTER);
                filaSaldoInt[count].setLayoutParams(layoutCeldas);
                filaSaldoInt[count].setTypeface(null, Typeface.BOLD);
                filaSaldoInt[count].setText(redondearNumero(documentos.get(count).getSaldo()+documentos.get(count).getInteres()));
                filaSaldoInt[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaSaldoInt[count].setTextSize(13);
                filaSaldoInt[count].setPadding(5, 5, 5, 5);

                cobrado[count]=false;
                if ( count % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

                this.seleccion[count] = new CheckBox(this);
                this.seleccion[count].setEnabled(false);

                fila.addView(indice[count]);
                fila.addView(filaDocs[count]);
                fila.addView(filaLetra[count]);
                fila.addView(filaFE[count]);
                fila.addView(filaPL[count]);
                fila.addView(filaFV[count]);
                fila.addView(filaDV[count]);
                fila.addView(filaFP[count]);
                fila.addView(filaValor[count]);
                fila.addView(filaVC[count]);
                fila.addView(filaSaldo[count]);
                fila.addView(filaInt[count]);
                fila.addView(filaSaldoInt[count]);
                fila.addView(filaCobro[count]);
                fila.addView(seleccion[count]);
                tabla.addView(fila);
                count++;
            }while (count<documentos.size());
        this.calcularAbonosCobro();
    }
    public ArrayList<CobradoFactura> totalCobradoFactura(ArrayList<DocumentoCobrado> documentos){
        /*Sacar solo facturas cobradas*/
        ArrayList<String> docs= new ArrayList<>();
        ArrayList<CobradoFactura> cobrado= new ArrayList<CobradoFactura>();
        for(int i = 0; i<documentos.size(); i++){
            if(!docs.contains(documentos.get(i).getDocumento())){
                docs.add(documentos.get(i).getDocumento());
            }
        }
        /*Sumar valor cobrado x factura*/
        for(int i=0; i<docs.size(); i++){
            double pre = 0.0;
            for(int j=0; j<documentos.size(); j++){
                if(documentos.get(j).getDocumento().equals(docs.get(i))){
                    pre+=documentos.get(j).getCobrado();
                }
            }
            cobrado.add(new CobradoFactura(docs.get(i),pre));
        }
        return cobrado;
    }
    public void alterarReporteSaldos(ArrayList<CobradoFactura> cobrados){
        for(CobradoFactura cob: cobrados){
            for(int i=0; i<fact.length; i++){
                if(fact[i]!=null&&fact[i].getText().toString().equals(cob.getDocumento())&&!cob.getDocumento().contains("INTERES")){
                    double deuda=Double.parseDouble(debe[i].getText().toString());
                    double cancela=Double.parseDouble(haber[i].getText().toString())+cob.getTotal_cobrado();
                    haber[i].setText(redondearNumero(cancela));
                    haber[i].setTypeface(null, Typeface.BOLD);
                    saldo_fact[i].setText(redondearNumero(deuda-cancela));
                    saldo_fact[i].setTypeface(null, Typeface.BOLD);
                }
            }
        }
    }

    public void agregarFilasIniciales(String id_client){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarCarteraConInteres(id_client,dias_gracia,tasa_mora/100,false);
        TableRow fila;
        count=0;
        double sld_ini= 0.0;
        double sld_imp= 0.0;
        if(cursor.moveToFirst()){
            do{
                calcularCobro.setEnabled(true);
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                insPckID[count]= cursor.getString(0);

                formas[count]= cursor.getString(14);

                idvendedor[count]= cursor.getString(15);
                idcobrador[count]= cursor.getString(16);

                indice[count] = new TextView(this);
                indice[count].setText(String.valueOf(count+1));
                indice[count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                indice[count].setLayoutParams(layoutCeldas);
                indice[count].setGravity(Gravity.CENTER);
                indice[count].setEnabled(false);
                indice[count].setTextSize(13);
                indice[count].setPadding(5, 5, 5, 5);

                filaDocs[count]= new TextView(this);
                filaDocs[count].setMaxLines(1);
                filaDocs[count].setGravity(Gravity.LEFT);
                filaDocs[count].setLayoutParams(layoutCeldaCobro);
                filaDocs[count].setText(cursor.getString(1));
                filaDocs[count].setTextSize(13);
                filaDocs[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaDocs[count].setPadding(5, 5, 5, 5);

                filaCodigo[count]= new TextView(this);
                filaCodigo[count].setMaxLines(1);
                filaCodigo[count].setGravity(Gravity.LEFT);
                filaCodigo[count].setLayoutParams(layoutCeldas);
                filaCodigo[count].setText(cursor.getString(13));
                filaCodigo[count].setTextSize(13);
                filaCodigo[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaCodigo[count].setPadding(5, 5, 5, 5);

                filaLetra[count]= new TextView(this);
                filaLetra[count].setMaxLines(1);
                filaLetra[count].setGravity(Gravity.CENTER);
                filaLetra[count].setLayoutParams(layoutCeldas);
                filaLetra[count].setText(cursor.getString(12));
                filaLetra[count].setTextSize(13);
                filaLetra[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaLetra[count].setPadding(5, 5, 5, 5);

                filaFE[count]= new TextView(this);
                filaFE[count].setMaxLines(1);
                layoutCeldas.span=1;
                filaFE[count].setGravity(Gravity.CENTER);
                filaFE[count].setLayoutParams(layoutCeldas);
                filaFE[count].setText(cursor.getString(3));
                filaFE[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaFE[count].setTextSize(13);
                filaFE[count].setPadding(5, 5, 5, 5);

                filaFV[count]= new TextView(this);
                filaFV[count].setMaxLines(1);
                layoutCeldas.span=1;
                filaFV[count].setLayoutParams(layoutCeldas);
                filaFV[count].setGravity(Gravity.CENTER);
                filaFV[count].setText(cursor.getString(5));
                filaFV[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaFV[count].setTextSize(13);
                filaFV[count].setPadding(5, 5, 5, 5);

                filaPL[count]= new TextView(this);
                filaPL[count].setMaxLines(1);
                filaPL[count].setGravity(Gravity.CENTER);
                filaPL[count].setLayoutParams(layoutCeldas);
                filaPL[count].setText(cursor.getString(2));
                filaPL[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaPL[count].setTextSize(13);
                filaPL[count].setPadding(5, 5, 5, 5);


                filaDV[count] = new TextView(this);
                filaDV[count].setMaxLines(1);
                filaDV[count].setTextColor(resources.getColor(R.color.colorPrimary));
                filaDV[count].setGravity(Gravity.CENTER);
                filaDV[count].setTypeface(null, Typeface.BOLD);
                filaDV[count].setLayoutParams(layoutCeldas);
                filaDV[count].setText(cursor.getString(4));
                filaDV[count].setTextSize(13);
                filaDV[count].setPadding(5, 5, 5, 5);

                filaFP[count] = new TextView(this);
                filaFP[count].setMaxLines(1);
                filaFP[count].setTextColor(resources.getColor(R.color.textColorPrimary));
                filaFP[count].setGravity(Gravity.CENTER);
                filaFP[count].setLayoutParams(layoutCeldas);
                filaFP[count].setText(cursor.getString(17));
                filaFP[count].setTextSize(13);
                filaFP[count].setPadding(5, 5, 5, 5);

                filaValor[count] = new TextView(this);
                filaValor[count].setMaxLines(1);
                filaValor[count].setGravity(Gravity.CENTER);
                filaValor[count].setLayoutParams(layoutCeldas);
                filaValor[count].setText(redondearNumero(cursor.getDouble(8)));
                filaValor[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaValor[count].setTextSize(13);
                filaValor[count].setPadding(5, 5, 5, 5);

                filaVC[count] = new TextView(this);
                filaVC[count].setMaxLines(1);
                filaVC[count].setGravity(Gravity.CENTER);
                filaVC[count].setLayoutParams(layoutCeldas);
                filaVC[count].setText(redondearNumero(cursor.getDouble(7)));
                filaVC[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaVC[count].setTextSize(13);
                filaVC[count].setPadding(5, 5, 5, 5);

                filaSaldo[count] = new TextView(this);
                filaSaldo[count].setMaxLines(1);
                filaSaldo[count].setGravity(Gravity.CENTER);
                filaSaldo[count].setLayoutParams(layoutCeldas);
                filaSaldo[count].setTypeface(null, Typeface.BOLD);
                filaSaldo[count].setText(redondearNumero(cursor.getDouble(8)));
                filaSaldo[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaSaldo[count].setTextSize(13);
                filaSaldo[count].setPadding(5, 5, 5, 5);

                filaCobro[count]= new TextView(this);
                filaCobro[count].setMaxLines(1);
                filaCobro[count].setTextSize(13);
                filaCobro[count].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                filaCobro[count].setEnabled(false);
                filaCobro[count].setPadding(5, 5, 5, 5);
                filaCobro[count].setText("0.00");
                filaCobro[count].setGravity(Gravity.CENTER);
                filaCobro[count].setLayoutParams(layoutCeldas);

                filaInt[count]= new TextView(this);
                filaInt[count].setMaxLines(1);
                filaInt[count].setTextSize(13);
                filaInt[count].setTextColor(getResources().getColor(R.color.color_danger));
                filaInt[count].setEnabled(false);
                filaInt[count].setPadding(5, 5, 5, 5);filaInt[count].setText(redondearNumero(cursor.getDouble(11)));
                filaInt[count].setGravity(Gravity.CENTER);
                filaInt[count].setLayoutParams(layoutCeldas);

                filaSaldoInt[count]= new TextView(this);
                filaSaldoInt[count].setMaxLines(1);
                filaSaldoInt[count].setTextSize(13);
                filaSaldoInt[count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                filaSaldoInt[count].setTypeface(null, Typeface.BOLD);
                filaSaldoInt[count].setEnabled(false);
                filaSaldoInt[count].setPadding(5, 5, 5, 5);
                filaSaldoInt[count].setText(redondearNumero(cursor.getDouble(11)+cursor.getDouble(8)));
                filaSaldoInt[count].setGravity(Gravity.CENTER);
                filaSaldoInt[count].setLayoutParams(layoutCeldas);

                cobrado[count]=false;

                fila.setBackgroundColor(resources.getColor(R.color.windowBackground));

                this.seleccion[count] = new CheckBox(this);
                this.seleccion[count].setEnabled(false);
                this.seleccion[count].setTag(count);
                this.seleccion[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double valor = Double.parseDouble(montorecibido.getText().toString());
                        System.out.println("Dinero recibido: "+valor);
                        final int position = (Integer) v.getTag();
                        if(seleccion[position].isChecked()){
                            calcularAbonosPotestad(valor, position);
                        }else{
                            cobrado[position] = false;
                            filaCobro[position].setText("0.00");
                            filaCobro[position].setTextColor(resources.getColor(R.color.textColorPrimary));
                            calcularTotalCobro();
                        }
                    }
                });


                /*
                    Calcular Saldo Inicial
                 */
                sld_ini += cursor.getDouble(8);
                sld_imp += cursor.getDouble(11);


                fila.addView(indice[count]);
                fila.addView(filaDocs[count]);
                //fila.addView(filaCodigo[count]);
                fila.addView(filaLetra[count]);
                fila.addView(filaFE[count]);
                fila.addView(filaPL[count]);
                fila.addView(filaFV[count]);
                fila.addView(filaDV[count]);
                fila.addView(filaFP[count]);
                fila.addView(filaValor[count]);
                fila.addView(filaVC[count]);
                fila.addView(filaSaldo[count]);
                //fila.addView(filaSVencer[count]);
                //fila.addView(filaVencido[count]);
                fila.addView(filaInt[count]);
                fila.addView(filaSaldoInt[count]);
                fila.addView(filaCobro[count]);
                fila.addView(seleccion[count]);
                tabla.addView(fila);

                count++;
            }while (cursor.moveToNext());
            this.tot_saldo.setText(redondearNumero(sld_ini));
            this.tot_impuestos.setText(redondearNumero(sld_imp));
            this.tot_sld_imp.setText(redondearNumero(sld_imp+sld_ini));
        }
        cursor.close();
        helper.close();
    }
    public void agregarFilasSaldoFacturas(String id_client){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarSaldoFactura(id_client);
        TableRow fila;
        int ind=0;
        if(cursor.moveToFirst()){
            do{
                calcularCobro.setEnabled(true);
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                TextView nro = new TextView(this);
                nro.setText(String.valueOf(ind+1));
                nro.setTextColor(getResources().getColor(R.color.textColorPrimary));
                nro.setLayoutParams(layoutCeldas);
                nro.setGravity(Gravity.CENTER);
                nro.setEnabled(false);
                nro.setTextSize(13);
                nro.setPadding(5, 5, 5, 5);

                fact[ind]= new TextView(this);
                fact[ind].setMaxLines(1);
                fact[ind].setGravity(Gravity.LEFT);
                fact[ind].setLayoutParams(layoutCeldaCobro);
                fact[ind].setText(cursor.getString(0));
                fact[ind].setTextSize(13);
                fact[ind].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                fact[ind].setPadding(5, 5, 5, 5);

                debe[ind]= new TextView(this);
                debe[ind].setMaxLines(1);
                debe[ind].setGravity(Gravity.LEFT);
                debe[ind].setLayoutParams(layoutCeldas);
                debe[ind].setText(cursor.getString(1));
                debe[ind].setTextSize(13);
                debe[ind].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                debe[ind].setPadding(5, 5, 5, 5);

                haber[ind]= new TextView(this);
                haber[ind].setMaxLines(1);
                haber[ind].setGravity(Gravity.LEFT);
                haber[ind].setLayoutParams(layoutCeldas);
                haber[ind].setText(cursor.getString(2));
                haber[ind].setTextSize(13);
                haber[ind].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                haber[ind].setPadding(5, 5, 5, 5);

                saldo_fact[ind]= new TextView(this);
                saldo_fact[ind].setMaxLines(1);
                saldo_fact[ind].setGravity(Gravity.LEFT);
                saldo_fact[ind].setLayoutParams(layoutCeldas);
                saldo_fact[ind].setText(cursor.getString(3));
                saldo_fact[ind].setTextSize(13);
                saldo_fact[ind].setTextColor(this.getResources().getColor(R.color.textColorPrimary));
                saldo_fact[ind].setPadding(5, 5, 5, 5);

                this.opc[ind] = new CheckBox(this);
                this.opc[ind].setTag(ind);
                this.opc[ind].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = (Integer) v.getTag();
                        if(opc[position].isChecked()){
                            RecibirFacturas facturas= new RecibirFacturas(CobroY.this,fact[position].getText().toString(),clinombre.getText().toString(),cliCI.getText().toString(),cliComercial.getText().toString(),cliDir.getText().toString(),"1990-01-01");
                            facturas.ejecutartarea();
                            System.out.println("Descargar Factura: "+filaDocs[position].getText().toString());
                        }
                    }
                });


                fila.addView(nro);
                fila.addView(fact[ind]);
                fila.addView(debe[ind]);
                fila.addView(haber[ind]);
                fila.addView(saldo_fact[ind]);
                fila.addView(opc[ind]);
                tabla_facts.addView(fila);

                ind++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
    }
    /*
        Generar saldos y abonos
     */
    public void calcularAbonosCronologicos(double valor){
        recibido = valor;
        double suma=0;
        int ind=0;
        do{
            double subtotal = Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString());
            suma+=subtotal;
            double restante = valor-suma;
            if(restante<0){
                cobrado[ind] = true;
                restante=restante*(-1);
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString())-restante));
            }else{
                cobrado[ind] = true;
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString())));
            }
            filaCobro[ind].setTextColor(getResources().getColor(R.color.successfull));
            ind++;
        }while(suma<valor);
        calcularTotalCobro();
        if(Double.parseDouble(montorecibido.getText().toString())==Double.parseDouble(total_cobro.getText().toString())){
            GenerarFilasCobradas generarFilasCobradas= new GenerarFilasCobradas();
            generarFilasCobradas.execute();
        }
    }

    public void calcularAbonosPotestad(double valor, int ind){
        recibido = valor;
        double suma = Double.parseDouble(total_cobro.getText().toString());
        if(suma<valor){
            double subtotal = Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString());
            suma+=subtotal;
            double restante = valor-suma;
            if(restante<0){
                cobrado[ind] = true;
                restante=restante*(-1);
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString())-restante));
            }else{
                cobrado[ind] = true;
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())+Double.parseDouble(filaInt[ind].getText().toString())));
            }
            filaCobro[ind].setTextColor(getResources().getColor(R.color.successfull));
        }
        calcularTotalCobro();
        if(Double.parseDouble(montorecibido.getText().toString())==Double.parseDouble(total_cobro.getText().toString())){
            GenerarFilasCobradas generarFilasCobradas= new GenerarFilasCobradas();
            generarFilasCobradas.execute();
        }
        guardarCobro.setEnabled(true);
    }

    public void calcularAbonosCobro(){
        int ind = 0;
        double suma=0;
        double sub_cobrado = 0.0;
        do{
            double subtotal = Double.parseDouble(filaSaldo[ind].getText().toString());
            suma+=subtotal;
            double restante = this.recibido-suma;
            if(restante<0){
                cobrado[ind] = true;
                restante=restante*(-1);
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())-restante));
                this.documentos_cobrados.get(ind).setCobrado(Double.parseDouble(filaCobro[ind].getText().toString()));
                sub_cobrado+=Double.parseDouble(filaCobro[ind].getText().toString());

            }else{
                cobrado[ind] = true;
                filaCobro[ind].setText(redondearNumero(Double.parseDouble(filaSaldo[ind].getText().toString())));
                this.documentos_cobrados.get(ind).setCobrado(Double.parseDouble(filaCobro[ind].getText().toString()));
                sub_cobrado+=Double.parseDouble(filaCobro[ind].getText().toString());
            }
            filaCobro[ind].setTextColor(getResources().getColor(R.color.successfull));
            ind++;
        }while(suma<this.recibido);
        double sub_cuot=0.0;
        double sub_int=0.0;
        for(int i= 0;i<documentos_cobrados.size(); i++){
            if(!documentos_cobrados.get(i).getDocumento().contains("INTERES")){
                sub_cuot+=Double.parseDouble(filaCobro[i].getText().toString());
            }else{
                sub_int+=Double.parseDouble(filaCobro[i].getText().toString());
            }
        }

        calcularSaldosRestantes();
        calcularTotalCobro();
    }

    public void calcularSaldosRestantes(){
        for(int i=0; i<this.count;i++){
            this.filaSaldo[i].setText(redondearNumero(Double.parseDouble(filaSaldo[i].getText().toString())-Double.parseDouble(filaCobro[i].getText().toString())));

            this.documentos_cobrados.get(i).setSaldo(Double.parseDouble(filaSaldo[i].getText().toString())-Double.parseDouble(filaCobro[i].getText().toString()));
        }
    }

    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }

    public void calcularTotalCobro(){
        double sumatoria=0.0;
        for(int i=0; i<count;i++){
            sumatoria+=Double.parseDouble(filaCobro[i].getText().toString());
        }
        total_cobro.setText(redondearNumero(sumatoria));
    }
    public void regenerarFilasCobradas(){
        for(int i=0;i<LIMIT;i++){
            filaDocs[i]=null;/*filaVencido[i]=null;filaSVencer[i]=null;*/
            filaSaldo[i]=null;filaValor[i]=null;
            filaVC[i]=null;filaInt[i]=null;
            filaPL[i]=null;filaCobro[i]=null;
            filaFE[i]=null;filaFV[i]=null;
        }
        this.count=0;
        this.cleanTable(tabla);
        this.agregarFilasCobradas(documentos_cobrados);
        this.alterarReporteSaldos(this.totalCobradoFactura(documentos_cobrados));


    }
    private void cleanTable(TableLayout table) {
        int childCount = table.getChildCount();
        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }
    public ArrayList<DocumentoCobrado> generarFilasIntereses(ArrayList<DocumentoCobrado> docs){
        ArrayList<DocumentoCobrado> nuevos = new ArrayList<>();
        Collections.reverse(docs);
        for(DocumentoCobrado doc: docs){
            if(doc.getInteres() > 0.0){
                nuevos.add(new DocumentoCobrado(0,"INTERES:"+doc.getCodigo(), "INT:"+doc.getCodigo(), "", new ParseDates().changeDateToStringSimple1(new Date()),0,new ParseDates().changeDateToStringSimple1(new Date()),0,doc.getInteres(), 0.0,doc.getInteres(), 0.0,0.0,"VALOR POR INTERES DEL DOCUMENTO: "+doc.getDocumento()+":"+doc.getLetra(),"AP","0","0",this.vendedorid,""));//veriricar pendiente
            }
        }
        for(DocumentoCobrado nue: nuevos){
            docs.add(nue);
        }
        return docs;
    }
    /*
        clases para mostrar solo documentos cobrados
     */
    class GenerarFilasCobradas extends AsyncTask<String,Integer,ArrayList<DocumentoCobrado>> {
        private ProgressDialog progress;
        ArrayList<DocumentoCobrado> documentoCobrados;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(CobroY.this);
            progress.setTitle("Generando Documentos Cobrados");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(ArrayList<DocumentoCobrado> docs) {

            if(progress.isShowing()){
                progress.dismiss();
                if(docs!=null && docs.size()>0){
                    try{
                        for(DocumentoCobrado doc: documentoCobrados){
                            System.out.println("Documento cobrado1: "+doc.toString());
                        }
                        documentos_cobrados =this.documentoCobrados;
                        regenerarFilasCobradas();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        protected ArrayList<DocumentoCobrado> doInBackground(String... params) {
            try {
                this.documentoCobrados = obtenerDocumentosCobrados();
                if(conf_calc_int){
                    this.documentoCobrados = generarFilasIntereses(this.documentoCobrados);
                     Collections.reverse(documentoCobrados);
                }
                 Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return documentoCobrados;
        }
    }
    public ArrayList<DocumentoCobrado> obtenerDocumentosCobrados(){
        ArrayList<DocumentoCobrado> array = new ArrayList<>();
        /*Recorriendo vector original*/
        for(int i=0; i<LIMIT-1; i++){
            if (cobrado[i] == true){
                array.add(new DocumentoCobrado(
                        i,
                        filaDocs[i].getText().toString(),
                        filaCodigo[i].getText().toString(),
                        filaLetra[i].getText().toString(),
                        filaFE[i].getText().toString(),
                        Integer.parseInt(filaPL[i].getText().toString()),
                        filaFV[i].getText().toString(),
                        Integer.parseInt(filaDV[i].getText().toString()),
                        Double.parseDouble(filaValor[i].getText().toString()),
                        Double.parseDouble(filaVC[i].getText().toString()),
                        Double.parseDouble(filaSaldo[i].getText().toString()),
                        Double.parseDouble(filaInt[i].getText().toString()),
                        Double.parseDouble(filaCobro[i].getText().toString()),
                        "VALOR COBRADOR POR LA TRANSACCION: "+filaCodigo[i].getText().toString()+":"+filaLetra[i].getText().toString(),formas[i],
                        insPckID[i],idvendedor[i],this.vendedorid,""));
            }
        }
        return array;
    }

    /*

        Eventos para botones
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cobrar:
                if(new Validaciones().validar_decimales(montorecibido.getText().toString())){
                    double valor = Double.parseDouble(montorecibido.getText().toString());
                        if(conf_cronologico){
                            //generar cobro en orden cronologico
                            Toast.makeText(CobroY.this, "Generando cobro en orden cronologico", Toast.LENGTH_LONG).show();
                            calcularAbonosCronologicos(valor);
                            montorecibido.setEnabled(false);
                            calcularCobro.setEnabled(false);
                            montorecibido.clearFocus();
                            guardarCobro.setEnabled(true);
                        }else{
                            //cobrar en cualquier orden
                            montorecibido.setEnabled(false);
                            calcularCobro.setEnabled(false);
                            montorecibido.clearFocus();
                            for(int i =0; i<count; i++){
                                seleccion[i].setEnabled(true);
                            }
                            Toast.makeText(CobroY.this, "Generando cobro a potestad", Toast.LENGTH_LONG).show();

                        }
                }else{
                    montorecibido.requestFocus();
                    montorecibido.setError(getString(R.string.info_field_no_valid));
                }
                break;
            case R.id.guardar:
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(CobroY.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                if (conf_observacion){
                    if (!validarCamposVacios()) {
                        seleccionarFormaCobro();
                    }
                }else seleccionarFormaCobro();
                break;
        }
    }
/*
    Metodos para guardar el cobro
 */
public boolean validarCamposVacios() {
    if (TextUtils.isEmpty(observacion.getText().toString())) {
        observacion.requestFocus();
        observacion.setError(resources.getString(R.string.info_field_required));
        return true;
    }
    return false;
}

public void seleccionarFormaCobro(){
    LayoutInflater li = LayoutInflater.from(this);
    View promptsView = li.inflate(R.layout.opciones_pagoy, null);
    efectivo=(RadioButton)promptsView.findViewById(R.id.idContado);
    cheque=(RadioButton)promptsView.findViewById(R.id.idCredito);
    retencion=(RadioButton)promptsView.findViewById(R.id.idRetencion);
    autoCompleteBanco=(AutoCompleteTextView)promptsView.findViewById(R.id.autoCompleteBanco);
    AutoCompleteAdapterBanco adapter = this.new AutoCompleteAdapterBanco(getApplicationContext());
    autoCompleteBanco.setAdapter(adapter);
    autoCompleteBanco.setDropDownWidth(400);
    autoCompleteBanco.setOnItemClickListener(adapter);

    textNroCheque=(EditText)promptsView.findViewById(R.id.NroCheque);
    textNroCheque.setInputType(InputType.TYPE_CLASS_NUMBER);
    textNroCuenta=(EditText)promptsView.findViewById(R.id.NroCuenta);
    textNroCuenta.setInputType(InputType.TYPE_CLASS_NUMBER);
    textTitular=(EditText)promptsView.findViewById(R.id.titular);
    textFechaVenci=(EditText)promptsView.findViewById(R.id.FVenceCheque);
    msgBanco=(CardView) promptsView.findViewById(R.id.messageBanco);
    msgCheque=(CardView)promptsView.findViewById(R.id.messageCheque);
    msgRetencion=(CardView)promptsView.findViewById(R.id.messageRetencion);
    contentBanco=(CardView)promptsView.findViewById(R.id.contentBanco);
    contentCheque=(CardView)promptsView.findViewById(R.id.contentCheque);
    contentRetencion=(CardView) promptsView.findViewById(R.id.contentRetencion);

    textRetencionIVA=(EditText)promptsView.findViewById(R.id.RetencionIva);
    textRetencionIVABase=(EditText)promptsView.findViewById(R.id.RetencionBase);
    textRetencionRenta=(EditText)promptsView.findViewById(R.id.RetencioRenta);
    textRetencionRentaBase=(EditText)promptsView.findViewById(R.id.RetencioRentaBase);
    textSecuencial=(EditText)promptsView.findViewById(R.id.nroSerie);
    textNEstab=(EditText)promptsView.findViewById(R.id.nroEsta);
    textNPunto=(EditText)promptsView.findViewById(R.id.nroPun);
    textAutorizacion=(EditText)promptsView.findViewById(R.id.autorizacion);
    textFechaCaducidad=(EditText)promptsView.findViewById(R.id.FCaducidad);
    textFechaCaducidad=(EditText)promptsView.findViewById(R.id.FCaducidad);
    guardarCobroDialog=(Button)promptsView.findViewById(R.id.buttonGuardarCobro);

    cheque.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cheque.isChecked()) {
                mostrarContenedorBanco();
                ocultarMsgBanco();
                ocultarContenedorRetencion();
                mostrarMsgRetencion();
                efectivo.setChecked(false);
                retencion.setChecked(false);
            }
        }
    });
    efectivo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (efectivo.isChecked()) {
                cheque.setChecked(false);
                retencion.setChecked(false);
                ocultarContenedorBanco();
                mostrarMsgBanco();
                ocultarContenedorCheque();
                mostrarMsgCheque();
                mostrarMsgRetencion();
                ocultarContenedorRetencion();

                autoCompleteBanco.setEnabled(true);
                autoCompleteBanco.setText("");
                textTitular.setText("");
                textNroCuenta.setText("");
                textNroCheque.setText("");
                textFechaVenci.setText("");
            }
        }
    });
    retencion.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (retencion.isChecked()) {
                cheque.setChecked(false);
                efectivo.setChecked(false);
                ocultarContenedorBanco();
                mostrarMsgBanco();
                mostrarContenedorRetencion();
                ocultarMsgRetencion();
                mostrarMsgCheque();
                ocultarContenedorCheque();
                autoCompleteBanco.setEnabled(true);
                autoCompleteBanco.setText("");
                textTitular.setText("");
                textNroCuenta.setText("");
                textNroCheque.setText("");
                textFechaVenci.setText("");
            }
        }
    });

    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setView(promptsView);
    alertDialogBuilder.setTitle("Opciones de Pago");

        /*Cargar Listado*/
    //alertDialogBuilder.setCancelable(true);
    guardarCobroDialog.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (obtenerFormaPago()){
                case 0:
                    openDialogImpresion("0",null,null,null,null,null,null,null,null,null,null,null,null,null,null);
                    break;
                case 1:
                    /*if(validarCamposCheque()!=true){
                        openDialogImpresion("1",banco_identificador,textNroCheque.getText().toString(),textNroCuenta.getText().toString(),textTitular.getText().toString(),textFechaVenci.getText().toString(),
                                null,null,null,null,null,null,null,null,null);
                    }*/
                    break;
                case 2:
                    /*if(validarCamposRetencion()!=true){
                        guardarTrans(obtenerFormaPago(),null,null,null,null,null,
                                textRetencionIVA.getText().toString(),textRetencionIVABase.getText().toString(),textRetencionRenta.getText().toString(),textRetencionRentaBase.getText().toString(),textNEstab.getText().toString(),textNPunto.getText().toString(),textSecuencial.getText().toString(),textAutorizacion.getText().toString(),textFechaCaducidad.getText().toString());
                    }*/
                    break;
            }

        }
    });
    final AlertDialog alertDialog = alertDialogBuilder.create();
    //alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.show();
}

    public boolean validarCamposRetencion(){
        if (TextUtils.isEmpty(textRetencionIVA.getText().toString())) {
            textRetencionIVA.requestFocus();
            textRetencionIVA.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(textRetencionIVABase.getText().toString())) {
            textRetencionIVABase.requestFocus();
            textRetencionIVABase.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(textRetencionRenta.getText().toString())) {
            textRetencionRenta.requestFocus();
            textRetencionRenta.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(textRetencionRentaBase.getText().toString())) {
            textRetencionRentaBase.requestFocus();
            textRetencionRentaBase.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        else if(new Validaciones().validate_3_digits(textNEstab.getText().toString())!=true){
            textNEstab.requestFocus();
            textNEstab.setError(resources.getString(R.string.info_field_punto_est));
            return true;
        }else if(new Validaciones().validate_3_digits(textNPunto.getText().toString())!=true){
            textNPunto.requestFocus();
            textNPunto.setError(resources.getString(R.string.info_field_punto_est));
            return true;
        }else if(new Validaciones().validate_9_digits(textSecuencial.getText().toString())!=true){
            textSecuencial.requestFocus();
            textSecuencial.setError(resources.getString(R.string.info_field_serie));
            return true;
        }else if(new Validaciones().validate_10_digits(textAutorizacion.getText().toString())!=true&&new Validaciones().validate_37_digits(textAutorizacion.getText().toString())!=true&&new Validaciones().validate_49_digits(textAutorizacion.getText().toString())!=true){
            textAutorizacion.requestFocus();
            textAutorizacion.setError(resources.getString(R.string.info_field_autorizacion));
            return true;
        }else if(new Validaciones().validate_formato_fecha(textFechaCaducidad.getText().toString())!=true) {
            textFechaCaducidad.requestFocus();
            textFechaCaducidad.setError(resources.getString(R.string.info_field_fecha));
            return true;
        }
        return false;
    }
    public boolean validarCamposCheque(){
        if (TextUtils.isEmpty(autoCompleteBanco.getText().toString())) {
            autoCompleteBanco.requestFocus();
            autoCompleteBanco.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(textNroCheque.getText().toString())) {
            textNroCheque.requestFocus();
            textNroCheque.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(textNroCuenta.getText().toString())) {
            textNroCuenta.requestFocus();
            textNroCuenta.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(textTitular.getText().toString())) {
            textTitular.requestFocus();
            textTitular.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if(new Validaciones().validate_formato_fecha(textFechaVenci.getText().toString())!=true) {
            textFechaVenci.requestFocus();
            textFechaVenci.setError(resources.getString(R.string.info_field_fecha));
            return true;
        }else if(new Validaciones().validarFechaChequePostfechado(textFechaVenci.getText().toString())!=true) {
            textFechaVenci.requestFocus();
            textFechaVenci.setError(resources.getString(R.string.info_field_fecha_cheque));
            return true;
        }
        return false;
    }

    public int obtenerFormaPago(){
        if(efectivo.isChecked()){
            return 0;
        }else if(cheque.isChecked()){
            return  1;
        }
        else if(retencion.isChecked()){
            return  2;
        }
        return 0;
    }

    public int obtenerRespaldado(){
        if(cheque.isChecked()){
            return 1;
        }
        return 0;
    }
    /***
     ** Adaptador para bancos
     */
    class AutoCompleteAdapterBanco extends CursorAdapter implements AdapterView.OnItemClickListener{

        DBSistemaGestion dbSistemaGestion;
        public AutoCompleteAdapterBanco(Context context) {
            super(CobroY.this,null);
            dbSistemaGestion=new DBSistemaGestion(context);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

            Cursor cursor = null;
            try {
                cursor = dbSistemaGestion.filtrarBancos(
                        (constraint != null ? constraint.toString() : "@@@@"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final int columnIndex = cursor.getColumnIndexOrThrow(Banco.FIELD_descripcion);
            final String str = cursor.getString(columnIndex);
            return str;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int itemColumnIndex = cursor.getColumnIndexOrThrow(Banco.FIELD_codbanco);
            final int descColumnIndex = cursor.getColumnIndexOrThrow(Banco.FIELD_descripcion);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            text1.setText(cursor.getString(itemColumnIndex));
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            text2.setText(cursor.getString(descColumnIndex));

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.fila_banco, parent, false);
            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            String banco_selected = cursor.getString(cursor.getColumnIndexOrThrow(Banco.FIELD_descripcion));
            System.out.println("Banco seleccionado: "+banco_selected);
            banco_identificador=cursor.getString(cursor.getColumnIndexOrThrow(Banco.FIELD_codbanco));
            autoCompleteBanco.setEnabled(false);
            autoCompleteBanco.clearFocus();
            mostrarContenedorCheque();
            ocultarMsgCheque();
        }
    }//Fin adaptador autocomplete Banco
    /*
    Cargar configuracion de la transaccion
     */

    class CargarConfiguracionTransaccionTask extends AsyncTask<Void,Void,Void> {
        private ProgressDialog progress;
        String fecha;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(CobroY.this);
            progress.setTitle("Cargando Configuracion");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progress.isShowing()){
                progress.dismiss();
                emiTrans.setText(fecha);
                codTrans.setText(getIntent().getStringExtra("transaccion"));
                vendeTrans.setText(usuarioid);
                tipoTrans.setText(getIntent().getStringExtra("identificador"));
                cargarConfiguracionTransaccion();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                fecha=sdf.format(new Date());
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void cargarConfiguracionTransaccion(){
        System.out.println("codigo de la transaccion: " + getIntent().getStringExtra("transaccion"));
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(CobroY.this);
        String conf =ext.get(getString(R.string.key_conf_descarga_clientes),"0");
        this.conf_modo_busqueda = Integer.parseInt(conf);
        boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
        if (conf_observacion)mostrarContenedorObservaciones();


    }
/*
    Metodos para ocultar componentes
     */
public void ocultarMsgBanco(){
    if(msgBanco.getVisibility()==View.VISIBLE){
        animar(true,msgBanco);
        msgBanco.setVisibility(View.GONE);
    }
}
    public void mostrarMsgBanco(){
        if(msgBanco.getVisibility()==View.GONE){
            animar(true,msgBanco);
            msgBanco.setVisibility(View.VISIBLE);
        }
    }
    public void ocultarMsgRetencion(){
        if(msgRetencion.getVisibility()==View.VISIBLE){
            animar(true,msgRetencion);
            msgRetencion.setVisibility(View.GONE);
        }
    }
    public void mostrarMsgRetencion(){
        if(msgRetencion.getVisibility()==View.GONE){
            animar(true,msgRetencion);
            msgRetencion.setVisibility(View.VISIBLE);
        }
    }
    public void mostrarContenedorBanco(){
        if(contentBanco.getVisibility()==View.GONE){
            animar(false,contentBanco);
            contentBanco.setVisibility(View.VISIBLE);
        }
    }
    public void ocultarContenedorBanco(){
        if(contentBanco.getVisibility()==View.VISIBLE){
            animar(false,contentBanco);
            contentBanco.setVisibility(View.GONE);
        }
    }
    public void mostrarContenedorRetencion(){
        if(contentRetencion.getVisibility()==View.GONE){
            animar(false,contentRetencion);
            contentRetencion.setVisibility(View.VISIBLE);
        }
    }
    public void ocultarContenedorRetencion(){
        if(contentRetencion.getVisibility()==View.VISIBLE){
            animar(false,contentRetencion);
            contentRetencion.setVisibility(View.GONE);
        }
    }
    public void ocultarMsgCheque(){
        if(msgCheque.getVisibility()==View.VISIBLE){
            animar(true,msgCheque);
            msgCheque.setVisibility(View.GONE);
        }
    }
    public void mostrarMsgCheque(){
        if(msgCheque.getVisibility()==View.GONE){
            animar(true,msgCheque);
            msgCheque.setVisibility(View.VISIBLE);
        }
    }
    public void mostrarContenedorCheque(){
        if(contentCheque.getVisibility()==View.GONE){
            animar(false,contentCheque);
            contentCheque.setVisibility(View.VISIBLE);
        }
    }
    public void ocultarContenedorCheque(){
        if(contentCheque.getVisibility()==View.VISIBLE){
            animar(false,contentCheque);
            contentCheque.setVisibility(View.GONE);
        }
    }
    public void mostrarContenedorObservaciones(){
        if(layoutContenedorObs.getVisibility()==View.GONE){
            animar(false,layoutContenedorObs);
            layoutContenedorObs.setVisibility(View.VISIBLE);
        }
        /*Cargar Valores por Defecto Observacion*/
        observacion.setText(getString(R.string.observacion_defecto_cobroy));
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
    /*
    Metodos para generar detalles del cobro y transaccion
     */
    public boolean guardarTrans(int forma,String banco_id,String nro_cheque,String nro_cuenta,String titular,String vencimiento,String iva,String iva_base,String renta,String renta_base,String estab,String punto,String secuencial,String autorizacion,String caducidad){
        DBSistemaGestion helper= new DBSistemaGestion(CobroY.this);
        GeneradorClaves gen= new GeneradorClaves();
        gen.generarClave();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String fecha = sdf1.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String hora = sdf2.format(new Date());
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String modificacion=sdf3.format(new Date());
        Transaccion transaccion=new Transaccion(gen.generarClave(),codTrans.getText().toString()+"-"+getIntent().getStringExtra("identificador"),observacion.getText().toString()+" ; "+" ; "+";"+total_cobro.getText().toString()+"; "+" ; "+" ; "+" ; "+" ; "+" ; "+" ;",helper.numeroTransacciones(getIntent().getStringExtra("transaccion")+"-"+getIntent().getStringExtra("identificador"),vendedorid),fecha,hora,0,vendedorid,cli_identificador,null,"0000",modificacion);
        System.out.println("Transaccion generada: "+transaccion.toString());
        helper.crearTransaccion(transaccion);
        helper.close();
        //GuardarCobroTask guardarCobroTask= new GuardarCobroTask();
        //guardarCobroTask.execute(transaccion.getId_trans(),String.valueOf(forma),banco_id,nro_cheque,nro_cuenta,titular,vencimiento,iva,iva_base,renta,renta_base,estab,punto,secuencial,autorizacion,caducidad);
        return true;
    }

    /**
     * tarea Asincrona para guardar transaccion
     */
    public String obtenerObservaciones() {
        return observacion.getText().toString()+"; "+"; "+";"+total_cobro.getText().toString()+"; "+" ; "+" ; "+" ; "+" ; "+" ; "+" ;";
    }

    class GuardarTransaccionTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        private String prm_id,prm_forma,prm_bancoid,prm_nrocheque,prm_nrocuenta,prm_titular,prm_venci;
        private String prm_iva,prm_iva_base,prm_renta,prm_renta_base,prm_estab,prm_punto,prm_secuencial,prm_autorizacion,prm_caducidad;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(CobroY.this);
            progress.setTitle("Guardando Transaccion");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {

            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    GuardarDetallesTask guardarCobroTask= new GuardarDetallesTask();
                    guardarCobroTask.execute(prm_id,prm_forma,prm_bancoid,prm_nrocheque,prm_nrocuenta,prm_titular,prm_venci,prm_iva,prm_iva_base,prm_renta,prm_renta_base,prm_estab,prm_punto,prm_secuencial,prm_autorizacion,prm_caducidad);
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            prm_forma=params[0];prm_bancoid=params[1];prm_nrocheque=params[2];prm_nrocuenta=params[3];prm_titular=params[4];prm_venci=params[5];
            prm_iva=params[6];prm_iva_base=params[7];prm_renta=params[8];prm_renta_base=params[9];prm_estab=params[10];prm_punto=params[11];
            prm_secuencial=params[12];prm_autorizacion=params[13];
            //prm_caducidad=params[15];
            try {
                DBSistemaGestion helper= new DBSistemaGestion(CobroY.this);
                GeneradorClaves gen= new GeneradorClaves();
                gen.generarClave();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fecha = sdf1.format(new Date());
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String hora = sdf2.format(new Date());
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String modificacion=sdf3.format(new Date());
                Transaccion transaccion=new Transaccion(gen.generarClave(),getIntent().getStringExtra("transaccion")+"-"+getIntent().getStringExtra("identificador"),obtenerObservaciones(),helper.numeroTransacciones(getIntent().getStringExtra("transaccion")+"-"+getIntent().getStringExtra("identificador"),vendedorid),fecha,hora,0,vendedorid,cli_identificador,null,"0000",modificacion);
                System.out.println("Transaccion generada: "+transaccion.toString());
                helper.crearTransaccion(transaccion);
                prm_id= transaccion.getId_trans();
                helper.close();
                result=true;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }
    }

    /***
     * Tarea asincrona guardar detalles cobro
     */
    class GuardarDetallesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(CobroY.this);
            progress.setTitle("Guardando detalles");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {

            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    try{
                        switch (conf_tipo_envio){
                            case 0:
                                //envio inmediato
                                ArrayList<Transaccion> pendientes = new ArrayList<>();
                                DBSistemaGestion helper = new DBSistemaGestion(CobroY.this);
                                Cursor cursor= helper.consultarTransacciones(getIntent().getStringArrayExtra("accesos"),2,"COBRO",1,0);
                                if(cursor.moveToFirst()){
                                    do{
                                        pendientes.add(new Transaccion(
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_ID_Trans)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_descripcion)),
                                                cursor.getInt(cursor.getColumnIndex(Transaccion.FIELD_numTransaccion)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)),
                                                cursor.getInt(cursor.getColumnIndex(Transaccion.FIELD_band_enviado)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_vendedor_id)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_cliente_id)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_forma_cobro_id)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_referencia)),
                                                cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado))));
                                    }while (cursor.moveToNext());
                                }
                                EnviarTransaccionesPendientes enviarPendientes = new EnviarTransaccionesPendientes(CobroY.this, pendientes);
                                enviarPendientes.ejecutarTarea();
                                alertaTransaccionGuardada();
                                break;
                            case 1:
                                onBackPressed();
                                break;
                        }
                        alertaTransaccionGuardada();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                guardarDetalles(params[0],params[1],params[2],params[3],params[4],params[5],params[6],
                        params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15]
                );
                result=true;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }
    }
/*
Guardar detalles del cobro
 */
public void guardarDetalles(String transaccion,String  forma,String banco_id,String nro_cheque,String nro_cuenta,String titular,String vencimiento,String iva,String iva_base,String renta,String renta_base,String estab,String punto,String secuencial,String autorizacion,String caducidad){
    ArrayList<PCKardex> pckadexList = new ArrayList<>();
    int ind = 0;
    for(DocumentoCobrado doc: this.documentos_cobrados){
        System.out.println("Documento a guardar: "+doc.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String time=sdf.format(new Date());
        GeneradorClaves genkey = new GeneradorClaves();
        if(Double.parseDouble(filaCobro[ind].getText().toString())>0.0){
            PCKardex gen = new PCKardex(
                    genkey.generarClave(),
                    doc.getPckid(),
                    doc.getVencimiento(),
                    doc.getValor(),
                    doc.getCobrado(),
                    0,
                    0,
                    0,
                    0,
                    doc.getEmision(),
                    cli_identificador,
                    null,
                    null,
                    null,
                    null,
                    doc.getForma(),
                    transaccion,
                    doc.getIdvendedor(),doc.getIdcobrador(),//id del cobrador
                    doc.getDocumento(),
                    null,
                    0,
                    null,doc.getCodigo(),
                    doc.getLetra(),
                    null,
                    banco_id,
                    forma,
                    nro_cheque,
                    nro_cuenta,
                    titular,
                    vencimiento,
                    1,
                    obtenerRespaldado(),
                    renta,renta_base,
                    iva,iva_base,
                    estab,punto,
                    secuencial,autorizacion,
                    caducidad,
                    0,
                    doc.getObservacion(),
                    time);
            pckadexList.add(gen);
        }
        ind++;
    }
    /*Guardando Detalles Generados*/
    DBSistemaGestion helper = new DBSistemaGestion(CobroY.this);
    for(PCKardex pck: pckadexList){
        System.out.println("PCKardex Generado: "+pck.toString());
            helper.crearPCKardex(pck);
            helper.marcarCobradoPCKardex(pck.getIdasinado());
    }
    helper.close();
}

    /**
     * Alerta Transaccion Guardada
     */
    public void alertaTransaccionGuardada(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.save_success_transaction,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id3));
        ((TextView)layout.findViewById(R.id.toast_tittle)).setText("Transaccion Registrada");
        ((TextView)layout.findViewById(R.id.toast_subtittle)).setText("La transaccion ha sido registrada correctamente!");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    /**
     *Metodos para la impresion de comprobante
     */
    private void openDialogImpresion(final String param1, final String param2, final String param3, final String param4, final String param5, final String param6, final String param7,
                                     final String param8, final String param9, final String param10, final String param11, final String param12, final String param13, final String param14,
                                     final String param15){
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(CobroY.this);
        quitDialog.setTitle("Desea imprimir el comprobante?");
        //quitDialog.setCancelable(false);
        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ConectarImpresoraTask conectarImpresoraTask = new ConectarImpresoraTask();
                conectarImpresoraTask.execute( param1, param2, param3, param4, param5, param6, param7,
                        param8, param9, param10, param11, param12, param13, param14,
                        param15);
            }});

        if(!conf_impresion_obligatoria){
            quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    GuardarTransaccionTask saveTask= new GuardarTransaccionTask();
                    saveTask.execute(param1, param2, param3, param4, param5, param6, param7,
                            param8, param9, param10, param11, param12, param13, param14,
                            param15);
                }});
        }

        quitDialog.show();
    }

    /**
     * Metodo para conectar la impresora
     */

    class ConectarImpresoraTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        private String prm_forma,prm_bancoid,prm_nrocheque,prm_nrocuenta,prm_titular,prm_venci;
        private String prm_iva,prm_iva_base,prm_renta,prm_renta_base,prm_estab,prm_punto,prm_secuencial,prm_autorizacion,prm_caducidad;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(CobroY.this);
            progress.setTitle("Conectando Impresora");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {

            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    try{
                        openBT();
                        sendData(this.prm_forma,this.prm_bancoid,this.prm_nrocheque,this.prm_nrocuenta,this.prm_titular,this.prm_venci,this.prm_iva,this.prm_iva_base,this.prm_renta,this.prm_renta_base,this.prm_estab,this.prm_punto,this.prm_secuencial,this.prm_autorizacion);
                        impTrans.setText("Conectado");
                        impTrans.setTextColor(getResources().getColor(R.color.successfull));
                    }catch (IOException e){
                        impTrans.setText("Sin Conexion");
                        impTrans.setTextColor(getResources().getColor(R.color.color_danger));
                    }

                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            prm_forma=params[0];
            prm_bancoid=params[1];prm_nrocheque=params[2];prm_nrocuenta=params[3];prm_titular=params[4];prm_venci=params[5];
            prm_iva=params[6];prm_iva_base=params[7];prm_renta=params[8];prm_renta_base=params[9];prm_estab=params[10];prm_punto=params[11];
            prm_secuencial=params[12];prm_autorizacion=params[13];
            try {
                findBT();
                Thread.sleep(1000);
                result=true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }
    }
    /*Cargar metodos para la impresion*/
    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null) {
                //impTrans.setText("Sin Conexion");
                System.out.println("Bluetooth desactivado");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(CobroY.this, "El dispositivo no tiene habilitada la Conexion Bluetooth", Toast.LENGTH_LONG).show();
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(nombre_impresora)) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            System.out.println("Bluetooth activado");
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void
    openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();

            mmOutputStream = mmSocket.getOutputStream();
            byte[] format = { 27, 33, 0 };
            format[2] = ((byte)(0x5 | 0));
            mmOutputStream.write(format);
            printUnicode();
            //print normal text
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
            Toast.makeText(CobroY.this, "Conexion Establecida", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                                impTrans.setText(data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData(String prm1,String prm2,String prm3,String prm4,String prm5,String prm6,String prm7,String prm8,String prm9,String prm10,String prm11,String prm12,String prm13,String prm14) throws IOException {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String fecha = sdf1.format(new Date());
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String hora = sdf2.format(new Date());
            //imprimir cliente, fecha, nombre de la transaccion,transaccion,valor , saldo , valor cobrado
            String encabezado1 = "EMPRESA: "+this.nombreempresa+"\n";
            String encabezado2 = "DIR: "+this.direccionempresa + "\n"
                    + "CLIENTE: "+this.clinombre.getText().toString().toUpperCase() + "\n"
                    + "FECHA: "+fecha+" HORA: " +hora+ "\n";
            printNewLine();
            String detalle1 = "------------------------------------" + "\n"
                            + "TRANSACCION    CUOTA    PAGO    SALDO " + "\n"
                            + "------------------------------------" + "\n";
            printText(encabezado1);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(encabezado2);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(detalle1);
            String detalle2 = generarDetallesCobrados();

            String span5 = "SALDOS:";
            String detalle5 = generarSaldosRestantes();

            String span1 = "TOTAL COBRADO: "; String detalle3 = total_cobro.getText().toString()+ "\n";
            String span2 = "COBRADOR: ";String detalle4 = vendeTrans.getText().toString()+ "\n";

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(detalle2);

            printText(span5);
            printNewLine();
            printText(detalle5);

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            printText(span1);
            printText(detalle3);

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            printText(span2);
            printText(detalle4);

            printUnicode();
            printNewLine();
            printNewLine();
            this.closeBT();
            GuardarTransaccionTask saveTask= new GuardarTransaccionTask();
            saveTask.execute(prm1,prm2,prm3,prm4,prm5,prm6,prm7,prm8,prm9,prm10,prm11,prm12,prm13,prm14);
        } catch (Exception e) {
            Toast.makeText(CobroY.this, "No se puede completar la impresion del comprobante, revise que la impresora este encendida y que el dispositivo tenga activada la conexion bluetooth", Toast.LENGTH_LONG).show();
            reintentarImpresion(prm1,prm2,prm3,prm4,prm5,prm6,prm7,prm8,prm9,prm10,prm11,prm12,prm13,prm14);
        }
    }

    public void printUnicode(){
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.flush();
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarPreferenciasEmpresa(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(CobroY.this);
        nombreempresa=extraerConfiguraciones.get(getString(R.string.key_empresa_nombre),getString(R.string.pref_nombre_empresa));
        direccionempresa=extraerConfiguraciones.get(getString(R.string.key_empresa_direccion),getString(R.string.pref_direccion_empresa));
    }

    public String  generarDetallesCobrados(){
        String result ="";
        for(int i=0; i<count;i++){
            result += new Utils().completeChars(filaCodigo[i].getText().toString(),15)+"  "+new Utils().completeChars(filaValor[i].getText().toString(),5)+"   "+new Utils().completeChars(filaCobro[i].getText().toString(),5)+"   "+new Utils().completeChars(filaSaldo[i].getText().toString(),5)+"\n";
        }
        return result;
    }

    public String  generarSaldosRestantes(){
        String result ="";
        for(int i=0; i<fact.length; i++){
            if(fact[i]!=null){
                result += fact[i].getText().toString()+"    =    "+saldo_fact[i].getText().toString()+"\n";
            }
        }
        return result;
    }

    private void reintentarImpresion(final String prm1, final String prm2, final String prm3, final String prm4, final String prm5, final String prm6, final String prm7, final String prm8, final String prm9, final String prm10, final String prm11, final String prm12, final String prm13, final String prm14){
        android.support.v7.app.AlertDialog.Builder quitDialog
                = new android.support.v7.app.AlertDialog.Builder(CobroY.this);
        quitDialog.setTitle("No se pudo imprimir, intentar nuevamente?");

        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ConectarImpresoraTask conectarImpresoraTask = new ConectarImpresoraTask();
                conectarImpresoraTask.execute( prm1,prm2,prm3,prm4,prm5,prm6,prm7,prm8,prm9,prm10,prm11,prm12,prm13,prm14);
            }});

        if(!conf_impresion_obligatoria){
            quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    GuardarTransaccionTask saveTask = new GuardarTransaccionTask();
                    saveTask.execute(prm1,prm2,prm3,prm4,prm5,prm6,prm7,prm8,prm9,prm10,prm11,prm12,prm13,prm14);
                }});
        }
        quitDialog.show();
    }
    /*
        Clase para enviar transacciones en grupo
     */
    class EnviarTransaccionesPendientes {
        private Context context;
        private ArrayList<Transaccion> transacciones;
        private String ip;
        private String port;
        private String url;
        private String ws;
        private String base;
        private String codemp;

        public EnviarTransaccionesPendientes(Context context, ArrayList<Transaccion> trans) {
            this.context = context;
            this.transacciones = trans;
            cargarPreferenciasConexion();
        }
        public void cargarPreferenciasConexion(){
            ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
            ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
            port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
            url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
            ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_transacciones),context.getString(R.string.pref_ws_transacciones));
            base=extraerConfiguraciones.get(context.getString(R.string.key_empresa_base),context.getString(R.string.pref_base_empresa_default));
            codemp=extraerConfiguraciones.get(context.getString(R.string.key_empresa_codigo),context.getString(R.string.pref_codigo_empresa_default));
        }

        public void ejecutarTarea(){
            EnviarDetallesTransaccionTask async1=new EnviarDetallesTransaccionTask();
            async1.execute();
        }
        /*Tarea Asincrona*/
        class EnviarDetallesTransaccionTask extends AsyncTask<String,Integer,Boolean> {
            private ProgressDialog progress;
            private int num_enviados;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress=new ProgressDialog(context);
                progress.setCancelable(false);
                progress.setTitle("Enviando Transaccion");
                progress.setMessage("Espere...");
                progress.show();
            }

            @Override
            protected void onPostExecute(Boolean  response) {
                super.onPostExecute(response);
                if(progress.isShowing()){
                    progress.dismiss();
                    Toast ts= Toast.makeText(context, "Numero de transacciones enviadas: "+num_enviados,Toast.LENGTH_SHORT);
                    ts.show();
                    onBackPressed();
                }
            }

            @Override
            protected Boolean doInBackground(String... params) {
                num_enviados = 0;
                for(Transaccion trans: transacciones){
                    HttpParams httpParameters = new BasicHttpParams();
                    int timeoutConnection = 15000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                    int timeoutSocket = 15000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                    HttpClient httpClient = new DefaultHttpClient(httpParameters);
                    httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                    HttpGet get = new HttpGet("http://"+ip+":"+port+url+ws+"/"+ generarTramaEnvio(trans.getId_trans()));
                    get.setHeader("content-type", "application/json");
                    try
                    {
                        HttpResponse resp = httpClient.execute(get);
                        String respStr = EntityUtils.toString(resp.getEntity());
                        System.out.println("Respuesta transaccoin: "+respStr);
                        JSONObject obj = new JSONObject(respStr);
                        String response = obj.getString("estado");
                        if(new ValidateReferencia().validate(response)) {
                            trans.setReferencia(response);
                            modificarTransaccion(trans);
                            num_enviados ++;
                        }
                        Thread.sleep(50);
                    }catch(Exception ex){
                        Log.e("ServicioRest", "Error!", ex);
                    }
                }
                return true;
            }
        }
        public boolean modificarTransaccion(Transaccion transaccion){
            DBSistemaGestion helper= new DBSistemaGestion(context);
            transaccion.setBand_enviado(1);
            helper.modificarTransaccion(transaccion);
            helper.close();
            return true;
        }

        public String generarTramaEnvio(String transid){
            String trama = "";
            try{
                List<IVKardex_Serialize_Envio> array1 = new ArrayList();
                List<PKardex_Envio> array2= new ArrayList();
                List<PCKardex> array3= new ArrayList();

                DBSistemaGestion helper= new DBSistemaGestion(context);
                Cursor cursor=helper.consultarIVKardex(transid);
                if(cursor.moveToFirst()){
                    do{
                        IVKardex_Serialize_Envio ivKardex = new IVKardex_Serialize_Envio(
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cantidad)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_precio_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_bodega_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_inventario_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_band_aprobado))
                        );
                        System.out.println("Cargando IVKArdex: "+ivKardex.toString());
                        array1.add(ivKardex);
                    }while (cursor.moveToNext());
                }

                Cursor cursor1=helper.consultarPCKardex(transid);
                if(cursor1.moveToFirst()){
                    do{
                        PKardex_Envio pcka= new PKardex_Envio();
                        PCKardex pck= new PCKardex();
                        pcka.setId_asignado(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idasignado)));
                        pcka.setTsf_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_codforma)));
                        pcka.setValor_cancelado(cursor1.getDouble(cursor1.getColumnIndex(PCKardex.FIELD_pagado)));
                        pcka.setIdcobrador(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idcobrador)));
                        pcka.setIdvendedor(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idvendedor)));
                        pcka.setObservacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_observacion)).replace("/",":"));

                        pck.setBanco_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_banco_id)));
                        pck.setForma_pago(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_forma_pago)));
                        pck.setTitular(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_titular)));
                        pck.setNumero_cuenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_numero_cuenta)));
                        pck.setIva(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva)));
                        pck.setIva_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva_base)));
                        pck.setRenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta)));
                        pck.setRenta_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta_base)));
                        pck.setNum_ser_estab(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                        pck.setNum_ser_punto(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_punto)));
                        pck.setNum_ser_secuencial(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                        pck.setAutorizacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_autorizacion)));
                        pck.setCaducidad(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_caducidad)));
                        pck.setIdvendedor(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idvendedor)));
                        System.out.println("Cargando PCKArdex: "+pcka.toString());
                        array2.add(pcka);
                        array3.add(pck);
                    }while (cursor1.moveToNext());
                }
                cursor.close();
                cursor1.close();
                helper.close();
                /*Preparando Objeto para el envio*/
                Transaccion_Serialize_Envio trans_send = new Transaccion_Serialize_Envio();
                Cursor cursor2=helper.obtenerTransaccion(transid);
                if(cursor2.moveToFirst()){
                    trans_send.setId_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                    trans_send.setIdentificador(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_identificador)));
                    trans_send.setNumTransaccion(cursor2.getInt(cursor2.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                    trans_send.setFecha_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_fecha_trans)));
                    trans_send.setHora_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_hora_trans)));
                    trans_send.setCliente_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_cliente_id)));
                    trans_send.setForma_pago(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_forma_cobro_id)));
                    trans_send.setDescripcion(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_descripcion)));
                    trans_send.setVendedor_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_vendedor_id)));
                }
                cursor2.close();
                if(array3.size()>0){
                    trans_send.setBanco_id(array3.get(0).getBanco_id());
                    trans_send.setForma_pago(array3.get(0).getForma_pago());
                    trans_send.setTitular(array3.get(0).getTitular());
                    trans_send.setNumero_cheque(array3.get(0).getNumero_cheque());
                    trans_send.setNumero_cuenta(array3.get(0).getNumero_cuenta());
                    trans_send.setCheque_fecha_vencimiento(array3.get(0).getPago_fecha_vencimiento());
                    trans_send.setIva(array3.get(0).getIva());
                    trans_send.setIva_base(array3.get(0).getIva_base());
                    trans_send.setRenta(array3.get(0).getRenta());
                    trans_send.setRenta_base(array3.get(0).getRenta_base());
                    trans_send.setEstablecimiento(array3.get(0).getNum_ser_estab());
                    trans_send.setPunto(array3.get(0).getNum_ser_punto());
                    trans_send.setSecuencial(array3.get(0).getNum_ser_secuencial());
                    trans_send.setAutorizacion(array3.get(0).getAutorizacion());
                    trans_send.setCaducidad(array3.get(0).getCaducidad());
                }
                trans_send.setIvkardex(array1);
                trans_send.setPckardex(array2);
                trama = "TRANSACCION;"+base+";trama1;2016-01-01%2012:00:00;"+codemp+";"+trans_send.toString();
                System.out.println("Trama para envio: "+trama);
                trama = URLEncoder.encode(trama,"UTF-8");
            }catch (UnsupportedEncodingException ex){
                ex.printStackTrace();
            }
            return trama;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(CobroY.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cobroy, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.cobrar_mora);
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(CobroY.this);
        boolean pref=ext.getBoolean(getString(R.string.key_conf_calcula_interes),false);
        if(pref)register.setChecked(true);
        else register.setChecked(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.cobrar_mora:
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(CobroY.this);
                    if(!item.isChecked()){
                        item.setChecked(true);
                        ext.updateBool(getString(R.string.key_conf_calcula_interes),true);
                        this.reload();
                    } else{
                        item.setChecked(false);
                        ext.updateBool(getString(R.string.key_conf_calcula_interes),false);
                        this.reload();
                    }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void reload() {
        Intent intent = new Intent(CobroY.this, CobroY.class);
        intent.putExtra("transaccion", getIntent().getStringExtra("transaccion"));
        intent.putExtra("identificador", getIntent().getStringExtra("identificador"));
        intent.putExtra("accesos", getIntent().getStringArrayExtra("accesos"));
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
