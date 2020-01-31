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
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirIVGrupo2 {
    private Context context;
    private String ultMod;
    private String ip;
    private String port;
    private String url;
    private String ws;


    public RecibirIVGrupo2(Context context) {
        this.context = context;
        cargarPreferenciasConexion();

    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_ivg2),context.getString(R.string.pref_ws_ivg2));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_productos),context.getString(R.string.pref_sinc_productos_default));
    }

    public void ejecutartarea(){
        RecibirIVGrupo2sTask taskRecibirIVGrupo2s= new RecibirIVGrupo2sTask();
        taskRecibirIVGrupo2s.execute();
    }

    private class RecibirIVGrupo2sTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Grupo 2");
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
            DBSistemaGestion helper= new DBSistemaGestion(context);
            Boolean result=false;
            String fecha1=ultMod.replace(" ","%20");
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
                    IVGrupo2 cli=gson.fromJson(obj.toString(), IVGrupo2.class);
                    count++;
                    publishProgress(count);
                    if(helper.existeIVGrupo2(cli.getIdgrupo2())){
                        helper.modificarIVGrupo2(cli);
                    }else{helper.crearIVGrupo2(cli);}
                    System.out.println("IVGrupo2 recibido: "+cli.toString());
                }
                helper.close();
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
                     RecibirIVGrupo3 recibirIVGrupo3 = new RecibirIVGrupo3(context);
                    recibirIVGrupo3.ejecutartarea();
                 }
            }
        }
    }
}
