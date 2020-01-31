package ibzssoft.com.paginas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.FilterSpinnerAdapter;
import ibzssoft.com.adaptadores.TransVisitaAdapter;
import ibzssoft.com.adaptadores.ValidateReferencia;
import ibzssoft.com.enviar.IVKardex_Serialize_Envio;
import ibzssoft.com.enviar.PKardex_Envio;
import ibzssoft.com.enviar.Transaccion_Serialize_Envio;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.visita.crear.VisitaY;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

public class TabVisitaAll extends Fragment implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private TransVisitaAdapter mAdapter;
    private ArrayList<Transaccion> transacciones;
    private TextView countTrans;
    private String []accesos;
    private int opcion,orden,comodin;
    private String empresa,grupo;
    private static final String catalogo="VISITA";
    private Spinner sortSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_visita_all, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        countTrans= (TextView) view.findViewById(R.id.textCountVisitas);
        this.sortSpinner = (Spinner) view.findViewById(R.id.category_sort_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        this.extraerParametros();
        return view;
    }

    public void extraerParametros(){
        this.accesos= getArguments().getString("accesos").split(",");
        this.opcion=getArguments().getInt("opcion");
        this.orden=getArguments().getInt("ord_cat_trans");
        this.comodin=getArguments().getInt("ord_cat_trans_comodin");
        this.empresa=getArguments().getString("empresa");
        this.grupo=getArguments().getString("grupo");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        transacciones = new ArrayList<>();
        this.consultarTransacciones();
        mAdapter = new TransVisitaAdapter(getActivity(), this.transacciones,this.opcion);
        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.setAdapter(mAdapter);
        prepareSortSpinner();
    }

    private void prepareSortSpinner() {
        FilterSpinnerAdapter filterSpinnerAdapter = new FilterSpinnerAdapter(getActivity());
        sortSpinner.setAdapter(filterSpinnerAdapter);
        sortSpinner.setOnItemSelectedListener(null);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://todos
                        mAdapter.animateTo(filterEstado(transacciones, 2));
                        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
                        break;
                    case 1://enviados
                        mAdapter.animateTo(filterEstado(transacciones, 1));
                        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
                        break;
                    case 2://no enviados
                        mAdapter.animateTo(filterEstado(transacciones, 0));
                        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void consultarTransacciones(){
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarTransacciones(accesos,0,catalogo,orden,comodin);
        cargarListado(cursor);
        helper.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab_pedido, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
            new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    mAdapter.setFilter(transacciones);
                    return true; // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true; // Return true to expand action view
                }
            });
    }



    @Override
    public boolean onQueryTextChange(String query) {
        final List<Transaccion> filteredModelList = filter(transacciones, query);
        mAdapter.animateTo(filteredModelList);
        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
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
    public void cargarListado(Cursor cur){
        transacciones.clear();
        if(cur.moveToFirst()){
            do{
                transacciones.add(new Transaccion(
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_descripcion)),
                        cur.getInt(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_trans)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_hora_trans)),
                        cur.getInt(cur.getColumnIndex(Transaccion.FIELD_band_enviado)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_vendedor_id)),
                        cur.getString(cur.getColumnIndex(Cliente.FIELD_nombre)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_forma_cobro_id)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_referencia)),
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_grabado))));
            }while (cur.moveToNext());
        }
        cur.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nuevo:
                this.mostrarTransacciones();
                break;
            case R.id.action_enviar:
                android.support.v7.app.AlertDialog.Builder quitDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
                quitDialog.setTitle("Seguro deseas enviar todos los pedidos no enviados?");
                quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    Toast.makeText(getContext(),"Operacion no permitida",Toast.LENGTH_SHORT).show();
                    }
                });

                quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                });
                quitDialog.show();
                break;
            case R.id.action_eliminar:
                android.support.v7.app.AlertDialog.Builder quitDialog2 = new android.support.v7.app.AlertDialog.Builder(getActivity());
                quitDialog2.setTitle("Seguro deseas eliminar todos los pedidos?");
                quitDialog2.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        EliminarTransaccionesTask asynDel = new EliminarTransaccionesTask();
                        asynDel.execute();
                    }
                });
                quitDialog2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                quitDialog2.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Transaccion> filter(List<Transaccion> models, String query) {
        query = query.toLowerCase();
        final List<Transaccion> filteredModelList = new ArrayList<>();
        for (Transaccion model : models) {
            final String cliente = model.getCliente_id().toLowerCase();
            final String trans = model.getReferencia().toLowerCase();
            if (cliente.contains(query)||trans.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    private List<Transaccion> filterEstado(List<Transaccion> models, int estado) {
        final List<Transaccion> filteredModelList = new ArrayList<>();
        switch (estado){
            case 2:
                for (Transaccion model : models)
                    filteredModelList.add(model);
                break;
            default:
                for (Transaccion model : models) {
                    final int est = model.getBand_enviado();
                    if(est==estado) {
                        filteredModelList.add(model);
                    }
                }
                break;
        }
        return filteredModelList;
    }
    /*Cargar listado de transacciones*/
    public void mostrarTransacciones(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Transacción Disponible");
        final String []gntr = listadoNombresFormas(grupo,empresa,catalogo);
        alertDialogBuilder.setSingleChoiceItems(gntr, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialogBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(gntr.length>0){
                    int selectedPosition = ((android.support.v7.app.AlertDialog) dialog).getListView().getCheckedItemPosition();
                    Intent intent = new Intent(getContext(),VisitaY.class);
                    intent.putExtra("transaccion",gntr[selectedPosition]);
                    intent.putExtra("identificador",catalogo);
                    intent.putExtra("accesos", accesos);
                    intent.putExtra("opcion", opcion);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast ts= Toast.makeText(getActivity(),R.string.info_empty_trans,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public String [] listadoNombresFormas(String grupo,String emp,String trans){
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarPermisos(grupo, emp, trans, false, true,null);
        String [] array=new String[cursor.getCount()];
        int count = 0;
        if(cursor.moveToFirst()){
            do{
                array[count]= cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_codtrans));
                count++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        helper.close();
        return array;
    }

    /*
    Clase para eliminar transacciones en grupo
     */
    /*Tarea Asincrona*/
    class EliminarTransaccionesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(getActivity());
            progress.setCancelable(false);
            progress.setTitle("Eliminando Transacciones");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    consultarTransacciones();
                    mAdapter.setFilter(transacciones);
                    countTrans.setText(String.valueOf(mAdapter.getItemCount()));
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result;
            try{
                DBSistemaGestion helper = new DBSistemaGestion(getActivity());
                Cursor cursor = helper.consultarTransacciones(accesos, 0, catalogo,orden,comodin);
                if (cursor.moveToFirst()){
                    do{
                        helper.eliminarIVKardexTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                        //Eliminado pckcardex
                        helper.eliminarPCKardexTrans(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                        //Eliminado transaccion
                        helper.eliminarTransaccion(cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                    }while (cursor.moveToNext());
                }
                Thread.sleep(500);
                helper.close();
                result= true;
            }catch (Exception e){
                e.printStackTrace();
                result = false;
            }
            return result;
        }
    }
}
