package ibzssoft.com.fragment;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.dialogs.FilterDialogFragment;
import ibzssoft.com.adaptadores.ProductsRecyclerAdapter;
import ibzssoft.com.interfaces.CategoryRecyclerInterface;
import ibzssoft.com.interfaces.FilterDialogInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ShoppingCar;
import ibzssoft.com.listeners.OnSingleClickListener;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.drawerMenu.DrawerItemCategory;
import ibzssoft.com.modelo.vista.Item;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.MsgUtils;
import timber.log.Timber;

public class CategoryFragment extends Fragment implements SearchView.OnQueryTextListener  {

    private RecyclerView mRecyclerView;
    private ArrayList<Item> productos;
    private String [] lineas;

    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private GridLayoutManager productsRecyclerLayoutManager;
    // Filters parameters
    private int conf_category_default;
    //private String [] conf_filter_categorys;
    private int  conf_columns;

    private String filterParameters = null;
    private ImageView filterButton;

    private static final String CODCATEGORY = "codCategory";
    private static final String CATEGORY_NAME = "categoryName";
    private static final String NUMERO_GRUPO = "numGrupo";
    private static final String SEARCH_QUERY = "search_query";
    private static final String LIST_PRICES = "precios";


    private int numgrupo;
    private String codcategory;
    private String precios;

    /**
     * Search string. The value is set only if the fragment is launched in order to searching.
     */
    private String searchQuery = null;
    // Properties used to restore previous state
    private int toolbarOffset = -1;

