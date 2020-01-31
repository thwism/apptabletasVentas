package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.CatalogoCarteraAdapter;
import ibzssoft.com.adaptadores.CatalogoClientesAdapter;
import ibzssoft.com.adaptadores.CatalogoDescuentoAdapter;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.recibir.RecibirPermisos;
import ibzssoft.com.storage.DBSistemaGestion;

public class Catalogo_Descuentos extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener{

    private RecyclerView mRecyclerView;
    private CatalogoDescuentoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<Descuento> descuentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo__descuentos);
        System.out.println("Parametros cartera:"+getIntent().getBooleanExtra("cartera",false));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_descuento);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        descuentos= new ArrayList<>();
        consultarDescuentos(getIntent().getStringExtra("cliente"),getIntent().getStringExtra("producto"));
        mAdapter = new CatalogoDescuentoAdapter(this,descuentos);
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void consultarDescuentos(String cliente,String producto){
        /*DBSistemaGestion helper= new DBSistemaGestion(this);
        Cursor cursor=helper.consultarDescuentos(getIntent().getStringExtra("accesos").split(","),getIntent().getIntExtra("supervisor",0),cliente,producto);
        cargarListado(cursor);
        helper.close();*/
    }
    public void cargarListado(Cursor cur){
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
                        cur.getString(cur.getColumnIndex(Descuento.FIELD_fecha_grabado))
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
            final String text = model.getCliente_id().toLowerCase();
            final String text2 = model.getInventario_id().toLowerCase();
            if (text.contains(query)||text2.contains(query)){
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalogo_descuento, menu);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(Catalogo_Descuentos.this);
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
