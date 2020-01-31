package ibzssoft.com.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibzssoft.com.adaptadores.CustomRecyclerViewAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ProductImagesDetailRecyclerAdapter;
import ibzssoft.com.adaptadores.StaggeredGridLayoutAdapter;
import ibzssoft.com.enviar.UploadImage;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.ishidamovile.R;

public class ProductoDetalle extends Fragment {
    private TextView txtCodigo,txtCodigoAlterno,txtDescripcion,txtPrecio1,txtPrecio2,txtPrecio3,
            txtPrecio4,txtPrecio5,txtPrecio6,txtPrecio7,txtbandIVA,txtPorcentaje,txtModificacion,
            txtPresentacion,txtUnidad,
            txtCat2,txtCat4,txtCat6,txtCat8,txtCat10,txtCat12,txtCat1,txtCat3,txtCat5,txtCat7,txtCat9,txtCat11;
    private TextView txtLblPrecio1,txtLblPrecio2,txtLblPrecio3,txtLblPrecio4,txtLblPrecio5,txtLblPrecio6,txtLblPrecio7;
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila;
    private TextView [] bodegas;
    private TextView[] existencias;
    private TableRow.LayoutParams layoutCeldas,layoutCeldaDescripcion;
    private Resources resources;
    private String orden_bodegas;
    private int numdec;
    private String [] conf_precios;
    private Button showPrecios;


    private RecyclerView productImagesRecycler;
    private ProductImagesDetailRecyclerAdapter productImagesAdapter;
    private CustomRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void cargarPreferenciasEtiquetas(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        String cat1=extraerConfiguraciones.get(getActivity().getString(R.string.key_g1),getActivity().getString(R.string.pref_etiqueta_grupo1_inventario_default));
        String cat2=extraerConfiguraciones.get(getActivity().getString(R.string.key_g2),getActivity().getString(R.string.pref_etiqueta_grupo2_inventario_default));
        String cat3=extraerConfiguraciones.get(getActivity().getString(R.string.key_g3),getActivity().getString(R.string.pref_etiqueta_grupo3_inventario_default));
        String cat4=extraerConfiguraciones.get(getActivity().getString(R.string.key_g4),getActivity().getString(R.string.pref_etiqueta_grupo4_inventario_default));
        String cat5=extraerConfiguraciones.get(getActivity().getString(R.string.key_g5),getActivity().getString(R.string.pref_etiqueta_grupo5_inventario_default));
        String cat6=extraerConfiguraciones.get(getActivity().getString(R.string.key_g6),getActivity().getString(R.string.pref_etiqueta_grupo6_inventario_default));
        this.orden_bodegas=extraerConfiguraciones.get(getActivity().getString(R.string.key_act_bod),getActivity().getString(R.string.pref_act_bod_default));
        String dec=extraerConfiguraciones.get(getActivity().getString(R.string.key_empresa_decimales),"2");
        this.numdec = Integer.parseInt(dec);
        this.conf_precios = extraerConfiguraciones.getArray(getString(R.string.key_act_show_list_price),null);
        txtCat1.setText(cat1);
        txtCat3.setText(cat2);
        txtCat5.setText(cat3);
        txtCat7.setText(cat4);
        txtCat9.setText(cat5);
        txtCat11.setText(cat6);
    }
    public String redondearNumero(double numero) {
        int dec =this.numdec;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_detalle, container, false);
        showPrecios=(Button) view.findViewById(R.id.show);
        txtCodigo=(TextView)view.findViewById(R.id.formItemTxt2);
        txtCodigoAlterno=(TextView)view.findViewById(R.id.formItemTxt4);
        txtDescripcion=(TextView)view.findViewById(R.id.formItemTxt6);
        txtPrecio1=(TextView)view.findViewById(R.id.formItemTxt29);
        txtPrecio2=(TextView)view.findViewById(R.id.formItemTxt31);
        txtPrecio3=(TextView)view.findViewById(R.id.formItemTxt33);
        txtPrecio4=(TextView)view.findViewById(R.id.formItemTxt35);
        txtPrecio5=(TextView)view.findViewById(R.id.formItemTxt36);
        txtPrecio6=(TextView)view.findViewById(R.id.formItemTxt37);
        txtPrecio7=(TextView)view.findViewById(R.id.formItemTxt38);
        /*Etiquetas precios*/
        txtLblPrecio1=(TextView)view.findViewById(R.id.formItemTxt28);
        txtLblPrecio2=(TextView)view.findViewById(R.id.formItemTxt30);
        txtLblPrecio3=(TextView)view.findViewById(R.id.formItemTxt32);
        txtLblPrecio4=(TextView)view.findViewById(R.id.formItemTxt34);
        txtLblPrecio5=(TextView)view.findViewById(R.id.formItemTxt46);
        txtLblPrecio6=(TextView)view.findViewById(R.id.formItemTxt48);
        txtLblPrecio7=(TextView)view.findViewById(R.id.formItemTxt50);

