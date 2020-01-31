package ibzssoft.com.recibir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.json.Item;

/**
 * Created by root on 18/11/15.
 */
public class RecibirFacturas {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String factura;
    private String ruc;
    private String nombres;
    private String alterno;
    private String direccion;
    private String emision;
    public RecibirFacturas(Context context, String factura, String nombres, String ruc, String alterno, String direccion, String emision) {
        this.context = context;
        this.factura = factura;
        this.nombres = nombres;
        this.ruc = ruc;
        this.alterno = alterno;
        this.direccion = direccion;
        this.emision = emision;
        cargarPreferenciasConexion();
    }

    public void ejecutartarea(){
        RecibirFacturasTask taskRecibiFacturas= new RecibirFacturasTask();
        taskRecibiFacturas.execute();
    }


    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_facturas),context.getString(R.string.pref_ws_facturas));
    }

    private class RecibirFacturasTask extends AsyncTask<String,Integer,ArrayList<Item>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Descargando Facturas");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected ArrayList<Item> doInBackground(String... params) {
            ArrayList<Item> result=new ArrayList<Item>();
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+factura.replace(" ","%20"));
            System.out.println("http://"+ip+":"+port+url+ws+"/"+factura.replace(" ","%20"));
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                System.out.println("Datos recibo");
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Item per=gson.fromJson(obj.toString(), Item.class);
                    result.add(per);
                    System.out.println("Item recibido: "+per.toString());
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
        protected void onPostExecute(ArrayList<Item> s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.size()>=1){
                    RecibirDescuentoRecargo recibirDescuentoRecargo= new RecibirDescuentoRecargo(context,factura,nombres,ruc,alterno,direccion,emision,s);
                    recibirDescuentoRecargo.ejecutartarea();
                }else{
                    Toast ts = Toast.makeText(context, "No se puede descargar la factura", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }
    }
}
