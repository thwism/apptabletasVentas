package ibzssoft.com.ishidamovile;

import android.app.SearchManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ibzssoft.com.adaptadores.CatalogoVendedoresAdapter;
import ibzssoft.com.adaptadores.ItemDecorator;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;

public class Catalogo_Vendedores extends AppCompatActivity implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private CatalogoVendedoresAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Vendedor> vendedores;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private TextView countItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo__vendedores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_vendedores);
        countItems= (TextView) findViewById(R.id.textCountVendedor);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        vendedores= new ArrayList<>();
        // specify an adapter (see also next example)
        consultarProductos();
        mAdapter = new CatalogoVendedoresAdapter(this, vendedores);
        countItems.setText(String.valueOf(mAdapter.getItemCount()));
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new ItemDecorator(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Vendedor> filteredModelList = filter(vendedores, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        countItems.setText(String.valueOf(mAdapter.getItemCount()));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public void consultarProductos(){
        DBSistemaGestion helper= new DBSistemaGestion(this);
        Cursor cursor=helper.consultarVendedoresAll();
        cargarListado(cursor);
        cursor.close();
        helper.close();
    }

    public void cargarListado(Cursor cursor){
        if(cursor.moveToFirst()){
            do{
                Vendedor vendedor= new Vendedor(
                        cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_idvendedor)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_codvendedor)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_codusuario)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_nombre)),
                        cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_bandvalida)),
                        cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_bandvendedor)),
                        cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_bandcobrador)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_lineas)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_vendedores)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_ordenbodegas)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_rutastablet)),
                        cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_fechagrabado))
                );
                vendedores.add(vendedor);
            }while (cursor.moveToNext());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalogo_vededores, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(Catalogo_Vendedores.this);
        return true;
    }

    private List<Vendedor> filter(List<Vendedor> models, String query) {
        query = query.toLowerCase();

        final List<Vendedor> filteredModelList = new ArrayList<>();
        for (Vendedor model : models) {
            final String text = model.getNombre().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
