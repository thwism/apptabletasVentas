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
import ibzssoft.com.modelo.IVInventario;

/**
 * Created by root on 18/11/15.
 */
public class RecibirProductos {
    private Context context;
    private String ultMod;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String [] lineas;

    public RecibirProductos(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_productos),context.getString(R.string.pref_ws_productos));
        lineas=extraerConfiguraciones.get(context.getString(R.string.key_act_lin),context.getString(R.string.pref_act_lin_default)).split(",");
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_productos),context.getString(R.string.pref_sinc_productos_default));
    }

    public void ejecutartarea(){
        RecibirProductosTask taskRecibirProductos= new RecibirProductosTask();
        taskRecibirProductos.execute();
    }

    private class RecibirProductosTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Productos");
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
            String fecha1=ultMod.replace(" ", "%20");
           // System.out.println("Tamano de las lineas: "+lineas.length);
            for(int i=0; i<lineas.length; i++){
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+lineas[i]+"/"+fecha1);
                del.setHeader("content-type", "application/json");
                try
                {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    JSONArray respJSON = new JSONArray(respStr);
                    Gson gson= new Gson();
                    progress.setMax(respJSON.length());
                    int count=0;
                    for(int j=0; j<respJSON.length(); j++)
                    {
                        JSONObject obj = respJSON.getJSONObject(j);
                        IVInventario cli=gson.fromJson(obj.toString(), IVInventario.class);
                        count++;
                        if(helper.existeIVInventario(cli.getIdentificador())){
                            helper.modificarIVInventario(cli);
                        }else helper.crearIVInventario(cli);
                        publishProgress(count);
//                        System.out.println("Producto recibido: "+cli.toString());
                    }
                    result=true;
                }
                catch(Exception ex)
                {
                    Log.e("ServicioRest", "Error!", ex);
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
                    RecibirBodega recibirBodega= new RecibirBodega(context);
                    recibirBodega.ejecutartarea();
                }
            }
        }
    }
}
