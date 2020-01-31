package ibzssoft.com.recibir;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.PedidoPendiente;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirPedidosPendientes {
    private Context context;
    private String ultMod;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String [] accesos;
    private boolean conf_download;

    public RecibirPedidosPendientes(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_pedidos_pendientes),context.getString(R.string.pref_ws_pedidos_pendientes));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_clientes),context.getString(R.string.pref_sinc_clientes_default));
        accesos=extraerConfiguraciones.get(context.getString(R.string.key_act_acc),context.getString(R.string.pref_act_acc_default)).split(",");
        conf_download=extraerConfiguraciones.getBoolean(context.getString(R.string.key_conf_kardex_transaccion),false);
    }


    public void ejecutartarea(){
        RecibirPedidosPendientesTask taskRecibirDescuentos= new RecibirPedidosPendientesTask();
        taskRecibirDescuentos.execute();
    }

    private class RecibirPedidosPendientesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
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
            DBSistemaGestion helper = new DBSistemaGestion(context);
            Boolean result=false;
            String fecha1=ultMod.replace(" ", "%20");
            for(int i=0; i<accesos.length; i++){
                HttpParams httpParameters = new BasicHttpParams();
                int timeoutConnection = 30000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                int timeoutSocket = 30000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                HttpClient httpClient = new DefaultHttpClient(httpParameters);
                httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+accesos[i]);
                System.out.println("Solicitando pedidos pendientes: "+"http://"+ip+":"+port+url+ws+"/"+accesos[i]);
                del.setHeader("content-type", "application/json");
                try
                {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    JSONArray respJSON = new JSONArray(respStr);
                    Gson gson= new Gson();
                    progress.setMax(respJSON.length());
                    progress.setProgress(0);
                    int count=0;
                    if(respJSON.length()>0){
                        helper.vaciarPedidoPendiente();
                        for(int j=0; j<respJSON.length(); j++)
                        {
                            JSONObject obj = respJSON.getJSONObject(j);
                            PedidoPendiente cli=gson.fromJson(obj.toString(), PedidoPendiente.class);
                            count++;
                            helper.crearPedidoPendiente(cli);
                            publishProgress(count);
                        }
                    }
                    result=true;
                }
                catch(Exception ex)
                {
                    Log.e("ServicioRest", "Error!", ex);
                    result=false;
                }
            }
            helper.close();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    if(conf_download){
                        RecibirKardexTransaccion recibirKardexTransaccion= new RecibirKardexTransaccion(context);
                        recibirKardexTransaccion.ejecutartarea();
                    }else Toast.makeText(context, R.string.info_success_import,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
