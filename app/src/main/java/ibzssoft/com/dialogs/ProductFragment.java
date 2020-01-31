package ibzssoft.com.dialogs;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ProductImagesRecyclerAdapter;
import ibzssoft.com.interfaces.ProductDialogInterface;
import ibzssoft.com.interfaces.ProductImagesRecyclerInterface;
import ibzssoft.com.ishidamovile.BuildConfig;
import ibzssoft.com.ishidamovile.CONST;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ShoppingCar;
import ibzssoft.com.listeners.OnSingleClickListener;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.cart.CartProductItem;
import ibzssoft.com.modelo.vista.Item;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.CalculoDescuentos;
import timber.log.Timber;

public class ProductFragment extends Fragment {

    private ProductDialogInterface productDialogInterface;

    private ProgressBar progressView;
    private static final String PRODUCT_ID = "product_id";
    private static final String CLIENT_ID = "client_id";
    private static final String PRECIO_ITEM = "precio";
    // Fields referencing complex screen layouts.
    private View layoutEmpty;
    private RelativeLayout productContainer;
    private ScrollView contentScrollLayout;

    // Fields referencing product related views.
    private TextView productoDescripcion,productoPresentacion;
    private TextView productoPorcentajeDescuento;
    private TextView productoPrecio;
    private TextView productoPrecioFinal;

    /**
     * Refers to the displayed product.
     */
    private Item product;
    /**
     * Refers to a user-selected product variant
     */
    //private ProductVariant selectedProductVariant = null;

    /**
     * Spinner offering all available product colors.
     */

    //private SizeVariantSpinnerAdapter sizeVariantSpinnerAdapter;
    private ArrayList<String> productImagesUrls;
    private RecyclerView productImagesRecycler;
    private ProductImagesRecyclerAdapter productImagesAdapter;
    private ViewTreeObserver.OnScrollChangedListener scrollViewListener;

    // Indicates running add product to cart request.
    private ImageView addToCartImage;
    private ProgressBar addToCartProgress;

    /**
     * Floating button allowing add/remove product from wishlist.
     */
    //private FabButton wishlistButton;
    /**
     * Determine if product is in wishlist.
     */
    private boolean inWishlist = false;
    /**
     * Id of the wishlist item representing product.
     */
    private long wishlistId = CONST.DEFAULT_EMPTY_ID;

    /*Campos para reporte de existencias*/
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila;
    private TableRow.LayoutParams layoutCeldas,layoutCeldaDescripcion;
    private String orden_bodegas;

    /**
     * Create a new fragment instance for product detail.
     *
     * @param productId id of the product to show.
     * @return new fragment instance.
     */
    public static ProductFragment newInstance(String productId, String precio,String clienteid) {
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID,productId);
        args.putString(PRECIO_ITEM ,precio);
        args.putString(CLIENT_ID,clienteid);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        ShoppingCar.setActionBarTitle(getString(R.string.Product));

        View view = inflater.inflate(R.layout.fragment_product_shop, container, false);
        productImagesUrls = new ArrayList<>();
        progressView = (ProgressBar) view.findViewById(R.id.product_progress);

        productContainer = (RelativeLayout) view.findViewById(R.id.product_container);
        layoutEmpty = view.findViewById(R.id.product_empty_layout);
        contentScrollLayout = (ScrollView) view.findViewById(R.id.product_scroll_layout);

        productoDescripcion = (TextView) view.findViewById(R.id.descripcion_producto);
        productoPresentacion = (TextView) view.findViewById(R.id.presentacion_producto);
        productoPorcentajeDescuento = (TextView) view.findViewById(R.id.porcentaje_descuento_producto);
        productoPrecioFinal = (TextView) view.findViewById(R.id.precio_final_producto);
        productoPrecio = (TextView) view.findViewById(R.id.precio_producto);

        String productId = getArguments().getString(PRODUCT_ID);
        String precio = getArguments().getString(PRECIO_ITEM);
        int numprecio = Integer.parseInt(precio);
        String clienteid = getArguments().getString(CLIENT_ID);

        getProduct(productId, numprecio, clienteid);
        prepareProductImagesLayout(view);
        prepareReportStock(view);
        generateReportStock(productId);
        prepareButtons(view);

