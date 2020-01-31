package ibzssoft.com.ishidamovile.reportes;
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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ibzssoft.com.adaptadores.CatalogoClientesAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.SectorSpinnerAdapter;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.storage.DBSistemaGestion;

public class Clientes_Sector extends Fragment implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private CatalogoClientesAdapter mAdapter;
    private ArrayList<Cliente> clientes;
    private TextView countClients;
    private String[] accesos;
    private String vendedor;
    private Spinner sortSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_catalogo__clientes_sector, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        countClients = (TextView) view.findViewById(R.id.textCountClientes);
        this.sortSpinner= (Spinner) view.findViewById(R.id.filter_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        this.cargarPreferenciasCatalogo();
        this.extraerParametros();
        prepareClientes();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        prepareSortSpinner();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clientes_sector, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mAdapter.setFilter(clientes);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true; // Return true to expand action view
                    }
                });
    }



    private void prepareSortSpinner() {
        SectorSpinnerAdapter filterSpinnerAdapter = new SectorSpinnerAdapter(getActivity());
        sortSpinner.setAdapter(filterSpinnerAdapter);
        sortSpinner.setOnItemSelectedListener(null);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SortItem item = (SortItem) parent.getItemAtPosition(position);
                mAdapter.animateTo(filterClienteEstado(clientes, item.getValue()));
                countClients.setText(String.valueOf(mAdapter.getItemCount()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private List<Cliente> filterClienteEstado(List<Cliente> models, String idgrupo1) {
        final List<Cliente> filteredModelList = new ArrayList<>();
        for (Cliente model : models) {
            final String est = model.getIdgrupo1();
            if(est.equals(idgrupo1)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void prepareClientes(){
        clientes = new ArrayList<>();
        consultarClientes();
        mAdapter = new CatalogoClientesAdapter(getActivity(), this.clientes,this.vendedor);
        countClients.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void extraerParametros(){
        this.vendedor = getArguments().getString("vendedor").toString();
    }

    public void consultarClientes(){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor = helper.consultarCarteraxCobrador(accesos, true);
        cargarListado(cursor);
        helper.close();
    }

    /**
     * Metodos de filtrado
     */
    public void cargarPreferenciasCatalogo(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
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
