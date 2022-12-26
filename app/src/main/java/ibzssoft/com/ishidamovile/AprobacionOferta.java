package ibzssoft.com.ishidamovile;

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

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.ListadoCarteraAdaptador;
import ibzssoft.com.adaptadores.ListadoPreciosAdapter;
import ibzssoft.com.ishidamovile.reportes.DescuentosCliente;
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

public class AprobacionOferta extends AppCompatActivity implements View.OnClickListener {
    /*
          Campos de la cabecera
       */
    private TextView emision, hora, bodega, transaccion, precio, vendedor;
    /*
        Campos del cliente
     */
    private TextView cedula, nombres, comercial, direccion, telefono, correo, observacion;
    /*
        Campos de la cartera
     */
    private TextView vencidos, no_vencidos, saldo_vencer, saldo_vencido;
    /*
        Campos de validacion
     */
    private int limite, count, conf_pcg, conf_max_venci, conf_dias_gracia;
    private String cli_id;
    private int consu_final;
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
    private EditText[] numero, cantidad, precio_real, descuento, solicitado, total, cospro, utilidad;
    private int[] bandiva;
    private int[] bandpromo, bandapro;
    private int[] numprecio;
    private String[] porcdesc, porcsol;
    private String[] iditems, idpadres, keypadres, preciounitario;
    private int[] descpromo;
    private CheckBox[] seleccion;
    private AutoCompleteTextView[] descripcion;
    /*
        Recursos para inicializar tabla
     */
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila, layoutCeldaNro, layoutCeldas, layoutCeldaDescripcion;

    /*
        Variables total de transaccion
     */
    private TextView tran_sub_sin_iva, tran_sub_con_iva, tran_sub, tran_sub_iva, tran_sub_total;
    /*
        Botones de guardado y agregacion
     */
    private Button guardar;
    private ToggleButton aprobar;
    /*
        Transaccion observacion
     */
    private TextView trans_observacion, trans_at, trans_te, trans_fp, trans_vl;
    private TextView trans_cli_ruc, trans_cli_nombres, trans_cli_comercial, trans_cli_direccion, trans_cli_telefono;
    private Resources resources;

