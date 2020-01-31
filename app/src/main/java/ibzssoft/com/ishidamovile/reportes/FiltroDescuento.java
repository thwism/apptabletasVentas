package ibzssoft.com.ishidamovile.reportes;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.CatalogoClientesAdapter;
import ibzssoft.com.adaptadores.CatalogoDescuentoAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.storage.DBSistemaGestion;

public class FiltroDescuento extends Fragment implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private CatalogoDescuentoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<Descuento> descuentos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_filtro_descuento,null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_descuento);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        descuentos= new ArrayList<>();
        mAdapter = new CatalogoDescuentoAdapter(getActivity(),descuentos);
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        this.OpenCategroyDialogBox();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filtrar_descuentos, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        mAdapter.setFilter(descuentos);
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
    public boolean onQueryTextChange(String query) {
        final List<Descuento> filteredModelList = filter(descuentos, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private List<Descuento> filter(List<Descuento> models, String query) {
        query = query.toLowerCase();
        final List<Descuento> filteredModelList = new ArrayList<>();
        for (Descuento model : models) {
            final String cod = model.getFecha_grabado();
            final String cli = model.getCliente_id().toLowerCase();
            final String descrip= model.getInventario_id().toLowerCase();
            final String porc= String.valueOf(model.getPorcentaje());
            if (cod.contains(query) || cli.contains(query) || descrip.contains(query)|| porc.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void consultarDescuentos(String cliente,String producto){
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.consultarDescuentos(cliente,producto);
        cargarListado(cursor);
        helper.close();
    }
    public void cargarListado(Cursor cur){
        descuentos.clear();
        Descuento descuento=null;
        if(cur.moveToFirst()){
            do{
                descuento=  new Descuento(
                        cur.getString(cur.getColumnIndex(Descuento.FIELD_identificador)),
                        cur.getDouble(cur.getColumnIndex(Descuento.FIELD_porcentaje)),
                        cur.getInt(cur.getColumnIndex(Descuento.FIELD_estado)),
                        cur.getString(cur.getColumnIndex(Cliente.FIELD_nombre)),
                        cur.getString(cur.getColumnIndex(IVInventario.FIELD_descripcion)),
                        cur.getString(cur.getColumnIndex(Descuento.FIELD_vendedor_id)),
                        cur.getString(cur.getColumnIndex(IVInventario.FIELD_cod_item))
                );
                descuentos.add(descuento);
            }while (cur.moveToNext());
        }
        cur.close();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                this.OpenCategroyDialogBox();
                return true;
        }
        return true;
    }



    private void OpenCategroyDialogBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.filtro_descuento, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Parametros de busqueda");
        alert.setView(promptView);

        final EditText input1 = (EditText) promptView.findViewById(R.id.userInputDialog);
        final EditText input2 = (EditText) promptView.findViewById(R.id.userInputDialog2);

        alert.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String cli = input1.getText().toString();
                String item = input2.getText().toString();
                consultarDescuentos(cli,item);
                mAdapter.setFilter(descuentos);
            }
        });

        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog alert1 = alert.create();
        alert1.show();

    }

}
