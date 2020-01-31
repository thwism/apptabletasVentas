package ibzssoft.com.ishidamovile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ProductsRecyclerAdapter;
import ibzssoft.com.dialogs.ProductDialogFragment;
import ibzssoft.com.interfaces.CategoryRecyclerInterface;
import ibzssoft.com.interfaces.ProductDialogInterface;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.vista.Item;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.MsgUtils;

public class Catalogo_Items extends Fragment implements SearchView.OnQueryTextListener   {

    private RecyclerView mRecyclerView;
    private ProductsRecyclerAdapter productsRecyclerAdapter;
    private GridLayoutManager productsRecyclerLayoutManager;
    private ArrayList<Item> productos;
    private TextView countItems;
    private ArrayList<String> familias= new ArrayList<>();
    private ArrayList<String> codcategorias= new ArrayList<>();
    private ArrayList<String> categorias;
    private int selectPosition=0;
    private String []lineas;
    private String []lineas_visibles;
    private boolean filtrar_familia;
    /*Filtro Inicial*/
    String[] listItems;
    String[] listCodItems;
    boolean[] checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        extraerParametros();
        loadPreferences();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_catalogo__items, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.category_products_recycler);
        countItems = (TextView)view.findViewById(R.id.textCountItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        cargarListadoFamilias();
        this.listItems = listadoNombresFamilias();
        this.listCodItems = listadoCodCategorias();
        this.checkedItems = listadoSeleccionadosCategorias();
        this.productos = new ArrayList<>();
        this.categorias=new ArrayList<>();
        if(filtrar_familia)
            filterDialog(this.listItems,this.checkedItems);
        else
            prepareProducts(false);
    }

    public void prepareProducts(boolean by_familia){
        this.consultarProductos(by_familia);
        productsRecyclerAdapter = new ProductsRecyclerAdapter(getActivity(), this.productos,"2" ,new CategoryRecyclerInterface() {
            @Override
            public void onProductSelected(View caller, Item product) {
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
                }
                ProductDialogFragment filterDialogFragment = ProductDialogFragment.newInstance(product,new ProductDialogInterface() {
                    @Override
                    public void onAddShoppingCart(String filterUrl) {

                    }
                    @Override
                    public void onCancelledShopping() {

                    }
                });
                if (filterDialogFragment != null)
                    filterDialogFragment.show(getFragmentManager(), "filterDialogFragment");
                else {
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.SHORT);
                }
            }
        });
        //Fin listener filterButton
        productsRecyclerLayoutManager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(productsRecyclerLayoutManager);
        mRecyclerView.setAdapter(productsRecyclerAdapter);
        countItems.setText(String.valueOf(productsRecyclerAdapter.getItemCount()));
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Item> filteredModelList = filter(productos, query);
        productsRecyclerAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        countItems.setText(String.valueOf(productsRecyclerAdapter.getItemCount()));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void consultarProductos(boolean by_familia){
        this.loadPreferences();
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor = null;
        if(!by_familia)
            cursor=helper.consultarProductos(this.lineas);
        else
            cursor=helper.consultarProductos(this.lineas_visibles);

        cargarListado(cursor);
    }

    public void extraerParametros(){
        this.lineas= getArguments().getString("lineas").toString().split(",");
    }
    public void loadPreferences(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getContext());
        lineas_visibles = ext.get(getContext().getString(R.string.key_act_lin_vis_cat_item),"").split(",");
        filtrar_familia = ext.getBoolean(getContext().getString(R.string.key_conf_filtrar_items_familia),false);
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

                item.setPrecio1(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)));
                item.setPrecio2(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)));
                item.setPrecio3(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)));
                item.setPrecio4(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)));
                item.setPrecio5(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)));
                item.setPrecio6(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)));
                item.setPrecio7(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)));

                item.setIvg1(cursor.getString(cursor.getColumnIndex(IVGrupo1.FIELD_descripcion)));
                item.setIvg2(cursor.getString(cursor.getColumnIndex(IVGrupo2.FIELD_descripcion)));
                item.setIvg3(cursor.getString(cursor.getColumnIndex(IVGrupo3.FIELD_descripcion)));
                item.setIvg4(cursor.getString(cursor.getColumnIndex(IVGrupo4.FIELD_descripcion)));
                item.setIvg5(cursor.getString(cursor.getColumnIndex(IVGrupo5.FIELD_descripcion)));
                item.setIvg6(cursor.getString(cursor.getColumnIndex(IVGrupo6.FIELD_descripcion)));
                productos.add(item);
            }while (cursor.moveToNext());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalogo_productos, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        productsRecyclerAdapter.setFilter(productos);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.categorias:
                mostrarCategorias();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Item> filter(List<Item> models, String query) {
        query = query.toLowerCase();

        final List<Item> filteredModelList = new ArrayList<>();
        for (Item model : models) {
            final String text = model.getDescripcion().toLowerCase();
            final String present = model.getPresentacion().toLowerCase();
            final String codigo = model.getCod_item().toLowerCase();
            if (text.contains(query)||codigo.contains(query)||present.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    private List<Item> filterByCategory(List<Item> models, String query) {
        query = query.toLowerCase();
        final List<Item> filteredModelList = new ArrayList<>();
        if(query.equals(new String("TODOS").toLowerCase())){
            for (Item model : models) {
                filteredModelList.add(model);
            }
        }else{
            for (Item model : models) {
                if(model.getIvg2()!=null && !model.getIvg2().isEmpty()){
                    final String text = model.getIvg2().toLowerCase();
                    if(text.equals(query)) {
                        filteredModelList.add(model);
                    }
                }
            }
        }
        return filteredModelList;
    }
    /**
     * Mostrar Listado de categorias
     */
    /**
     * Mostrar Listado de categorias
     */
    public void cargarListadoFamilias(){
        familias.clear();
        codcategorias.clear();
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarFamilias();
        llenarListadoFamilias(cursor);
        helper.close();
    }

    public void cargarListadoCategoriasProductos(){
        familias.clear();
        codcategorias.clear();
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarCategorias();
        llenarListadoCategorias(cursor);
        helper.close();
    }
    public void llenarListadoFamilias(Cursor cur){
        int count=0;
        if(cur.moveToFirst()){
            do{
                familias.add(count,cur.getString(cur.getColumnIndex(IVGrupo1.FIELD_descripcion)));
                codcategorias.add(count,cur.getString(cur.getColumnIndex(IVGrupo1.FIELD_idgrupo1)));
                count++;
            }while (cur.moveToNext());
        }
    }

    public void llenarListadoCategorias(Cursor cur){
        categorias.add(0,"TODOS");
        int count=1;
        if(cur.moveToFirst()){
            do{
                categorias.add(count,cur.getString(cur.getColumnIndex(IVGrupo2.FIELD_descripcion)));
                count++;
            }while (cur.moveToNext());
        }
    }
    public String [] listadoNombresFamilias(){
        String [] array=new String[familias.size()];
        for(int i=0; i<familias.size(); i++){
            array[i]=familias.get(i);
        }
        return array;
    }

    public String [] listadoNombresCategorias(){
        String [] array=new String[categorias.size()];
        for(int i=0; i<categorias.size(); i++){
            array[i]=categorias.get(i);
        }
        return array;
    }

    public String [] listadoCodCategorias(){
        String [] array=new String[codcategorias.size()];
        for(int i=0; i<codcategorias.size(); i++){
            array[i]=codcategorias.get(i);
        }
        return array;
    }

    public boolean [] listadoSeleccionadosCategorias(){
        boolean [] array=new boolean[familias.size()];
        for(int i=0; i<familias.size(); i++){
            for(int j=0; j<lineas_visibles.length;j++){
                if(lineas_visibles[j].equals(codcategorias.get(i))){
                    mUserItems.add(i);
                    array[i]=true;
                }
            }
        }
        return array;
    }

    public void mostrarCategorias(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        cargarListadoCategoriasProductos();
        alertDialogBuilder.setSingleChoiceItems(listadoNombresCategorias(), selectPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                int selectedPosition = ((android.support.v7.app.AlertDialog) dialog).getListView().getCheckedItemPosition();
                selectPosition=selectedPosition;
                final List<Item> filteredModelList = filterByCategory(productos, categorias.get(selectedPosition));
                productsRecyclerAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                countItems.setText(String.valueOf(productsRecyclerAdapter.getItemCount()));
            }
        });

        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void filterDialog(String []categorias, boolean[]selecionados){
        try{
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
            mBuilder.setTitle(R.string.pref_tittle_filter_items_family);
            mBuilder.setMultiChoiceItems(categorias, selecionados, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                    if(isChecked){
                        mUserItems.add(position);
                    }else{
                        mUserItems.remove((Integer.valueOf(position)));
                    }
                }
            });

            mBuilder.setCancelable(false);
            mBuilder.setPositiveButton(R.string.ACCEPT, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                    String item = "";
                    for (int i = 0; i < mUserItems.size(); i++) {
                        item = item + listCodItems[mUserItems.get(i)];
                        if (i != mUserItems.size() - 1) {
                            item = item + ",";
                        }
                    }
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getContext());
                    ext.update(getContext().getString(R.string.key_act_lin_vis_cat_item),item);
                    prepareProducts(true);
                }
            });

            mBuilder.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
