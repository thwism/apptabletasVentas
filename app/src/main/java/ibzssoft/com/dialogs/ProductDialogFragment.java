package ibzssoft.com.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ProductImagesRecyclerAdapter;
import ibzssoft.com.interfaces.ProductDialogInterface;
import ibzssoft.com.interfaces.ProductImagesRecyclerInterface;
import ibzssoft.com.ishidamovile.CONST;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.listeners.OnSingleClickListener;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.vista.Item;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

public class ProductDialogFragment extends DialogFragment {

    private ProductDialogInterface productDialogInterface;
    private RecyclerView productImagesRecycler;
    private ProductImagesRecyclerAdapter productImagesAdapter;
    private Item product;
    private TextView  productDesc, productPres, productCodi, productAlte, productUni,productMod,productIVA, productPorc;
    private ProgressBar product_progress;
    private View product_scroll_layout;
    /*Campos para reporte de precios*/
    private TextView productPrice1, productPrice2, productPrice3, productPrice4, productPrice5, productPrice6, productPrice7;
    private TextView lblproductPrice1, lblproductPrice2, lblproductPrice3, lblproductPrice4, lblproductPrice5, lblproductPrice6, lblproductPrice7;
    /*Campos para reporte de existencias*/
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila;
    private TableRow.LayoutParams layoutCeldas,layoutCeldaDescripcion;
    private String orden_bodegas;
    private ProgressBar progressView;

    // Fields referencing complex screen layouts.
    private View layoutEmpty;
    private RelativeLayout productContainer;
    private ScrollView contentScrollLayout;
    private ViewTreeObserver.OnScrollChangedListener scrollViewListener;
    private  TextView category1,category2,category3,category4,category5,category6;
    private String [] conf_precios;
    // Indicates running add product to cart request.
    private ImageView addToCartImage;
    private ProgressBar addToCartProgress;


    public static ProductDialogFragment newInstance(Item product,ProductDialogInterface filterDialogInterface) {
        ProductDialogFragment prodcutDialogFragment = new ProductDialogFragment();
        if (filterDialogInterface == null) {
            return null;
        }
        prodcutDialogFragment.product= product;
        prodcutDialogFragment.productDialogInterface= filterDialogInterface;
        return prodcutDialogFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
       View view = inflater.inflate(R.layout.fragment_product, container, false);
        progressView = (ProgressBar) view.findViewById(R.id.product_progress);
        productContainer = (RelativeLayout) view.findViewById(R.id.product_container);
        layoutEmpty = view.findViewById(R.id.product_empty_layout);
        contentScrollLayout = (ScrollView) view.findViewById(R.id.product_scroll_layout);
        prepareEtiquetasItem(view);
        prepareProgressBar(view);
        prepareProductDetail(view);
        prepateProductMoreInfo(view);
        prepareProductImagesLayout(view);
        prepareScrollViewAndWishlist(view);
        prepareReportStock(view);
        generateReportStock(this.product.getIdentificador());
        return view;
    }

