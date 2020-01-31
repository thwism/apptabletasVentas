package ibzssoft.com.ishidamovile.visita.crear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

public class VisitaY extends AppCompatActivity implements
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener
{
    private TextView date1;
    private TextView hora;

    private static final int LIMIT= 200;
    private AutoCompleteTextView autoCompleteCliente;
    private TextView codTrans,emiTrans,tipoTrans,vendeTrans,cliCI,clinombre,cliComercial,cliDir,cliTel,cliEmail;
    private CardView layoutContenedorObs;
    private TextView [] filaDocs,filaCodigo;
    private TextView [] filaFE,filaFV,filaPL,filaDV,filaValor,filaVC,filaSaldo,filaLetra;
    private CheckBox [] seleccion;
    private TextView [] indice;
    private Resources resources;
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaCobro;
    private String [] insPckID;
    private String [] formas;
    private String [] idvendedor;
    private String [] idcobrador;
    private boolean [] cobrado;
    private EditText observacion,contesta,relacion;
    /*Campos para la retencion*/
    private  String cli_identificador;
    private double dias_gracia=0;
    private int count = 0;
    /*Variable global para documentos cobrados*/
    private int  conf_modo_busqueda;
    private String vendedorid,usuarioid;
    private Button guardarVisita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_y);
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
        observacion = (EditText)findViewById(R.id.txtnuevoCobroObservacion);
        contesta = (EditText)findViewById(R.id.txtnuevaVisitaContesta);
        relacion = (EditText)findViewById(R.id.txtnuevaVisitaRelacion);
        codTrans=(TextView)findViewById(R.id.nuevoCobroCodTrans);
        emiTrans=(TextView)findViewById(R.id.nuevoCobroFecha);
        tipoTrans=(TextView)findViewById(R.id.nuevoCobroTipo);
        vendeTrans=(TextView)findViewById(R.id.nuevoCobroVendedor);
        cliCI=(TextView)findViewById(R.id.nuevoCobroCI_RUC);
        clinombre=(TextView)findViewById(R.id.nuevoCobroNombres);
        cliComercial=(TextView)findViewById(R.id.nuevoCobroNombreAlt);
        cliDir=(TextView)findViewById(R.id.nuevoCobroDireccion);
        cliTel=(TextView)findViewById(R.id.nuevoCobroTelefono);
        cliEmail=(TextView)findViewById(R.id.nuevoCobroCorreo);
        layoutContenedorObs=(CardView) findViewById(R.id.contenedorObservaciones);
        date1 = (TextView)findViewById(R.id.fecha_inicio);
        hora = (TextView)findViewById(R.id.hora);
        loadDates();
        filaDocs = new  TextView [LIMIT];
        filaCodigo = new  TextView [LIMIT];
        filaLetra= new  TextView [LIMIT];
        cobrado = new boolean[LIMIT];
        insPckID = new String [LIMIT];
        formas = new String [LIMIT];
        idvendedor= new String [LIMIT];
        idcobrador = new String [LIMIT];
        seleccion=new  CheckBox[LIMIT];
        filaFE= new  TextView [LIMIT];filaFV= new  TextView [LIMIT];filaPL= new  TextView [LIMIT];filaDV= new  TextView [LIMIT];filaValor= new  TextView [LIMIT];filaVC= new  TextView [LIMIT];/*filaSVencer= new  TextView [LIMIT];filaVencido=new  TextView [LIMIT];*/filaSaldo=new  TextView [LIMIT];
        indice=new  TextView[LIMIT];

        cobrado = new boolean[LIMIT];
        /**
         * campos opciones de pago
         */
        cli_identificador="";
        tabla= (TableLayout)findViewById(R.id.nuevoCobroTableLayout);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas.span=1;
        layoutCeldaCobro = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaCobro.span=2;


        AutoCompleteAdapter adapter = this.new AutoCompleteAdapter(getApplicationContext());
        autoCompleteCliente.setAdapter(adapter);
        autoCompleteCliente.setOnItemClickListener(adapter);
        this.conf_modo_busqueda= 0;
        this.guardarVisita=(Button)findViewById(R.id.guardar);
        this.guardarVisita.setEnabled(false);
        this.date1.setOnClickListener(this);
        this.hora.setOnClickListener(this);
        this.guardarVisita.setOnClickListener(this);
        cargarConfiguracionCobro();
        CargarConfiguracionTransaccionTask taskHeadTransaction= new CargarConfiguracionTransaccionTask();
        taskHeadTransaction.execute();
    }

    public void loadDates(){
        hora.setText(new ParseDates().changeDateToStringSimpleTime());
        date1.setText(new ParseDates().changeDateToStringSimple1(new ParseDates().sumarRestarDiasYear(new Date(),-1)));
    }

    @Override
    public void onResume(){
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        date1.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = hourOfDay+":"+(minute);
        hora.setText(time);
    }

    public void cargarConfiguracionCobro(){
        ExtraerConfiguraciones ext= new ExtraerConfiguraciones(VisitaY.this);
        String dg = ext.get(getString(R.string.key_conf_dias_gracia_mora),"0");
        dias_gracia = Integer.parseInt(dg);
    }
    /**
       *Adaptador para autocomplete cliente
     **/
    class AutoCompleteAdapter extends CursorAdapter implements AdapterView.OnItemClickListener{

        DBSistemaGestion dbSistemaGestion;
        public AutoCompleteAdapter(Context context) {
            super(VisitaY.this,null);
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
            guardarVisita.setEnabled(true);
            agregarFilasIniciales(idCliente);
        }
    }//Fin adaptador autocomplete Cliente
    public void agregarFilasIniciales(String id_client){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarCarteraConInteres(id_client,dias_gracia,0.0,false);
        TableRow fila;
        count=0;
        if(cursor.moveToFirst()){
            do{
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

                cobrado[count]=false;

                fila.setBackgroundColor(resources.getColor(R.color.windowBackground));

                this.seleccion[count] = new CheckBox(this);
                this.seleccion[count].setTag(count);
                this.seleccion[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                /*
                    Calcular Saldo Inicial
                 */
                fila.addView(indice[count]);
                fila.addView(filaDocs[count]);
                fila.addView(filaLetra[count]);
                fila.addView(filaFE[count]);
                fila.addView(filaPL[count]);
                fila.addView(filaFV[count]);
                fila.addView(filaDV[count]);
                fila.addView(filaValor[count]);
                fila.addView(filaVC[count]);
                fila.addView(filaSaldo[count]);
                fila.addView(seleccion[count]);
                tabla.addView(fila);
                count++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
    }




    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }
/*
    Metodos para guardar el cobro
 */
public boolean validarCamposVacios() {
    if (TextUtils.isEmpty(contesta.getText().toString())) {
        contesta.requestFocus();
        contesta.setError(resources.getString(R.string.info_field_required));
        return true;
    }
    if (TextUtils.isEmpty(relacion.getText().toString())) {
        relacion.requestFocus();
        relacion.setError(resources.getString(R.string.info_field_required));
        return true;
    }
    if (TextUtils.isEmpty(observacion.getText().toString())) {
        observacion.requestFocus();
        observacion.setError(resources.getString(R.string.info_field_required));
        return true;
    }
    return false;
}


    class CargarConfiguracionTransaccionTask extends AsyncTask<Void,Void,Void> {
        private ProgressDialog progress;
        String fecha;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(VisitaY.this);
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
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(VisitaY.this);
        String conf =ext.get(getString(R.string.key_conf_descarga_clientes),"0");
        this.conf_modo_busqueda = Integer.parseInt(conf);
        boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
        if (conf_observacion)mostrarContenedorObservaciones();


    }

    public void mostrarContenedorObservaciones(){
        if(layoutContenedorObs.getVisibility()==View.GONE){
            animar(false,layoutContenedorObs);
            layoutContenedorObs.setVisibility(View.VISIBLE);
        }
        /*Cargar Valores por Defecto Observacion*/
        observacion.setText(getString(R.string.observacion_defecto_visitay));
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

    /**
     * tarea Asincrona para guardar transaccion
     */
    public String obtenerObservaciones() {
            //observacion,solitante,tiempo entrega,forma,validez,ruc, razon social, nombre comercial, direccion,telefono
            return observacion.getText().toString() +" ; " + " ; "+ " ; "  + " ; "  + " ; " + " ; "  + " ; " +  " ; "  + " ; "+ " ; "+" ; "+" ; "+date1.getText().toString()+";"+hora.getText().toString()+";"+contesta.getText().toString()+";"+relacion.getText().toString();
    }

    class GuardarTransaccionTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        private String prm_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(VisitaY.this);
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
                    guardarCobroTask.execute(prm_id);
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                DBSistemaGestion helper= new DBSistemaGestion(VisitaY.this);
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
            progress=new ProgressDialog(VisitaY.this);
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
                        onBackPressed();
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
                DBSistemaGestion helper = new DBSistemaGestion(VisitaY.this);
                ArrayList<PCKardex> detalles = obtenerLetrasMarcadas(params[0]);
                for(PCKardex letra : detalles){
                    helper.crearPCKardex(letra);
                    System.out.println("PCKardex Generado: "+letra.toString());
                }
                result=true;
                helper.close();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }
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

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(VisitaY.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fecha_inicio:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        VisitaY.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setThemeDark(true);
                dpd.dismissOnPause(false);
                dpd.showYearPickerFirst(false);
                dpd.setTitle("Fecha proxima visita");
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.hora:
                Calendar time = Calendar.getInstance();
                TimePickerDialog dtd = TimePickerDialog.newInstance(
                        VisitaY.this,
                        time.get(Calendar.HOUR),
                        time.get(Calendar.MINUTE),
                        true);
                dtd.setThemeDark(true);
                dtd.dismissOnPause(false);
                dtd.setTitle("Hora proxima visita");
                dtd.show(getFragmentManager(), "Timepickerdialog");
                break;
            case R.id.guardar:
                if(!validarCamposVacios()){
                    if (validarSeleccionados()){
                        android.support.v7.app.AlertDialog.Builder quitDialog
                                = new android.support.v7.app.AlertDialog.Builder(VisitaY.this);
                        quitDialog.setTitle("Está seguro de guardar la transaccion?");

                        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                GuardarTransaccionTask saveTask = new GuardarTransaccionTask();
                                saveTask.execute();
                            }});
                        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }});

                        quitDialog.show();
                    }else Toast.makeText(VisitaY.this,"Seleccione al menos una letra", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public ArrayList<PCKardex> obtenerLetrasMarcadas(String transaccion){
        ArrayList<PCKardex> pckardexList= new ArrayList<>();
        for(int i= 0; i<this.count; i++){
            if(seleccion[i].isChecked()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String time=sdf.format(new Date());
                GeneradorClaves genkey = new GeneradorClaves();
                PCKardex pcKardex = new PCKardex();
                pcKardex.setIdcartera(genkey.generarClave());
                pcKardex.setIdasinado(insPckID[i]);
                pcKardex.setFechavenci(filaFV[i].getText().toString());
                pcKardex.setValor(Double.parseDouble(filaValor[i].getText().toString()));
                pcKardex.setFechaemision(filaFE[i].getText().toString());
                pcKardex.setIdprovcli(cli_identificador);
                pcKardex.setForma_pago(formas[i]);
                pcKardex.setTransid(transaccion);
                pcKardex.setIdvendedor(idvendedor[i]);
                pcKardex.setIdcobrador(idcobrador[i]);
                pcKardex.setTrans(filaDocs[i].getText().toString());
                pcKardex.setCantcheque(0);
                pcKardex.setOrdencuota(filaLetra[i].getText().toString());
                pcKardex.setBand_generado(1);
                pcKardex.setObservacion("VISITA GENERADA: DOCUMENTO : "+insPckID[i]);
                pckardexList.add(pcKardex);

            }
        }
        return pckardexList;
    }
    public boolean validarSeleccionados(){
        for(int i=0; i<this.count; i++){
            if(seleccion[i].isChecked())
                return true;
        }
        return false;
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

}
