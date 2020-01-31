package ibzssoft.com.enviar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 19/04/16.
 */
public class EnviarUbicacion {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String idcliente;
    private String coordenadas;

    public EnviarUbicacion(String idcliente, String coordenadas,  Context context) {
        this.context=context;
        this.idcliente=idcliente;
        this.coordenadas=coordenadas;
        cargarPreferenciasConexion();
    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_ubicacion_gps),context.getString(R.string.pref_ws_ubicacion_gps));
    }
    public boolean ejecutarTarea(){
        EnviarUbicacionGPS sendTransTask= new EnviarUbicacionGPS();
        sendTransTask.execute(idcliente,coordenadas);
        return true;
    }

    private class EnviarUbicacionGPS extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Enviando Ubicacion GPS");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost del = new HttpPost("http://"+ip+":"+port+url+ws);
            JsonObject innerObject = new JsonObject();
            innerObject.addProperty("idprovcli", params[0]);
            innerObject.addProperty("coordenadas", params[1]);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            del.setHeader("Accept", "application/json");
            del.setHeader("Content-type", "application/json");

            try
            {
                StringEntity se = new StringEntity(innerObject.toString());
                del.setEntity(se);
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("Respuesta transaccion: "+respStr);
                JSONObject obj = new JSONObject(respStr);
                int response = obj.getInt("estado");
                if(response==1) {
                    result = true;
                }
                helper.close();
                Thread.sleep(1000);
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
                    new Alertas(context,"Datos exportados correctamente", "La ubicacion GPS se ha exportado al sistema Sii4A").mostrarMensaje();
                }
            }
        }
    }

}