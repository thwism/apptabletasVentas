package ibzssoft.com.ishidamovile;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.CatalogoClientesAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.storage.DBSistemaGestion;

public class Catalogo_Clientes extends Fragment implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private CatalogoClientesAdapter mAdapter;
    private ArrayList<Cliente> clientes;
    private TextView countClients;
    private int conf_descarga_cartera;
    private int  conf_descarga_clientes;
    private int  conf_mostrar_clientes;
    private String[] rutas;
    private String[] accesos;
    private String vendedor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_catalogo__clientes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        countClients = (TextView) view.findViewById(R.id.textCountClientes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        this.cargarPreferenciasCatalogo();
        this.extraerParametros();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        clientes = new ArrayList<>();
        switch (this.conf_mostrar_clientes){
            case 1:
                this.consultarClientes();
                break;
            case 2:
                this.consultarClientesxCartera();
                break;
            case 3:
                this.consultarClientesxDesc();
                break;
        }
        mAdapter = new CatalogoClientesAdapter(getActivity(), this.clientes,this.vendedor);
        countClients.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalogo_clientes, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        mAdapter.setFilter(clientes);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    public void extraerParametros(){
        this.vendedor = getArguments().getString("vendedor").toString();
    }

    public void consultarClientes(){
        Cursor cursor = null;
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        switch (conf_descarga_clientes){
            case 0:
                //vendedor
                cursor=helper.consultarClientesxVendedor(accesos);
                break;
            case 1:
                //cobrador
                cursor=helper.consultarClientesxCobrador(accesos);
                break;
            case 2:
                //todos
                cursor=helper.consultarClientesTodo();
                break;
        }
        cargarListado(cursor);
        helper.close();
    }
    /*
        Descuentos por Cliente
     */
    public void consultarClientesxCartera(){
        Cursor cursor = null;
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        switch (conf_descarga_cartera){
            case 0:
                //cartera por vendedor
                System.out.println("Mostrando cartera por vendedor!!!");
                cursor=helper.consultarCarteraxVendedor(accesos, true);
                break;
            case 1:
                //cartera por rutas
                System.out.println("Mostrando cartera por rutas!!!");
                cursor=helper.consultarCarteraxRutas(rutas, true);
                break;
            case 2:
                //cartera por cobrador
                System.out.println("Mostrando cartera por cobrador!!!");
                cursor=helper.consultarCarteraxCobrador(accesos, true);
                break;
        }
        cargarListado(cursor);
        helper.close();
    }
    /*
        Clientes con Descuentos
     */
    public void consultarClientesxDesc(){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor =helper.consultarDescuentosxAgrupados();
        cargarListado(cursor);
        helper.close();
    }
    /**
     * Metodos de filtrado
     */
    public void cargarPreferenciasCatalogo(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        String conf = extraerConfiguraciones.get(getString(R.string.key_conf_descarga_cartera),"0");
        String conf2 = extraerConfiguraciones.get(getString(R.string.key_conf_descarga_clientes),"0");
        String conf3 = extraerConfiguraciones.get(getString(R.string.key_conf_catalogo_clientes),"1");
        conf_descarga_cartera= Integer.parseInt(conf);
        conf_descarga_clientes= Integer.parseInt(conf2);
        conf_mostrar_clientes= Integer.parseInt(conf3);
        rutas=extraerConfiguraciones.get(getString(R.string.key_act_rut),getString(R.string.pref_act_rut_default)).split(",");
        accesos=extraerConfiguraciones.get(getString(R.string.key_act_acc),getString(R.string.pref_act_acc_default)).split(",");
    }
    @Override
    public boolean onQueryTextChange(String query) {
        final List<Cliente> filteredModelList = filter(clientes, query);
        mAdapter.animateTo(filteredModelList);
        countClients.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }



    /**
     * Consultar clientes
     */
    public void cargarListado(Cursor cursor){
        clientes.clear();
        if(cursor.moveToFirst()){
            do{
                Cliente cliente= new Cliente(
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovcli)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion1)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion2)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono1)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono2)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fax)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_email)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_banco)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numcuenta)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direcbanco)),
                        cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_bandproveedor)),
                        cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_bandcliente)),
                        cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_estado)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idparroquia)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo1)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo2)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo3)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo4)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idvendedor)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idcobrador)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovincia)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idcanton)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_diasplazo)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_observacion)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numprecio)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numserie)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono3)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_posgooglemaps)),
                        cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fechagrabado))
                );
                clientes.add(cliente);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    /*
0. Todos
1. Cartera
2. Descuentos
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_cartera:
                this.consultarClientesxCartera();
                this.mAdapter.setFilter(clientes);
                this.countClients.setText(String.valueOf(mAdapter.getItemCount()));
                return true;
            case R.id.setting_descuento:
                this.consultarClientesxDesc();
                this.mAdapter.setFilter(clientes);
                this.countClients.setText(String.valueOf(mAdapter.getItemCount()));
                return true;
            case R.id.setting_all:
                this.consultarClientes();
                this.mAdapter.setFilter(clientes);
                this.countClients.setText(String.valueOf(mAdapter.getItemCount()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Cliente> filter(List<Cliente> models, String query) {
        query = query.toLowerCase();

        final List<Cliente> filteredModelList = new ArrayList<>();
        for (Cliente model : models) {
            final String text = model.getNombre().toLowerCase();
            final String alterno = model.getNombrealterno().toLowerCase();
            final String cedula= model.getRuc().toLowerCase();
            if (text.contains(query) || alterno.contains(query) || cedula.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
