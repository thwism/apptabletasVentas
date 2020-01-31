package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.CatalogoPedidosPendientesAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.PedidoPendiente;
import ibzssoft.com.recibir.RecibirPedidosPendientes;
import ibzssoft.com.storage.DBSistemaGestion;

public class PedidosPedientes extends Fragment implements SearchView.OnQueryTextListener  {

    private RecyclerView mRecyclerView;
    private CatalogoPedidosPendientesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<PedidoPendiente> pedidos;
    private TextView countPedidos;
    private String ip,port,url,ws;
    private String[]accesos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_pedidos_pedientes, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        countPedidos= (TextView) view.findViewById(R.id.textCountPPendientes);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        pedidos= new ArrayList<>();
        this.cargarPreferenciasCatalogo();
        try {
            this.consultarPedidosPendientes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mAdapter = new CatalogoPedidosPendientesAdapter(getActivity(), pedidos);
        countPedidos.setText(String.valueOf(mAdapter.getItemCount()));
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }

    public void cargarPreferenciasCatalogo(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        accesos=extraerConfiguraciones.get(getString(R.string.key_act_acc),getString(R.string.pref_act_acc_default)).split(",");
        ip=extraerConfiguraciones.get(getString(R.string.key_conf_ip),getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(getString(R.string.key_conf_port),getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(getString(R.string.key_conf_url),getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(getString(R.string.key_ws_pedidos_pendientes),getString(R.string.pref_ws_pedidos_pendientes));
    }

    public void consultarPedidosPendientes() throws SQLException {
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor = helper.consultarPedidosPendientes(accesos);
        cargarListado(cursor);
    }
    public void cargarListado(Cursor cursor){
        pedidos.clear();
        if(cursor.moveToFirst()){
            do{
                PedidoPendiente ivInventario= new PedidoPendiente(
                        cursor.getString(cursor.getColumnIndex(PedidoPendiente.FIELD_idprovcli)),
                        cursor.getString(cursor.getColumnIndex(PedidoPendiente.FIELD_ruc_ci)),
                        cursor.getString(cursor.getColumnIndex(PedidoPendiente.FIELD_nombres)),
                        cursor.getString(cursor.getColumnIndex(PedidoPendiente.FIELD_nombre_alterno)),
                        cursor.getInt(cursor.getColumnIndex(PedidoPendiente.FIELD_items)),
                        cursor.getString(cursor.getColumnIndex(PedidoPendiente.FIELD_vendedor_id)));
                pedidos.add(ivInventario);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<PedidoPendiente> filteredModelList = filter(pedidos, query);
        mAdapter.animateTo(filteredModelList);
        countPedidos.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<PedidoPendiente> filter(List<PedidoPendiente> models, String query) {
        query = query.toLowerCase();

        final List<PedidoPendiente> filteredModelList = new ArrayList<>();
        for (PedidoPendiente model : models) {
            final String text = model.getNombres().toLowerCase();
            final String alterno = model.getNombre_alterno().toLowerCase();
            if (text.contains(query) || alterno.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pedidos_pendientes, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        mAdapter.setFilter(pedidos);
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
            case R.id.action_sinc_ppendiente:
                RecibirPedidosPendientesTask recibirPP= new RecibirPedidosPendientesTask();
                recibirPP.execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

        private class RecibirPedidosPendientesTask extends AsyncTask<String, Integer, Boolean> {
            private ProgressDialog progress;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress = new ProgressDialog(getActivity());
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setTitle("Descargando Pedidos Pendientes");
                progress.setCancelable(false);
                progress.setMessage("Espere...");
                progress.show();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progress.setProgress(values[0]);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                DBSistemaGestion helper = new DBSistemaGestion(getActivity());
                Boolean result = false;
                for (int i = 0; i < accesos.length; i++) {
                    HttpParams httpParameters = new BasicHttpParams();
                    int timeoutConnection = 50000;
                    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                    int timeoutSocket = 50000;
                    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                    HttpClient httpClient = new DefaultHttpClient(httpParameters);
                    httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                    HttpGet del = new HttpGet("http://" + ip + ":" + port + url + ws + "/" + accesos[i]);
                    System.out.println("Solicitando pedidos pendientes: " + "http://" + ip + ":" + port + url + ws + "/" + accesos[i]);
                    del.setHeader("content-type", "application/json");
                    try {
                        HttpResponse resp = httpClient.execute(del);
                        String respStr = EntityUtils.toString(resp.getEntity());
                        JSONArray respJSON = new JSONArray(respStr);
                        Gson gson = new Gson();
                        progress.setMax(respJSON.length());
                        progress.setProgress(0);
                        int count = 0;
                        if (respJSON.length() > 0) {
                            helper.vaciarPedidoPendiente();
                            for (int j = 0; j < respJSON.length(); j++) {
                                JSONObject obj = respJSON.getJSONObject(j);
                                PedidoPendiente cli = gson.fromJson(obj.toString(), PedidoPendiente.class);
                                count++;
                                helper.crearPedidoPendiente(cli);
                                publishProgress(count);
                                System.out.println("ppendiente recibido: "+cli.toString());
                            }
                        }
                        result = true;
                    } catch (Exception ex) {
                        Log.e("ServicioRest", "Error!", ex);
                        result = false;
                    }
                }
                helper.close();
                return result;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                if (progress.isShowing()) {
                    progress.dismiss();
                    if (s) {
                        try{
                            consultarPedidosPendientes();
                            mAdapter.setFilter(pedidos);
                            countPedidos.setText(String.valueOf(mAdapter.getItemCount()));
                        }catch (SQLException sql){
                            new Alertas(getActivity(),"Error al importar pedidos pendientes",sql.getMessage()).mostrarMensaje();
                        }
                    }else {
                        Toast ts= Toast.makeText(getActivity(), "No se pudo descargar la informacion, revise su conexion a internet",Toast.LENGTH_SHORT);
                        ts.show();
                    }
                }
            }
        }
}