    public void prepareEtiquetasItem(View view){
        category1 =(TextView) view.findViewById(R.id.etiqueta1);
        category2 =(TextView) view.findViewById(R.id.etiqueta2);
        category3 =(TextView) view.findViewById(R.id.etiqueta3);
        category4 =(TextView) view.findViewById(R.id.etiqueta4);
        category5 =(TextView) view.findViewById(R.id.etiqueta5);
        category6 =(TextView) view.findViewById(R.id.etiqueta6);
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        this.conf_precios = extraerConfiguraciones.getArray(getString(R.string.key_act_show_list_price),null);
        String cat1=extraerConfiguraciones.get(getActivity().getString(R.string.key_g1),getActivity().getString(R.string.pref_etiqueta_grupo1_inventario_default));
        String cat2=extraerConfiguraciones.get(getActivity().getString(R.string.key_g2),getActivity().getString(R.string.pref_etiqueta_grupo2_inventario_default));
        String cat3=extraerConfiguraciones.get(getActivity().getString(R.string.key_g3),getActivity().getString(R.string.pref_etiqueta_grupo3_inventario_default));
        String cat4=extraerConfiguraciones.get(getActivity().getString(R.string.key_g4),getActivity().getString(R.string.pref_etiqueta_grupo4_inventario_default));
        String cat5=extraerConfiguraciones.get(getActivity().getString(R.string.key_g5),getActivity().getString(R.string.pref_etiqueta_grupo5_inventario_default));
        String cat6=extraerConfiguraciones.get(getActivity().getString(R.string.key_g6),getActivity().getString(R.string.pref_etiqueta_grupo6_inventario_default));
        this.orden_bodegas=extraerConfiguraciones.get(getActivity().getString(R.string.key_act_bod),getActivity().getString(R.string.pref_act_bod_default));
        String dec=extraerConfiguraciones.get(getActivity().getString(R.string.key_empresa_decimales),"2");
        category1.setText(cat1);
        category2.setText(cat2);
        category3.setText(cat3);
        category4.setText(cat4);
        category5.setText(cat5);
        category6.setText(cat6);
    }
    public void prepareProgressBar(View view){
        this.product_progress = (ProgressBar) view.findViewById(R.id.product_progress);
        this.product_scroll_layout = view.findViewById(R.id.product_scroll_layout);
    }
    public void prepareReportStock(View view){
        tabla= (TableLayout)view.findViewById(R.id.listaExistenciaProducto);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,50,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion.span=2;
        layoutCeldas.span=1;
    }
    public void generateReportStock(String id_item){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.obtenerExistenciaItem(id_item, this.orden_bodegas.split(","));
        int count = 0;
        TableRow fila;
        if(cursor.moveToFirst()){
            do{
                fila = new TableRow(getActivity());
                fila.setLayoutParams(layoutFila);

                TextView nro = new TextView(getActivity());
                nro.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));
                nro.setPadding(5, 5, 5, 5);
                nro.setText(String.valueOf(count+1));

                TextView bodega= new TextView(getActivity());
                bodega.setLayoutParams(layoutCeldaDescripcion);
                bodega.setPadding(5, 5, 5, 5);
                bodega.setText(cursor.getString(cursor.getColumnIndex(Bodega.FIELD_codbodega)));
                bodega.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));

                TextView exist  = new TextView(getActivity());
                exist.setLayoutParams(layoutCeldas);
                exist.setPadding(5, 5, 5, 5);
                exist.setGravity(Gravity.CENTER);
                exist.setText(cursor.getString(cursor.getColumnIndex(Existencia.FIELD_existencia)));
                exist.setTextColor(getContext().getResources().getColor(R.color.textColorPrimary));

                fila.setPadding(15, 0, 15, 0);
                fila.addView(nro);
                fila.addView(bodega);
                fila.addView(exist);
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
    /**
     * Prepare product prices and related products views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    public void prepateProductMoreInfo(View view){
        productPrice1 = (TextView) view.findViewById(R.id.precio1);
        productPrice2 = (TextView) view.findViewById(R.id.precio2);
        productPrice3 = (TextView) view.findViewById(R.id.precio3);
        productPrice4 = (TextView) view.findViewById(R.id.precio4);
        productPrice5 = (TextView) view.findViewById(R.id.precio5);
        productPrice6 = (TextView) view.findViewById(R.id.precio6);
        productPrice7 = (TextView) view.findViewById(R.id.precio7);
        lblproductPrice1 = (TextView) view.findViewById(R.id.lblprecio1);
        lblproductPrice2 = (TextView) view.findViewById(R.id.lblprecio2);
        lblproductPrice3 = (TextView) view.findViewById(R.id.lblprecio3);
        lblproductPrice4 = (TextView) view.findViewById(R.id.lblprecio4);
        lblproductPrice5 = (TextView) view.findViewById(R.id.lblprecio5);
        lblproductPrice6 = (TextView) view.findViewById(R.id.lblprecio6);
        lblproductPrice7 = (TextView) view.findViewById(R.id.lblprecio7);
        productUni = (TextView) view.findViewById(R.id.unidad);
        productMod = (TextView) view.findViewById(R.id.modificacion);
        productIVA = (TextView) view.findViewById(R.id.paga_iva);
        productPorc = (TextView) view.findViewById(R.id.porcentaje);

        DBSistemaGestion helper = new DBSistemaGestion(getContext());
        Cursor cursor  = helper.consultarItem(this.product.getIdentificador());
        if(cursor.moveToFirst()){
            productUni.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad)));
            productMod.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_fecha_grabado)));
            if(cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva))!=0)
                productIVA.setText(getString(R.string.si));
            else productIVA.setText(getString(R.string.no));
            productPorc.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_porc_iva)));
            productAlte.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_alterno)));

            double porc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_porc_iva));

            double precio1_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1))+
                                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
            double precio2_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2))+
                                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
            double precio3_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3))+
                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
            double precio4_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4))+
                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
            double precio5_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5))+
                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
            double precio6_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6))+
                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
            double precio7_inc_iva = cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7))+
                    (porc_iva*cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));

            productPrice1.setText(String.valueOf(precio1_inc_iva));
            productPrice2.setText(String.valueOf(precio2_inc_iva));
            productPrice3.setText(String.valueOf(precio3_inc_iva));
            productPrice4.setText(String.valueOf(precio4_inc_iva));
            productPrice5.setText(String.valueOf(precio5_inc_iva));
            productPrice6.setText(String.valueOf(precio6_inc_iva));
            productPrice7.setText(String.valueOf(precio7_inc_iva));

            productPrice1.setVisibility(View.GONE);lblproductPrice1.setVisibility(View.GONE);
            productPrice2.setVisibility(View.GONE);lblproductPrice2.setVisibility(View.GONE);
            productPrice3.setVisibility(View.GONE);lblproductPrice3.setVisibility(View.GONE);
            productPrice4.setVisibility(View.GONE);lblproductPrice4.setVisibility(View.GONE);
            productPrice5.setVisibility(View.GONE);lblproductPrice5.setVisibility(View.GONE);
            productPrice6.setVisibility(View.GONE);lblproductPrice6.setVisibility(View.GONE);
            productPrice7.setVisibility(View.GONE);lblproductPrice7.setVisibility(View.GONE);
            /*Filtrando visualizacion de precios*/
            for(int i=0; i<this.conf_precios.length;i++){
                int price = Integer.parseInt(conf_precios[i]);
                switch (price){
                    case 1:productPrice1.setVisibility(View.VISIBLE);lblproductPrice1.setVisibility(View.VISIBLE);break;
                    case 2:productPrice2.setVisibility(View.VISIBLE);lblproductPrice2.setVisibility(View.VISIBLE);break;
                    case 3:productPrice3.setVisibility(View.VISIBLE);lblproductPrice3.setVisibility(View.VISIBLE);break;
                    case 4:productPrice4.setVisibility(View.VISIBLE);lblproductPrice4.setVisibility(View.VISIBLE);break;
                    case 5:productPrice5.setVisibility(View.VISIBLE);lblproductPrice5.setVisibility(View.VISIBLE);break;
                    case 6:productPrice6.setVisibility(View.VISIBLE);lblproductPrice6.setVisibility(View.VISIBLE);break;
                    case 7:productPrice7.setVisibility(View.VISIBLE);lblproductPrice7.setVisibility(View.VISIBLE);break;
                }
            }
        }
        cursor.close();
        helper.close();
    }
    /**
     * Prepare product images and related products views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    public void prepareProductDetail(View view){
        productDesc = (TextView) view.findViewById(R.id.descripcion);
        productPres = (TextView) view.findViewById(R.id.presentacion);
        productCodi = (TextView) view.findViewById(R.id.codigo);
        productUni = (TextView) view.findViewById(R.id.unidad);
        productAlte = (TextView) view.findViewById(R.id.alterno);

        productDesc.setText(this.product.getDescripcion());
        productPres.setText(this.product.getPresentacion());
        productCodi.setText(this.product.getCod_item());
    }
    /**
     * Prepare product images and related products views, adapters and listeners.
     *
     * @param view fragment base view.
     */
    private void prepareProductImagesLayout(View view) {
        productImagesRecycler = (RecyclerView) view.findViewById(R.id.product_images_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        productImagesRecycler.setLayoutManager(linearLayoutManager);
        productImagesAdapter = new ProductImagesRecyclerAdapter(getActivity(),this.product, new ProductImagesRecyclerInterface() {
            @Override
            public void onImageSelected(View v, int position) {
                ArrayList<String> productImagesUrls = new ArrayList<>();
                productImagesUrls.add(product.getRuta_img1());
                productImagesUrls.add(product.getRuta_img2());
                productImagesUrls.add(product.getRuta_img3());
                ProductImagesDialogFragment imagesDialog = ProductImagesDialogFragment.newInstance(productImagesUrls, position);
                if (imagesDialog != null)
                    imagesDialog.show(getFragmentManager(), ProductImagesDialogFragment.class.getSimpleName());
                else {
                    Timber.e("%s Called with empty image list", ProductImagesDialogFragment.class.getSimpleName());
                }
            }
        });
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
        try{
            this.product_progress.setVisibility(View.GONE);
            this.product_scroll_layout.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
        setContentVisible(CONST.VISIBLE.CONTENT);
    }
    /**
     * Prepare scroll view related animations and floating wishlist button.
     *
     * @param view fragment base view.
     */
    private void prepareScrollViewAndWishlist(View view) {
        final View productBackground = view.findViewById(R.id.product_background);

        scrollViewListener = new ViewTreeObserver.OnScrollChangedListener() {
            private boolean alphaFull = false;

            @Override
            public void onScrollChanged() {
                int scrollY = contentScrollLayout.getScrollY();
                if (productImagesRecycler != null) {

                    float alphaRatio;
                    if (productImagesRecycler.getHeight() > scrollY) {
                        productImagesRecycler.setTranslationY(scrollY / 2);
                        alphaRatio = (float) scrollY / productImagesRecycler.getHeight();
                    } else {
                        alphaRatio = 1;
                    }
//                    Timber.e("scrollY:" + scrollY + ". Alpha:" + alphaRatio);

                    if (alphaFull) {
                        if (alphaRatio <= 0.99) alphaFull = false;
                    } else {
                        if (alphaRatio >= 0.9) alphaFull = true;
                        productBackground.setAlpha(alphaRatio);
                    }
                } else {
                    Timber.e("Null productImagesScroll");
                }
            }
        };
    }
    /**
     * Display content layout, progress bar or empty layout.
     *
     * @param visible enum value defining visible layout.
     */
    private void setContentVisible(CONST.VISIBLE visible) {
        if (layoutEmpty != null && contentScrollLayout != null && progressView != null) {
            switch (visible) {
                case EMPTY:
                    layoutEmpty.setVisibility(View.VISIBLE);
                    contentScrollLayout.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.GONE);
                    break;
                case PROGRESS:
                    layoutEmpty.setVisibility(View.GONE);
                    contentScrollLayout.setVisibility(View.INVISIBLE);
                    progressView.setVisibility(View.VISIBLE);
                    break;
                default: // Content
                    layoutEmpty.setVisibility(View.GONE);
                    contentScrollLayout.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
            }
        } else {
            Timber.e(new RuntimeException(), "Setting content visibility with null views.");
        }
    }

    @Override
    public void onResume() {
        if (contentScrollLayout != null) contentScrollLayout.getViewTreeObserver().addOnScrollChangedListener(scrollViewListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (contentScrollLayout != null) contentScrollLayout.getViewTreeObserver().removeOnScrollChangedListener(scrollViewListener);
        super.onPause();
    }

}