        txtbandIVA=(TextView)view.findViewById(R.id.formItemTxt12);
        txtPorcentaje=(TextView)view.findViewById(R.id.formItemTxt14);
        txtPresentacion=(TextView)view.findViewById(R.id.formItemTxt66);
        txtUnidad=(TextView)view.findViewById(R.id.formItemTxt67);
        txtModificacion=(TextView)view.findViewById(R.id.formItemTxt39);
        txtCat2=(TextView)view.findViewById(R.id.formItemCat2);
        txtCat4=(TextView)view.findViewById(R.id.formItemCat4);
        txtCat6=(TextView)view.findViewById(R.id.formItemCat6);
        txtCat8=(TextView)view.findViewById(R.id.formItemCat8);
        txtCat10=(TextView)view.findViewById(R.id.formItemCat10);
        txtCat12=(TextView)view.findViewById(R.id.formItemCat12);
        txtCat1=(TextView)view.findViewById(R.id.formItemCat1);
        txtCat3=(TextView)view.findViewById(R.id.formItemCat3);
        txtCat5=(TextView)view.findViewById(R.id.formItemCat5);
        txtCat7=(TextView)view.findViewById(R.id.formItemCat7);
        txtCat9=(TextView)view.findViewById(R.id.formItemCat9);
        txtCat11=(TextView)view.findViewById(R.id.formItemCat11);
        tabla= (TableLayout)view.findViewById(R.id.listaExistenciaProducto);
        //img1=(ImageView)view.findViewById(R.id.image1);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,50,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion.span=2;
        layoutCeldas.span=1;
        bodegas=new TextView[30];existencias=new TextView[30];
        resources=getActivity().getResources();
        String id_item=getActivity().getIntent().getStringExtra("item");
        cargarPreferenciasEtiquetas();
        cargarDatosProducto(id_item);
        showPrecios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listadoPreciosDialog();
            }
        });
        //cargarImagenesProducto(id_item);
        cargarExistencias(id_item);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getWidgets(view);
        //getLayoutManagerRequest();
        //setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_producto_detalle, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_upload:
                //Intent intent = new Intent(getActivity(), UploadImage.class);
                //startActivity(intent);
            break;
        }
        return true;
    }
