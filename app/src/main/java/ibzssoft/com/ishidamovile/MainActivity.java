package ibzssoft.com.ishidamovile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.enviar.UploadImage;
import ibzssoft.com.fragment.Settings_Fragment;
import ibzssoft.com.ishidamovile.reportes.Clientes_Sector;
import ibzssoft.com.ishidamovile.reportes.FiltroDescuento;
import ibzssoft.com.paginas.TabCobroAll;
import ibzssoft.com.paginas.TabIngProTermAll;
import ibzssoft.com.paginas.TabOfertaAll;
import ibzssoft.com.paginas.TabPedidoAll;
import ibzssoft.com.paginas.TabTransBodegaAll;
import ibzssoft.com.paginas.TabVisitaAll;
import ibzssoft.com.recibir.RecibirProvincias;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private TextView nombre_usuario,nombre_empresa,nombre_grupo;
    private ImageButton refresh_profile;
    private  String prf_usuario_nombre,prf_usuario,prf_vendedor,prf_grupo,prf_cod_empresa,prf_empresa,prf_lineas,prf_accesos,prf_ip,prf_port,prf_url,prf_rutas,prf_bodegas;
    private String prf_impuestos;
    private int prf_supervisor,prf_numdec, prf_busqueda_clientes, prf_orden_cat_trans, prf_orden_cat_comodin;
    private boolean prf_menu_oferta,prf_menu_pedido,prf_menu_factura,prf_menu_nota,prf_menu_pago, prf_menu_cobro, prf_menu_visita,prf_menu_carrito,prf_menu_transferencia,prf_menu_ipt,prf_menu_clientes, prf_menu_productos, prf_menu_cartera, prf_menu_descuentos,prf_menu_ppendientes,prf_menu_cventas,prf_menu_balance,prf_menu_sectores,prf_menu_opciones,prf_show_img_items;
    private int opcion;
    private static MainActivity mInstance = null;

    private String encoded_image;
    private String id_item;
    private int position_gallery;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;

    private Uri picUri;
    final int CROP_PIC = 2;

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;

        setContentView(R.layout.activity_main);
        this.opcion=0;
        encoded_image = null;
        id_item = null;
        position_gallery = 0;
        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView= (NavigationView) findViewById(R.id.shitstuff);
        nombre_usuario=(TextView)mNavigationView.findViewById(R.id.drawer_header_name);
        nombre_empresa=(TextView)mNavigationView.findViewById(R.id.drawer_header_emp);
        nombre_grupo=(TextView)mNavigationView.findViewById(R.id.drawer_header_grupo);
        refresh_profile=(ImageButton) mNavigationView.findViewById(R.id.refresh_profile);
        cargarPreferencias();
        cargarDatosPerfil();
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragmentPedido as the first Fragment
         */
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        /**
         * Setup Drawer Toggle of the Toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerToggle = this.setupDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        /*
            Setup saved instance
         */
        if(savedInstanceState!=null){
            this.opcion=savedInstanceState.getInt("opcion",0);
            this.setupDrawerContent(mNavigationView);
            this.selectDrawerItem(mNavigationView.getMenu().findItem(this.opcion));
        }else{
            this.opcion=getIntent().getIntExtra("opcion",0);
            this.setupDrawerContent(mNavigationView);
            if(this.opcion!=0){
                this.selectDrawerItem(mNavigationView.getMenu().findItem(this.opcion));
            }else this.selectDrawerItem(mNavigationView.getMenu().findItem(R.id.navigation_item_pedido));
        }

        /*
        /*
            Setup click events to drawer menu
         */

        this.cargarComponentesMenuPrincipal(mNavigationView);
        this.refresh_profile.setOnClickListener(this);
        /**
         * Cargar animacion
         */
        /*int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        Window window = getWindow();
        window.setLayout(width, height);
        window.setWindowAnimations(R.style.dialogFragmentAnimation);*/
    }


    /**
     * Method creates fragment transaction and replace current fragment with new one.
     *
     * @param newFragment    new fragment used for replacement.
     * @param transactionTag text identifying fragment transaction.
     */
    private void replaceFragment(Fragment newFragment, String transactionTag) {
        if (newFragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
    //        fragmentTransaction.setAllowOptimization(false);
            fragmentTransaction.addToBackStack(transactionTag);
            fragmentTransaction.replace(R.id.containerView, newFragment).commit();
            frgManager.executePendingTransactions();
        } else {
            Timber.e(new RuntimeException(), "Replace fragments with null newFragment parameter.");
        }
    }



    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.tittle_dialog_desc_promo, R.string.tittle_estado_cuenta);
    }
    /*
    Eventos para el menu principal
    * */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                     selectDrawerItem(menuItem);
                    return true;
                }
            });
    }
    public Bundle crearParametros(){
        Bundle bundle = new Bundle();
        bundle.putString("ip",prf_ip);
        bundle.putString("port",prf_port);
        bundle.putString("url",prf_url);
        bundle.putString("vendedor",prf_vendedor);
        bundle.putString("usuario",prf_usuario);
        bundle.putString("empresa",prf_cod_empresa);
        bundle.putString("accesos",prf_accesos);
        bundle.putString("lineas",prf_lineas);
        bundle.putString("bodegas",prf_bodegas);
        bundle.putString("grupo",prf_grupo);
        bundle.putString("impuestos",prf_impuestos);
        bundle.putInt("numdec",prf_numdec);
        bundle.putInt("opcion",this.opcion);
        bundle.putInt("busqueda_clientes",this.prf_busqueda_clientes);
        bundle.putInt("ord_cat_trans",this.prf_orden_cat_trans);
        bundle.putInt("ord_cat_trans_comodin",this.prf_orden_cat_comodin);
        return bundle;
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_sub_menu_account_item1://catalogo de mis permisos
                fragmentClass = Mis_Permisos.class;
                break;
            case R.id.navigation_item_cat_1: //catalogo de clientes
                fragmentClass = Catalogo_Clientes.class;
               break;
            case R.id.navigation_item_cartera: //catalogo de clientes
                fragmentClass = Catalogo_Cartera.class;
                break;
            case R.id.navigation_item_descuentos: //filtro para descuentos
                fragmentClass = FiltroDescuento.class;
                break;
            case R.id.navigation_item_ppendiente: //pedidos pendientes
                fragmentClass = PedidosPedientes.class;
                break;
            case R.id.nav_sub_menu_perm_item2: //configuraciones del sistema
                fragmentClass = Configuraciones.class;
                Intent intent= new Intent(MainActivity.this,Configuraciones.class);
                startActivity(intent);
                break;
            case R.id.navigation_item_cat_2: //catalogo de clientes
                if(prf_show_img_items) fragmentClass = Catalogo_Items.class;
                 else fragmentClass = Catalogo_Productos.class;
                break;
            case R.id.navigation_item_sinc: //sincronizacion
                fragmentClass = Sincronizacion.class;
                break;
            case R.id.nav_sub_menu_account_item3:
                fragmentClass = Info_App.class;
                break;
            case R.id.nav_sub_menu_account_item2:
                fragmentClass = Info_Company.class;
                break;
            case R.id.navigation_item_pedido:
                fragmentClass = TabPedidoAll.class;
                break;
            case R.id.navigation_item_oferta:
                fragmentClass = TabOfertaAll.class;
                break;
            case R.id.navigation_item_cobro:
                fragmentClass = TabCobroAll.class;
                break;
            case R.id.navigation_item_trans_bodega:
                fragmentClass = TabTransBodegaAll.class;
                break;
            case R.id.navigation_item_ipt:
                fragmentClass = TabIngProTermAll.class;
                break;
            case R.id.navigation_item_visita:
                fragmentClass = TabVisitaAll.class;
                break;
            case R.id.nav_sub_menu_account_item4:
                 fragmentClass = Configuraciones.class;
                 this.mensajeSalir();
                break;
            case R.id.nav_sub_menu_account_item5:
                fragmentClass = ConfigurarMiCuenta.class;
                break;
            case R.id.navigation_item_clientes_sector:
                fragmentClass = Clientes_Sector.class;
                break;
            default:
                fragmentClass = Settings_Fragment.class;
                break;
        }

        try {
            this.opcion=menuItem.getItemId();
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(crearParametros());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace((R.id.containerView),fragment).commit();
            getSupportActionBar().setTitle(menuItem.getTitle());
            mDrawerLayout.closeDrawers();
        } catch (Exception e) {
            //new Alertas(this,"Error creando fragment",e.getMessage()).mostrarMensaje();
            //e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("opcion",opcion);
    }

    public void cargarDatosPerfil(){
        nombre_empresa.setText(prf_empresa);
        nombre_usuario.setText(prf_usuario_nombre);
        nombre_grupo.setText(prf_grupo);
        if(prf_supervisor!=0)nombre_usuario.setText(prf_usuario_nombre+getString(R.string.pref_titulo_supervisor));
    }

    public void cargarPreferencias(){
        ExtraerConfiguraciones ext= new ExtraerConfiguraciones(MainActivity.this);
        prf_cod_empresa=ext.get(getString(R.string.key_empresa_codigo),getString(R.string.pref_codigo_empresa_default));
        prf_empresa=ext.get(getString(R.string.key_empresa_nombre),getString(R.string.pref_nombre_empresa_default));
        prf_usuario_nombre=ext.get(getString(R.string.key_act_nom),getString(R.string.pref_act_nom_default));
        prf_usuario=ext.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
        prf_bodegas=ext.get(getString(R.string.key_act_bod),getString(R.string.pref_act_bod_default));
        prf_vendedor=ext.get(getString(R.string.key_act_ven),getString(R.string.pref_act_ven_default));
        prf_ip=ext.get(getString(R.string.key_conf_ip),getString(R.string.pref_ip_default));
        prf_port=ext.get(getString(R.string.key_conf_port),getString(R.string.pref_port_default));
        prf_url=ext.get(getString(R.string.key_conf_url),getString(R.string.pref_url_default));
        if(ext.get(getString(R.string.key_act_sup),getString(R.string.pref_act_sup_default)).equals("1")){prf_supervisor=1;}
        prf_grupo=ext.get(getString(R.string.key_act_gru),getString(R.string.pref_act_gru_default));
        prf_accesos=ext.get(getString(R.string.key_act_acc),getString(R.string.pref_act_acc_default));
        prf_rutas=ext.get(getString(R.string.key_act_rut),getString(R.string.pref_act_rut_default));
        prf_impuestos=ext.get(getString(R.string.key_empresa_iva),getString(R.string.pref_iva_empresa));
        prf_numdec=Integer.parseInt(ext.get(getString(R.string.key_empresa_decimales),"2"));
        prf_lineas=ext.get(getString(R.string.key_act_lin),getString(R.string.pref_act_lin_default));
        prf_menu_oferta=ext.getBoolean(getString(R.string.key_menu_oferta),true);
        prf_menu_pedido=ext.getBoolean(getString(R.string.key_menu_pedido),true);
        prf_menu_factura=ext.getBoolean(getString(R.string.key_menu_factura),false);
        prf_menu_nota=ext.getBoolean(getString(R.string.key_menu_nota),false);
        prf_menu_pago=ext.getBoolean(getString(R.string.key_menu_pago),false);
        prf_menu_cobro=ext.getBoolean(getString(R.string.key_menu_cobro),false);
        prf_menu_visita=ext.getBoolean(getString(R.string.key_menu_visita),false);
        prf_menu_carrito=ext.getBoolean(getString(R.string.key_menu_shop),false);
        prf_menu_transferencia=ext.getBoolean(getString(R.string.key_menu_transferencia),false);
        prf_menu_ipt=ext.getBoolean(getString(R.string.key_menu_ipt),false);
        prf_menu_clientes=ext.getBoolean(getString(R.string.key_menu_clientes),true);
        prf_menu_productos=ext.getBoolean(getString(R.string.key_menu_productos),true);
        prf_menu_cartera=ext.getBoolean(getString(R.string.key_menu_cartera),true);
        prf_menu_descuentos=ext.getBoolean(getString(R.string.key_menu_descuentos),true);
        prf_menu_ppendientes=ext.getBoolean(getString(R.string.key_menu_ppendientes),true);
        prf_menu_cventas=ext.getBoolean(getString(R.string.key_menu_cventas),false);
        prf_menu_balance=ext.getBoolean(getString(R.string.key_menu_balance),false);
        prf_menu_sectores=ext.getBoolean(getString(R.string.key_conf_filtrar_clientes_sectores),false);
        prf_menu_opciones=ext.getBoolean(getString(R.string.key_menu_opciones),true);
        prf_show_img_items=ext.getBoolean(getString(R.string.key_act_show_image_items),false);
        String conf2 = ext.get(getString(R.string.key_conf_descarga_clientes),"0");
        prf_busqueda_clientes= Integer.parseInt(conf2);
        String conf3 = ext.get(getString(R.string.key_conf_orden_cat_trans),getString(R.string.pref_orden_default_cat_trans));
        prf_orden_cat_trans= Integer.parseInt(conf3);
        String conf4 = ext.get(getString(R.string.key_conf_orden_cat_trans_comodin),getString(R.string.pref_orden_comodin_cat_trans_default));
        prf_orden_cat_comodin= Integer.parseInt(conf4);
    }
    public void cargarComponentesMenuPrincipal(NavigationView navigationView){
        if(prf_menu_pedido) navigationView.getMenu().findItem(R.id.navigation_item_pedido).setVisible(true);
        else  navigationView.getMenu().findItem(R.id.navigation_item_pedido).setVisible(false);

        if(prf_menu_oferta)navigationView.getMenu().findItem(R.id.navigation_item_oferta).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_oferta).setVisible(false);

        if(prf_menu_factura)navigationView.getMenu().findItem(R.id.navigation_item_factura).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_factura).setVisible(false);

        if(prf_menu_nota)navigationView.getMenu().findItem(R.id.navigation_item_nota).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_nota).setVisible(false);

        if(prf_menu_pago)navigationView.getMenu().findItem(R.id.navigation_item_pago).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_pago).setVisible(false);

        if(prf_menu_cobro)navigationView.getMenu().findItem(R.id.navigation_item_cobro).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_cobro).setVisible(false);

        if(prf_menu_visita)navigationView.getMenu().findItem(R.id.navigation_item_visita).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_visita).setVisible(false);

        if(prf_menu_carrito)navigationView.getMenu().findItem(R.id.navigation__item_carrito).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation__item_carrito).setVisible(false);

        if(prf_menu_transferencia)navigationView.getMenu().findItem(R.id.navigation_item_trans_bodega).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_trans_bodega).setVisible(false);

        if(prf_menu_ipt)navigationView.getMenu().findItem(R.id.navigation_item_ipt).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_ipt).setVisible(false);
        /*
            Opciones de Catalogo
         */
        if(prf_menu_clientes)navigationView.getMenu().findItem(R.id.navigation_item_cat_1).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_cat_1).setVisible(false);

        if(prf_menu_productos)navigationView.getMenu().findItem(R.id.navigation_item_cat_2).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_cat_2).setVisible(false);
        /*
            Opciones de Configuracion
         */
        if(prf_menu_opciones)navigationView.getMenu().findItem(R.id.nav_sub_menu_perm_item1).setVisible(true);
        else navigationView.getMenu().findItem(R.id.nav_sub_menu_perm_item1).setVisible(false);
        if(prf_supervisor==1)navigationView.getMenu().findItem(R.id.nav_sub_menu_perm_item1).setVisible(true);
        /*
            Opciones de Reporte
         */
        if(prf_menu_cartera)navigationView.getMenu().findItem(R.id.navigation_item_cartera).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_cartera).setVisible(false);

        if(prf_menu_descuentos)navigationView.getMenu().findItem(R.id.navigation_item_descuentos).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_descuentos).setVisible(false);

        if(prf_menu_ppendientes)navigationView.getMenu().findItem(R.id.navigation_item_ppendiente).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_ppendiente).setVisible(false);

        if(prf_menu_cventas) navigationView.getMenu().findItem(R.id.navigation_item_cventas).setVisible(true);
        else  navigationView.getMenu().findItem(R.id.navigation_item_cventas).setVisible(false);

        if(prf_menu_balance) navigationView.getMenu().findItem(R.id.navigation_item_balance).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_balance).setVisible(false);

        if(prf_menu_sectores) navigationView.getMenu().findItem(R.id.navigation_item_clientes_sector).setVisible(true);
        else navigationView.getMenu().findItem(R.id.navigation_item_clientes_sector).setVisible(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh_profile:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertDialogBuilder.setTitle("Actualizacion de datos iniciales");
                alertDialogBuilder
                        .setMessage("A continuacion se actualizaran los catálogos iniciales del sistema")
                        .setCancelable(false)
                        .setPositiveButton("Proceder",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                RecibirProvincias recibirProvincias= new RecibirProvincias(MainActivity.this);
                                recibirProvincias.ejecutartarea();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

    private void mensajeSalir(){
        android.support.v7.app.AlertDialog.Builder quitDialog
                = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        quitDialog.setTitle("Seguro deseas salir?");

        quitDialog.setPositiveButton("Salir", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }});

        quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }});

        quitDialog.show();
    }

    public void onBackPressed() {
     this.mensajeSalir();
    }

    public void chooseImage(String product_id, int numero) {
        this.id_item = product_id;
        this.position_gallery = numero;

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FROM_GALLARY);

        } else {
            Toast.makeText(MainActivity.this,
                    "No activity found to perform this task",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void takeImage(String idItem, int posicion_galeria){
        try{
            this.id_item = idItem;
            this.position_gallery = posicion_galeria;
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, PICK_FROM_CAMERA);
        }catch (ActivityNotFoundException e){
            Toast toast = Toast.makeText(this, "This device doesn't support the crop action!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCrop(Uri uri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);

        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String savebitmap(Context context, Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator +context.getString(R.string.title_folder_root)+ File.separator +new GeneradorClaves().generarClave()+".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    try{
                        picUri = data.getData();
                        System.out.println(picUri.toString());
                        performCrop(picUri);
                    }catch (Exception e){
                     e.printStackTrace();
                    }

                    break;
                case PICK_FROM_GALLARY:
                        Bitmap originBitmap = null;
                    if (data.getData() != null) {
                        Uri selectedImage = data.getData();
                        Toast.makeText(MainActivity.this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                        System.out.println(selectedImage.toString());
                        InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(
                                    selectedImage);
                            originBitmap = BitmapFactory.decodeStream(imageStream);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (originBitmap != null) {

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            this.encoded_image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if(this.encoded_image!=null && this.id_item!=null){
                                UploadImage upload = new UploadImage(this);
                                upload.ejecutartarea(this.encoded_image,this.id_item, this.position_gallery);

                            }
                        }
                    } else {
                        Bundle extras = data.getExtras();
                        Bitmap thePic = extras.getParcelable("data");
                        try{
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            thePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            this.encoded_image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if(this.encoded_image!=null && this.id_item!=null){
                                System.out.println(this.encoded_image);
                                UploadImage upload = new UploadImage(this);
                                upload.ejecutartarea(this.encoded_image,this.id_item, this.position_gallery);
                            }
                        }catch (Exception io){
                            io.printStackTrace();
                        }
                    }
                    break;
            }
        }

    }
}