package ibzssoft.com.recibir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.reportes.ItemsPendientes;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.ItemPendiente;

/**
 * Created by root on 18/11/15.
 */
public class RecibirItemsPendientes {

    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String cliente;

    public RecibirItemsPendientes(Context context, String cliente) {
        this.context = context;
        this.cliente = cliente;
        cargarPreferenciasConexion();
    }

    public void ejecutartarea(){
        RecibirItemsPendientesTask taskRecibiFacturas= new RecibirItemsPendientesTask();
        taskRecibiFacturas.execute();
    }


    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_items_pendientes),context.getString(R.string.pref_ws_items_pendientes));
    }


    private class RecibirItemsPendientesTask extends AsyncTask<String,Integer,ArrayList<ItemPendiente>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Descargando Items Pendientes");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected ArrayList<ItemPendiente> doInBackground(String... params) {
            ArrayList<ItemPendiente> result=new ArrayList<ItemPendiente>();
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 60000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+cliente);
            System.out.println("Solicitando item prendiente: "+"http://"+ip+":"+port+url+ws+"/"+cliente);

            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                System.out.println("Respuesta recibida: "+respStr);
                Gson gson= new Gson();
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    ItemPendiente per=gson.fromJson(obj.toString(), ItemPendiente.class);
                    result.add(per);
                    System.out.println("Item pendiente recibido: "+per.toString());
                }
                Thread.sleep(100);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemPendiente> s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.size()>=1){
                    Activity activity = (Activity) context;
                    Intent intent= new Intent(activity, ItemsPendientes.class);
                    intent.putExtra("items",s);
                    intent.putExtra("cliente",cliente);
                    activity.startActivity(intent);
                    Toast ts = Toast.makeText(context, "Informacion descargada correctamente", Toast.LENGTH_LONG);
                    ts.show();
                }else{
                    Toast ts = Toast.makeText(context, "No se puede descargar la informacion", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }
    }
}