    public static CategoryFragment newInstance(int categoryId, String name, String type,String precios){
        Bundle args = new Bundle();
        args.putInt(NUMERO_GRUPO, categoryId);
        args.putString(CATEGORY_NAME, name);
        args.putString(CODCATEGORY, type);
        args.putString(CODCATEGORY, type);
        args.putString(SEARCH_QUERY, null);
        args.putString(LIST_PRICES, precios);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Show product list populated from drawer menu.
     *
     * @param drawerItemCategory corresponding drawer menu item.
     * @return new fragment instance.
     */
    public static CategoryFragment newInstance(DrawerItemCategory drawerItemCategory) {
        if (drawerItemCategory != null)
            return newInstance(drawerItemCategory.getNumgrupo(), drawerItemCategory.getName(), drawerItemCategory.getType(),drawerItemCategory.getPrecios());
        else {
            Timber.e(new RuntimeException(), "Creating category with null arguments");
            return null;
        }
    }

    /**
     * Create a new fragment instance for product detail.
     *
     * @param productId id of the product to show.
     * @return new fragment instance.
     */
    public static CategoryFragment newInstance(long productId) {
        Bundle args = new Bundle();
        //args.putLong(PRODUCT_ID, productId);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        loadPreferences();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.category_products_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        Bundle startBundle = getArguments();
        if (startBundle != null) {
            numgrupo= startBundle.getInt(NUMERO_GRUPO, 0);
            String categoryName = startBundle.getString(CATEGORY_NAME, "");
            codcategory = startBundle.getString(CODCATEGORY, "codcategory");
            precios = startBundle.getString(LIST_PRICES, "precios");
            searchQuery = startBundle.getString(SEARCH_QUERY, null);
            boolean isSearch = false;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                isSearch = true;
                numgrupo = -10;
                categoryName = searchQuery;
            }

            Timber.d("Category type: %s. CategoryId: %d. FilterUrl: %s.", codcategory, numgrupo, filterParameters);

            AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.category_appbar_layout);
            if (toolbarOffset != -1) appBarLayout.offsetTopAndBottom(toolbarOffset);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    toolbarOffset = i;
                }
            });
            ShoppingCar.setActionBarTitle(categoryName);
            this.filterButton = (ImageView) view.findViewById(R.id.category_filter_button);
            /*filterButton.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(new FilterDialogInterface() {
                            @Override
                            public void onFilterSelected(String newFilterUrl) {
                                filterParameters = newFilterUrl;
                                filterButton.setImageResource(R.drawable.filter_selected);

                                String [] categorys=newFilterUrl.split(";");
                                consultarProductos(categorys[0],categorys[1],categorys[2],categorys[3],categorys[4],categorys[5]);
                            }

                            @Override
                            public void onFilterCancelled() {
                                filterParameters = null;
                                filterButton.setImageResource(R.drawable.filter_unselected);
                            }
                        },conf_filter_categorys);//parametros para filtrado de productos
                        if (filterDialogFragment != null)
                            filterDialogFragment.show(getFragmentManager(), "filterDialogFragment");
                        else {
                            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.SHORT);
                        }
                    }
            });*/
            //Fin listener filterButton
        }else{
            MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, getString(R.string.Internal_error), MsgUtils.ToastLength.LONG);
            Timber.e(new RuntimeException(), "Run category fragment without arguments.");
        }
            this.filterButton = (ImageView) view.findViewById(R.id.category_filter_button);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_category, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        productsRecyclerAdapter.setFilter(productos);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true; // Return true to expand action view
                    }
                });
    }
    public void loadPreferences(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
        conf_category_default = Integer.parseInt(ext.get(getString(R.string.key_conf_carrito_categorias),"1"));
        //conf_filter_categorys = ext.getArray(getString(R.string.key_act_filter_categorys_shop_cart),null);
        String conf= ext.get(getString(R.string.key_conf_column_shop),"4");
        conf_columns = Integer.parseInt(conf);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.productos = new ArrayList<>();
        switch (numgrupo){
            case 1:consultarProductos(codcategory,null,null,null,null,null);break;
            case 2:consultarProductos(null,codcategory,null,null,null,null);break;
            case 3:consultarProductos(null,null,codcategory,null,null,null);break;
            case 4:consultarProductos(null,null,null,codcategory,null,null);break;
            case 5:consultarProductos(null,null,null,null,codcategory,null);break;
            case 6:consultarProductos(null,null,null,null,null,codcategory);break;
        }
        productsRecyclerAdapter = new ProductsRecyclerAdapter(getActivity(), this.productos,precios, new CategoryRecyclerInterface() {
            @Override
            public void onProductSelected(View view, Item product) {
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                }
                ((ShoppingCar) getActivity()).onProductSelected(product.getCod_item());
            }
        });
        //Fin listener filterButton
        productsRecyclerLayoutManager = new GridLayoutManager(getActivity(), this.conf_columns);
        mRecyclerView.setLayoutManager(productsRecyclerLayoutManager);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
    }



    public void consultarProductos(String cod_pcg1,String cod_pcg2,String cod_pcg3,String cod_pcg4,String cod_pcg5,String cod_pcg6){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getContext());
        this.lineas = ext.get(getString(R.string.key_act_lin),getString(R.string.pref_act_lin_default)).split(",");
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.consultarProductosShop(this.lineas, cod_pcg1,cod_pcg2,cod_pcg3,cod_pcg4,cod_pcg5,cod_pcg6);
        cargarListado(cursor);
        helper.close();
    }

    public void cargarListado(Cursor cursor){
        if(cursor.moveToFirst()){
            do{
                Item item= new Item();
                item.setIdentificador(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_identificador)));
                item.setCod_item(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)));
                item.setDescripcion(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
                item.setPresentacion(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
                item.setRuta_img1(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img1)));
                item.setRuta_img2(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img2)));
                item.setRuta_img3(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_img3)));
                item.setIvg1(cursor.getString(cursor.getColumnIndex(IVGrupo1.FIELD_descripcion)));
                item.setIvg2(cursor.getString(cursor.getColumnIndex(IVGrupo2.FIELD_descripcion)));
                item.setIvg3(cursor.getString(cursor.getColumnIndex(IVGrupo3.FIELD_descripcion)));
                item.setIvg4(cursor.getString(cursor.getColumnIndex(IVGrupo4.FIELD_descripcion)));
                item.setIvg5(cursor.getString(cursor.getColumnIndex(IVGrupo5.FIELD_descripcion)));
                item.setIvg6(cursor.getString(cursor.getColumnIndex(IVGrupo6.FIELD_descripcion)));
                item.setPrecio1(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
                item.setPrecio2(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
                item.setPrecio3(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
                item.setPrecio4(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
                item.setPrecio5(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
                item.setPrecio6(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
                item.setPrecio7(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));
                productos.add(item);
            }while (cursor.moveToNext());
        }
    }

    private List<Item> filter(List<Item> models, String query) {
        query = query.toLowerCase();
        final List<Item> filteredModelList = new ArrayList<>();
        for (Item model : models) {
            final String descripcion = model.getDescripcion().toLowerCase();
            final String codigo = model.getCod_item().toLowerCase();
            if (descripcion.contains(query)||codigo.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Item> filteredModelList = filter(productos, newText);
        productsRecyclerAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

}
