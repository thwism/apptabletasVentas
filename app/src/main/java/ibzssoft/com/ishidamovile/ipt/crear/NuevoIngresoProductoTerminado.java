package ibzssoft.com.ishidamovile.ipt.crear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.Validaciones;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

public class NuevoIngresoProductoTerminado extends AppCompatActivity implements View.OnClickListener {
    /*
        Campos de la cabecera
     */
    private TextView emision, bodega, transaccion, precio, vendedor;

    /*
        Campos de validacion
     */
    private int count;
    private String  cli_id;
    /*
        Contenedores de la vista
     */
    private CardView contenedor_observaciones;
    /*
        Campos para la tabla de items [VISTA]
     */
    private EditText [] numero, cantidad;
    private int [] bandiva;
    private int [] bandpromo;
    private int [] numprecio;
    private String [] iditems,idpadres,keypadres;
    private int [] descpromo;
    private CheckBox[] seleccion;
    private AutoCompleteTextView [] descripcion;
    private EditText[] existencias;
    /*
        Recursos para inicializar tabla
     */
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila, layoutCeldaNro, layoutCeldas, layoutCeldaDescripcion;
    /*
        Botones de guardado y agregacion
     */
    private Button guardar, agregar;
    private ToggleButton editar, eliminar;
    /*
        Transaccion observacion
     */
    private TextView trans_observacion;
    private Resources resources;

