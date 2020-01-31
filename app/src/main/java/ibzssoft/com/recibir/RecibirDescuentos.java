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
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.Sincronizacion;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.Descuento;

/**
 * Created by root on 18/11/15.
 */
public class RecibirDescuentos {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String [] accesos;

    public RecibirDescuentos(Context context) {
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
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_descuentos),context.getString(R.string.pref_ws_descuentos));
        accesos=extraerConfiguraciones.get(context.getString(R.string.key_act_acc),context.getString(R.string.pref_act_acc_default)).split(",");
    }

    private class RecibirDescuentosTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Descuentos");
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
                        helper.vaciarDescuentos(accesos[i]);
                        for(int j=0; j<respJSON.length(); j++)
                        {
                            JSONObject obj = respJSON.getJSONObject(j);
                            Descuento cli=gson.fromJson(obj.toString(), Descuento.class);
                            helper.crearDescuento(cli);
                            count++;
                            publishProgress(count);
//                            System.out.println("Descuento recibido: "+cli.toString());
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
                    ExtraerConfiguraciones extraerConfiguraciones=new ExtraerConfiguraciones(context);
                    extraerConfiguraciones.update(context.getString(R.string.key_sinc_clientes),new ParseDates().getNowDateString());
                    RecibirPedidosPendientes recibirPedidosPendientes= new RecibirPedidosPendientes(context);
                    recibirPedidosPendientes.ejecutartarea();
                }
            }
        }
    }
}
