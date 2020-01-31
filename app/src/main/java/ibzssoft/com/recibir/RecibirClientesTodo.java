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
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirClientesTodo {
    private Context context;
    private String ultMod;
    private String ws;
    private String ip;
    private String port;
    private String url;
    private int conf_descarga_cartera;

    public RecibirClientesTodo(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_clientes_all),context.getString(R.string.pref_ws_clientes_all));
        String conf=extraerConfiguraciones.get(context.getString(R.string.key_conf_descarga_cartera),"0");
        conf_descarga_cartera = Integer.parseInt(conf);
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_clientes),context.getString(R.string.pref_sinc_clientes_default));
    }
    public void ejecutartarea(){
        RecibirClientesTask taskRecibirClientes= new RecibirClientesTask();
        taskRecibirClientes.execute();
    }

    private class RecibirClientesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Clientes");
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
            String fecha1 =ultMod.replace(" ", "%20");

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            System.out.println("solicitando clientes todo: "+"http://"+ip+":"+port+url+ws+"/"+fecha1);
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
                for(int j=0; j<respJSON.length(); j++)
                {
                    JSONObject obj = respJSON.getJSONObject(j);
                    Cliente cli=gson.fromJson(obj.toString(), Cliente.class);
                    if(helper.existeCliente(cli.getIdprovcli())){
                        helper.modificarCliente(cli);
                    }else helper.crearCliente(cli);
                    count++;
                    publishProgress(count);
                    //System.out.println("Cliente todo recibido: "+cli.toString());
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
                    switch (conf_descarga_cartera){
                        case 0:
                            RecibirCarteraVendedor recibirCarteraVendedor = new RecibirCarteraVendedor(context);
                            recibirCarteraVendedor.ejecutartarea();
                            break;
                        case 1:
                            RecibirCarteraRutas recibirCarteraRutas= new RecibirCarteraRutas(context);
                            recibirCarteraRutas.ejecutartarea();
                            break;
                        case 2:
                            RecibirCarteraCobrador recibirCobrador = new RecibirCarteraCobrador(context);
                            recibirCobrador.ejecutartarea();
                            break;
                    }
                }
            }
        }
    }
}