        return view;
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
        // Prepare related products
        //RecyclerView relatedProductsRecycler = (RecyclerView) view.findViewById(R.id.product_recommended_images_recycler);
        //relatedProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    public void prepareReportStock(View view){
        tabla= (TableLayout)view.findViewById(R.id.listaExistenciaProducto);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT,50,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaDescripcion.span=2;
        layoutCeldas.span=1;
    }

    public void loadPreferences(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        this.orden_bodegas=extraerConfiguraciones.get(getActivity().getString(R.string.key_act_bod),getActivity().getString(R.string.pref_act_bod_default));
    }

    public void generateReportStock(String id_item){
        loadPreferences();
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

    private void prepareButtons(View view) {
        addToCartImage = (ImageView) view.findViewById(R.id.product_add_to_cart_image);
        addToCartProgress = (ProgressBar) view.findViewById(R.id.product_add_to_cart_progress);
        addToCartProgress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.textIconColorPrimary), PorterDuff.Mode.MULTIPLY);
        View addToCart = view.findViewById(R.id.product_add_to_cart_layout);
        addToCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                postProductToCart();
            }
        });
    }
    private void postProductToCart() {

            if (addToCartImage != null) addToCartImage.setVisibility(View.INVISIBLE);
            if (addToCartProgress != null) addToCartProgress.setVisibility(View.VISIBLE);
            if (BuildConfig.DEBUG) Timber.d("AddToCartResponse: %s", "respuesta");
            if (addToCartImage != null) addToCartImage.setVisibility(View.VISIBLE);
            if (addToCartProgress != null) addToCartProgress.setVisibility(View.INVISIBLE);

            //Analytics.logAddProductToCart(product.getRemoteId(), product.getName(), product.getDiscountPrice());
            CartProductItem detail=  new CartProductItem();
            detail.setProductId(product.getCod_item());

            detail.setQuantity(1);
            detail.setTotalItemPrice(product.getPrecio_final());
            detail.setTotalItemPriceFormatted("0.00");
            ShoppingCar.updateCartCountNotification(detail);

            String result = getString(R.string.Product) + " " + getString(R.string.added_to_cart);
            Snackbar snackbar = Snackbar.make(productContainer, result, Snackbar.LENGTH_LONG)
                    .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.colorAccent))
                    .setAction(R.string.Go_to_cart, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getActivity() instanceof ShoppingCar)
                                ((ShoppingCar) getActivity()).onCartSelected();
                        }
                    });
            TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            //ShoppingCar.getInstance().addToRequestQueue(addToCart, CONST.PRODUCT_REQUESTS_TAG);
    }
    /**
     * Load product data.
     *
     * @param productId id of product.
     */
    private void getProduct(final String productId, int numprecio, String clienteId) {
        // Load product info
        setContentVisible(CONST.VISIBLE.PROGRESS);
        try{
            DBSistemaGestion helper= new DBSistemaGestion(getContext());
            Cursor cursor = helper.consultarItem(productId);
            if(cursor.moveToFirst()){
                this.product = new Item();
                this.product.setIdentificador(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_identificador)));
                this.product.setPresentacion(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
                this.product.setCod_item(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)));
                this.product.setRuta_img1(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img1)));
                this.product.setRuta_img2(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img2)));
                this.product.setRuta_img3(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img3)));
                this.product.setDescripcion(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                this.product.setIvg1(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg1)));
                this.product.setIvg2(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg2)));
                this.product.setIvg3(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg3)));
                this.product.setIvg4(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg4)));
                this.product.setIvg5(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg5)));
                this.product.setIvg6(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_ivg6)));
                this.product.setPrecio1(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
                this.product.setPrecio2(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
                this.product.setPrecio3(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
                this.product.setPrecio4(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
                this.product.setPrecio5(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
                this.product.setPrecio6(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
                this.product.setPrecio7(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));

                ShoppingCar.setActionBarTitle(this.product.getDescripcion());
                setContentVisible(CONST.VISIBLE.CONTENT);
            }
            cursor.close();
            helper.close();
            productoDescripcion.setText(this.product.getDescripcion());
            productoPresentacion.setText(this.product.getPresentacion());
            if(!product.getPresentacion().isEmpty())productoPresentacion.setVisibility(View.VISIBLE);

            double precio_product=0.0;

            switch (numprecio){
                case 1:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio1()));precio_product=this.product.getPrecio1();break;
                case 2:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio2()));precio_product=this.product.getPrecio2();break;
                case 3:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio3()));precio_product=this.product.getPrecio3();break;
                case 4:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio4()));precio_product=this.product.getPrecio4();break;
                case 5:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio5()));precio_product=this.product.getPrecio5();break;
                case 6:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio6()));precio_product=this.product.getPrecio6();break;
                case 7:productoPrecio.setText("$"+redondearNumero(this.product.getPrecio7()));precio_product=this.product.getPrecio7();break;
            }

            /*
                Validando descuentos disponibles
             */
            CalculoDescuentos desc = new CalculoDescuentos(getContext(),clienteId, productId);
            double porcentaje = desc.obtenerPorcentajeDescuento();
            double precio_real = desc.obtenerPrecioReal(precio_product, porcentaje);
            productoPorcentajeDescuento.setText(redondearNumero(porcentaje)+"%");
            productoPrecioFinal.setText("$"+precio_real);
            this.product.setPrecio_final(precio_real);
            setContentVisible(CONST.VISIBLE.CONTENT);
        }catch (Exception e){
            e.printStackTrace();
            setContentVisible(CONST.VISIBLE.EMPTY);
        }
    }

    public String redondearNumero(double numero) {
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",", ".");
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

}
