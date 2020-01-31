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
import ibzssoft.com.modelo.PCGrupo2;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 28/22/25.
 */
public class RecibirPCGrupo2 {
    private Context context;
    private String ultMod;
    private String ip;
    private String port;
    private String ws;
    private String url;

    public RecibirPCGrupo2(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_pcg2),context.getString(R.string.pref_ws_pcg2));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_clientes),context.getString(R.string.pref_sinc_clientes_default));
    }

    public void ejecutartarea(){
        RecibirPCGrupo2sTask taskRecibirPCGrupo2s= new RecibirPCGrupo2sTask();
        taskRecibirPCGrupo2s.execute();
    }

    private class RecibirPCGrupo2sTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando PCGrupo 2");
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
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    PCGrupo2 per=gson.fromJson(obj.toString(), PCGrupo2.class);
                    if (helper.existePCGrupo2(per.getCodgrupo2())){
                        helper.modificarPCGrupo2(per);
                    } else helper.crearPCGrupo2(per);
                    System.out.println("PCGrpo2 recibido: "+per.toString());
                    count++;
                    publishProgress(count);
                }
                helper.close();
                result=true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    RecibirPCGrupo3 recibirPCGrupo3 = new RecibirPCGrupo3(context);
                    recibirPCGrupo3.ejecutartarea();
                }
            }
        }
    }
}
