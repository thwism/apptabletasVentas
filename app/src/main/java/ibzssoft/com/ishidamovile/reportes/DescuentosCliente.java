package ibzssoft.com.ishidamovile.reportes;

import android.app.SearchManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.CatalogoDescuentoClienteAdapter;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.storage.DBSistemaGestion;

public class DescuentosCliente extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{
    private TextView txtNombres,txtComercial;
    private RecyclerView mRecyclerView;
    private CatalogoDescuentoClienteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<Descuento> descuentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descuentos_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNombres= (TextView)findViewById(R.id.estadoNombres);
        txtComercial=(TextView)findViewById(R.id.estadoComercial);
        txtNombres.setText("Descuentos :: "+getIntent().getStringExtra("descripcion"));
        if(getIntent().getStringExtra("comercial").isEmpty()){
            txtComercial.setVisibility(View.GONE);
        }else{
            txtComercial.setText(getIntent().getStringExtra("comercial"));
            txtComercial.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //cargar datos iniciales
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_desc_cli);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        descuentos= new ArrayList<>();
        consultarDescuentos(getIntent().getStringExtra("cliente"));
        mAdapter = new CatalogoDescuentoClienteAdapter(this, descuentos);
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void consultarDescuentos(String id_cliente){
        DBSistemaGestion helper= new DBSistemaGestion(this);
        Cursor cursor=helper.consultarDescuentosxCliente(id_cliente);
        cargarListado(cursor);
        helper.close();
    }
    public void cargarListado(Cursor cur){
        Descuento descuento=null;
        if(cur.moveToFirst()){
            do{
                descuento=  new Descuento(
                        cur.getString(cur.getColumnIndex(Descuento.FIELD_identificador)),
                        cur.getDouble(cur.getColumnIndex(Descuento.FIELD_porcentaje)),
                        cur.getInt(cur.getColumnIndex(Descuento.FIELD_estado)),
                        cur.getString(cur.getColumnIndex(Descuento.FIELD_identificador)),
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
            final String text2 = model.getInventario_id().toLowerCase();
            final String text3 = model.getFecha_grabado().toLowerCase();
            if (text2.contains(query)||text3.contains(query)){
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cliente_descuento, menu);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(DescuentosCliente.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}