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
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.Grupo;

/**
 * Created by root on 18/11/15.
 */
public class RecibirGrupos {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String ultMod;
    public RecibirGrupos(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_grupos),context.getString(R.string.pref_ws_grupos));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_inicial),context.getString(R.string.pref_sinc_inicial_default));
    }
    public void ejecutartarea(){
        RecibirGruposTask taskRecibirGrupos= new RecibirGruposTask();
        taskRecibirGrupos.execute();
    }

    private class RecibirGruposTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Grupos");
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
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
            String fecha1=ultMod.replace(" ", "%20");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+fecha1);
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                progress.setMax(respJSON.length());
                int count=0;
                if(respJSON.length()>0){
                    for(int i=0; i<respJSON.length(); i++)
                    {
                        JSONObject obj = respJSON.getJSONObject(i);
                        Grupo gru=gson.fromJson(obj.toString(), Grupo.class);
                        if(helper.existeGrupo(gru.getCodgrupo())){
                            helper.modificarGrupo(gru);
                        }else{
                            helper.crearGrupo(gru);
                        }
                        count++;
                        publishProgress(count);
                        System.out.println("Grupo recibido: "+gru.toString());
                    }
                }
                helper.close();
                Thread.sleep(100);
                    result=true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    RecibirPermisos recibirPermisos= new RecibirPermisos(context);
                    recibirPermisos.ejecutartarea();
                }
            }
        }
    }
}
