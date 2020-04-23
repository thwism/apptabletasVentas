package ibzssoft.com.recibir;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.CONST;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirCarteraCobrador {
    private Context context;
    private String [] accesos;
    private String ip;
    private String port;
    private String url;
    private String ws;
    public RecibirCarteraCobrador(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_cartera_cobrador),context.getString(R.string.pref_ws_cartera_cobrador));
        accesos=extraerConfiguraciones.get(context.getString(R.string.key_act_acc),context.getString(R.string.pref_act_rut_default)).split(",");
    }
    public void ejecutartarea(){
        RecibirClientesTask taskRecibirClientes= new RecibirClientesTask();
        taskRecibirClientes.execute();
    }

    private class RecibirClientesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        int nItems=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Cartera por Cobrador");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.setProgress(0);
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
            helper.vaciarCartera();
            for(int i=0;i<accesos.length;i++){
                HttpClient httpClient = new DefaultHttpClient();
                System.out.println("solicitando cartera cobrador: "+"http://"+ip+":"+port+url+ws+"/"+accesos[i]);
                httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                //Se agrega un campo adicional que es la clave para desencriptar informacion 22/04/2020
                HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+accesos[i]+"/"+CONST.CLAVE_DESENCRIPTAR);
                del.setHeader("content-type", "application/json");
                try
                {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    JSONArray respJSON = new JSONArray(respStr);
                    Gson gson= new Gson();
                    nItems=respJSON.length();
                    progress.setMax(respJSON.length());
                    int count=0;
                    for(int j=0; j<respJSON.length(); j++)
                    {
                        JSONObject obj = respJSON.getJSONObject(j);
                        PCKardex pcKardex=gson.fromJson(obj.toString(), PCKardex.class);
                        count++;
                        publishProgress(count);
                        helper.crearPCKardex(pcKardex);
                        System.out.println("PCKardex recibido: "+pcKardex.toString());
                    }
                    result=true;
                }
                catch(Exception ex)
                {
                    Log.e("ServicioRest", "Error!", ex);
                    result=false;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    RecibirDescuentos recibirDescuentos = new RecibirDescuentos(context);
                    recibirDescuentos.ejecutartarea();
                }
            }
        }
    }
}
