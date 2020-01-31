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
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.KardexTransaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirKardexTransaccion {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String [] accesos;

    public RecibirKardexTransaccion(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void ejecutartarea(){
        RecibirDescuentosTask taskRecibirDescuentos= new RecibirDescuentosTask();
        taskRecibirDescuentos.execute();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_kardextransaccion),context.getString(R.string.pref_ws_kardextransaccion));
        accesos=extraerConfiguraciones.get(context.getString(R.string.key_act_acc),context.getString(R.string.pref_act_acc_default)).split(",");
    }

    private class RecibirDescuentosTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Cartera Cobrada");
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
            for(int i=0; i<accesos.length; i++){
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+accesos[i]);
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
                        helper.vaciarKardexTransaccion();
                        for(int j=0; j<respJSON.length(); j++)
                        {
                            JSONObject obj = respJSON.getJSONObject(j);
                            KardexTransaccion cli=gson.fromJson(obj.toString(), KardexTransaccion.class);
                            helper.crearKardexTransaccion(cli);
                            count++;
                            publishProgress(count);
                            System.out.println("Kardex Transaccion recibido: "+cli.toString());
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
                    Toast.makeText(context, "Descarga de Kardex Completada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
