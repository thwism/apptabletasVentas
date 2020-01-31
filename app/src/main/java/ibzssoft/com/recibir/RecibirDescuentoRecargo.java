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
import java.util.List;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.ishidamovile.FacturaDetalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.json.RecargoDescuento;
import ibzssoft.com.modelo.json.Item;

/**
 * Created by root on 18/11/15.
 */
public class    RecibirDescuentoRecargo {
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
    private ArrayList<Item> items;
    public RecibirDescuentoRecargo(Context context, String factura, String nombres, String ruc, String alterno, String direccion, String emision, ArrayList<Item> items) {
        this.context = context;
        this.factura = factura;
        this.nombres = nombres;
        this.ruc = ruc;
        this.alterno = alterno;
        this.direccion = direccion;
        this.emision = emision;
        this.items=items;
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
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_recargos),context.getString(R.string.pref_ws_recargos));
    }

    private class RecibirFacturasTask extends AsyncTask<String,Integer,ArrayList<RecargoDescuento>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Descargando Recargos y Descuentos");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected ArrayList<RecargoDescuento> doInBackground(String... params) {
            ArrayList<RecargoDescuento> result=new ArrayList<>();
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+factura.replace(" ","%20"));
            System.out.println("Recargos: "+"http://"+ip+":"+port+url+ws+"/"+factura.replace(" ","%20"));
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    RecargoDescuento per=gson.fromJson(obj.toString(), RecargoDescuento.class);
                    result.add(per);
                    System.out.println("Recargo recibido: "+per.toString());
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
        protected void onPostExecute(ArrayList<RecargoDescuento> s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.size()>=1){
                    Activity activity = (Activity) context;
                    Intent intent= new Intent(activity, FacturaDetalle.class);
                    intent.putExtra("filas",s.size());
                    intent.putExtra("recargo",s);
                    intent.putExtra("items",items);
                    intent.putExtra("factura",factura);
                    intent.putExtra("ruc",ruc);
                    intent.putExtra("nombre",nombres);
                    intent.putExtra("alterno",alterno);
                    intent.putExtra("direccion",direccion);
                    intent.putExtra("emision",emision);
                    activity.startActivity(intent);
                    Toast ts = Toast.makeText(context, "Proceso completado exitosamente", Toast.LENGTH_LONG);
                    ts.show();
                }else{
                    Toast ts = Toast.makeText(context, "No se puede descargar la factura", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }
    }
}
