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
import ibzssoft.com.modelo.Banco;
import ibzssoft.com.modelo.GNOpcion;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.ValoresGNOpcion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirGNOpcion {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;

    public RecibirGNOpcion(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_gnopcion),context.getString(R.string.pref_ws_gnopcion));
    }

    public void ejecutartarea(){
        RecibirBodegasTask taskRecibirBodegas= new RecibirBodegasTask();
        taskRecibirBodegas.execute();
    }

    private class RecibirBodegasTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando GNOpcion");
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
            ExtraerConfiguraciones ext= new ExtraerConfiguraciones(context);
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
                helper.vaciarGNOpcion();
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    GNOpcion per=gson.fromJson(obj.toString(), GNOpcion.class);
                    count++;
                    helper.crearGNOpcion(per);
                    publishProgress(count);
                    System.out.println("GNOpcion recibido: "+per.toString());
                    ValoresGNOpcion gno = ValoresGNOpcion.valueOf(per.getCodigo());
                    switch (gno){
                        case AplicaInteresMora:
                            if(per.getValor().equals("1"))
                            ext.updateBool(context.getString(R.string.key_conf_calcula_interes),true);
                            else ext.updateBool(context.getString(R.string.key_conf_calcula_interes),false);
                        break;
                        case OrdenBodegas:
                            ext.update(context.getString(R.string.key_conf_orden_bodegas),per.getValor());
                            break;
                        case TasaMoraAnual:
                                ext.update(context.getString(R.string.key_conf_tasa_mora),per.getValor());
                        break;
                        case DiasGraciaMora:
                            ext.update(context.getString(R.string.key_conf_dias_gracia_mora),per.getValor());
                        break;
                        case NumDecimales:
                            ext.update(context.getString(R.string.key_conf_num_decimales),per.getValor());
                            break;
                        case AplicaCalculoMensual:
                            ext.update(context.getString(R.string.key_conf_aplica_calculo_mensual),per.getValor());
                            break;
                        case ConfigIshidaMovil:
                            ext.update(context.getString(R.string.key_conf_ishida_movil),per.getValor());
                            break;
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
                   RecibirEmpresas recibirEmpresas = new RecibirEmpresas(context);
                   recibirEmpresas.ejecutartarea();
                }
            }
        }
    }
}
