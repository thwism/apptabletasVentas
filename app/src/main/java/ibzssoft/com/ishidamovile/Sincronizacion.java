package ibzssoft.com.ishidamovile;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Arrays;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.eliminar.EliminarIVGrupo1;
import ibzssoft.com.eliminar.EliminarPCGrupo1;
import ibzssoft.com.modelo.Actualizacion;
import ibzssoft.com.recibir.RecibirDescuentoRecargo;
import ibzssoft.com.recibir.RecibirFacturas;
import ibzssoft.com.recibir.RecibirIVGrupo1;
import ibzssoft.com.enviar.UploadImage;
import ibzssoft.com.recibir.RecibirImagenes;
import ibzssoft.com.recibir.RecibirPCGrupo1;

public class Sincronizacion extends Fragment {
    boolean estado=false;
    private ListView sincronizaciones;
    private CoordinatorLayout cordinator;
    private SincronizacionListAdapter adaptador;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String sinc_clientes;
    private String sinc_productos;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_sincronizacion, container, false);
        this.cargarPreferenciasCatalogo();
        setHasOptionsMenu(true);
        sincronizaciones=(ListView)view.findViewById(R.id.listadoSincronizaciones);
        cordinator=(CoordinatorLayout) view.findViewById(R.id.coordinator);
        adaptador= new SincronizacionListAdapter(getActivity(),llenarListado());
        sincronizaciones.setAdapter(adaptador);
        TareaProbarConexion taskTestConection= new TareaProbarConexion();
        taskTestConection.execute(ip, port,url);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sincronizacion, menu);
    }

    public void cargarPreferenciasCatalogo(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(getActivity());
        ip=extraerConfiguraciones.get(getString(R.string.key_conf_ip),getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(getString(R.string.key_conf_port),getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(getString(R.string.key_conf_url),getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(getString(R.string.key_ws_test),getString(R.string.pref_ws_test));
        sinc_clientes= extraerConfiguraciones.get(getString(R.string.key_sinc_clientes),getString(R.string.pref_sinc_clientes_default));
        sinc_productos= extraerConfiguraciones.get(getString(R.string.key_sinc_productos),getString(R.string.pref_sinc_productos_default));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_vaciar_clientes:
                android.support.v7.app.AlertDialog.Builder quitDialog
                        = new android.support.v7.app.AlertDialog.Builder(getActivity());
                quitDialog.setTitle("Esta seguro de vaciar el catálogo de clientes?");

                quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EliminarPCGrupo1 eliminarPCGrupo1= new EliminarPCGrupo1(getActivity());
                        eliminarPCGrupo1.ejecutarTarea();
                    }});

                quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }});

                quitDialog.show();
                return true;
            case R.id.action_vaciar_items:
                android.support.v7.app.AlertDialog.Builder quitDialog2
                        = new android.support.v7.app.AlertDialog.Builder(getActivity());
                quitDialog2.setTitle("Esta seguro de vaciar el catálogo de productos?");

                quitDialog2.setPositiveButton("Si", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        EliminarIVGrupo1 eliminarIVGrupo1= new EliminarIVGrupo1(getActivity());
                        eliminarIVGrupo1.ejecutarTarea();
                    }});

                quitDialog2.setNegativeButton("No", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }});

                quitDialog2.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    public ArrayList<Actualizacion> llenarListado(){
        ArrayList<Actualizacion>acts=new ArrayList<>(Arrays.asList(
                new Actualizacion("Sinc. Clientes",sinc_clientes),
                new Actualizacion("Sinc. Productos",sinc_productos)
        ));
        return acts;
    }


    private class TareaProbarConexion extends AsyncTask<String,Float,Boolean> {
        @Override
        protected Boolean doInBackground(String... urls) {
            Boolean result = false;
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet get = new HttpGet("http://" + ip+ ":" +port+ url+ ws);
            System.out.println("http://" + ip+ ":" +port+ url+ ws);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject obj = new JSONObject(respStr);
                System.out.println("Response: "+respStr);
                int response = obj.getInt("estado");

                if (response == 1) {
                    result = true;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (!s) {
                estado = false;
                Snackbar.make(cordinator, "No es posible conectarse ahora", Snackbar.LENGTH_INDEFINITE).setAction("Reintentar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TareaProbarConexion taskTestConection= new TareaProbarConexion();
                        taskTestConection.execute(ip, port, url);
                    }
                }).show();
                //PENDIENTE
            } else estado = true;
        }
    }
    /*
    Adaptador de lista
     */

    public class SincronizacionListAdapter extends ArrayAdapter<Actualizacion> {

        public SincronizacionListAdapter(Context context, ArrayList<Actualizacion> acts) {
            super(context, 0, acts);
        }

        public void update(ArrayList<Actualizacion> acts){
            clear();
            addAll(acts);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Actualizacion act = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.fila_sincronizacion, parent, false);
            }
            TextView actModulo = (TextView) convertView.findViewById(R.id.text1);
            TextView actFecha = (TextView) convertView.findViewById(R.id.text2);
            actModulo.setText(act.getModulo());
            actFecha.setText(act.getFecha());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                                if(estado){
                                    //RecibirImagenes recibirImagenes= new RecibirImagenes(getContext());
                                    //recibirImagenes.ejecutartarea();
                                     RecibirPCGrupo1 init1= new RecibirPCGrupo1(getContext());
                                     init1.ejecutartarea();
                                    //RecibirFacturas recargos= new RecibirFacturas(getContext(),"FC-8879","RICARDO ORELLANA","0106858210001","ISHIDA&ASOCIDOS","AV. ORDOÑEZ LASSO","1990-01-01 12:00:00");
                                    //recargos.ejecutartarea();
                                }
                            break;
                        case 1:
                                if(estado){
                                    RecibirIVGrupo1 init2= new RecibirIVGrupo1(getContext());
                                    init2.ejecutartarea();
                                }
                            break;
                    }
                    adaptador.update(llenarListado());
                }
            });
            return convertView;
        }
    }

}
