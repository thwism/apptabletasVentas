package ibzssoft.com.ishidamovile.transbodega.modificar;

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
import android.view.Menu;
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
import android.widget.ListView;
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
import ibzssoft.com.adaptadores.ListadoCarteraAdaptador;
import ibzssoft.com.adaptadores.ListadoPreciosAdapter;
import ibzssoft.com.adaptadores.Validaciones;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Promocion;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

public class ModificaTransferencia extends AppCompatActivity implements View.OnClickListener{
    /*
        Campos de la cabecera
     */
    private TextView emision,hora, bodega, transaccion, precio, vendedor;
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
    private String [] porcdesc;
    private String [] iditems,idpadres,keypadres;
    private int [] descpromo;
    private CheckBox[] seleccion;
    private AutoCompleteTextView [] descripcion;
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
    private int count;
    private String origen,destino,lineas,bodegas;
    private int decimales;
    /*Campos para seleccionar bodegas*/
    private Spinner bod_origen, bod_destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_transferencia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.inicializarCamposFormulario();
        this.cargarPreferencias();
        this.cargarDatos(getIntent().getStringExtra("transid"));
        this.inicializarCamposTabla();
        this.agregarFilasIniciales();
    }

    public void cargarPreferencias(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(this);
        this.lineas = ext.get(getString(R.string.key_act_lin),getString(R.string.pref_act_lin_default));
        this.bodegas = ext.get(getString(R.string.key_act_bod),getString(R.string.pref_act_bod_default));
        String dec= ext.get(getString(R.string.key_empresa_decimales),"2");
        this.decimales = Integer.parseInt(dec);
    }

    public void inicializarCamposFormulario(){
     /*
        Campos de la transaccion
     */
        this.emision= (TextView)findViewById(R.id.nuevoPedidoFecha);
        this.hora= (TextView)findViewById(R.id.nuevoPedidoHora);
        this.bodega= (TextView)findViewById(R.id.nuevoPedidoBodega);
        this.precio= (TextView)findViewById(R.id.nuevoPedidoPrecio);
        this.transaccion= (TextView)findViewById(R.id.nuevoPedidoTrans);
        this.vendedor= (TextView)findViewById(R.id.nuevoPedidoVendedor);

        this.count = 0;
        /*
            Contenedores de la vista
        */
        this.contenedor_observaciones= (CardView)findViewById(R.id.contenedorObservaciones);

        /*
            Campos observacion transaccion
         */
        trans_observacion = (TextView)findViewById(R.id.txtNuevoPedidoObservacion);
        resources = this.getResources();
    }
    public void inicializarCamposTabla(){
        /* Campos de la tabla de items [VISTA] */
        seleccion= new CheckBox[100];
        numero= new EditText[100];
        descripcion= new AutoCompleteTextView[100];
        cantidad = new EditText[100];
        bandiva = new int[100];
        bandpromo = new int[100];
        numprecio  = new int[100];
        porcdesc= new String[100];
        iditems = new String[100];
        idpadres= new String[100];
        keypadres= new String[100];
        descpromo = new int[100];
        /* Recursos para inicializar tabla */
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
        guardar.setOnClickListener(this);
        bod_origen = (Spinner)findViewById(R.id.bod_origen);
        bod_destino = (Spinner)findViewById(R.id.bod_destino);
        DBSistemaGestion helper = new DBSistemaGestion(ModificaTransferencia.this);
        final Bodega[] spinnerLists = helper.cargarBodegasTransferencia();
        SpinnerAdapter adapterSpinner= new SpinnerAdapter(ModificaTransferencia.this,R.layout.fila_bodega,spinnerLists);
        bod_origen.setAdapter(adapterSpinner);
        bod_destino.setAdapter(adapterSpinner);
        bod_origen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position>0){
                    Bodega obj = (Bodega)(adapterView.getItemAtPosition(position));
                    origen = obj.getCodbodega();
                }else{
                    for(int i=0; i<spinnerLists.length;i++) if(spinnerLists[i].getCodbodega().equals(origen))bod_origen.setSelection(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        bod_destino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position>0){
                    Bodega obj = (Bodega)(adapterView.getItemAtPosition(position));
                    destino = obj.getCodbodega();
                }else{
                    for(int i=0; i<spinnerLists.length;i++) if(spinnerLists[i].getCodbodega().equals(destino))bod_destino.setSelection(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /*
        * Cargar Configuracion de la Transaccion
     */
    public void cargarDatos(String id_trans) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.obtenerTransaccion(id_trans);
        if (cursor.moveToFirst()) {
            emision.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            hora.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)));
            vendedor.setText(cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
            Cursor cursor1 = helper.consultarGNTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            if (cursor1.moveToFirst()) {
                Cursor cursor2 = helper.consultarBodega(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                if (cursor2.moveToFirst()) {
                    bodega.setText(cursor2.getString(cursor2.getColumnIndex(Bodega.FIELD_codbodega)));
                }
                cursor2.close();
                transaccion.setText(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_codtrans)));
                /*Configuracion de observaciones y precios*/
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(ModificaTransferencia.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs), true);
                if (conf_observacion) mostrarContenedorObservaciones();cargarObservaciones(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_descripcion)));
                if (cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")) {
                    precio.setText(getString(R.string.info_precio_pcgrupo_enabled));
                } else {
                    precio.setText(getString(R.string.info_precio_pcgrupo_disable));
                }
            }
            cursor1.close();
            cursor.close();
            helper.close();
        }
    }

    public void cargarObservaciones(String datos){
        //observacion,solitante,tiempo entrega,forma,validez,atencion
        String data[]=datos.split(";");
        try{
            if(data[0].isEmpty()!=true) trans_observacion.setText(data[0]);
            if(data[10].isEmpty()!=true) origen=data[10];
            if(data[11].isEmpty()!=true) destino=data[11];
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Metodos para agregar/ eliminar / editar filas de la tabla de items
     */
    public void agregarFilasIniciales() {
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.consultarIVKardex(getIntent().getStringExtra("transid"));
        this.count=0;
        if(cursor.moveToFirst()){
            do {
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);
                boolean bandpro = false;
                this.iditems[this.count] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_identificador));
                this.idpadres[this.count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id));
                this.bandiva[this.count] = cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva));
                this.porcdesc[this.count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_descuento));
                this.numprecio[this.count] = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio));
                this.descpromo[this.count] = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo));
                this.keypadres[count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_keypadre));
                if (idpadres[count] != null) bandpro = true;

                if(this.idpadres[this.count]!=null) this.bandpromo[this.count] =  1;
                this.numero[this.count] = new EditText(this);
                this.numero[this.count].setText(String.valueOf(this.count + 1));
                this.numero[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if(bandpro) this.numero[this.count].setTextColor(getResources().getColor(R.color.successfull));
                this.numero[this.count].setLayoutParams(layoutCeldas);
                this.numero[this.count].setBackgroundResource(R.drawable.text_field);
                this.numero[this.count].setGravity(Gravity.CENTER);
                this.numero[this.count].setEnabled(false);
                this.numero[this.count].setTextSize(14);
                this.numero[this.count].setPadding(5, 5, 5, 5);

                AutoCompleteProductoSelected adapter = this.new AutoCompleteProductoSelected(getApplicationContext(), count);
                this.descripcion[this.count] = new AutoCompleteTextView(this);
                this.descripcion[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.descripcion[this.count].setHint(getString( R.string.hint_producto));
                this.descripcion[this.count].setDropDownWidth(600);
                if(bandpro)  this.descripcion[this.count].setTextColor(getResources().getColor(R.color.successfull));
                this.descripcion[this.count].setEnabled(false);
                this.descripcion[this.count].setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                this.descripcion[this.count].setLayoutParams(layoutCeldaDescripcion);
                this.descripcion[this.count].setBackgroundResource(R.drawable.text_field);
                this.descripcion[this.count].setGravity(Gravity.CENTER_VERTICAL);
                this.descripcion[this.count].setTextSize(14);
                this.descripcion[this.count].setPadding(5, 5, 5, 5);
                this.descripcion[count].setOnItemClickListener(adapter);
                this.descripcion[count].setAdapter(adapter);


                this.cantidad[this.count] = new EditText(this);
                this.cantidad[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if(bandpro) this.cantidad[this.count].setTextColor(getResources().getColor(R.color.successfull));
                this.cantidad[this.count].setLayoutParams(layoutCeldas);
                this.cantidad[this.count].setBackgroundResource(R.drawable.text_field);
                this.cantidad[this.count].setGravity(Gravity.CENTER);
                this.cantidad[this.count].setHint("0");
                this.cantidad[this.count].setInputType(InputType.TYPE_CLASS_NUMBER);
                this.cantidad[this.count].setEnabled(false);
                this.cantidad[this.count].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_cantidad)));
                this.cantidad[this.count].setTextSize(14);
                this.cantidad[this.count].setPadding(5, 5, 5, 5);


                this.seleccion[count] = new CheckBox(this);
                this.seleccion[count].setMinimumHeight(50);
        /*Agregando identificador por fila*/
                this.seleccion[count].setTag(count);
                this.cantidad[count].setTag(count);
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

                fila.addView(numero[this.count]);
                fila.addView(descripcion[this.count]);
                fila.addView(cantidad[this.count]);
                fila.addView(seleccion[this.count]);
                tabla.addView(fila);
                this.count++;
            }while (cursor.moveToNext());
        }
    }

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
                    int cant = Integer.parseInt(cantidad[position].getText().toString());
                    cantidad[position].setEnabled(false);
                    descripcion[position].setEnabled(false);
                    editar.setEnabled(true);guardar.setEnabled(true);
                    eliminar.setEnabled(true);
                    agregar.setEnabled(true);
                    editar.setChecked(false);
                    //calcularSubtotalItem(position);
                    //calcularTotalTransaccion();
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
        fila.addView(cantidad[count]);
        //fila.addView(precio_real[count]);
        //fila.addView(descuento[count]);
        //fila.addView(total[count]);
        fila.addView(seleccion[count]);
        tabla.addView(fila);
        count++;

    }
    /**
     *Metodo para mostrar opciones por fila
     */
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


    public boolean validarExistencia(String id_item, String id_bodega, int cantidad) {
        boolean result = false;
        try {
            DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
            Cursor cursor = helper.obtenerExistenciaItemBodega(id_item, id_bodega);
            if (cursor.moveToFirst()) {
                if (cursor.getInt(cursor.getColumnIndex(Existencia.FIELD_existencia)) >= cantidad) {
                    alertaExistenciaItem(cursor.getInt(cursor.getColumnIndex(Existencia.FIELD_existencia)), cursor.getString(cursor.getColumnIndex(Bodega.FIELD_codbodega)));
                } else {
                    alertaStockInsuficiente(id_bodega);
                }
            } else {
                alertaStockInsuficiente(id_bodega);
            }
            cursor.close();
            helper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean verificarExistenciaDescuento(String id_cli, String id_item) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        boolean result = helper.existeDescuentoItem(id_item, id_cli);
        helper.close();
        return result;
    }
    public boolean verificarExistenciaPromocion(String id_item, double cantidad) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        boolean result = helper.existePromocionItem(id_item, cantidad);
        helper.close();
        return result;
    }



    class AutoCompleteProductoSelected extends CursorAdapter implements AdapterView.OnItemClickListener {
        DBSistemaGestion dbSistemaGestion;
        int posicion;
        public AutoCompleteProductoSelected(Context context, int row) {
            super(ModificaTransferencia.this, null);
            dbSistemaGestion = new DBSistemaGestion(context);
            this.posicion= row;
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
            String codItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_cod_item));
            String descItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_descripcion));
            String preItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_presentacion));
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(descripcion[posicion], InputMethodManager.SHOW_IMPLICIT);
            descripcion[posicion].setText(descItem);
            cantidad[posicion].setText("");
            cantidad[posicion].setEnabled(true);
            cantidad[posicion].requestFocus();
            /*Arreglos normales*/
            bandiva[posicion] = ivaItem;
            iditems[posicion] = idItem;

            dbSistemaGestion.close();
        }
    }

    public void eliminarFila(int posicion){
        try{
            if(bandpromo[posicion]!=1){
                eliminarHijos(iditems[posicion], keypadres[posicion]);
                numero[posicion].setTextColor(resources.getColor(R.color.color_danger));
                descripcion[posicion].setText("FILA BORRADA");descripcion[posicion].setTextColor(resources.getColor(R.color.color_danger));
                cantidad[posicion].setText("XXX");cantidad[posicion].setTextColor(resources.getColor(R.color.color_danger));
                /*Cambiar de variables Arreglos Normales*/
                bandiva[posicion] = 0;
                bandpromo[posicion] = 0;
                numprecio[posicion] = 0;
                idpadres[posicion] = null;
                iditems[posicion] = null;
                keypadres[posicion] = null;
                numprecio[posicion] = 0;
                descpromo[posicion] = 0;
               // System.out.println("Limite luego de eliminar: "+this.limite);
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
       // System.out.println("Intentando eliminar hijos de: "+id_padre);
        for(int i =0; i<count; i++){
            /*System.out.println("posicion: "+i+" id_hijo: "
                    +iditems[i]+" keypadre: "
                    +keypadre+" bandpromo: "
                    +bandpromo[i]+" keypadre_hijo: "
                    +keypadres[i]);*/
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
        //System.out.println("Limite luego de eliminar hijo: "+this.limite);
    }

    public String obtenerObservaciones() {
        //observacion,solitante,tiempo entrega,forma,validez,ruc, razon social, nombre comercial, direccion,telefono
        return trans_observacion.getText().toString() +";" + ";"+ ";"  + ";"  + ";" + ";"  + ";" +  ";"  + ";"+ ";"+origen+";"+destino;
    }

    public void alertaExistenciaItem(int cantidad, String bodega) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.info_existencia_producto,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id1));
        ((TextView) layout.findViewById(R.id.toast_stock)).setText("Stock en Bodega: " + cantidad);
        ((TextView) layout.findViewById(R.id.toast_bodega)).setText("Bodega Utilizada: " + bodega);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertaStockInsuficiente(String idbodega) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.info_stock_insuficiente,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id1));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 80);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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


    //Observaciones
    public void mostrarContenedorObservaciones() {
        if (contenedor_observaciones.getVisibility() == View.GONE) {
            animar(false, contenedor_observaciones);
            contenedor_observaciones.setVisibility(View.VISIBLE);
        }
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
    /*
    * Agregando eventos
    */

    public boolean guardarTrans() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Esta seguro de guardar la transaccion?");
        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DBSistemaGestion helper = new DBSistemaGestion(ModificaTransferencia.this);
                GeneradorClaves gen = new GeneradorClaves();
                gen.generarClave();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String fecha = sdf1.format(new Date());
                Cursor cursor1= helper.obtenerTransaccion(getIntent().getStringExtra("transid"));
                Transaccion trans=null;
                if(cursor1.moveToFirst()){
                    trans=new Transaccion(
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_ID_Trans)),
                            transaccion.getText().toString(),
                            obtenerObservaciones(),
                            cursor1.getInt(cursor1.getColumnIndex(Transaccion.FIELD_numTransaccion)),
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_fecha_trans)),
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_hora_trans)),
                            cursor1.getInt(cursor1.getColumnIndex(Transaccion.FIELD_band_enviado)),
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_vendedor_id)),
                            "",null,
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_referencia)),fecha);
                }
                helper.modificarTransaccion(trans);
                helper.close();
                eliminarDetallesAnteriores(getIntent().getStringExtra("transid"));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agregar:
                    agregarFila();
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
                System.out.println("Metodo borrar");
                if(eliminar.isChecked()){
                    System.out.println("Borrando");
                    //si esta habilitado
                    editar.setEnabled(false);editar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }else{
                    agregar.setEnabled(true);guardar.setEnabled(true);
                    editar.setEnabled(true);
                    System.out.println("Borrar");
                    eliminar.setEnabled(true);eliminar.setChecked(false);
                    for(int i =0; i<count; i++){
                        seleccion[i].setChecked(false);
                    }
                }
                break;
            case R.id.guardar:
                System.out.println("Intentando guardar:");
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(ModificaTransferencia.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                if (conf_observacion) {
                    if (!validarCamposVacios()) {
                        guardarTransaccion();
                    }
                } else guardarTransaccion();
                break;
        }
    }

    public int filasCompletadas(){
        int result = 0;
        for(int i=0; i<count; i++){
            System.out.println("Cadenas comparadas en filas completas: "+iditems[i]+";"+descripcion[i].getText().toString());
            if(iditems[i]!=null && !descripcion[i].equals("FILA BORRADA")){
                result++;
            }
        }
        return result;
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

    public boolean validarCamposVacios() {
        if(origen.isEmpty()||origen==null){
            Toast.makeText(ModificaTransferencia.this, "Seleccione una bodega Origen",Toast.LENGTH_LONG).show();
            return true;
        }
        if(destino.isEmpty()||destino==null){
            Toast.makeText(ModificaTransferencia.this, "Seleccione una bodega Destino",Toast.LENGTH_LONG).show();
            return true;
        }
        if (TextUtils.isEmpty(trans_observacion.getText().toString())){
            trans_observacion.requestFocus();
            trans_observacion.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        return false;
    }

    public void eliminarDetallesAnteriores(String id_trans){
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor1=helper.consultarIVKardex(id_trans);
        if(cursor1.moveToFirst()){
            do{
                System.out.println("IVKardex a eliminar: "+cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador)));
                helper.eliminarIVKardex(cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador)));
            }while(cursor1.moveToNext());
        }
        helper.close();
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
        DBSistemaGestion helper= new DBSistemaGestion(ModificaTransferencia.this);
        for(int i=0; i<count;i++ ) {
            if(iditems[i]!=null&&!descripcion[i].equals("FILA BORRADA")){
                IVKardex detalle = new IVKardex(
                        gen.generarClave(),
                        Double.parseDouble(cantidad[i].getText().toString()),
                        0.0,
                        0.0,//costo real total
                        0.0,
                        0.0,//precio real total
                        "",
                        0.0,//descuento
                        0.0, //descuento real
                        id_trans,
                        bodega_id,
                        iditems[i],
                        idpadres[i],
                        keypadres[i],
                        descpromo[i],
                        numprecio[i],0.0,0, fecha);
                ivks.add(detalle);
            }
        }
        helper.close();
        return  ivks;
    }

    /*Adaptador para lista desplegable*/
    public class SpinnerAdapter extends ArrayAdapter<Bodega> {

        private Bodega[] myObjs;

        public SpinnerAdapter(Context context, int textViewResourceId,
                              Bodega[] myObjs) {
            super(context, textViewResourceId, myObjs);
            this.myObjs = myObjs;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View spView = inflater.inflate(R.layout.fila_bodega, parent, false);
            TextView nombre = (TextView)spView.findViewById(R.id.txtNumeroPrecio);
            TextView codigo = (TextView)spView.findViewById(R.id.txtValorPrecio1);
            codigo.setText(myObjs[position].getCodbodega());
            nombre.setText(myObjs[position].getNombre());
            return spView;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View dropDownView = inflater.inflate(R.layout.fila_bodega, parent, false);
            TextView nombre = (TextView)dropDownView.findViewById(R.id.txtNumeroPrecio);
            TextView codigo = (TextView)dropDownView.findViewById(R.id.txtValorPrecio1);
            codigo.setText(myObjs[position].getCodbodega());
            nombre.setText(myObjs[position].getNombre());
            return dropDownView;
        }
    }


    class GuardarDetallesTask extends AsyncTask<String, Void, Void> {
        String id_trans, bodega_id;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ModificaTransferencia.this);
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
            DBSistemaGestion helper = new DBSistemaGestion(ModificaTransferencia.this);
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
        Intent intent = new Intent(ModificaTransferencia.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
