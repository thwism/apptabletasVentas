package ibzssoft.com.recibir;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import ibzssoft.com.modelo.Provincia;
/**
 * Created by root on 18/11/15.
 */
public class RecibirProvincias {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String ultMod;
    private RecibirProvinciasTask taskRecibirProvincias;

    public RecibirProvincias(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_provincias),context.getString(R.string.pref_ws_provincias));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_inicial),context.getString(R.string.pref_sinc_inicial_default));
    }

    public void ejecutartarea(){
        taskRecibirProvincias= new RecibirProvinciasTask();
        taskRecibirProvincias.execute();
    }
    public AsyncTask.Status getStatus(){
        return taskRecibirProvincias.getStatus();
    }

    private class RecibirProvinciasTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Provincias");
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
            String fecha1=ultMod.replace(" ","%20");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            System.out.println("http://"+ip+":"+port+url+ws+"/"+fecha1);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+fecha1);
            del.setHeader("content-type", "application/json;charset = utf-8");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println(respStr);
                JSONArray respJSON = new JSONArray(respStr);
                progress.setMax(respJSON.length());
                Gson gson= new Gson();
                int count = 0;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Provincia per=gson.fromJson(obj.toString(), Provincia.class);
                    count++;
                    if(helper.existeProvincia(per.getIdprovincia())){
                        helper.modificarProvincia(per);
                        System.out.println("Provincia modificada: "+per.toString());
                    }else helper.crearProvincia(per);
                    publishProgress(count);
                }
                helper.close();
                Thread.sleep(50);
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
                    RecibirCanton recibirCanton= new RecibirCanton(context);
                    recibirCanton.ejecutartarea();
                }
            }
        }
    }
}