    /**
     * Variables para preferencias
     */
    private String impuestos, idvendedor, idusuario, lineas, bodegas;
    private int decimales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprobacion_oferta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.cargarPreferencias();
        this.inicializarCamposFormulario();
        this.cargarDatos(getIntent().getStringExtra("transid"));
        inicializarCamposTabla(this.limite);
        agregarFilasIniciales();
    }

    public void cargarPreferencias() {

        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(this);
        this.impuestos = ext.get(getString(R.string.key_empresa_iva), "12");
        this.idvendedor = ext.get(getString(R.string.key_act_ven), getString(R.string.pref_act_ven_default));
        this.lineas = ext.get(getString(R.string.key_act_lin), getString(R.string.pref_act_lin_default));
        this.idusuario = ext.get(getString(R.string.key_act_user), getString(R.string.pref_act_user_default));
        this.bodegas = ext.get(getString(R.string.key_act_bod), getString(R.string.pref_act_bod_default));
        String dec = ext.get(getString(R.string.key_empresa_decimales), "2");
        this.decimales = Integer.parseInt(dec);
    }


    public void inicializarCamposFormulario() {
     /*
        Campos de la transaccion
     */
        this.emision = (TextView) findViewById(R.id.nuevoPedidoFecha);
        this.hora = (TextView) findViewById(R.id.nuevoPedidoHora);
        this.bodega = (TextView) findViewById(R.id.nuevoPedidoBodega);
        this.precio = (TextView) findViewById(R.id.nuevoPedidoPrecio);
        this.transaccion = (TextView) findViewById(R.id.nuevoPedidoTrans);
        this.vendedor = (TextView) findViewById(R.id.nuevoPedidoVendedor);
     /*
        Campos del cliente
     */
        this.cedula = (TextView) findViewById(R.id.nuevoPedidoCI_RUC);
        this.nombres = (TextView) findViewById(R.id.nuevoPedidoNombres);
        this.comercial = (TextView) findViewById(R.id.nuevoPedidoNombreAlt);
        this.direccion = (TextView) findViewById(R.id.nuevoPedidoDireccion);
        this.telefono = (TextView) findViewById(R.id.nuevoPedidoTelefono);
        this.correo = (TextView) findViewById(R.id.nuevoPedidoCorreo);
        this.observacion = (TextView) findViewById(R.id.nuevoPedidoObs);
        /*
        * Campos de validacion
        */
        this.limite = 0;
        this.count = 0;
        this.cli_id = "";
        this.consu_final = 0;
        this.conf_pcg = 0;
        this.conf_max_venci = 0;
        this.conf_dias_gracia = 0;
        /*
            Contenedores de la vista
        */
        this.contenedor_cartera = (CardView) findViewById(R.id.contenedorCartera);
        this.contenedor_observaciones = (CardView) findViewById(R.id.contenedorObservaciones);
        this.contenedor_items = (CardView) findViewById(R.id.contenedorItems);
        this.contenedor_adicional = (CardView) findViewById(R.id.contenedorAdicionales);
         /*
            Campos del cartera
         */
        vencidos = (TextView) findViewById(R.id.nuevoPedidoDocsVencidos);
        no_vencidos = (TextView) findViewById(R.id.nuevoPedidoDocsVencer);
        saldo_vencer = (TextView) findViewById(R.id.nuevoPedidoSaldoVencer);
        saldo_vencido = (TextView) findViewById(R.id.nuevoPedidoSaldoVencido);
        /*
            Campos total transaccion
         */
        tran_sub_con_iva = (TextView) findViewById(R.id.subtotal);
        tran_sub_sin_iva = (TextView) findViewById(R.id.subtotal_0);
        tran_sub = (TextView) findViewById(R.id.subtotal_total);
        tran_sub_iva = (TextView) findViewById(R.id.iva);
        tran_sub_total = (TextView) findViewById(R.id.total);
        /*
            Campos observacion transaccion
         */
        trans_observacion = (TextView) findViewById(R.id.txtNuevoPedidoObservacion);
        trans_at = (TextView) findViewById(R.id.txtNuevoPedidoAT);
        trans_te = (TextView) findViewById(R.id.txtNuevoPedidoTE);
        trans_fp = (TextView) findViewById(R.id.txtNuevoPedidoFP);
        trans_vl = (TextView) findViewById(R.id.txtNuevoPedidoVL);

        trans_cli_ruc = (TextView) findViewById(R.id.info_cliente_ruc);
        trans_cli_nombres = (TextView) findViewById(R.id.info_cliente_nombres);
        trans_cli_comercial = (TextView) findViewById(R.id.info_cliente_comercial);
        trans_cli_direccion = (TextView) findViewById(R.id.info_cliente_direccion);
        trans_cli_telefono = (TextView) findViewById(R.id.info_cliente_telefono);

        resources = this.getResources();
    }

    public void inicializarCamposTabla(int maximo) {
        /*
            Campos de la tabla de items [VISTA]
         */
        seleccion = new CheckBox[100];
        numero = new EditText[100];
        descripcion = new AutoCompleteTextView[100];
        cantidad = new EditText[100];
        precio_real = new EditText[100];
        descuento = new EditText[100];
        solicitado = new EditText[100];
        cospro = new EditText[100];
        utilidad = new EditText[100];
        total = new EditText[100];
        bandiva = new int[100];
        bandpromo = new int[100];
        bandapro = new int[100];
        numprecio = new int[100];
        porcdesc = new String[100];
        porcsol = new String[100];

        iditems = new String[100];
        preciounitario = new String[100];
        idpadres = new String[100];
        keypadres = new String[100];
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
        aprobar = (ToggleButton) findViewById(R.id.aprobar);
        guardar = (Button) findViewById(R.id.guardar);
        resources = this.getResources();
        guardar.setOnClickListener(this);
        guardar.setOnClickListener(this);
        aprobar.setOnClickListener(this);
    }

    /*
        * Cargar Configuracion de la Transaccion
     */
    public void cargarDatos(String id_trans) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.obtenerTransaccion(id_trans);
        if (cursor.moveToFirst()) {
            cli_id = cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovcli));
            cedula.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)));
            if (cedula.getText().toString().equals("9999999999999")) {
                consu_final = 1;
                mostrarContenedorAdicional();
            }
            observacion.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_observacion)));
            nombres.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)));
            comercial.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)));
            direccion.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion1)));
            telefono.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono1)));
            correo.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_email)));
            emision.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            hora.setText(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_hora_trans)));
            vendedor.setText(cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
            //System.out.println("Parametro para el cursor: " + cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            Cursor cursor1 = helper.consultarGNTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            if (cursor1.moveToFirst()) {
                Cursor cursor2 = helper.consultarBodega(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                if (cursor2.moveToFirst()) {
                    bodega.setText(cursor2.getString(cursor2.getColumnIndex(Bodega.FIELD_codbodega)));
                }
                cursor2.close();
                transaccion.setText(cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_codtrans)));
                String conf_limite = cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_numfilas));
                String max_pck = cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_maxdocs));
                this.limite = Integer.parseInt(conf_limite);
                this.conf_max_venci = Integer.parseInt(max_pck);
                //System.out.println("Filas maximas para esta transaccion: " + this.limite);
                String conf_dg = cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_diasgracia));
                conf_dias_gracia = (Integer.parseInt(conf_dg));
                //System.out.println("Dias Gracia cartera: "+conf_dias_gracia);
                /*Configuracion de observaciones y precios*/
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(AprobacionOferta.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs), true);
                if (conf_observacion) mostrarContenedorObservaciones();
                cargarObservaciones(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_descripcion)));
                if (cursor1.getString(cursor1.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")) {
                    precio.setText(getString(R.string.info_precio_pcgrupo_enabled));
                    int conf_num_pcg = ext.configuracionPreciosGrupos();
                    //      System.out.println("Numero tabla precios: " + conf_num_pcg);
                    if (precio.getText().toString().equals(getString(R.string.info_precio_pcgrupo_disable)) != true) {
                        cargarPrecio(cli_id, conf_num_pcg);
                    }
                } else {
                    precio.setText(getString(R.string.info_precio_pcgrupo_disable));
                }
            }
            cursor1.close();
            cargarConfiguracionCartera();
            int actions = cargarConfiguracionCartera();
            //System.out.println("Configuracion cartera: " + actions);
            switch (actions) {
                case 0:
                    //deshabilitar
                    mostrarContenedorItems();
                    ocultarContenedorCartera();
                    break;
                case 1:
                    //advertir
                    generarReporteCartera(cli_id);
                    mostrarContenedorItems();
                    break;
                case 2:
                    //bloquear
                    generarReporteCartera(cli_id);
                    int docs = Integer.parseInt(vencidos.getText().toString());
                    System.out.println("Documentos vencidos: " + docs + " maximo: " + conf_max_venci);
                    if (docs > conf_max_venci) {
                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(AprobacionOferta.this);
                        builder1.setTitle("Transacción Bloqueada");
                        builder1.setMessage("Revise el numero de documentos vencidos");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        mostrarContenedorItems();
                                        dialog.cancel();
                                    }
                                });

                        android.support.v7.app.AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    break;
            }
            cursor.close();
            helper.close();
        }
    }

    public void cargarObservaciones(String datos) {
        //observacion,solitante,tiempo entrega,forma,validez,atencion
        System.out.println("Observaciones Cargadas: " + datos);
        String data[] = datos.split(";");
        try {
            if (data[0].isEmpty() != true) trans_observacion.setText(data[0]);
            if (data[1].isEmpty() != true) trans_at.setText(data[1]);
            if (data[2].isEmpty() != true) trans_te.setText(data[2]);
            if (data[3].isEmpty() != true) trans_fp.setText(data[3]);
            if (data[4].isEmpty() != true) trans_vl.setText(data[4]);
            if (data[5].isEmpty() != true) trans_cli_ruc.setText(data[5]);
            if (data[6].isEmpty() != true) trans_cli_nombres.setText(data[6]);
            if (data[7].isEmpty() != true) trans_cli_comercial.setText(data[7]);
            if (data[8].isEmpty() != true) trans_cli_direccion.setText(data[8]);
            if (data[9].isEmpty() != true) trans_cli_telefono.setText(data[9]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarReporteCartera(String cliente) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.cartera, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        /*Cargar Listado*/
        ListView listadoCartera = (ListView) promptsView.findViewById(R.id.vst_cartera_listado);
        String[] from = new String[]{};
        int[] to = new int[]{};
        DBSistemaGestion helper = new DBSistemaGestion(this);
        Cursor cursor = helper.consultarCarteraCliente(cliente, true, this.conf_dias_gracia);//dias de gracia
        ListadoCarteraAdaptador adapter = new ListadoCarteraAdaptador(AprobacionOferta.this, R.layout.fila_cartera_cliente, cursor, from, to);
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
            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            Toast ts = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.empty_cartera), Toast.LENGTH_SHORT);
            ts.show();
        }
        helper.close();
    }

    public void cargarInfoCartera(Cursor cur) {
        int count1 = 0;
        double tmp_saldo_vencer = 0.0;
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
                } else {
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

    public int cargarConfiguracionCartera() {
        DBSistemaGestion helper = new DBSistemaGestion(AprobacionOferta.this);
        Cursor cursor = helper.consultarGNTrans(transaccion.getText().toString());
        int actions = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                actions = cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_opciones));
            }
        }
        helper.close();
        return actions;
    }

    /*
         Metodo para cargar precios
     */
    public boolean cargarPrecio(String cliente, int numgrupo) {
        System.out.println("Parame");
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

    /***
     * Metodos para agregar/ eliminar / editar filas de la tabla de items
     */
    public void agregarFilasIniciales() {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarIVKardex(getIntent().getStringExtra("transid"));
        this.count = 0;
        if (cursor.moveToFirst()) {
            do {
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);

                this.iditems[this.count] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_identificador));
                this.idpadres[this.count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id));
                this.bandiva[this.count] = cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva));
                this.porcdesc[this.count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_descuento));
                this.porcsol[this.count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol));

                this.numprecio[this.count] = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio));

                this.descpromo[this.count] = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo));
                this.keypadres[count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_keypadre));
                this.bandapro[this.count] = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_band_aprobado));


                this.preciounitario[count] = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_cos_total));

                if (this.idpadres[this.count] != null) this.bandpromo[this.count] = 1;
                this.numero[this.count] = new EditText(this);
                this.numero[this.count].setText(String.valueOf(this.count + 1));
                this.numero[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));

                if (this.bandpromo[this.count] == 1)
                    this.numero[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.numero[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.numero[this.count].setLayoutParams(layoutCeldas);
                this.numero[this.count].setBackgroundResource(R.drawable.text_field);
                this.numero[this.count].setGravity(Gravity.CENTER);
                this.numero[this.count].setEnabled(false);
                this.numero[this.count].setTextSize(14);
                this.numero[this.count].setPadding(5, 5, 5, 5);

                this.descripcion[this.count] = new AutoCompleteTextView(this);
                this.descripcion[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                this.descripcion[this.count].setHint(getString(R.string.hint_producto));
                if (this.bandpromo[this.count] == 1)
                    this.descripcion[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.descripcion[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.descripcion[this.count].setEnabled(false);
                this.descripcion[this.count].setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                this.descripcion[this.count].setLayoutParams(layoutCeldaDescripcion);
                this.descripcion[this.count].setBackgroundResource(R.drawable.text_field);
                this.descripcion[this.count].setGravity(Gravity.CENTER_VERTICAL);
                this.descripcion[this.count].setTextSize(14);
                this.descripcion[this.count].setPadding(5, 5, 5, 5);


                this.cantidad[this.count] = new EditText(this);
                this.cantidad[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.cantidad[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.cantidad[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.cantidad[this.count].setLayoutParams(layoutCeldas);
                this.cantidad[this.count].setBackgroundResource(R.drawable.text_field);
                this.cantidad[this.count].setGravity(Gravity.CENTER);
                this.cantidad[this.count].setHint("0");
                this.cantidad[this.count].setInputType(InputType.TYPE_CLASS_NUMBER);
                this.cantidad[this.count].setEnabled(false);
                this.cantidad[this.count].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_cantidad)));
                this.cantidad[this.count].setTextSize(14);
                this.cantidad[this.count].setPadding(5, 5, 5, 5);

                this.precio_real[this.count] = new EditText(this);
                this.precio_real[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.precio_real[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.precio_real[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.precio_real[this.count].setLayoutParams(layoutCeldas);
                this.precio_real[this.count].setBackgroundResource(R.drawable.text_field);
                this.precio_real[this.count].setGravity(Gravity.CENTER);
                this.precio_real[this.count].setEnabled(false);
                this.precio_real[this.count].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total))));
                this.precio_real[this.count].setTextSize(14);
                this.precio_real[this.count].setPadding(5, 5, 5, 5);

                this.descuento[this.count] = new EditText(this);
                this.descuento[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.descuento[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.descuento[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.descuento[this.count].setLayoutParams(layoutCeldas);
                this.descuento[this.count].setBackgroundResource(R.drawable.text_field);
                this.descuento[count].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                this.descuento[this.count].setGravity(Gravity.CENTER);
                this.descuento[this.count].setEnabled(false);
                this.descuento[this.count].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento))));
                this.descuento[this.count].setTextSize(14);
                this.descuento[this.count].setPadding(5, 5, 5, 5);

                this.solicitado[this.count] = new EditText(this);
                this.solicitado[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.solicitado[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.solicitado[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.solicitado[this.count].setLayoutParams(layoutCeldas);
                this.solicitado[this.count].setBackgroundResource(R.drawable.text_field);
                this.solicitado[this.count].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                this.solicitado[this.count].setGravity(Gravity.CENTER);
                this.solicitado[this.count].setEnabled(false);
                this.solicitado[this.count].setText(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)));
                this.solicitado[this.count].setTextSize(14);
                this.solicitado[this.count].setPadding(5, 5, 5, 5);

                this.cospro[this.count] = new EditText(this);
                this.cospro[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.cospro[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.cospro[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.cospro[this.count].setLayoutParams(layoutCeldas);
                this.cospro[this.count].setBackgroundResource(R.drawable.text_field);
                this.cospro[count].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                this.cospro[this.count].setGravity(Gravity.CENTER);
                this.cospro[this.count].setEnabled(false);
                this.cospro[this.count].setText(redondearNumeroBase4(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_pro))));
                this.cospro[this.count].setTextSize(14);
                this.cospro[this.count].setPadding(5, 5, 5, 5);

                this.utilidad[this.count] = new EditText(this);
                this.utilidad[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.utilidad[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.utilidad[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.utilidad[this.count].setLayoutParams(layoutCeldas);
                this.utilidad[this.count].setBackgroundResource(R.drawable.text_field);
                this.utilidad[count].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                this.utilidad[this.count].setGravity(Gravity.CENTER);
                this.utilidad[this.count].setEnabled(false);
                double util = this.calcularUtilidad(redondearNumeroBase4(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_pro))), redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_total))), cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)), redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento))));
                if (util < 0)
                    this.utilidad[this.count].setTextColor(getResources().getColor(R.color.color_danger));
                this.utilidad[this.count].setText(redondearNumero(util));
                this.utilidad[this.count].setTextSize(14);
                this.utilidad[this.count].setPadding(5, 5, 5, 5);

                this.total[this.count] = new EditText(this);
                this.total[this.count].setTextColor(getResources().getColor(R.color.textColorPrimary));
                if (this.bandpromo[this.count] == 1)
                    this.total[this.count].setTextColor(getResources().getColor(R.color.successfull));
                if (this.bandapro[this.count] == 1)
                    this.total[this.count].setTextColor(getResources().getColor(R.color.updateCatalog));
                this.total[this.count].setLayoutParams(layoutCeldas);
                this.total[this.count].setBackgroundResource(R.drawable.text_field);
                this.total[this.count].setGravity(Gravity.CENTER);
                this.total[this.count].setEnabled(false);
                this.total[this.count].setText(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total)) * cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_cantidad))));
                this.total[this.count].setTextSize(14);
                this.total[this.count].setPadding(5, 5, 5, 5);

                this.seleccion[count] = new CheckBox(this);
                this.seleccion[count].setMinimumHeight(50);
                if (cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)).compareTo("") == 0) {
                    this.seleccion[count].setEnabled(false);
                }

                /*Agregando identificador por fila*/
                this.seleccion[count].setTag(count);
                this.cantidad[count].setTag(count);
                this.descuento[count].setTag(count);
                /*Listener para aprobacion*/
                this.seleccion[count].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = (Integer) v.getTag();
                        if (aprobar.isChecked()) {
                            System.out.println("aprobar fila: " + position);
                            if (seleccion[position].isChecked()) {
                                if (bandapro[position] != 1) aprobarFila(position);
                            } else if (bandapro[position] != 0) desaprobarFila(position);
                        } else {
                            if (iditems[position] != null && seleccion[position].isChecked()) {
                                crearDialogInfoItem(position);
                            }
                        }
                    }
                });

                fila.setPadding(0, 5, 0, 5);
                fila.setMinimumHeight(50);

                fila.addView(numero[this.count]);
                fila.addView(descripcion[this.count]);
                fila.addView(cantidad[this.count]);
                fila.addView(precio_real[this.count]);
                fila.addView(descuento[this.count]);
                fila.addView(solicitado[this.count]);
                fila.addView(cospro[this.count]);
                fila.addView(utilidad[this.count]);
                fila.addView(total[this.count]);
                fila.addView(seleccion[this.count]);
                tabla.addView(fila);
                this.calcularSubtotalItem(this.count);
                this.count++;
            } while (cursor.moveToNext());
            calcularTotalTransaccion();
        }
    }

    /**
     * Metodo para mostrar opciones por fila
     */
    public double calcularUtilidad(String cospro, String preciolista, String porcsolicita, String desc_org) {
        double porc = 0.0;
        double pre = Double.parseDouble(preciolista);
        double cosprom = Double.parseDouble(cospro);
        double desorg = Double.parseDouble(desc_org);
        if (!porcsolicita.isEmpty()) {
            porc = Double.parseDouble(porcsolicita) / 100;
        } else {
            porc = desorg / 100;
        }
        double pre_porc = pre * porc;
        double val = (pre_porc - pre) * -1;
        double res = 1 - (cosprom / val);
        return res * 100;
    }


    //Mostrar Info items
    public void crearDialogInfoItem(int posicion) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.info_detalle_item_oferta, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle(R.string.tittle_dialog_show_detalle);
        //Variables de la vista
        TextView code = (TextView) promptsView.findViewById(R.id.info_item_code);
        TextView nombre = (TextView) promptsView.findViewById(R.id.info_item_nombre);
        TextView precio_real_org = (TextView) promptsView.findViewById(R.id.info_item_precio_real_org);
        TextView precio_real_sol = (TextView) promptsView.findViewById(R.id.info_item_precio_real_sol);
        TextView cant = (TextView) promptsView.findViewById(R.id.info_item_cantidad);
        TextView porc_org = (TextView) promptsView.findViewById(R.id.info_item_porcentaje_original);
        TextView porc_sol = (TextView) promptsView.findViewById(R.id.info_item_porcentaje_solicitado);
        TextView total_org = (TextView) promptsView.findViewById(R.id.info_item_total_orinal);
        TextView total_sol = (TextView) promptsView.findViewById(R.id.info_item_total_solicitado);
        TextView precio_real_org_iva = (TextView) promptsView.findViewById(R.id.info_item_precio_real_org_iva);
        TextView precio_real_sol_iva = (TextView) promptsView.findViewById(R.id.info_item_precio_real_sol_iva);

        TextView precio = (TextView) promptsView.findViewById(R.id.info_item_precio);
        TextView presenta = (TextView) promptsView.findViewById(R.id.info_item_presentacion);
        TextView und = (TextView) promptsView.findViewById(R.id.info_item_unidad);
        TextView desc = (TextView) promptsView.findViewById(R.id.info_item_descuento);
        TextView imp = (TextView) promptsView.findViewById(R.id.info_item_iva);
        TextView desc_org = (TextView) promptsView.findViewById(R.id.info_item_descuento_original);
        TextView desc_sol = (TextView) promptsView.findViewById(R.id.info_item_descuento_solicitado);
        TableLayout tablaExistencia = (TableLayout) promptsView.findViewById(R.id.infoExiTableLayoutProm);
        TableRow.LayoutParams layoutRow = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50, 1);
        TableRow.LayoutParams layoutCell = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1);
        //cargando datos
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarItem(iditems[posicion]);
        if (cursor.moveToFirst()) {
            code.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)));
            nombre.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
            porc_org.setText(redondearNumero(new Double(descuento[posicion].getText().toString())));
            porc_sol.setText(solicitado[posicion].getText().toString());
            cant.setText(cantidad[posicion].getText().toString());
            presenta.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
            und.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad)));
            double puItem = Double.parseDouble(preciounitario[posicion]);
            precio.setText(redondearNumero(puItem));
            if (bandiva[posicion] != 1) {
                imp.setText("NO");
            }
            if (cantidad[posicion].getText().toString() != null && !cantidad[posicion].getText().toString().isEmpty()) {//verificar que la cantidad no este vacia
                double cnt = (Double.parseDouble(cantidad[posicion].getText().toString()));
                double prc = new Double(descuento[posicion].getText().toString());
                double porcentaje = prc / 100;
                double precio_total = cnt * puItem;
                double descuento = precio_total * porcentaje;
                double pre_real = (precio_total - descuento) / cnt;
                double tot = cnt * pre_real;
                precio_real_org.setText(redondearNumero(pre_real));
                precio_real_org_iva.setText(redondearNumero(pre_real));
                desc_org.setText(redondearNumero(new Double(descuento)));
                total_org.setText(redondearNumero(new Double(tot)));
                if (bandiva[posicion] == 1) {
                    String prf_imp = this.impuestos;
                    double porc = 0.0;
                    if (prf_imp != null) porc = new Double(prf_imp) / 100;
                    double pre_real_org_iva = pre_real * porc;
                    precio_real_org_iva.setText(redondearNumero(pre_real_org_iva + pre_real));
                }
                /*Calculo para descuento solicitado*/
                if (!solicitado[posicion].getText().toString().isEmpty()) {
                    double prc_sol = new Double(solicitado[posicion].getText().toString());
                    double porcentaje_sol = prc_sol / 100;
                    double precio_total_sol = cnt * puItem;
                    double descuento_sol = precio_total_sol * porcentaje_sol;
                    double pre_real_sol = (precio_total_sol - descuento_sol) / cnt;
                    double tot_sol = cnt * pre_real_sol;
                    precio_real_sol.setText(redondearNumero(pre_real_sol));
                    precio_real_sol_iva.setText(redondearNumero(pre_real_sol));
                    desc_sol.setText(redondearNumero(new Double(descuento_sol)));
                    total_sol.setText(redondearNumero(new Double(tot_sol)));
                    if (bandiva[posicion] == 1) {
                        String prf_imp = this.impuestos;
                        double porc = 0.0;
                        if (prf_imp != null) porc = new Double(prf_imp) / 100;
                        double pre_real_sol_iva = pre_real_sol * porc;
                        precio_real_sol_iva.setText(redondearNumero(pre_real_sol_iva + pre_real_sol));
                    }
                }
            }
        }
        cursor.close();
    /*Generar reporte de existencia*/
        TableRow fila;
        int pst = 0;
        Cursor cur1 = helper.obtenerExistenciaItem(iditems[posicion], bodegas.split(","));
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
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void crearDialogOptionsDescPromo(final int position) {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.tittle_dialog_desc_promo);
        alertDialogBuilder.setSingleChoiceItems(new String[]{"Descuento", "Promocion"}, 0, null);
        /*Cargar Listado*/
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                ListView lv = ((AlertDialog) dialog).getListView();
                int posicion = lv.getCheckedItemPosition();
                System.out.println("Posicion: " + position);
                switch (posicion) {
                    case 0:
                        System.out.println("Aplicando Descuento: " + iditems[position]);
                        double desc = calcularDescuentoItem(cli_id, iditems[position], position);
                        descuento[position].setText(redondearNumero(desc));
                        cantidad[position].setEnabled(false);
                        descripcion[position].setEnabled(false);
                        descuento[position].requestFocus();
                        descuento[position].setSelection(descuento[position].getText().length());
                        descuento[position].setEnabled(true);
                        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.showSoftInput(descuento[position], InputMethodManager.SHOW_IMPLICIT);
                        break;
                    case 1:
                        System.out.println("Aplicando Promocion: " + posicion);
                        int cantidad_padre = Integer.parseInt(cantidad[position].getText().toString());
                        calcularPromociones(iditems[position], cantidad_padre, keypadres[position], position);
                        cantidad[position].setEnabled(false);
                        descripcion[position].setEnabled(false);
                        descuento[position].requestFocus();
                        descuento[position].setSelection(descuento[position].getText().length());
                        descuento[position].setEnabled(true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(descuento[position], InputMethodManager.SHOW_IMPLICIT);
                        break;
                }
            }
        });
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean calcularSubtotalItem(int row) {
        System.out.println("Calculando subtotal item");
        double cnt = Double.parseDouble(cantidad[row].getText().toString());
        double precio = Double.parseDouble(preciounitario[row]);
        double porcentaje = Double.parseDouble(descuento[row].getText().toString()) / 100;

        double precio_total = cnt * precio;
        double desc = precio_total * porcentaje;
        double precioreal = (precio_total - desc) / cnt;
        double subtotal = cnt * precioreal;
        precio_real[row].setText(redondearNumero(precioreal));
        total[row].setText(redondearNumero(subtotal));
        System.out.println("Calculando subtotal item porcentaje: " + porcentaje + " precio: " + precio + " precio_real: " + precio_real);
        if (Double.parseDouble(descuento[row].getText().toString()) > 0) descpromo[row] = 1;
        return true;
    }

    /*
    Metodo para cargar descuento
 */
    public void crearDialogPrecios(final String[] valores, final String[] indices, final int fila) {
        LayoutInflater li = LayoutInflater.from(AprobacionOferta.this);
        View promptsView = li.inflate(R.layout.seleccionar_precio,
                null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(AprobacionOferta.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Seleccione un precio");
        /*Cargar Listado*/
        final ListView listadoPrecios = (ListView) promptsView.findViewById(R.id.seleccionarPrecio);
        ListadoPreciosAdapter adapter = new ListadoPreciosAdapter(AprobacionOferta.this, valores, indices);
        listadoPrecios.setAdapter(adapter);
        alertDialogBuilder.setCancelable(false);
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        listadoPrecios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                precio_real[fila].setText(valores[position]);
                preciounitario[fila] = valores[position];
                numprecio[fila] = Integer.parseInt(indices[position]);
                System.out.println("Precio seleccionado: " + valores[position] + ";" + indices[position]);
            }
        });
    }

    public String[] generarListadoPrecios(String item) throws SQLException {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarItem(item);
        String[] result = new String[7];
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result[0] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio1));
                result[1] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio2));
                result[2] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio3));
                result[3] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio4));
                result[4] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio5));
                result[5] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio6));
                result[6] = cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_precio7));
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

    //calcular total de la transaccion
    public boolean calcularTotalTransaccion() {
        double subtotal_sin_iva = 0.0;
        double subtotal_con_iva = 0.0;
        double total_iva = 0.0;
        try {
            for (int i = 0; i < this.count; i++) {
                //System.out.println("subtotales: " + insTotales[i] + " -> " + insDescripciones[i] + " -> " + insIDItem[i] + " -> PADRE: " + insPadre[i] + " PROMO-> " + insPromo[i]);
                if (iditems[i] != null)
                    if (this.bandiva[i] != 1) {
                        subtotal_sin_iva += Double.parseDouble(this.total[i].getText().toString());
                    } else {
                        subtotal_con_iva += Double.parseDouble(this.total[i].getText().toString());
                    }
            }
            this.tran_sub_sin_iva.setText(redondearNumero(subtotal_sin_iva));
            this.tran_sub_con_iva.setText(redondearNumero(subtotal_con_iva));
            this.tran_sub.setText(redondearNumero(subtotal_con_iva + subtotal_sin_iva));
            String prf_imp = this.impuestos;
            double porc = 0.0;
            if (prf_imp != null) porc = new Double(prf_imp) / 100;
            total_iva = (subtotal_con_iva * porc);
            this.tran_sub_iva.setText(redondearNumero(total_iva));
            double monto = subtotal_con_iva + subtotal_sin_iva + total_iva;
            this.tran_sub_total.setText(redondearNumero(monto));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //Validar promociones
    public boolean calcularPromociones(String id_item_padre, int cantidad_padre, String key_padre, int posicion) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor = helper.consultarPromocionItem(id_item_padre, cantidad_padre);
        if (cursor.moveToFirst()) {
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
            sub_promo = cantidad_total * precio_item_hijo;
        }
        cursor.close();
        helper.close();
        return true;
    }

    public void aprobarFila(int posicion) {
        try {
            bandapro[posicion] = 1;
            numero[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            descripcion[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            cantidad[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            precio_real[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            descuento[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            total[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            solicitado[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            cospro[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            utilidad[posicion].setTextColor(resources.getColor(R.color.updateCatalog));
            //calcularSubtotalItem(posicion);
                /*Cambiar de variables Arreglos Normales*/
        } catch (Exception e) {
            e.printStackTrace();
            Toast ts = Toast.makeText(getApplicationContext(), resources.getString(R.string.info_imposible_delete_row), Toast.LENGTH_SHORT);
            ts.show();
        }
        calcularTotalTransaccion();
    }

    public void desaprobarFila(int posicion) {
        try {
            bandapro[posicion] = 0;
            numero[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            descripcion[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            cantidad[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            precio_real[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            descuento[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            solicitado[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            total[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            cospro[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            utilidad[posicion].setTextColor(resources.getColor(R.color.textColorPrimary));
            /*Cambiar de variables Arreglos Normales*/
            //calcularSubtotalItem(posicion);
        } catch (Exception e) {
            e.printStackTrace();
            Toast ts = Toast.makeText(getApplicationContext(), resources.getString(R.string.info_imposible_delete_row), Toast.LENGTH_SHORT);
            ts.show();
        }
        calcularTotalTransaccion();
    }


    public String obtenerObservaciones() {
        //observacion,solitante,tiempo entrega,forma,validez,ruc, razon social, nombre comercial, direccion,telefono
        return trans_observacion.getText().toString() + ";" + trans_at.getText().toString() + ";" + trans_te.getText().toString() + ";" + trans_fp.getText().toString() + ";" + trans_vl.getText().toString() + ";" + trans_cli_ruc.getText().toString() + ";" + trans_cli_nombres.getText().toString() + ";" + trans_cli_comercial.getText().toString() + ";" + trans_cli_direccion.getText().toString() + ";" + trans_cli_telefono.getText().toString();
    }


    public void alertaTransaccionGuardada() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.save_success_transaction,
                (ViewGroup) findViewById(R.id.custom_toast_layout_id3));
        ((TextView) layout.findViewById(R.id.toast_tittle)).setText("Transacción Registrada");
        ((TextView) layout.findViewById(R.id.toast_subtittle)).setText("La transacción ha sido registrada correctamente!");
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
        int dec = this.decimales;
        String clave = "";
        switch (dec) {
            case 2:
                clave = "0.00";
                break;
            case 3:
                clave = "0.000";
                break;
            case 4:
                clave = "0.0000";
                break;
            case 5:
                clave = "0.00000";
                break;
        }
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
    }

    public String redondearNumeroBase4(double numero) {
        String clave = "0.0000";
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
    }

    /*
    * Agregando eventos
    */
    public boolean guardarTrans() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Esta seguro de guardar la transacción?");
        alertDialogBuilder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                DBSistemaGestion helper = new DBSistemaGestion(AprobacionOferta.this);
                GeneradorClaves gen = new GeneradorClaves();
                gen.generarClave();
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String fecha = sdf1.format(new Date());
                Cursor cursor1 = helper.obtenerTransaccion(getIntent().getStringExtra("transid"));
                Transaccion trans = null;
                if (cursor1.moveToFirst()) {
                    trans = new Transaccion(cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_ID_Trans)),
                            transaccion.getText().toString(),
                            obtenerObservaciones(),
                            cursor1.getInt(cursor1.getColumnIndex(Transaccion.FIELD_numTransaccion)),
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_fecha_trans)),
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_hora_trans)),
                            cursor1.getInt(cursor1.getColumnIndex(Transaccion.FIELD_band_enviado)),
                            idvendedor,
                            cli_id, null,
                            cursor1.getString(cursor1.getColumnIndex(Transaccion.FIELD_referencia)), fecha);
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
        switch (v.getId()) {

            case R.id.guardar:
                //System.out.println("Intentando guardar:");
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(AprobacionOferta.this);
                boolean conf_observacion = ext.getBoolean(getString(R.string.key_act_obs), true);
                if (conf_observacion) {
                    if (!validarCamposVacios()) {
                        if (consu_final == 1) {
                            if (!validarCamposVaciosCliente()) {
                                guardarTransaccion();
                            }
                        } else {
                            guardarTransaccion();
                        }
                    }
                } else guardarTransaccion();
                break;
            case R.id.aprobar:
                if (aprobar.isChecked()) {
                    System.out.println("Aprobando");
                    //si esta habilitado
                    for (int i = 0; i < count; i++) {
                        seleccion[i].setChecked(false);
                    }
                } else {
                    guardar.setEnabled(true);
                    for (int i = 0; i < count; i++) {
                        seleccion[i].setChecked(false);
                    }
                }
                break;
        }

    }

    public int filasCompletadas() {
        int result = 0;
        for (int i = 0; i < count; i++) {
            System.out.println("Cadenas comparadas en filas completas: " + iditems[i] + ";" + descripcion[i].getText().toString());
            if (iditems[i] != null && !descripcion[i].getText().toString().equals("FILA BORRADA")) {
                result++;
            }
        }
        return result;
    }


    public boolean guardarTransaccion() {
        if (filasCompletadas() > 0) {
            guardarTrans();
        } else {
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
        if (TextUtils.isEmpty(trans_te.getText().toString())) {
            trans_te.requestFocus();
            trans_te.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_vl.getText().toString())) {
            trans_vl.requestFocus();
            trans_vl.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_fp.getText().toString())) {
            trans_fp.requestFocus();
            trans_fp.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_observacion.getText().toString())) {
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
        } else if (TextUtils.isEmpty(trans_cli_comercial.getText().toString())) {
            trans_cli_comercial.requestFocus();
            trans_cli_comercial.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_cli_direccion.getText().toString())) {
            trans_cli_direccion.requestFocus();
            trans_cli_direccion.setError(resources.getString(R.string.info_field_required));
            return true;
        } else if (TextUtils.isEmpty(trans_cli_telefono.getText().toString())) {
            trans_cli_telefono.requestFocus();
            trans_cli_telefono.setError(resources.getString(R.string.info_field_required));
            return true;
        }
        return false;
    }

    public void eliminarDetallesAnteriores(String id_trans) {
        DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
        Cursor cursor1 = helper.consultarIVKardex(id_trans);
        if (cursor1.moveToFirst()) {
            do {
                // System.out.println("Eliminando detalle anterior: "+cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador)));
                helper.eliminarIVKardex(cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador)));
            } while (cursor1.moveToNext());
        }
        helper.close();
    }

    /*
        Guardar Detalles
     */
    public ArrayList<IVKardex> generarDetalles(String id_trans, String bodega_id) {
        ArrayList<IVKardex> ivks = new ArrayList<>();
        GeneradorClaves gen = new GeneradorClaves();
        gen.generarClave();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String fecha = sdf.format(new Date());
        DBSistemaGestion helper = new DBSistemaGestion(AprobacionOferta.this);
//        System.out.println("Generando detalles!!");
        for (int i = 0; i < count; i++) {
            //          System.out.println("iditems: "+iditems[i]+";"+descripcion[i].getText().toString());
            if (iditems[i] != null && !descripcion[i].getText().toString().equals("FILA BORRADA")) {
                /*System.out.println("cantidad: "+Double.parseDouble(cantidad[i].getText().toString()));
                System.out.println("precio_real: "+Double.parseDouble(precio_real[i].getText().toString()));
                System.out.println("descuento: "+Double.parseDouble(descuento[i].getText().toString()));
                System.out.println("preciounitario: "+Double.parseDouble(preciounitario[i]));*/
                IVKardex detalle = new IVKardex(
                        gen.generarClave(),
                        Double.parseDouble(cantidad[i].getText().toString()),
                        Double.parseDouble(preciounitario[i]),//precio de lista
                        0.0,//costo real total
                        Double.parseDouble(cantidad[i].getText().toString()) * Double.parseDouble(precio_real[i].getText().toString()),//precio total
                        Double.parseDouble(precio_real[i].getText().toString()),//precio real total
                        solicitado[i].getText().toString(),
                        Double.parseDouble(descuento[i].getText().toString()),//descuento
                        (Double.parseDouble(preciounitario[i]) * Double.parseDouble(cantidad[i].getText().toString())) * (Double.parseDouble(descuento[i].getText().toString()) / 100), //descuento real
                        id_trans,
                        bodega_id,
                        iditems[i],
                        idpadres[i],
                        keypadres[i],
                        descpromo[i],
                        numprecio[i],
                        Double.parseDouble(cospro[i].getText().toString()), bandapro[i], fecha);
                System.out.println("IVKardex Generado: " + detalle.toString());
                ivks.add(detalle);
            }
        }
        helper.close();
        return ivks;
    }


    class GuardarDetallesTask extends AsyncTask<String, Void, Void> {
        String id_trans, bodega_id;
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AprobacionOferta.this);
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
            DBSistemaGestion helper = new DBSistemaGestion(AprobacionOferta.this);
            try {
                List<IVKardex> list = generarDetalles(id_trans, bodega_id);
                for (IVKardex ivk : list) {
                    System.out.println("IVKardex a guardar: " + ivk.toString());
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
            case R.id.action_view_carter:
                if (!this.cli_id.isEmpty()) {
                    generarReporteCartera(this.cli_id);
                }
                break;
            case R.id.action_view_descuentos:
                Intent intent19 = new Intent(AprobacionOferta.this, DescuentosCliente.class);
                intent19.putExtra("cliente", cli_id);
                intent19.putExtra("descripcion", nombres.getText().toString());
                intent19.putExtra("comercial", comercial.getText().toString());
                startActivity(intent19);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aprobacion_oferta, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AprobacionOferta.this, MainActivity.class);
        intent.putExtra("opcion", getIntent().getIntExtra("opcion", 0));
        startActivity(intent);
        finish();
    }

}