    private String idvendedor,idusuario,lineas,bodegas;
    private int decimales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.inicializarCamposFormulario();
        this.cargarPreferencias();
        CargarConfiguracionTransaccion load_config = new CargarConfiguracionTransaccion();
        load_config.execute();
        this.inicializarCamposTabla();
    }
    public void cargarPreferencias(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(this);
        this.idvendedor = ext.get(getString(R.string.key_act_ven),getString(R.string.pref_act_ven_default));
        this.lineas = ext.get(getString(R.string.key_act_lin),getString(R.string.pref_act_lin_default));
        this.idusuario = ext.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
        this.bodegas = ext.get(getString(R.string.key_act_bod),getString(R.string.pref_act_bod_default));
        String dec= ext.get(getString(R.string.key_empresa_decimales),"2");
        this.decimales = Integer.parseInt(dec);
    }
    public void inicializarCamposFormulario(){
     /*
        Campos de la transaccion
     */
        this.emision= (TextView)findViewById(R.id.nuevoPedidoFecha);
        this.bodega= (TextView)findViewById(R.id.nuevoPedidoBodega);
        this.precio= (TextView)findViewById(R.id.nuevoPedidoPrecio);
        this.transaccion= (TextView)findViewById(R.id.nuevoPedidoTrans);
        this.vendedor= (TextView)findViewById(R.id.nuevoPedidoVendedor);

        /*
        * Campos de validacion
        */
        this.count = 0;
        this.cli_id  = "";
        /*
            Contenedores de la vista
        */
        this.contenedor_observaciones= (CardView)findViewById(R.id.contenedorObservaciones);

        /*
            Campos observacion transaccion
         */
        trans_observacion = (TextView)findViewById(R.id.txtNuevoPedidoObservacion);
    }

    public void inicializarCamposTabla(){
        /*
            Campos de la tabla de items [VISTA]
         */
        seleccion= new CheckBox[100];
        numero= new EditText[100];
        descripcion= new AutoCompleteTextView[100];
        existencias = new EditText[100];
        cantidad = new EditText[100];
        bandiva = new int[100];
        bandpromo = new int[100];
        numprecio = new int[100];
        iditems = new String[100];
        idpadres= new String[100];
        keypadres= new String[100];
        descpromo = new int[100];
        /*
            Recursos para inicializar tabla
         */
        tabla = (TableLayout) findViewById(R.id.nuevoPedidoTableLayout);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100, 1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutCeldaNro = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        layoutCeldaDescripcion.span = 2;
        layoutCeldas.span = 1;
        layoutCeldaNro.span = 1;
        editar = (ToggleButton)findViewById(R.id.editar);
        eliminar = (ToggleButton) findViewById(R.id.eliminar);
        guardar = (Button)findViewById(R.id.guardar);
        agregar = (Button) findViewById(R.id.agregar);
        resources = this.getResources();
        guardar.setOnClickListener(this);
        editar.setOnClickListener(this);
        agregar.setOnClickListener(this);
        eliminar.setOnClickListener(this);
    }
    /*
        * Cargar Configuracion de la Transaccion
     */
    class CargarConfiguracionTransaccion extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;
        String conf_emision, conf_bodega, conf_transaccion, conf_precio, conf_vendedor;
        String [] conf_precios;
        int conf_limite, conf_num_pcg, conf_max_docs,conf_dias,conf_modo;
        private boolean conf_observacion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(NuevoIngresoProductoTerminado.this);
            progress.setTitle("Cargando Configuracion");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progress.isShowing()) {
                progress.dismiss();
                emision.setText(this.conf_emision);
                bodega.setText(this.conf_bodega);
                precio.setText(this.conf_precio);
                vendedor.setText(this.conf_vendedor);
                transaccion.setText(this.conf_transaccion);
                if(conf_observacion);mostrarContenedorObservaciones();
                editar.setEnabled(false);eliminar.setEnabled(false);
                agregar.setEnabled(false);guardar.setEnabled(false);
                agregarFila();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
                Cursor cursor = helper.consultarGNTrans(getIntent().getStringExtra("transaccion"));
                conf_transaccion = getIntent().getStringExtra("transaccion");
                conf_vendedor= idusuario;
                if (cursor.moveToFirst()) {
                    String num_filas = cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_numfilas));
                    String max_pck = cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_maxdocs));
                    conf_limite = (Integer.parseInt(num_filas));
                    conf_max_docs = (Integer.parseInt(max_pck));
                    String conf_dg=cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_diasgracia));
                    conf_dias =(Integer.parseInt(conf_dg));
                    Cursor cursor1 = helper.consultarBodega(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                    if (cursor1.moveToFirst()) {
                        conf_bodega = cursor1.getString(cursor1.getColumnIndex(Bodega.FIELD_codbodega));
                    }
                    /*Configuracion de observaciones y precios*/
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(NuevoIngresoProductoTerminado.this);
                    conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                    String conf =ext.get(getString(R.string.key_conf_descarga_clientes),"0");
                    conf_precios = ext.getArray(getString(R.string.key_act_list_price),null);
                    conf_modo = Integer.parseInt(conf);
                    cursor1.close();
                    if (cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")) {
                        conf_precio = getString(R.string.info_precio_pcgrupo_enabled);
                        conf_num_pcg = ext.configuracionPreciosGrupos();
                    } else {
                        conf_precio =  getString(R.string.info_precio_pcgrupo_disable);
                    }
                }
                cursor.close();
                helper.close();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                conf_emision = sdf.format(new Date());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

/*
        Clase para seleccionar cliente
     */
