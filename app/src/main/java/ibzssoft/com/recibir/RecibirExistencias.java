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
import ibzssoft.com.modelo.Existencia;
/**
 * Created by root on 18/11/15.
 */
public class RecibirExistencias {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;

    public RecibirExistencias(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_existencias),context.getString(R.string.pref_ws_existencias));
    }

    public void ejecutartarea(){
        RecibirExistenciasTask taskRecibirDescuentos= new RecibirExistenciasTask();
        taskRecibirDescuentos.execute();
    }

    private class RecibirExistenciasTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Existencias");
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
            DBSistemaGestion helper = new DBSistemaGestion(context);
            helper.vaciarExistencias();
            Boolean result=false;
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws);
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                progress.setMax(respJSON.length());
                int count=0;
                helper.vaciarExistencias();
                for(int j=0; j<respJSON.length(); j++)
                {
                    JSONObject obj = respJSON.getJSONObject(j);
                    Existencia cli=gson.fromJson(obj.toString(), Existencia.class);
                    count++;
                    helper.crearExistencia(cli);
                    publishProgress(count);
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
                    RecibirPromociones recibirPromociones = new RecibirPromociones(context);
                    recibirPromociones.ejecutartarea();
                }
            }
        }
    }
}