*/
    public void cargarDatosProducto(String id_item){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.obtenerItem(id_item);
        if(cursor.moveToFirst()){

            String codigo=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item));
            String alterno=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_alterno));
            String descripcion=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion));
            String presentacion=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion));
            String unidad=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad));
            String precio1=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
            String precio2=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
            String precio3=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
            String precio4=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
            String precio5=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
            String precio6=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
            String precio7=redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));
            int band_iva=cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva));
            String procentaje=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_porc_iva));
            String modificacion=cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_fecha_grabado));
            String cat1=cursor.getString(cursor.getColumnIndex(IVGrupo1.FIELD_descripcion));
            String cat2=cursor.getString(cursor.getColumnIndex(IVGrupo2.FIELD_descripcion));
            String cat3=cursor.getString(cursor.getColumnIndex(IVGrupo3.FIELD_descripcion));
            String cat4=cursor.getString(cursor.getColumnIndex(IVGrupo4.FIELD_descripcion));
            String cat5=cursor.getString(cursor.getColumnIndex(IVGrupo5.FIELD_descripcion));
            String cat6=cursor.getString(cursor.getColumnIndex(IVGrupo6.FIELD_descripcion));
            if(codigo!=null){txtCodigo.setText(codigo);}
            if(alterno!=null){txtCodigoAlterno.setText(alterno);}
            if(descripcion!=null){txtDescripcion.setText(descripcion);}
            if(presentacion!=null){txtPresentacion.setText(presentacion);}
            if(unidad!=null){txtUnidad.setText(unidad);}
            /*Cargando precios a la vista*/
            txtPrecio1.setText(precio1);txtPrecio1.setVisibility(View.GONE);txtLblPrecio1.setVisibility(View.GONE);
            txtPrecio2.setText(precio2);txtPrecio2.setVisibility(View.GONE);txtLblPrecio2.setVisibility(View.GONE);
            txtPrecio3.setText(precio3);txtPrecio3.setVisibility(View.GONE);txtLblPrecio3.setVisibility(View.GONE);
            txtPrecio4.setText(precio4);txtPrecio4.setVisibility(View.GONE);txtLblPrecio4.setVisibility(View.GONE);
            txtPrecio5.setText(precio5);txtPrecio5.setVisibility(View.GONE);txtLblPrecio5.setVisibility(View.GONE);
            txtPrecio6.setText(precio6);txtPrecio6.setVisibility(View.GONE);txtLblPrecio6.setVisibility(View.GONE);
            txtPrecio7.setText(precio7);txtPrecio7.setVisibility(View.GONE);txtLblPrecio7.setVisibility(View.GONE);
            /*Filtrando visualizacion de precios*/
            for(int i=0; i<this.conf_precios.length;i++){
                int price = Integer.parseInt(conf_precios[i]);
                switch (price){
                    case 1:txtPrecio1.setVisibility(View.VISIBLE);txtLblPrecio1.setVisibility(View.VISIBLE);break;
                    case 2:txtPrecio2.setVisibility(View.VISIBLE);txtLblPrecio2.setVisibility(View.VISIBLE);break;
                    case 3:txtPrecio3.setVisibility(View.VISIBLE);txtLblPrecio3.setVisibility(View.VISIBLE);break;
                    case 4:txtPrecio4.setVisibility(View.VISIBLE);txtLblPrecio4.setVisibility(View.VISIBLE);break;
                    case 5:txtPrecio5.setVisibility(View.VISIBLE);txtLblPrecio5.setVisibility(View.VISIBLE);break;
                    case 6:txtPrecio6.setVisibility(View.VISIBLE);txtLblPrecio6.setVisibility(View.VISIBLE);break;
                    case 7:txtPrecio7.setVisibility(View.VISIBLE);txtLblPrecio7.setVisibility(View.VISIBLE);break;
                }
            }
            if(band_iva!=1){txtbandIVA.setText("No"); txtbandIVA.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));}
            if(procentaje!=null){txtPorcentaje.setText(procentaje);}
            if(modificacion!=null){txtModificacion.setText(modificacion);}
            txtCat2.setText(cat1);
            txtCat4.setText(cat2);
            txtCat6.setText(cat3);
            txtCat8.setText(cat4);
            txtCat10.setText(cat5);
            txtCat12.setText(cat6);
        }
        cursor.close();
        helper.close();
    }

    /**
     * Prepare product images and related products views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    private void prepareProductImagesLayout(View view,String id_item) {
        productImagesRecycler = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productImagesRecycler.setLayoutManager(linearLayoutManager);
        productImagesAdapter = new ProductImagesDetailRecyclerAdapter(getActivity(),id_item);
        productImagesRecycler.setAdapter(productImagesAdapter);
        ViewGroup.LayoutParams params = productImagesRecycler.getLayoutParams();
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        // For small screen even smaller images.
        if (densityDpi <= DisplayMetrics.DENSITY_MEDIUM) {
            params.height = (int) (dm.heightPixels * 0.4);
        } else {
            params.height = (int) (dm.heightPixels * 0.48);
        }
    }

    private void getWidgets(View view) {
        productImagesRecycler = (RecyclerView) view.findViewById(R.id.my_recycler_view);
    }
    private void listadoPreciosDialog(){
        android.support.v7.app.AlertDialog.Builder quitDialog
                = new android.support.v7.app.AlertDialog.Builder(getActivity());
        quitDialog.setTitle("Listado de Precios");
        quitDialog.setMessage(
                "Precio 1: "+txtPrecio1.getText().toString()+"\n"+
                        "Precio 2: "+txtPrecio2.getText().toString()+"\n"+
                        "Precio 3: "+txtPrecio3.getText().toString()+"\n"+
                        "Precio 4: "+txtPrecio4.getText().toString()+"\n"+
                        "Precio 5: "+txtPrecio5.getText().toString()+"\n"+
                        "Precio 6: "+txtPrecio6.getText().toString()+"\n"+
                        "Precio 7: "+txtPrecio7.getText().toString());
        quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }});
        quitDialog.show();
    }
    private void setStaggeredGridLayoutManager() {
        productImagesRecycler.setHasFixedSize(true);
        ArrayList<String> productImagesUrls = new ArrayList<>();
        productImagesUrls.add("http://static3.esoterismos.com/wp-content/uploads/2008/09/Sonar-con-una-cama-amplia-y-vacia-600x419.jpg");
        productImagesUrls.add("http://www.elcomercio.com/files/article_main/uploads/2016/01/13/5696b2914cb27.png");
        mLayoutManager = new StaggeredGridLayoutManager(1, GridLayoutManager.VERTICAL);
        productImagesRecycler.setLayoutManager(mLayoutManager);
        adapter = new StaggeredGridLayoutAdapter(getActivity(), productImagesUrls);
    }

    /*private void getLayoutManagerRequest() {
            setStaggeredGridLayoutManager();
            setAdapter();
    }*/

    private void setAdapter() {
        productImagesRecycler.setAdapter(adapter);
    }

    public void cargarExistencias(String id_item){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.obtenerExistenciaItem(id_item, this.orden_bodegas.split(","));
        int count = 0;
        TableRow fila;
        if(cursor.moveToFirst()){
            do{
                fila = new TableRow(getActivity());
                fila.setLayoutParams(layoutFila);
                /*Cargar Configuracion de la fila*/
                TextView nro = new TextView(getActivity());
                nro.setMaxLines(1);
                nro.setGravity(Gravity.CENTER);
                nro.setTextSize(14);
                nro.setLayoutParams(layoutCeldas);
                nro.setPadding(5, 5, 5, 5);
                nro.setEnabled(false);
                nro.setFocusable(false);
                nro.setTextColor(resources.getColor(R.color.textColorPrimary));
                nro.setText(String.valueOf(count+1));


                existencias[count] = new TextView(getActivity());
                existencias[count].setMaxLines(1);
                existencias[count].setGravity(Gravity.CENTER);
                existencias[count].setTextSize(14);
                existencias[count].setLayoutParams(layoutCeldas);
                existencias[count].setPadding(5, 5, 5, 5);
                existencias[count].setEnabled(false);
                existencias[count].setFocusable(false);
                existencias[count].setTextColor(resources.getColor(R.color.textColorPrimary));
                existencias[count].setText(cursor.getString(cursor.getColumnIndex(Existencia.FIELD_existencia)));

                bodegas[count] = new TextView(getActivity());
                bodegas[count].setMaxLines(1);
                bodegas[count].setGravity(Gravity.CENTER);
                bodegas[count].setTextSize(14);
                bodegas[count].setLayoutParams(layoutCeldaDescripcion);
                bodegas[count].setPadding(5, 5, 5, 5);
                bodegas[count].setText(cursor.getString(cursor.getColumnIndex(Bodega.FIELD_codbodega)));
                bodegas[count].setTextColor(resources.getColor(R.color.textColorPrimary));
                bodegas[count].setEnabled(false);
                bodegas[count].setFocusable(false);

                fila.setPadding(5, 0, 5, 0);
                fila.addView(nro);
                fila.addView(bodegas[count]);
                fila.addView(existencias[count]);
                tabla.addView(fila);
                count++;
            }while(cursor.moveToNext());
        }else{
            fila = new TableRow(getActivity());
            fila.setLayoutParams(layoutFila);
            TextView info= new TextView(getActivity());
            info.setText("Producto sin Stock");
            info.setLayoutParams(layoutCeldas);
            info.setGravity(Gravity.CENTER);
            fila.setPadding(10, 5, 10, 5);
            fila.addView(info);
            tabla.addView(fila);
        }
        cursor.close();
        helper.close();
    }
}