/*Auto text Adapter*/
    class AutoCompleteProductoSelected extends CursorAdapter implements AdapterView.OnItemClickListener {
        DBSistemaGestion dbSistemaGestion;
        int posicion;
        public AutoCompleteProductoSelected(Context context, int row) {
            super(NuevoIngresoProductoTerminado.this, null);
            dbSistemaGestion = new DBSistemaGestion(context);
            this.posicion = row;
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            Cursor cursor = null;
            try {
                cursor = dbSistemaGestion.buscarItemsDescripcion(
                        (constraint != null ? constraint.toString() : "@@@@"), lineas.split(","),bodegas.split(","));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final String str = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion));
            return str;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int itemColumnIndex = cursor.getColumnIndexOrThrow(IVInventario.FIELD_cod_item);
            final int descColumnIndex = cursor.getColumnIndexOrThrow(IVInventario.FIELD_descripcion);
            TextView text1 = (TextView) view.findViewById(R.id.codigo);
            text1.setText(cursor.getString(itemColumnIndex));
            TextView text2 = (TextView) view.findViewById(R.id.descripcion);
            text2.setText(cursor.getString(descColumnIndex));
            TextView text3 = (TextView) view.findViewById(R.id.existencia);
            text3.setText(cursor.getString(12));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.fila_autocomplete_producto, parent, false);
            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            String idItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_identificador));
            int ivaItem = cursor.getInt(cursor.getColumnIndexOrThrow(IVInventario.FIELD_band_iva));
            String descItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_descripcion));
            String exist = cursor.getString(12);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(descripcion[posicion], InputMethodManager.SHOW_IMPLICIT);
            descripcion[posicion].setText(descItem);
            existencias[posicion].setText(exist);
            cantidad[posicion].setText("");
            cantidad[posicion].setEnabled(true);
            cantidad[posicion].requestFocus();
            /*Arreglos normales*/
            bandiva[posicion] = ivaItem;
            iditems[posicion] = idItem;
            dbSistemaGestion.close();
        }
    }
    //fin clase autoadapter



    /*
        Metodos para ocultar contenedores
     */
    //Observaciones
    public void mostrarContenedorObservaciones() {
        if (contenedor_observaciones.getVisibility() == View.GONE) {
            animar(false, contenedor_observaciones);
            contenedor_observaciones.setVisibility(View.VISIBLE);
        }
        /*Cargar Valores por Defecto Observacion*/
        trans_observacion.setText(getString(R.string.observacion_defecto_ipt));
    }
    //animar
    private void animar(boolean mostrar, CardView layout) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        } else {
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        }
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layout.setLayoutAnimation(controller);
        layout.startAnimation(animation);
    }
    /*
        Metodo para redondear numero a 2 decimales
     */
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
    /***
     * Metodos para agregar/ eliminar / editar filas de la tabla de items
     */
    public void agregarFila(){

            editar.setEnabled(false);guardar.setEnabled(false);
            eliminar.setEnabled(false);agregar.setEnabled(false);
            editar.setChecked(false);eliminar.setChecked(false);

            TableRow fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            this.numero[count] = new EditText(this);
            this.numero[count].setText(String.valueOf(count + 1));
            this.numero[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.numero[count].setLayoutParams(layoutCeldas);
            this.numero[count].setBackgroundResource(R.drawable.text_field);
            this.numero[count].setGravity(Gravity.CENTER);
            this.numero[count].setEnabled(false);
            this.numero[count].setTextSize(14);
            this.numero[count].setPadding(5, 5, 5, 5);

            AutoCompleteProductoSelected adapter = this.new AutoCompleteProductoSelected(getApplicationContext(), count);
            this.descripcion[count] = new AutoCompleteTextView(this);
            this.descripcion[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.descripcion[count].setHint(getString( R.string.hint_producto));
            this.descripcion[count].setDropDownWidth(600);
            this.descripcion[count].setEnabled(true);
            this.descripcion[count].setLayoutParams(layoutCeldaDescripcion);
            this.descripcion[count].setBackgroundResource(R.drawable.text_field);
            this.descripcion[count].setGravity(Gravity.CENTER_VERTICAL);
            this.descripcion[count].setTextSize(14);
            this.descripcion[count].setPadding(5, 5, 5, 5);
            this.descripcion[count].setOnItemClickListener(adapter);
            this.descripcion[count].setAdapter(adapter);

            this.existencias[count] = new EditText(this);
            this.existencias[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.existencias[count].setLayoutParams(layoutCeldas);
            this.existencias[count].setBackgroundResource(R.drawable.text_field);
            this.existencias[count].setGravity(Gravity.CENTER);
            this.existencias[count].setHint("0");
            this.existencias[count].setInputType(InputType.TYPE_CLASS_NUMBER);
            this.existencias[count].setEnabled(false);
            this.existencias[count].setTextSize(14);
            this.existencias[count].setPadding(5, 5, 5, 5);

            this.cantidad[count] = new EditText(this);
            this.cantidad[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.cantidad[count].setLayoutParams(layoutCeldas);
            this.cantidad[count].setBackgroundResource(R.drawable.text_field);
            this.cantidad[count].setGravity(Gravity.CENTER);
            this.cantidad[count].setHint("0");
            this.cantidad[count].setInputType(InputType.TYPE_CLASS_NUMBER);
            this.cantidad[count].setEnabled(false);
            this.cantidad[count].setTextSize(14);
            this.cantidad[count].setPadding(5, 5, 5, 5);

            this.seleccion[count] = new CheckBox(this);
            this.seleccion[count].setMinimumHeight(50);
        /*Agregando identificador por fila*/
            this.seleccion[count].setTag(count);
            this.cantidad[count].setTag(count);
            this.keypadres[count] = new GeneradorClaves().generarClave();
        /*listener para campo cantidad*/
            this.cantidad[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    if(new Validaciones().validar_enteros(cantidad[position].getText().toString())){
                        cantidad[position].setEnabled(false);
                        descripcion[position].setEnabled(false);
                        editar.setEnabled(true);guardar.setEnabled(true);
                        eliminar.setEnabled(true);
                        agregar.setEnabled(true);
                        editar.setChecked(false);
                        agregarFilaOpcional();
                    }else{
                        cantidad[position].requestFocus();
                        cantidad[position].setError(getString(R.string.info_field_no_valid));
                    }
                }
            });

            this.seleccion[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    if(eliminar.isChecked()){
                        eliminarFila(position);
                    }else if(editar.isChecked()){
                        System.out.println("Intentando editar fila: "+position);
                        if(numeroSeleccionados()==1 && numeroEditados()==0 && iditems[position]!=null){
                            System.out.println("Numeros de editados: "+numeroEditados());
                            modificarFila(position);
                        }
                    }else{
                        if(iditems[position]!=null && seleccion[position].isChecked()){
                            crearDialogInfoItem(position);
                        }
                    }
                }
            });

            fila.setPadding(0,5,0,5);
            fila.setMinimumHeight(50);

            fila.addView(numero[count]);
            fila.addView(descripcion[count]);
            fila.addView(existencias[count]);
            fila.addView(cantidad[count]);
            //fila.addView(precio_real[count]);
            //fila.addView(descuento[count]);
            //fila.addView(total[count]);
            fila.addView(seleccion[count]);
            tabla.addView(fila);
            count++;

    }

    public int numeroSeleccionados(){
        int result = 0 ;
        for(int i=0; i<count;i++){
            if(seleccion[i].isChecked()){
                result++;
            }
        }
        return result;
    }

    public int numeroEditados(){
        int result = 0 ;
        for(int i=0; i<count;i++){
            if(descripcion[i].isEnabled()||cantidad[i].isEnabled()){
                result++;
            }
        }
        return result;
    }

    public void eliminarFila(int posicion){
        try{
            if(bandpromo[posicion]!=1){
                eliminarHijos(iditems[posicion], keypadres[posicion]);
                numero[posicion].setTextColor(resources.getColor(R.color.color_danger));
                descripcion[posicion].setText("FILA BORRADA");descripcion[posicion].setTextColor(resources.getColor(R.color.color_danger));
                cantidad[posicion].setText("XXX");cantidad[posicion].setTextColor(resources.getColor(R.color.color_danger));
                existencias[posicion].setText("XXX");existencias[posicion].setTextColor(resources.getColor(R.color.color_danger));
                /*Cambiar de variables Arreglos Normales*/
                bandiva[posicion] = 0;
                bandpromo[posicion] = 0;
                numprecio[posicion] = 0;
                idpadres[posicion] = null;
                iditems[posicion] = null;
                keypadres[posicion] = null;
                numprecio[posicion] = 0;
                descpromo[posicion] = 0;
            }else{
                Toast ts= Toast.makeText(getApplicationContext(),resources.getString(R.string.info_fila_promocion_del),Toast.LENGTH_SHORT);
                ts.show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast ts= Toast.makeText(getApplicationContext(),resources.getString(R.string.info_imposible_delete_row),Toast.LENGTH_SHORT);
            ts.show();
        }
    }

    public void eliminarHijos(String id_padre, String keypadre){
        for(int i =0; i<count; i++){
            if(idpadres[i]!=null && idpadres[i].equals(id_padre) && keypadres[i].equals(keypadre)){
                numero[i].setTextColor(resources.getColor(R.color.color_danger));
                descripcion[i].setText("FILA BORRADA");descripcion[i].setTextColor(resources.getColor(R.color.color_danger));
                cantidad[i].setText("XXX");cantidad[i].setTextColor(resources.getColor(R.color.color_danger));

                bandiva[i] = 0;
                bandpromo[i] = 0;
                numprecio[i] = 0;
                idpadres[i] = null;
                iditems[i] = null;
                keypadres[i] = null;
                numprecio[i] = 0;
                descpromo[i] = 0;
            }
        }
    }



    public void agregarFilaOpcional() {
        AlertDialog.Builder quitDialog
                = new AlertDialog.Builder(this);
        quitDialog.setTitle("Deseas ingresar otra fila?");

        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                agregarFila();
            }
        });

        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });

        quitDialog.show();
    }


    /*
    Metodos para guardar transaccion
     */
    public boolean guardarTrans() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Esta seguro de guardar la transaccion?");
        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DBSistemaGestion helper = new DBSistemaGestion(NuevoIngresoProductoTerminado.this);
                GeneradorClaves gen = new GeneradorClaves();
                gen.generarClave();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fecha = sdf1.format(new Date());
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss", Locale.US);
                String hora = sdf2.format(new Date());
                SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String modificacion = sdf3.format(new Date());
                Transaccion trans = new Transaccion(gen.generarClave(), transaccion.getText().toString() + "-" + getIntent().getStringExtra("identificador"), obtenerObservaciones(), helper.numeroTransacciones(getIntent().getStringExtra("transaccion") + "-" + getIntent().getStringExtra("identificador"), idvendedor), fecha, hora, 0, idvendedor, cli_id, null, "0000", modificacion);
                helper.crearTransaccion(trans);
                System.out.println("Transaccion guardada: "+trans.toString());
                helper.close();
                GuardarDetallesTask taskSaveDetalles = new GuardarDetallesTask();
                taskSaveDetalles.execute(trans.getId_trans(), bodega.getText().toString());
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return true;
    }

    /*
        Guardar Detalles
     */
    public ArrayList<IVKardex> generarDetalles(String id_trans, String bodega_id){
        ArrayList<IVKardex> ivks = new ArrayList<>();
        GeneradorClaves gen= new GeneradorClaves();
        gen.generarClave();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String fecha=sdf.format(new Date());
        DBSistemaGestion helper= new DBSistemaGestion(NuevoIngresoProductoTerminado.this);
        for(int i=0; i<count;i++ ) {
            if(iditems[i]!=null&&!descripcion[i].equals("FILA BORRADA")){
                IVKardex detalle = new IVKardex(
                        gen.generarClave(),
                        Double.parseDouble(cantidad[i].getText().toString()),
                        0.0,
                        0.0,//costo real total
                        0.0,//precio total
                        0.0,
                        "", //descuento solicitado
                        0.0,
                        0.0, //descuento real en dolares
                        id_trans,
                        bodega_id,
                        iditems[i],
                        idpadres[i],
                        keypadres[i],
                        descpromo[i],
                        numprecio[i], 0.0,0,fecha);
                ivks.add(detalle);
            }
        }
        helper.close();
        return  ivks;
    }

    class GuardarDetallesTask extends AsyncTask<String, Void, Void> {
        String id_trans, bodega_id;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(NuevoIngresoProductoTerminado.this);
            progress.setCancelable(false);
            progress.setTitle("Registrando items");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progress.isShowing()) {
                progress.dismiss();
                alertaTransaccionGuardada();
                onBackPressed();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            id_trans = params[0];
            bodega_id = params[1];
            DBSistemaGestion helper = new DBSistemaGestion(NuevoIngresoProductoTerminado.this);
            try {
                List<IVKardex> list = generarDetalles(id_trans, bodega_id);
                for(IVKardex ivk: list){
                    System.out.println("IVKardex a guardar: "+ivk.toString());
                    helper.crearIVKardex(ivk);
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            helper.close();
            return null;
        }
    }



//Mostrar Info items
public void crearDialogInfoItem(int posicion) {
    LayoutInflater li = LayoutInflater.from(this);
    View promptsView = li.inflate(R.layout.info_transferencia_item, null);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setView(promptsView);
    alertDialogBuilder.setTitle(R.string.tittle_dialog_show_detalle);
    //Variables de la vista
    TextView code = (TextView) promptsView.findViewById(R.id.info_item_code);
    TextView nombre = (TextView) promptsView.findViewById(R.id.info_item_nombre);
    TextView cant = (TextView) promptsView.findViewById(R.id.info_item_cantidad);
    TextView presenta = (TextView) promptsView.findViewById(R.id.info_item_presentacion);
    TextView und = (TextView) promptsView.findViewById(R.id.info_item_unidad);
    TextView imp = (TextView) promptsView.findViewById(R.id.info_item_iva);
    TableLayout tablaExistencia = (TableLayout) promptsView.findViewById(R.id.infoExiTableLayoutProm);
    TableRow.LayoutParams layoutRow = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50, 1);
    TableRow.LayoutParams layoutCell = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
    //cargando datos
    DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
    Cursor cursor = helper.consultarItem(iditems[posicion]);
    if(cursor.moveToFirst()){
        code.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)));
        nombre.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
        cant.setText(cantidad[posicion].getText().toString());
        presenta.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
        und.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad)));
    }
    cursor.close();
    /*Generar reporte de existencia*/
    TableRow fila;
    int pst = 0;
    Cursor cur1 = helper.obtenerExistenciaItem(iditems[posicion],bodegas.split(","));
    if (cur1.moveToFirst()) {
        do {
            fila = new TableRow(this);
            fila.setLayoutParams(layoutRow);

            layoutCell.span = 1;
            TextView nro = new TextView(this);
            nro.setTextSize(12);
            nro.setText(String.valueOf(pst + 1));
            nro.setTextColor(resources.getColor(R.color.textColorPrimary));
            nro.setLayoutParams(layoutCeldas);
            nro.setGravity(Gravity.CENTER);
            nro.setPadding(5, 5, 5, 5);

            TextView bod = new TextView(this);
            bod.setTextSize(12);
            bod.setText(cur1.getString(cur1.getColumnIndex(Bodega.FIELD_codbodega)));
            bod.setTextColor(resources.getColor(R.color.textColorPrimary));
            bod.setLayoutParams(layoutCeldas);
            bod.setGravity(Gravity.CENTER);
            bod.setPadding(5, 5, 5, 5);

            TextView stock = new TextView(this);
            stock.setTextSize(12);
            stock.setText(cur1.getString(cur1.getColumnIndex(Existencia.FIELD_existencia)));
            stock.setTextColor(resources.getColor(R.color.textColorPrimary));
            stock.setLayoutParams(layoutCeldas);
            stock.setGravity(Gravity.CENTER);
            stock.setPadding(5, 5, 5, 5);

            fila.addView(nro);
            fila.addView(bod);
            fila.addView(stock);
            tablaExistencia.addView(fila);
            pst++;
        } while (cur1.moveToNext());
    } else {
        fila = new TableRow(this);
        fila.setLayoutParams(layoutRow);
        TextView info = new TextView(this);
        info.setText("PRODUCTO SIN STOCK");
        info.setLayoutParams(layoutCell);
        info.setGravity(Gravity.CENTER);
        fila.setPadding(10, 20, 10, 20);
        fila.addView(info);
        tablaExistencia.addView(fila);
    }
    cur1.close();
    helper.close();
    /*Generar reporte de existencia*/
    /*Cargar Listado*/
    alertDialogBuilder.setCancelable(true);
    alertDialogBuilder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    });
    final AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
}

    public String obtenerObservaciones() {
        //observacion,solitante,tiempo entrega,forma,validez,ruc, razon social, nombre comercial, direccion,telefono
        return trans_observacion.getText().toString() +";" + ";"+ ";"  + ";"  + ";" + ";"  + ";" +  ";"  + ";"+ ";"+";";
    }


    public void alertaTransaccionGuardada() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.save_success_transaction,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id3));
        ((TextView) layout.findViewById(R.id.toast_tittle)).setText("Transaccion Registrada");
        ((TextView) layout.findViewById(R.id.toast_subtittle)).setText("la transaccion ha sido registrada correctamente!");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_change_client:
                if(!cli_id.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NuevoIngresoProductoTerminado.this);
                    alertDialogBuilder
                            .setTitle("Cambiar Cliente")
                            .setMessage("Al seleccionar otro cliente se perderan los items ingresados")
                            .setCancelable(false)
                            .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    reload();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agregar:
                if(count>0){
                    agregarFila();
                }
                break;
            case R.id.editar:
                if(editar.isChecked()){
                    System.out.println("Editando");
                    //si esta habilitado
                    eliminar.setEnabled(false);eliminar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }else{
                    agregar.setEnabled(true);guardar.setEnabled(true);
                    eliminar.setEnabled(true);
                    System.out.println("Editar");
                    eliminar.setEnabled(true);eliminar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }
                break;
            case R.id.eliminar:
                if(eliminar.isChecked()){
                //si esta habilitado
                    editar.setEnabled(false);editar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }else{
                    agregar.setEnabled(true);guardar.setEnabled(true);
                    editar.setEnabled(true);
                    eliminar.setEnabled(true);eliminar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }
                break;
            case R.id.guardar:
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(NuevoIngresoProductoTerminado.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                if (conf_observacion) {
                    if (!validarCamposVacios()) {
                        guardarTransaccion();
                    }
                } else guardarTransaccion();
                break;
        }
    }

    public boolean guardarTransaccion(){
        if(filasCompletadas()>0){
            guardarTrans();
        }else{
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Debe ingresar al menos un producto");
            alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.dismiss();
                }
            });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return true;
    }

    public int filasCompletadas(){
        int result = 0;
        for(int i=0; i<count; i++){
            if(iditems[i]!=null && !descripcion[i].equals("FILA BORRADA")){
                result++;
            }
        }
        return result;
    }

    public boolean validarCamposVacios() {
        if (TextUtils.isEmpty(trans_observacion.getText().toString())){
            trans_observacion.requestFocus();
            trans_observacion.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        return false;
    }

    public void modificarFila(int posicion){
        if(bandpromo[posicion]!=1){
            eliminarHijos(iditems[posicion], keypadres[posicion]);
            /*Bloqueando botones*/
            agregar.setEnabled(false);
            guardar.setEnabled(false);
            eliminar.setEnabled(false);
            editar.setEnabled(false);

            bandiva[posicion] = 0;
            bandpromo[posicion] = 0;
            numprecio[posicion] = 0;
            iditems[posicion] = null;
            numprecio[posicion] = 0;
            descpromo[posicion] = 0;
            //idpadres[posicion] = null;

            descripcion[posicion].setEnabled(true);
            descripcion[posicion].requestFocus();
            descripcion[posicion].setSelection(descripcion[posicion].getText().length());
            cantidad[posicion].setEnabled(false);
        }else{
            Toast ts= Toast.makeText(getApplicationContext(),resources.getString(R.string.info_fila_promocion_del),Toast.LENGTH_SHORT);
            ts.show();
        }
    }

    /*
            Metodo para recargar los datos iniciales
         */
    public void reload() {
        Intent intent = new Intent(NuevoIngresoProductoTerminado.this, NuevoIngresoProductoTerminado.class);
        intent.putExtra("transaccion", getIntent().getStringExtra("transaccion"));
        intent.putExtra("identificador", getIntent().getStringExtra("identificador"));
        intent.putExtra("accesos", getIntent().getStringArrayExtra("accesos"));
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NuevoIngresoProductoTerminado.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
