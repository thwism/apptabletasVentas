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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.Promocion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirPromociones {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;

    public RecibirPromociones(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_promociones),context.getString(R.string.pref_ws_promociones));
    }


    public void ejecutartarea(){
        RecibirPromocionsTask taskRecibirPromocions= new RecibirPromocionsTask();
        taskRecibirPromocions.execute();
    }

    private class RecibirPromocionsTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Promociones");
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
            Boolean result=false;
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            System.out.println("consultando promocion: "+"http://"+ip+":"+port+url+ws);
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
                if(respJSON.length()>0){
                    helper.vaciarPromocion();
                    for(int i=0; i<respJSON.length(); i++)
                    {
                        JSONObject obj = respJSON.getJSONObject(i);
                        Promocion cli=gson.fromJson(obj.toString(), Promocion.class);
                        helper.crearPromocion(cli);
                        publishProgress(count);
                        System.out.println("Promocion recibida: "+cli.toString());
                        count++;
                    }
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
                    /*ExtraerConfiguraciones extraerConfiguraciones=new ExtraerConfiguraciones(context);
                    extraerConfiguraciones.update(context.getString(R.string.key_sinc_productos),new ParseDates().getNowDateString());
                    Toast ts= Toast.makeText(context, R.string.info_success_import,Toast.LENGTH_SHORT);
                    ts.show();*/
                    RecibirImagenes recibirImagenes = new RecibirImagenes(context);
                    recibirImagenes.ejecutartarea();
                }
            }
        }
    }
}
