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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ibzssoft.com.adaptadores.ComodinSpinnerAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.adaptadores.FilterSpinnerAdapter;
import ibzssoft.com.adaptadores.SortSpinnerAdapter;
import ibzssoft.com.adaptadores.TransOfertaAdapter;
import ibzssoft.com.adaptadores.ValidateReferencia;
import ibzssoft.com.enviar.IVKardex_Serialize_Envio;
import ibzssoft.com.enviar.PKardex_Envio;
import ibzssoft.com.enviar.Transaccion_Serialize_Envio;
import ibzssoft.com.ishidamovile.CONST;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.oferta.crear.NuevaOfertaG;
import ibzssoft.com.ishidamovile.oferta.crear.NuevaOfertaJM;
import ibzssoft.com.ishidamovile.oferta.crear.NuevaOfertaM;
import ibzssoft.com.ishidamovile.oferta.screens.CodigosOferta;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.recibir.RecibirOfertaAprobacion;
import ibzssoft.com.storage.DBSistemaGestion;

public class TabOfertaAll extends Fragment implements SearchView.OnQueryTextListener  {
    private RecyclerView mRecyclerView;
    private TransOfertaAdapter mAdapter;
    private ArrayList<Transaccion> transacciones;
    private TextView countTrans;
    private String []accesos;
    private int opcion,orden,comodin;
    private static final String catalogo="OFERTA";
    private String empresa,grupo,ip,port,url,ws,ultmod;
    private boolean import_of;
    private Spinner filterSpinner;
    private Spinner sortSpinner;
    private Spinner comodinSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_oferta_all, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        countTrans= (TextView) view.findViewById(R.id.textCountOfertas);
        this.filterSpinner= (Spinner) view.findViewById(R.id.filter_spinner);
        this.sortSpinner= (Spinner) view.findViewById(R.id.sort_spinner);
        this.comodinSpinner= (Spinner) view.findViewById(R.id.comodin_spinner);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        this.extraerParametros();
        return view;
    }

    public void extraerParametros(){
        this.accesos= getArguments().getString("accesos").split(",");
        this.empresa= getArguments().getString("empresa");
        this.grupo= getArguments().getString("grupo");
        this.ip= getArguments().getString("ip");
        this.port= getArguments().getString("port");
        this.url= getArguments().getString("url");
        this.opcion=getArguments().getInt("opcion");
        this.orden=getArguments().getInt("ord_cat_trans");
        this.comodin=getArguments().getInt("ord_cat_trans_comodin");
        ExtraerConfiguraciones ext= new ExtraerConfiguraciones(getActivity());
        this.import_of=ext.getBoolean(getString(R.string.key_conf_import_trans),false);
        this.ws=ext.get(getString(R.string.key_ws_clientes_all),getString(R.string.pref_ws_clientes_all));
        this.ultmod=ext.get(getString(R.string.key_sinc_clientes),getString(R.string.pref_sinc_clientes_default));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        transacciones = new ArrayList<>();
        this.consultarTransacciones();
        mAdapter = new TransOfertaAdapter(getActivity(), this.transacciones,this.empresa,this.opcion,this.import_of);
        countTrans.setText(String.valueOf(mAdapter.getItemCount()));
        mRecyclerView.setAdapter(mAdapter);
        prepareFilterSpinner();
        prepareSortSpinner();
        prepareComodinSpinner();
    }

    private void prepareFilterSpinner() {
        FilterSpinnerAdapter filterSpinnerAdapter = new FilterSpinnerAdapter(getActivity());
        filterSpinner.setAdapter(filterSpinnerAdapter);
        filterSpinner.setOnItemSelectedListener(null);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void prepareComodinSpinner() {
        ComodinSpinnerAdapter comodinSpinnerAdapter = new ComodinSpinnerAdapter(getActivity());
        comodinSpinner.setAdapter(comodinSpinnerAdapter);
        comodinSpinner.setOnItemSelectedListener(null);
        comodinSpinner.setSelection(comodin);
        comodinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.animateTo(sortList(transacciones, sortSpinner.getSelectedItemPosition(),position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void prepareSortSpinner() {
        SortSpinnerAdapter filterSpinnerAdapter = new SortSpinnerAdapter(getActivity());
        sortSpinner.setAdapter(filterSpinnerAdapter);
        sortSpinner.setOnItemSelectedListener(null);
        sortSpinner.setSelection(orden);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.animateTo(sortList(transacciones, position,comodinSpinner.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem register = menu.findItem(R.id.action_aprobacion);
        if(import_of)register.setVisible(true);
        else register.setVisible(false);

    }

    public void consultarTransacciones(){
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarTransacciones(accesos,0,catalogo,orden,comodin);
        cargarListado(cursor);
        helper.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tab_oferta, menu);
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
                Transaccion trans = new Transaccion(
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
                        cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_grabado)));
                trans.setFecha_envio(cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_envio)));
                transacciones.add(trans);
            }while (cur.moveToNext());
        }
        cur.close();
    }

    /*
0. Todos
1. Cartera
2. Descuentos
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nuevo:
                    this.mostrarTransacciones();
                break;
            case R.id.action_enviar:
                android.support.v7.app.AlertDialog.Builder quitDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
                quitDialog.setTitle("Seguro deseas enviar todas las ofertas no enviadas?");
                quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        EnviarTransacciones enviarTransacciones= new EnviarTransacciones(getActivity());
                        enviarTransacciones.ejecutarTarea();
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
                quitDialog2.setTitle("Seguro deseas eliminar todas las ofertas?");
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
            case R.id.action_aprobacion:

                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Advertencia");
                alertDialogBuilder
                        .setMessage("Esta seguro de importar todas las ofertas no aprobadas desde Sii4A?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RecibirClientesTask recibirClientesTask= new RecibirClientesTask();
                                recibirClientesTask.execute();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
    private List<Transaccion> sortList(List<Transaccion> models, int sort, final int criterio) {
        System.out.println("Ordenando por: "+sort+" creiterio: "+criterio);
        switch (sort){
            case 0: //ordenar por nro de transaccion
                Collections.sort(models, new Comparator<Transaccion>() {
                    @Override
                    public int compare(Transaccion transaccion, Transaccion t1) {
                        if(criterio!=0){
                            return new Integer(transaccion.getNumTransaccion()).compareTo(new Integer(t1.getNumTransaccion()));//ascendente
                        }else return new Integer(t1.getNumTransaccion()).compareTo(new Integer(transaccion.getNumTransaccion()));//descendente
                    }
                });
                break;
            case 1: //ordenar por nombre de cliente
                Collections.sort(models, new Comparator<Transaccion>() {
                    @Override
                    public int compare(Transaccion transaccion, Transaccion t1) {
                        if(criterio!=0){
                            return transaccion.getCliente_id().compareTo(t1.getCliente_id());//ascendente
                        }else return t1.getCliente_id().compareTo(transaccion.getCliente_id());//descendente
                    }
                });
                break;
            case 2: //ordenar por codigo transaccion
                Collections.sort(models, new Comparator<Transaccion>() {
                    @Override
                    public int compare(Transaccion transaccion, Transaccion t1) {
                        if(criterio!=0){
                            return transaccion.getReferencia().compareTo(t1.getReferencia());//ascendente
                        }else return t1.getReferencia().compareTo(transaccion.getReferencia());//descendente
                    }
                });
                break;
            case 3: //ordenar por fecha de envio
                System.out.println("Ordenando por fecha de envio: "+comodin);
                Collections.sort(models, new Comparator<Transaccion>() {
                    @Override
                    public int compare(Transaccion transaccion, Transaccion t1) {
                        if(criterio!=0){
                            return transaccion.getFecha_envio().compareTo(t1.getFecha_envio());//ascendente
                        }else return t1.getFecha_envio().compareTo(transaccion.getFecha_envio());//descendente
                    }
                });
                break;
            case 4: //ordenar por fecha de grabado
                Collections.sort(models, new Comparator<Transaccion>() {
                    @Override
                    public int compare(Transaccion transaccion, Transaccion t1) {
                        if(criterio!=0){
                            return transaccion.getFecha_grabado().compareTo(t1.getFecha_grabado());//ascendente
                        }else return t1.getFecha_grabado().compareTo(transaccion.getFecha_grabado());//descendente
                    }
                });
                break;
        }
        return models;
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
                    int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                    CodigosOferta gno = CodigosOferta.valueOf(empresa.toUpperCase());
                    Intent intent;
                    switch (gno){
                        case CONSGYP2015: intent = new Intent(getContext(),NuevaOfertaG.class);break;
                        case CONSGYP2016: intent = new Intent(getContext(),NuevaOfertaG.class);break;
                        case CONSGYP2018: intent = new Intent(getContext(),NuevaOfertaG.class);break;
                        case CONSGYP2019: intent = new Intent(getContext(),NuevaOfertaG.class);break;
                        case CONSGYP2020: intent = new Intent(getContext(),NuevaOfertaG.class);break;
                        case JM2012: intent = new Intent(getContext(),NuevaOfertaJM.class);break;
                        case JM2013: intent = new Intent(getContext(),NuevaOfertaJM.class);break;
                        case JM2014: intent = new Intent(getContext(),NuevaOfertaJM.class);break;
                        case JM2015: intent = new Intent(getContext(),NuevaOfertaJM.class);break;
                        case JM2016: intent = new Intent(getContext(),NuevaOfertaJM.class);break;
                        default:intent = new Intent(getContext(),NuevaOfertaM.class);break;
                    }
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
    /*Cargar clase para enviar transacciones en grupo*/
    class EnviarTransacciones {
        private Context context;
        private String ip;
        private String port;
        private String url;
        private String ws;
        private String base;
        private String codemp;

        public EnviarTransacciones(Context context) {
            this.context = context;
            cargarPreferenciasConexion();
        }
        public void cargarPreferenciasConexion(){
            ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
            ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
            port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
            url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
            ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_transacciones),context.getString(R.string.pref_ws_transacciones));
            base=extraerConfiguraciones.get(context.getString(R.string.key_empresa_base),context.getString(R.string.pref_base_empresa_default));
            codemp=extraerConfiguraciones.get(context.getString(R.string.key_empresa_codigo),context.getString(R.string.pref_codigo_empresa_default));
        }

        public void ejecutarTarea(){
            EnviarDetallesTransaccionTask async1=new EnviarDetallesTransaccionTask();
            async1.execute();
        }
        /*Tarea Asincrona*/
        class EnviarDetallesTransaccionTask extends AsyncTask<String,Integer,String> {
            private ProgressDialog progress;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress=new ProgressDialog(context);
                progress.setCancelable(false);
                progress.setTitle("Enviando Transacciones");
                progress.setMessage("Espere...");
                progress.show();
            }

            @Override
            protected void onPostExecute(String  response) {
                super.onPostExecute(response);
                if(progress.isShowing()){
                    progress.dismiss();
                    consultarTransacciones();
                    mAdapter.setFilter(transacciones);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String result=null;
                /*Consulta de transacciones no enviadas*/
                for(Transaccion trans: transacciones){
                    if(trans.getBand_enviado()==0){
                        HttpParams httpParameters = new BasicHttpParams();
                        int timeoutConnection = 5000;
                        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                        int timeoutSocket = 5000;
                        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                        HttpClient httpClient = new DefaultHttpClient(httpParameters);
                        httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                        HttpGet get = new HttpGet("http://"+ip+":"+port+url+ws+"/"+ generarTramaEnvio(trans.getId_trans()));
                        get.setHeader("content-type", "application/json");
                        try {
                            HttpResponse resp = httpClient.execute(get);
                            String respStr = EntityUtils.toString(resp.getEntity());
                            //System.out.println("Respuesta transaccion: " + respStr);
                            JSONObject obj = new JSONObject(respStr);
                            String response = obj.getString("estado");
                            if (new ValidateReferencia().validate(response)) {
                                modificarTransaccion(trans.getId_trans(),response);
                            }else {
                                Toast.makeText(context,"No es posible enviar la transaccion",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                return result;
            }
        }
        public boolean modificarTransaccion(String trans,String referencia){
            DBSistemaGestion helper= new DBSistemaGestion(context);
            helper.marcarTransaccionComoEnviado(trans,referencia);
            helper.close();
            return true;
        }

        public String generarTramaEnvio(String idtransaccion){
            String trama = "";
            try{
                List<IVKardex_Serialize_Envio> array1 = new ArrayList();
                List<PKardex_Envio> array2= new ArrayList();
                List<PCKardex> array3= new ArrayList();

                DBSistemaGestion helper= new DBSistemaGestion(context);
                Cursor cursor=helper.consultarIVKardex(idtransaccion);
                if(cursor.moveToFirst()){
                    do{
                        IVKardex_Serialize_Envio ivKardex= new IVKardex_Serialize_Envio(
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cantidad)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_precio_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_bodega_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_inventario_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)),0);
                        System.out.println("Cargando IVKArdex: "+ivKardex.toString());
                        array1.add(ivKardex);
                    }while (cursor.moveToNext());
                }

                Cursor cursor1=helper.consultarPCKardex(idtransaccion);
                if(cursor1.moveToFirst()){
                    do{
                        PKardex_Envio pcka= new PKardex_Envio();
                        PCKardex pck= new PCKardex();
                        pcka.setId_asignado(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idasignado)));
                        pcka.setTsf_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_codforma)));
                        pcka.setValor_cancelado(cursor1.getDouble(cursor1.getColumnIndex(PCKardex.FIELD_pagado)));
                        pcka.setObservacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_observacion)).replace("/",":"));

                        pck.setBanco_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_banco_id)));
                        pck.setForma_pago(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_forma_pago)));
                        pck.setTitular(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_titular)));
                        pck.setNumero_cuenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_numero_cuenta)));
                        pck.setIva(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva)));
                        pck.setIva_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva_base)));
                        pck.setRenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta)));
                        pck.setRenta_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta_base)));
                        pck.setNum_ser_estab(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                        pck.setNum_ser_punto(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_punto)));
                        pck.setNum_ser_secuencial(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                        pck.setAutorizacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_autorizacion)));
                        pck.setCaducidad(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_caducidad)));
                        pck.setIdvendedor(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idvendedor)));
                        System.out.println("Cargando PCKArdex: "+pcka.toString());
                        array2.add(pcka);
                        array3.add(pck);
                    }while (cursor1.moveToNext());
                }
                cursor.close();
                cursor1.close();
                helper.close();
                /*Preparando Objeto para el envio*/
                Transaccion_Serialize_Envio trans_send = new Transaccion_Serialize_Envio();
                Cursor cursor2=helper.obtenerTransaccion(idtransaccion);
                if(cursor2.moveToFirst()){
                    trans_send.setId_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                    trans_send.setIdentificador(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_identificador)));
                    trans_send.setNumTransaccion(cursor2.getInt(cursor2.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                    trans_send.setFecha_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_fecha_trans)));
                    trans_send.setHora_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_hora_trans)));
                    trans_send.setCliente_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_cliente_id)));
                    trans_send.setForma_pago(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_forma_cobro_id)));
                    trans_send.setDescripcion(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_descripcion)));
                    trans_send.setVendedor_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_vendedor_id)));
                }
                cursor2.close();
                if(array3.size()>0){
                    trans_send.setBanco_id(array3.get(0).getBanco_id());
                    trans_send.setForma_pago(array3.get(0).getForma_pago());
                    trans_send.setTitular(array3.get(0).getTitular());
                    trans_send.setNumero_cheque(array3.get(0).getNumero_cheque());
                    trans_send.setNumero_cuenta(array3.get(0).getNumero_cuenta());
                    trans_send.setCheque_fecha_vencimiento(array3.get(0).getPago_fecha_vencimiento());
                    trans_send.setIva(array3.get(0).getIva());
                    trans_send.setIva_base(array3.get(0).getIva_base());
                    trans_send.setRenta(array3.get(0).getRenta());
                    trans_send.setRenta_base(array3.get(0).getRenta_base());
                    trans_send.setEstablecimiento(array3.get(0).getNum_ser_estab());
                    trans_send.setPunto(array3.get(0).getNum_ser_punto());
                    trans_send.setSecuencial(array3.get(0).getNum_ser_secuencial());
                    trans_send.setAutorizacion(array3.get(0).getAutorizacion());
                    trans_send.setCaducidad(array3.get(0).getCaducidad());
                }
                trans_send.setIvkardex(array1);
                trans_send.setPckardex(array2);
                trama = "TRANSACCION;"+base+";trama1;2016-01-01%2012:00:00;"+codemp+";"+trans_send.toString();
                System.out.println("Trama para envio: "+trama);
                trama = URLEncoder.encode(trama,"UTF-8");
            }catch (UnsupportedEncodingException ex){
                ex.printStackTrace();
            }
            return trama;
        }
    }
    /*Fin clase para enviar en grupo*/
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
    /**
     * Tarea asincrona para recibir clientes modificados
     */
    private class RecibirClientesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(getActivity());
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Actualizando Clientes");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
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
            Boolean result=false;
            String fecha1 =ultmod.replace(" ", "%20");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            //Se agrega un campo adicional que es la clave para desencriptar informacion 22/04/2020
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+fecha1+"/"+CONST.CLAVE_DESENCRIPTAR);
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                progress.setMax(respJSON.length());
                int count=0;
                for(int j=0; j<respJSON.length(); j++)
                {
                    JSONObject obj = respJSON.getJSONObject(j);
                    Cliente cli=gson.fromJson(obj.toString(), Cliente.class);
                    if(helper.existeCliente(cli.getIdprovcli())){
                        helper.modificarCliente(cli);
                    }else helper.crearCliente(cli);
                    Thread.sleep(500);
                    count++;
                    publishProgress(count);
                    System.out.println("Cliente todo single recibido: "+cli.toString());
                }
                result=true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                result=false;
            }
            helper.close();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    ExtraerConfiguraciones extraerConfiguraciones=new ExtraerConfiguraciones(getActivity());
                    extraerConfiguraciones.update(getActivity().getString(R.string.key_sinc_clientes),new ParseDates().getNowDateString());
                    EliminarTransaccionesAprobacionTask taskDel= new EliminarTransaccionesAprobacionTask();
                    taskDel.execute();
                }
            }
        }
    }
    /**
     * Tarea asincrona para eliminar transacciones y recibir nuevas para aprobacion
     */
    class EliminarTransaccionesAprobacionTask extends AsyncTask<String,Integer,Boolean> {
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
                    RecibirOfertaAprobacion recibirOfertaAprobacion= new RecibirOfertaAprobacion(getActivity(),opcion);
                    recibirOfertaAprobacion.ejecutartarea();
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
