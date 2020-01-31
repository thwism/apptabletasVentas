package ibzssoft.com.ishidamovile.oferta.crear;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import ibzssoft.com.adaptadores.Alertas;
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
import ibzssoft.com.storage.DBSistemaGestion;

public class NuevaOfertaM extends AppCompatActivity implements View.OnClickListener{
    /*
        Campos de la cabecera
     */
    private TextView emision, bodega, transaccion, precio, vendedor;
    /*
        Campos del cliente
     */
    private AutoCompleteTextView autoCompleteCliente;
    private TextView cedula, nombres, comercial, direccion, telefono, correo, observacion;
    /*
        Campos de la cartera
     */
    private TextView vencidos, no_vencidos, saldo_vencer, saldo_vencido;
    /*
        Campos de validacion
     */
    private int limite,count,conf_pcg,conf_max_venci,conf_dias_gracia, conf_modo_busqueda;
    private String [] conf_precios_disponibles;
    private String  cli_id;
    private int  consu_final;
    /*
        Contenedores de la vista
     */
    private CardView contenedor_cartera;
    private CardView contenedor_observaciones;
    private CardView contenedor_items;
    private CardView contenedor_adicional;
    /*
        Campos para la tabla de items [VISTA]
     */
    private EditText [] numero, cantidad, precio_real, total;
    private EditText [] descuento;
    private int [] bandiva;
    private int [] bandpromo;
    private int [] numprecio;
    private String [] iditems,idpadres,keypadres,preciounitario;
    private int [] descpromo;
    private CheckBox[] seleccion;
    private AutoCompleteTextView [] descripcion;
    /*
        Recursos para inicializar tabla
     */
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila, layoutCeldaNro, layoutCeldas, layoutCeldaDescripcion;
    /*
        Variables total de transaccion
     */
    private TextView tran_sub_sin_iva,tran_sub_con_iva, tran_sub,tran_sub_iva,tran_sub_total;
    /*
        Botones de guardado y agregacion
     */
    private Button guardar, agregar;
    private ToggleButton editar, eliminar;
    /*
        Transaccion observacion
     */
    private TextView trans_observacion, trans_te,trans_at, trans_vl, trans_fp ;
    private TextView trans_cli_ruc, trans_cli_nombres,trans_cli_comercial, trans_cli_direccion, trans_cli_telefono;
    private Resources resources;

    private String impuestos,idvendedor,idusuario,lineas,bodegas;
    private boolean val_duplicado;
    private int decimales;

    private String  numdec_ptotal, numdec_punitario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_ofertam);
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
        this.impuestos = ext.get(getString(R.string.key_empresa_iva),"12");
        this.idvendedor = ext.get(getString(R.string.key_act_ven),getString(R.string.pref_act_ven_default));
        this.lineas = ext.get(getString(R.string.key_act_lin),getString(R.string.pref_act_lin_default));
        this.idusuario = ext.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
        this.bodegas = ext.get(getString(R.string.key_act_bod),getString(R.string.pref_act_bod_default));
        this.val_duplicado= ext.getBoolean(getString(R.string.key_act_validar_duplicidad),false);
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
        Campos del cliente
     */
        this.autoCompleteCliente = (AutoCompleteTextView) findViewById(R.id.autoCompleteCliente);
        AutoCompleteClienteSelected adapter = new AutoCompleteClienteSelected(NuevaOfertaM.this);
        this.autoCompleteCliente.setAdapter(adapter);
        this.autoCompleteCliente.setDropDownWidth(500);
        this.autoCompleteCliente.setOnItemClickListener(adapter);
        this.cedula = (TextView)findViewById(R.id.nuevoPedidoCI_RUC);
        this.nombres= (TextView)findViewById(R.id.nuevoPedidoNombres);
        this.comercial= (TextView)findViewById(R.id.nuevoPedidoNombreAlt);
        this.direccion= (TextView)findViewById(R.id.nuevoPedidoDireccion);
        this.telefono= (TextView)findViewById(R.id.nuevoPedidoTelefono);
        this.correo= (TextView)findViewById(R.id.nuevoPedidoCorreo);
        this.observacion = (TextView)findViewById(R.id.nuevoPedidoObs);
        /*
        * Campos de validacion
        */
        this.limite = 0;
        this.count = 0;
        this.cli_id  = "";
        this.consu_final= 0;
        this.conf_pcg = 0;
        this.conf_max_venci = 0;
        this.conf_dias_gracia= 0;
        this.conf_modo_busqueda= 0;
        this.conf_precios_disponibles = new String[]{};
        /*
            Contenedores de la vista
        */
        this.contenedor_cartera = (CardView)findViewById(R.id.contenedorCartera);
        this.contenedor_adicional = (CardView)findViewById(R.id.contenedorAdicionales);
        this.contenedor_observaciones= (CardView)findViewById(R.id.contenedorObservaciones);
        this.contenedor_items= (CardView)findViewById(R.id.contenedorItems);
         /*
            Campos del cartera
         */
        vencidos = (TextView)findViewById(R.id.nuevoPedidoDocsVencidos);
        no_vencidos = (TextView)findViewById(R.id.nuevoPedidoDocsVencer);
        saldo_vencer= (TextView)findViewById(R.id.nuevoPedidoSaldoVencer);
        saldo_vencido = (TextView)findViewById(R.id.nuevoPedidoSaldoVencido);
        /*
            Campos total transaccion
         */
        tran_sub_con_iva = (TextView)findViewById(R.id.subtotal);
        tran_sub_sin_iva = (TextView)findViewById(R.id.subtotal_0);
        tran_sub = (TextView)findViewById(R.id.subtotal_total);
        tran_sub_iva = (TextView)findViewById(R.id.iva);
        tran_sub_total = (TextView)findViewById(R.id.total);
        /*
            Campos observacion transaccion
         */
        trans_observacion = (TextView)findViewById(R.id.txtNuevoPedidoObservacion);
        trans_vl= (TextView)findViewById(R.id.txtNuevoPedidoVL);
        trans_fp= (TextView)findViewById(R.id.txtNuevoPedidoFP);
        trans_te= (TextView)findViewById(R.id.txtNuevoPedidoTE);
        trans_at= (TextView)findViewById(R.id.txtNuevoPedidoAT);

        trans_cli_ruc = (TextView)findViewById(R.id.info_cliente_ruc);
        trans_cli_nombres= (TextView)findViewById(R.id.info_cliente_nombres);
        trans_cli_comercial= (TextView)findViewById(R.id.info_cliente_comercial);
        trans_cli_direccion = (TextView)findViewById(R.id.info_cliente_direccion);
        trans_cli_telefono= (TextView)findViewById(R.id.info_cliente_telefono);
    }
    public void inicializarCamposTabla(){
        /*
            Campos de la tabla de items [VISTA]
         */
        seleccion= new CheckBox[100];
        numero= new EditText[100];
        descripcion= new AutoCompleteTextView[100];
        cantidad = new EditText[100];
        precio_real= new EditText[100];
        total= new EditText[100];
        descuento = new EditText[100];
        bandiva = new int[100];
        bandpromo = new int[100];
        numprecio = new int[100];
        iditems = new String[100];
        preciounitario = new String[100];
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
        String []conf_precios;
        int conf_limite, conf_num_pcg, conf_max_docs,conf_dias,conf_modo;
        boolean conf_observacion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(NuevaOfertaM.this);
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
                limite = this.conf_limite;
                conf_pcg = this.conf_num_pcg;
                conf_max_venci= this.conf_max_docs;
                conf_dias_gracia= this.conf_dias;
                conf_modo_busqueda= this.conf_modo;
                conf_precios_disponibles= this.conf_precios;
                if(conf_observacion);mostrarContenedorObservaciones();
                editar.setEnabled(false);eliminar.setEnabled(false);
                agregar.setEnabled(false);guardar.setEnabled(false);
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
                    if(conf_limite<=0)conf_limite = 100;
                    conf_max_docs = (Integer.parseInt(max_pck));
                    String conf_dg=cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_diasgracia));
                    conf_dias =(Integer.parseInt(conf_dg));
                    Cursor cursor1 = helper.consultarBodega(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                    if (cursor1.moveToFirst()) {
                        conf_bodega = cursor1.getString(cursor1.getColumnIndex(Bodega.FIELD_codbodega));
                    }
                    /*Configuracion de observaciones y precios*/
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(NuevaOfertaM.this);
                    conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                    String conf =ext.get(getString(R.string.key_conf_descarga_clientes),"0");
                    conf_precios = ext.get(getString(R.string.key_act_list_price),"1").split(",");
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
    class AutoCompleteClienteSelected extends CursorAdapter implements AdapterView.OnItemClickListener {
        DBSistemaGestion dbSistemaGestion;
        public AutoCompleteClienteSelected(Context context) {
            super(NuevaOfertaM.this, null);
            dbSistemaGestion = new DBSistemaGestion(context);
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
            final String str = cursor.getString(columnIndex) + " --> " + cursor.getString(columnIndex2);
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
            String itemAlterno = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_nombrealterno));
            String itemCI = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc));
            String itemDir = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_direccion1));
            String itemTel = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_telefono1));
            String itemCor = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_email));
            String itemObs = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_observacion));
            if(itemCI.equals("9999999999999")){
                consu_final = 1;
                mostrarContenedorAdicional();
            }
            cli_id = idCliente;
            cedula.setText(itemCI);
            nombres.setText(itemNombre);
            comercial.setText(itemAlterno);
            direccion.setText(itemDir);
            telefono.setText(itemTel);
            correo.setText(itemCor);
            observacion.setText(itemObs);
            autoCompleteCliente.setText("");
            autoCompleteCliente.setEnabled(false);
            autoCompleteCliente.setFocusable(false);
            //cargar configuracion segun pcgrupo
            if (precio.getText().toString().equals(getString(R.string.info_precio_pcgrupo_disable)) != true) {
                cargarPrecio(idCliente, conf_pcg);
            }
            int actions = cargarConfiguracionCartera();
            System.out.println("Configuracion cartera: "+actions);
            switch (actions){
                case 0:
                    //deshabilitar
                    mostrarContenedorItems();
                    ocultarContenedorCartera();
                    break;
                case 1:
                    //advertir
                    generarReporteCartera(idCliente);
                    mostrarContenedorItems();
                    break;
                case 2:
                    //bloquear
                    generarReporteCartera(idCliente);
                    int docs = Integer.parseInt(vencidos.getText().toString());
                    System.out.println("Documentos vencidos: "+docs+" maximo: "+conf_max_venci);
                    if(docs>conf_max_venci){
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(NuevaOfertaM.this);
                        builder1.setTitle("Transaccion Bloqueada");
                        builder1.setMessage("Revise el numero de documentos vencidos");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mostrarContenedorItems();
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    break;
            }
            agregarFila();
            dbSistemaGestion.close();
        }
    }
    //fin clase autoadapter


    public String[] generarListadoPrecios(String item,String [] listprecio) throws SQLException {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarItem(item);
        String[] result = new String[listprecio.length];
        if (cursor != null) {
            if (cursor.moveToFirst()){
                for(int i=0; i<result.length;i++){
                    switch (Integer.parseInt(listprecio[i])){
                        case 1:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));break;
                        case 2:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));break;
                        case 3:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));break;
                        case 4:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));break;
                        case 5:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));break;
                        case 6:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));break;
                        case 7:result[i] = redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));break;
                    }
                }
            }
        }
        helper.close();
        return result;
    }

    public boolean cargarPrecioUnitarioItem(Cursor cursor, int posicion) {
        double precioUnitario = 0.00;
        int precioPCgrupo = Integer.parseInt(precio.getText().toString());
        switch (precioPCgrupo) {
            case 1:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
                break;
            case 2:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
                break;
            case 3:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
                break;
            case 4:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
                break;
            case 5:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
                break;
            case 6:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
                break;
            case 7:
                precioUnitario = Double.parseDouble(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio7)));
                break;
        }
        //cargando precio a la tabla
        numprecio[posicion] = precioPCgrupo;
        preciounitario[posicion] = redondearNumero(precioUnitario);
        precio_real[posicion].setText(redondearNumero(precioUnitario));
        return true;
    }
    /*
        Metodo para cargar descuento
     */
    public double calcularDescuentoItem(String cli_id, String item_id, int posicion) {
        double result = 0.0;
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarDescuentoItem(cli_id, item_id);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getDouble(cursor.getColumnIndex(Descuento.FIELD_porcentaje));
                Toast ts = Toast.makeText(getApplicationContext(), getString(R.string.info_descuento) + redondearNumero(result) + "%", Toast.LENGTH_SHORT);
                ts.show();
                descpromo[posicion] = 1;
            }
        }
        cursor.close();
        helper.close();
        return result;
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
    /*
        Metodos para cargar la cartera del cliente
     */
    public int cargarConfiguracionCartera() {
        DBSistemaGestion helper = new DBSistemaGestion(NuevaOfertaM.this);
        Cursor cursor = helper.consultarGNTrans(getIntent().getStringExtra("transaccion"));
        int actions = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                actions =  cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_opciones));
            }
        }
        helper.close();
        return actions;
    }
    public void generarReporteCartera(String cliente) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.cartera, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        /*Cargar Listado*/
        ListView listadoCartera = (ListView) promptsView.findViewById(R.id.vst_cartera_listado);
        String[] from = new String[]{};
        int[] to = new int[]{};
        DBSistemaGestion helper = new DBSistemaGestion(this);
        Cursor cursor = helper.consultarCarteraCliente(cliente, true, this.conf_dias_gracia);//dias de gracia
        ListadoCarteraAdaptador adapter = new ListadoCarteraAdaptador(NuevaOfertaM.this, R.layout.fila_cartera_cliente, cursor, from, to);
        listadoCartera.setAdapter(adapter);
        /*Validar existencia de deudas*/
        cargarInfoCartera(cursor);
        if (cursor.getCount() > 0) {
            mostrarContenedorCartera();
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            Toast ts = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.empty_cartera), Toast.LENGTH_SHORT);
            ts.show();
        }
        helper.close();
    }
    public void cargarInfoCartera(Cursor cur){
        int count1 = 0;
        double tmp_saldo_vencer  = 0.0;
        double tmp_saldo_vencido = 0.0;
        int count2 = 0;
        if (cur.moveToFirst()) {
            do {
                //verificar si el documento esta vencido
                if (cur.getInt(cur.getColumnIndex(PCKardex.FIELD_dvencidos)) > 0) {
                    tmp_saldo_vencido += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldovencido));
                    count1++;
                    vencidos.setText(String.valueOf(count1));
                    saldo_vencido.setText(redondearNumero(tmp_saldo_vencido));
                }else{
                    vencidos.setText(String.valueOf(0));
                    saldo_vencido.setText(redondearNumero(0.00));
                }
                //verificar si el documento no esta vencido
                if (cur.getInt(cur.getColumnIndex(PCKardex.FIELD_dvencidos)) <= 0) {
                    tmp_saldo_vencer += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldoxvence));
                    count2++;
                    no_vencidos.setText(String.valueOf(count2));
                    saldo_vencer.setText(redondearNumero(tmp_saldo_vencer));
                } else {
                    no_vencidos.setText(String.valueOf(0));
                    saldo_vencer.setText(redondearNumero(0.00));
                }
            } while (cur.moveToNext());
        }
    }
    /*
        Metodos para ocultar contenedores
     */
    //Observaciones
    public void mostrarContenedorObservaciones() {
        if (contenedor_observaciones.getVisibility() == View.GONE) {
            animar(false, contenedor_observaciones);
            contenedor_observaciones.setVisibility(View.VISIBLE);
        }
    }

    public void mostrarContenedorAdicional() {
        if (contenedor_adicional.getVisibility() == View.GONE) {
            animar(false, contenedor_adicional);
            contenedor_adicional.setVisibility(View.VISIBLE);
        }
    }

    //Cartera
    public void mostrarContenedorCartera() {
        if (contenedor_cartera.getVisibility() == View.GONE) {
            animar(false, contenedor_cartera);
            contenedor_cartera.setVisibility(View.VISIBLE);
        }
    }

    public void ocultarContenedorCartera() {
        if (contenedor_cartera.getVisibility() == View.VISIBLE) {
            animar(true, contenedor_cartera);
            contenedor_cartera.setVisibility(View.GONE);
        }
    }



    public void mostrarContenedorItems() {
        if (contenedor_items.getVisibility() == View.GONE) {
            animar(false, contenedor_items);
            contenedor_items.setVisibility(View.VISIBLE);
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
    /***
     * Metodos para agregar/ eliminar / editar filas de la tabla de items
     */
    public void agregarFila(){

        if(this.count<this.limite){
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
            this.descripcion[count].setDropDownWidth(600);
            this.descripcion[count].setHint(getString( R.string.hint_producto));
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

            this.precio_real[count] = new EditText(this);
            this.precio_real[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.precio_real[count].setLayoutParams(layoutCeldas);
            this.precio_real[count].setText("0.0");
            this.precio_real[count].setBackgroundResource(R.drawable.text_field);
            this.precio_real[count].setGravity(Gravity.CENTER);
            this.precio_real[count].setEnabled(false);
            this.precio_real[count].setTextSize(14);
            this.precio_real[count].setPadding(5, 5, 5, 5);

            this.descuento[count] = new EditText(this);
            this.descuento[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.descuento[count].setLayoutParams(layoutCeldas);
            this.descuento[count].setBackgroundResource(R.drawable.text_field);
            this.descuento[count].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            this.descuento[count].setGravity(Gravity.CENTER);
            this.descuento[count].setEnabled(false);
            this.descuento[count].setTextSize(14);
            this.descuento[count].setText("0.0");
            this.descuento[count].setPadding(5, 5, 5, 5);

            this.total[count] = new EditText(this);
            this.total[count].setTextColor(resources.getColor(R.color.textColorPrimary));
            this.total[count].setLayoutParams(layoutCeldas);
            this.total[count].setBackgroundResource(R.drawable.text_field);
            this.total[count].setGravity(Gravity.CENTER);
            this.total[count].setEnabled(false);
            this.total[count].setText("0.0");
            this.total[count].setTextSize(14);
            this.total[count].setPadding(5, 5, 5, 5);

            this.seleccion[count] = new CheckBox(this);
            this.seleccion[count].setMinimumHeight(50);
        /*Agregando identificador por fila*/
            this.seleccion[count].setTag(count);
            this.cantidad[count].setTag(count);
            this.descuento[count].setTag(count);
            this.keypadres[count] = new GeneradorClaves().generarClave();
        /*listener para campo cantidad*/
            this.cantidad[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    if(new Validaciones().validar_enteros(cantidad[position].getText().toString())){
                        int cant = Integer.parseInt(cantidad[position].getText().toString());
                        validarExistencia(iditems[position], bodega.getText().toString(), cant);
                        if(verificarExistenciaDescuento(cli_id, iditems[position]) && verificarExistenciaPromocion(iditems[position], cant)) {
                            crearDialogOptionsDescPromo(position);
                        } else if (verificarExistenciaDescuento(cli_id, iditems[position])) {
                            double desc = calcularDescuentoItem(cli_id, iditems[position],position);
                            descuento[position].setText(redondearNumero(desc));
                            cantidad[position].setEnabled(false);
                            descripcion[position].setEnabled(false);
                            descuento[position].requestFocus();
                            descuento[position].setSelection(descuento[position].getText().length());
                            descuento[position].setEnabled(true);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(descuento[position], InputMethodManager.SHOW_IMPLICIT);
                        } else if (verificarExistenciaPromocion(iditems[position], cant)) {
                            calcularPromociones(iditems[position], cant, keypadres[position], position);
                            cantidad[position].setEnabled(false);
                            descripcion[position].setEnabled(false);
                            descuento[position].requestFocus();
                            descuento[position].setSelection(descuento[position].getText().length());
                            descuento[position].setEnabled(true);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(descuento[position], InputMethodManager.SHOW_IMPLICIT);
                        }else{
                            cantidad[position].setEnabled(false);
                            descripcion[position].setEnabled(false);
                            descuento[position].requestFocus();
                            descuento[position].setSelection(descuento[position].getText().length());
                            descuento[position].setEnabled(true);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(descuento[position], InputMethodManager.SHOW_IMPLICIT);
                        }
                    }else{
                        cantidad[position].requestFocus();
                        cantidad[position].setError(getString(R.string.info_field_no_valid));
                    }
                }
            });
            this.descuento[count].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = (Integer) v.getTag();
                    if(!descuento[position].getText().toString().isEmpty()){
                        double valor  = Double.parseDouble(descuento[position].getText().toString());
                        if(valor >= 0.0 && valor <= 100){
                            calcularSubtotalItem(position);
                            editar.setEnabled(true);guardar.setEnabled(true);
                            eliminar.setEnabled(true);
                            agregar.setEnabled(true);
                            editar.setChecked(false);
                            descuento[position].setEnabled(false);
                            calcularTotalTransaccion();
                            agregarFilaOpcional();
                        }else{
                            descuento[position].requestFocus();
                            descuento[position].setError(getString(R.string.info_field_no_valid));
                        }
                    }else{
                        descuento[position].requestFocus();
                        descuento[position].setError(getString(R.string.info_field_no_valid));
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
                        if(numeroSeleccionados()==1 && numeroEditados()==0 && iditems[position]!=null){
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
            fila.addView(precio_real[count]);
            fila.addView(descuento[count]);
            fila.addView(total[count]);
            fila.addView(seleccion[count]);
            tabla.addView(fila);
            count++;
        }else{
            Toast ts = Toast.makeText(getApplicationContext(), resources.getString(R.string.info_max_row_trans), Toast.LENGTH_SHORT);
            ts.show();
        }
    }
    /*Auto text Adapter*/
    class AutoCompleteProductoSelected extends CursorAdapter implements AdapterView.OnItemClickListener {
        DBSistemaGestion dbSistemaGestion;
        int posicion;
        public AutoCompleteProductoSelected(Context context, int row) {
            super(NuevaOfertaM.this, null);
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
            String descItem = cursor.getString(cursor.getColumnIndexOrThrow(IVInventario.FIELD_descripcion));

            if(val_duplicado){
                if(!validarDuplicado(idItem)){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(descripcion[posicion], InputMethodManager.SHOW_IMPLICIT);
                    descripcion[posicion].setText(descItem);
                    cantidad[posicion].setText("");
                    cantidad[posicion].setEnabled(true);
                    cantidad[posicion].requestFocus();
            /*Arreglos normales*/
                    bandiva[posicion] = ivaItem;
                    iditems[posicion] = idItem;

                    if (precio.getText().toString().equals(getString(R.string.info_precio_pcgrupo_disable))) {
                        try {
                            crearDialogPrecios(generarListadoPrecios(idItem,conf_precios_disponibles), conf_precios_disponibles, posicion);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //si el precio es acorde a PCGrupo
                        cargarPrecioUnitarioItem(cursor, posicion);
                    }
                    dbSistemaGestion.close();
                }else{
                    new Alertas(NuevaOfertaM.this,"Advertencia","El item seleccionado ya fue ingresado").mostrarMensaje();
                }
            }else{
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(descripcion[posicion], InputMethodManager.SHOW_IMPLICIT);
                descripcion[posicion].setText(descItem);
                cantidad[posicion].setText("");
                cantidad[posicion].setEnabled(true);
                cantidad[posicion].requestFocus();
            /*Arreglos normales*/
                bandiva[posicion] = ivaItem;
                iditems[posicion] = idItem;

                if (precio.getText().toString().equals(getString(R.string.info_precio_pcgrupo_disable))) {
                    try {
                        crearDialogPrecios(generarListadoPrecios(idItem,conf_precios_disponibles), conf_precios_disponibles, posicion);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    //si el precio es acorde a PCGrupo
                    cargarPrecioUnitarioItem(cursor, posicion);
                }
                dbSistemaGestion.close();
            }
        }
    }
    //fin clase autoadapter
    /*
        Validar si el item a ingresar ya fue registrado
     */
    public boolean validarDuplicado(String iditem){
        if(this.count>0){
            for(int i=0; i<this.count; i++){
                if(iditems[i]!=null)
                    if (iditems[i].equals(iditem))return true;
            }
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
            preciounitario[posicion] = null;
            descpromo[posicion] = 0;
            descuento[posicion].setText("0.00");
            //idpadres[posicion] = null;
            descripcion[posicion].setEnabled(true);
            descripcion[posicion].requestFocus();
            descripcion[posicion].setSelection(descripcion[posicion].getText().length());
            cantidad[posicion].setEnabled(false);
        }else{
            Toast ts= Toast.makeText(getApplicationContext(),resources.getString(R.string.info_fila_promocion_del),Toast.LENGTH_SHORT);
            ts.show();
        }
        calcularTotalTransaccion();
    }

    public void crearDialogOptionsDescPromo(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.tittle_dialog_desc_promo);
        alertDialogBuilder.setSingleChoiceItems(new String[]{"Descuento","Promocion"}, 0, null);
        /*Cargar Listado*/
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                ListView lv = ((AlertDialog) dialog).getListView();
                int posicion = lv.getCheckedItemPosition();
                switch (posicion) {
                    case 0:
                        double desc = calcularDescuentoItem(cli_id,iditems[position],position);
                        descuento[position].setText(redondearNumero(desc));
                       // solicitado[position].setText(redondearNumero(desc));
                        cantidad[position].setEnabled(false);
                        descripcion[position].setEnabled(false);
                        break;
                    case 1:
                        int cantidad_padre = Integer.parseInt(cantidad[position].getText().toString());
                        calcularPromociones(iditems[position],cantidad_padre,keypadres[position],position);
                        cantidad[position].setEnabled(false);
                        descripcion[position].setEnabled(false);
                        break;
                }
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //calcular total de la transaccion
    public boolean calcularTotalTransaccion() {
        double subtotal_sin_iva = 0.0;
        double subtotal_con_iva = 0.0;
        double total_iva = 0.0;
        try {
            for (int i = 0; i < this.count; i++) {
                if(iditems[i]!=null)
                    if (this.bandiva[i]!=1) {
                        subtotal_sin_iva += Double.parseDouble(this.total[i].getText().toString());
                    } else {
                        subtotal_con_iva += Double.parseDouble(this.total[i].getText().toString());
                    }
            }
            this.tran_sub_sin_iva.setText(redondearNumero(subtotal_sin_iva));
            this.tran_sub_con_iva.setText(redondearNumero(subtotal_con_iva));
            this.tran_sub.setText(redondearNumero(subtotal_con_iva + subtotal_sin_iva));
            String prf_imp = impuestos;
            double porc = 0.0;
            if(prf_imp!=null)porc=new Double(prf_imp)/100;
            total_iva = (subtotal_con_iva * porc);
            this.tran_sub_iva.setText(redondearNumero(total_iva));
            double monto = subtotal_con_iva + subtotal_sin_iva + total_iva;
            this.tran_sub_total.setText(redondearNumero(monto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean calcularSubtotalItem(int row) {
        double cnt = Double.parseDouble(cantidad[row].getText().toString());
        double precio = Double.parseDouble(preciounitario[row]);
        double porcentaje = Double.parseDouble(descuento[row].getText().toString()) / 100;

        double precio_total = cnt * precio;
        double desc = precio_total * porcentaje;
        double precioreal = (precio_total - desc) / cnt;
        double subtotal = cnt * precioreal;
        precio_real[row].setText(redondearNumero(precioreal));
        total[row].setText(redondearNumero(subtotal));
        if(Double.parseDouble(descuento[row].getText().toString())>0)descpromo[row] = 1;
        return true;
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
            if(iditems[posicion]!=null){
                if(bandpromo[posicion]!=1){
                    eliminarHijos(iditems[posicion], keypadres[posicion]);
                    numero[posicion].setTextColor(resources.getColor(R.color.color_danger));
                    descripcion[posicion].setText("FILA BORRADA");descripcion[posicion].setTextColor(resources.getColor(R.color.color_danger));
                    cantidad[posicion].setText("XXX");cantidad[posicion].setTextColor(resources.getColor(R.color.color_danger));
                    precio_real[posicion].setText("XXX");precio_real[posicion].setTextColor(resources.getColor(R.color.color_danger));
                    total[posicion].setText("XXX");total[posicion].setTextColor(resources.getColor(R.color.color_danger));
                    descuento[posicion].setText("XXX");descuento[posicion].setTextColor(resources.getColor(R.color.color_danger));
                /*Cambiar de variables Arreglos Normales*/
                    bandiva[posicion] = 0;
                    bandpromo[posicion] = 0;
                    numprecio[posicion] = 0;
                    idpadres[posicion] = null;
                    iditems[posicion] = null;
                    keypadres[posicion] = null;
                    numprecio[posicion] = 0;
                    preciounitario[posicion] = null;
                    descpromo[posicion] = 0;
                    this.limite++;
                }else Toast.makeText(getApplicationContext(),resources.getString(R.string.info_fila_promocion_del),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast ts= Toast.makeText(getApplicationContext(),resources.getString(R.string.info_imposible_delete_row),Toast.LENGTH_SHORT);
            ts.show();
        }
        calcularTotalTransaccion();
    }

    public void eliminarHijos(String id_padre, String keypadre){
        for(int i =0; i<count; i++){
            if(idpadres[i]!=null && idpadres[i].equals(id_padre) && keypadres[i].equals(keypadre)){

                numero[i].setTextColor(resources.getColor(R.color.color_danger));
                descripcion[i].setText("FILA BORRADA");descripcion[i].setTextColor(resources.getColor(R.color.color_danger));
                cantidad[i].setText("XXX");cantidad[i].setTextColor(resources.getColor(R.color.color_danger));
                precio_real[i].setText("XXX");precio_real[i].setTextColor(resources.getColor(R.color.color_danger));
                descuento[i].setText("XXX");descuento[i].setTextColor(resources.getColor(R.color.color_danger));
                total[i].setText("XXX");total[i].setTextColor(resources.getColor(R.color.color_danger));

                bandiva[i] = 0;
                bandpromo[i] = 0;
                numprecio[i] = 0;
                idpadres[i] = null;
                iditems[i] = null;
                keypadres[i] = null;
                numprecio[i] = 0;
                preciounitario[i] = null;
                descpromo[i] = 0;
                this.limite++;
            }
        }
    }


    public void agregarFilaPromocion(String id_item_padre, String keypadre, String id_item_hijo, String descripcion_hijo, int band_iva, double precio_item_hijo, int cantidad_total, double sub) {
        TableRow fila;
        if (count < limite) {
            iditems[count] = id_item_hijo;
            idpadres[count] = id_item_padre;
            keypadres[count] = keypadre;
            descpromo[count] = 2;
            bandpromo[count]=1;
            preciounitario[count] = redondearNumero(precio_item_hijo);
            numprecio[count] = Integer.parseInt(precio.getText().toString());
            if (band_iva != 0) {
                bandiva[count] = 1;
            }
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            layoutCeldaNro.span = 1;

            this.numero[count] = new EditText(this);
            this.numero[count].setText(String.valueOf(count + 1));
            this.numero[count].setTextColor(resources.getColor(R.color.successfull));
            this.numero[count].setLayoutParams(layoutCeldas);
            this.numero[count].setBackgroundResource(R.drawable.text_field);
            this.numero[count].setGravity(Gravity.CENTER);
            this.numero[count].setEnabled(false);
            this.numero[count].setTextSize(14);
            this.numero[count].setPadding(5, 5, 5, 5);

            this.descripcion[count] = new AutoCompleteTextView(this);
            this.descripcion[count].setTextColor(resources.getColor(R.color.successfull));
            this.descripcion[count].setText(descripcion_hijo);
            this.descripcion[count].setEnabled(false);
            this.descripcion[count].setLayoutParams(layoutCeldaDescripcion);
            this.descripcion[count].setBackgroundResource(R.drawable.text_field);
            this.descripcion[count].setGravity(Gravity.CENTER_VERTICAL);
            this.descripcion[count].setTextSize(14);
            this.descripcion[count].setPadding(5, 5, 5, 5);

            this.cantidad[count] = new EditText(this);
            this.cantidad[count].setTextColor(resources.getColor(R.color.successfull));
            this.cantidad[count].setLayoutParams(layoutCeldas);
            this.cantidad[count].setBackgroundResource(R.drawable.text_field);
            this.cantidad[count].setGravity(Gravity.CENTER);
            this.cantidad[count].setText(redondearNumero(cantidad_total));
            this.cantidad[count].setInputType(InputType.TYPE_CLASS_NUMBER);
            this.cantidad[count].setEnabled(false);
            this.cantidad[count].setTextSize(14);
            this.cantidad[count].setPadding(5, 5, 5, 5);

            this.precio_real[count] = new EditText(this);
            this.precio_real[count].setTextColor(resources.getColor(R.color.successfull));
            this.precio_real[count].setLayoutParams(layoutCeldas);
            this.precio_real[count].setText(redondearNumero(precio_item_hijo));
            this.precio_real[count].setBackgroundResource(R.drawable.text_field);
            this.precio_real[count].setGravity(Gravity.CENTER);
            this.precio_real[count].setEnabled(false);
            this.precio_real[count].setTextSize(14);
            this.precio_real[count].setPadding(5, 5, 5, 5);

            this.descuento[count] = new EditText(this);
            this.descuento[count].setTextColor(resources.getColor(R.color.successfull));
            this.descuento[count].setLayoutParams(layoutCeldas);
            this.descuento[count].setBackgroundResource(R.drawable.text_field);
            this.descuento[count].setGravity(Gravity.CENTER);
            this.descuento[count].setEnabled(false);
            this.descuento[count].setText("0.0");
            this.descuento[count].setTextSize(14);
            this.descuento[count].setPadding(5, 5, 5, 5);

            this.total[count] = new EditText(this);
            this.total[count].setTextColor(resources.getColor(R.color.successfull));
            this.total[count].setLayoutParams(layoutCeldas);
            this.total[count].setText(redondearNumero(sub));
            this.total[count].setBackgroundResource(R.drawable.text_field);
            this.total[count].setGravity(Gravity.CENTER);
            this.total[count].setEnabled(false);
            this.total[count].setTextSize(14);
            this.total[count].setPadding(5, 5, 5, 5);

            seleccion[count] = new CheckBox(this);
            seleccion[count].setLayoutParams(layoutCeldas);
            this.seleccion[count].setMinimumHeight(50);

            this.seleccion[count].setTag(count);

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

            fila.setMinimumHeight(50);
            fila.setPadding(0, 5, 0, 5);
            fila.addView(numero[count]);
            fila.addView(descripcion[count]);
            fila.addView(cantidad[count]);
            fila.addView(precio_real[count]);
            fila.addView(descuento[count]);
            fila.addView(total[count]);
            fila.addView(seleccion[count]);
            tabla.addView(fila);
            calcularSubtotalItem(count);
            this.count++;
        }else{
            Toast ts = Toast.makeText(getApplicationContext(), resources.getString(R.string.info_max_row_trans), Toast.LENGTH_SHORT);
            ts.show();
        }
    }
    //Mostrar Info items
    public void crearDialogInfoItem(int posicion) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.info_detalle_item, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle(R.string.tittle_dialog_show_detalle);
        //Variables de la vista
        TextView code = (TextView) promptsView.findViewById(R.id.info_item_code);
        TextView nombre = (TextView) promptsView.findViewById(R.id.info_item_nombre);
        TextView precio_real = (TextView) promptsView.findViewById(R.id.info_item_precio_real);
        TextView cant = (TextView) promptsView.findViewById(R.id.info_item_cantidad);
        TextView porc = (TextView) promptsView.findViewById(R.id.info_item_porcentaje);
        TextView total = (TextView) promptsView.findViewById(R.id.info_item_total);
        TextView precio = (TextView) promptsView.findViewById(R.id.info_item_precio);
        TextView presenta = (TextView) promptsView.findViewById(R.id.info_item_presentacion);
        TextView und = (TextView) promptsView.findViewById(R.id.info_item_unidad);
        TextView desc = (TextView) promptsView.findViewById(R.id.info_item_descuento);
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
            porc.setText(descuento[posicion].getText().toString());
            cant.setText(cantidad[posicion].getText().toString());
            presenta.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
            und.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad)));
            double puItem = Double.parseDouble(preciounitario[posicion]);
            precio.setText(redondearNumero(puItem));
            if (bandiva[posicion]!=1) {
                imp.setText("NO");
            }
            if(cantidad[posicion].getText().toString()!=null && !cantidad[posicion].getText().toString().isEmpty()){//verificar que la cantidad no este vacia
                double cnt = (Double.parseDouble(cantidad[posicion].getText().toString()));
                double prc = new Double(descuento[posicion].getText().toString());
                double porcentaje = prc/100 ;
                double precio_total = cnt*puItem;
                double descuento = precio_total*porcentaje;
                double pre_real = (precio_total - descuento) / cnt;
                double tot = cnt * pre_real;
                precio_real.setText(redondearNumero(pre_real));
                desc.setText(redondearNumero(new Double(descuento)));
                total.setText(redondearNumero(new Double(tot)));
            }
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
    //Validar promociones
    public boolean calcularPromociones(String id_item_padre, int cantidad_padre, String key_padre, int posicion) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarPromocionItem(id_item_padre,cantidad_padre);
        if (cursor.moveToFirst()){
            descpromo[posicion] = 0;
            int cantidad_minima = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Promocion.FIELD_cantidad_min)));
            String id_hijo = cursor.getString(cursor.getColumnIndex(Promocion.FIELD_id_inven_promo));
            double precio_item_hijo = cursor.getDouble(cursor.getColumnIndex(Promocion.FIELD_precio_promo));
            int cantidad_item_hijo = cursor.getInt(cursor.getColumnIndex(Promocion.FIELD_cantidad_promo));
            String desc_item_hijo = "";
            int iva_item_hijo = 0;
            Cursor cursor1 = helper.obtenerItem(id_hijo);
            if (cursor1.moveToFirst()) {
                iva_item_hijo = cursor1.getInt(cursor1.getColumnIndex(IVInventario.FIELD_band_iva));
                desc_item_hijo = cursor1.getString(cursor1.getColumnIndex(IVInventario.FIELD_descripcion));
            }
            cursor1.close();
                /*cargando arreglos globales*/
            int veces = (int) (cantidad_padre / cantidad_minima);
            int cantidad_total = 0;
            double sub_promo = 0.0;
            for (int i = 0; i < veces; i++) {
                cantidad_total += cantidad_item_hijo;
            }
            sub_promo = cantidad_total*precio_item_hijo;
            agregarFilaPromocion(id_item_padre,key_padre,id_hijo,desc_item_hijo,iva_item_hijo,precio_item_hijo,cantidad_total,sub_promo);
        }
        cursor.close();
        helper.close();
        return true;
    }
    /*
         Metodo para cargar precios
     */
    public boolean cargarPrecio(String cliente, int numgrupo) {
        try {
            DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
            Cursor cursor1 = helper.consultarPCGrupo(numgrupo, cliente);
            if (cursor1.moveToFirst()) {
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

    public void crearDialogPrecios(final String[] valores, final String[] indices, final int fila) {
        LayoutInflater li = LayoutInflater.from(NuevaOfertaM.this);
        View promptsView = li.inflate(R.layout.seleccionar_precio,
                null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NuevaOfertaM.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Seleccione un precio");
        /*Cargar Listado*/
        final ListView listadoPrecios = (ListView) promptsView.findViewById(R.id.seleccionarPrecio);
        ListadoPreciosAdapter adapter = new ListadoPreciosAdapter(NuevaOfertaM.this, valores, indices);
        listadoPrecios.setAdapter(adapter);
        alertDialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        listadoPrecios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                precio_real[fila].setText(valores[position]);
                preciounitario[fila] = valores[position];
                numprecio[fila] = Integer.parseInt(indices[position]);
            }
        });
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
                dialog.cancel();
            }
        });
        quitDialog.show();
    }
    public boolean validarCamposVacios() {
        if (TextUtils.isEmpty(trans_at.getText().toString())) {
            trans_at.requestFocus();
            trans_at.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_te.getText().toString())) {
            trans_te.requestFocus();
            trans_te.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_vl.getText().toString())){
            trans_vl.requestFocus();
            trans_vl.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_fp.getText().toString())){
            trans_fp.requestFocus();
            trans_fp.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_observacion.getText().toString())){
            trans_observacion.requestFocus();
            trans_observacion.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        return false;
    }
    public boolean validarCamposVaciosCliente() {
        if (TextUtils.isEmpty(trans_cli_ruc.getText().toString())) {
            trans_cli_ruc.requestFocus();
            trans_cli_ruc.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_cli_nombres.getText().toString())) {
            trans_cli_nombres.requestFocus();
            trans_cli_nombres.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_cli_comercial.getText().toString())){
            trans_cli_comercial.requestFocus();
            trans_cli_comercial.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_cli_direccion.getText().toString())){
            trans_cli_direccion.requestFocus();
            trans_cli_direccion.setError(resources.getString(R.string.info_field_required));
            return true;
        }else if (TextUtils.isEmpty(trans_cli_telefono.getText().toString())){
            trans_cli_telefono.requestFocus();
            trans_cli_telefono.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        return false;
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
            if(iditems[i]!=null && !descripcion[i].getText().toString().equals("FILA BORRADA")){
                result++;
            }
        }
        return result;
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
                DBSistemaGestion helper = new DBSistemaGestion(NuevaOfertaM.this);
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
    public String obtenerObservaciones() {
        return trans_observacion.getText().toString() +";" +trans_at.getText().toString()+ ";"+trans_te.getText().toString()+ ";" +trans_fp.getText().toString() + ";"  +trans_vl.getText().toString()+ ";" +trans_cli_ruc.getText().toString()+ ";"  +trans_cli_nombres.getText().toString()+ ";" +trans_cli_comercial.getText().toString()+  ";"  +trans_cli_direccion.getText().toString()+ ";"+trans_cli_telefono.getText().toString();
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
        DBSistemaGestion helper= new DBSistemaGestion(NuevaOfertaM.this);
        for(int i=0; i<count;i++) {
            if(iditems[i]!=null&&!descripcion[i].getText().toString().equals("FILA BORRADA")){
                IVKardex detalle = new IVKardex(
                        gen.generarClave(),
                        Double.parseDouble(cantidad[i].getText().toString()),
                        Double.parseDouble(preciounitario[i]),//precio de lista
                        0.0,//costo real total
                        Double.parseDouble(cantidad[i].getText().toString()) * Double.parseDouble(precio_real[i].getText().toString()),//precio total
                        Double.parseDouble(precio_real[i].getText().toString()),//precio real total
                        "",//descuento solicitado
                        Double.parseDouble(descuento[i].getText().toString()),//descuento real
                        (Double.parseDouble(preciounitario[i])*Double.parseDouble(cantidad[i].getText().toString()))*(Double.parseDouble(descuento[i].getText().toString())/100), //descuento real en dolares
                        id_trans,
                        bodega_id,
                        iditems[i],
                        idpadres[i],
                        keypadres[i],
                        descpromo[i],
                        numprecio[i],0.0,0,fecha);
                System.out.println("IVKardex generado: "+detalle.toString());
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
            progress = new ProgressDialog(NuevaOfertaM.this);
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
            DBSistemaGestion helper = new DBSistemaGestion(NuevaOfertaM.this);
            try {
                List<IVKardex> list = generarDetalles(id_trans, bodega_id);
                for(IVKardex ivk: list){
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agregar:
                if(count>0){
                    agregarFila();
                }else{
                    if(cli_id!=null){
                        agregarFila();
                    }else{
                        autoCompleteCliente.requestFocus();
                        autoCompleteCliente.setError(getString(R.string.info_no_select_cli));
                    }
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
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(NuevaOfertaM.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs),true);
                if (conf_observacion) {
                    if (!validarCamposVacios()) {
                        if(consu_final==1){
                            if(!validarCamposVaciosCliente()){
                                guardarTransaccion();
                            }
                        }else{
                            guardarTransaccion();
                        }
                    }
                } else guardarTransaccion();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nueva_oferta, menu);
        return true;
    }

    public void reload() {
        Intent intent = new Intent(NuevaOfertaM.this, NuevaOfertaM.class);
        intent.putExtra("transaccion", getIntent().getStringExtra("transaccion"));
        intent.putExtra("identificador", getIntent().getStringExtra("identificador"));
        intent.putExtra("accesos", getIntent().getStringExtra("accesos"));
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_change_client:
                if(!cli_id.isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NuevaOfertaM.this);
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
            case R.id.action_view_carter:
                if(!this.cli_id.isEmpty()){
                    generarReporteCartera(this.cli_id);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NuevaOfertaM.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }
}
