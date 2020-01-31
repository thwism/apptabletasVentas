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
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ReporteChequesPostfechados;
import ibzssoft.com.modelo.Cheque;

/**
 * Created by root on 18/11/15.
 */
public class RecibirCheques {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String trans;
    private String factura;
    private String cedula;
    private String nombres;
    private String comercial;
    private String direccion;
    private String fecha;

    public
    RecibirCheques(Context context,String trans, String factura, String cedula, String nombres, String comercial, String direccion,String fecha ) {
        this.context = context;
        this.trans=trans;
        cargarPreferenciasConexion();
        this.factura = factura;
        this.cedula = cedula;
        this.nombres = nombres;
        this.comercial = comercial;
        this.direccion = direccion;
        this.fecha = fecha;
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
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_cheques),context.getString(R.string.pref_ws_cheques));
    }
    private class RecibirFacturasTask extends AsyncTask<String,Integer,ArrayList<Cheque>> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Descargando Cheques Postfechados");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected ArrayList<Cheque> doInBackground(String... params) {
            ArrayList<Cheque> result=new ArrayList<Cheque>();
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 3000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+trans);

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
                    Cheque per=gson.fromJson(obj.toString(), Cheque.class);
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
        protected void onPostExecute(ArrayList<Cheque> s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.size()>=1){
                    for(Cheque ts: s){
                        System.out.println("Cheque: "+ts.toString());
                    }
                    Activity activity = (Activity) context;
                    Intent intent= new Intent(activity, ReporteChequesPostfechados.class);
                    intent.putExtra("cheques",s);
                    intent.putExtra("factura",factura);
                    intent.putExtra("cedula",cedula);
                    intent.putExtra("nombres",nombres);
                    intent.putExtra("comercial",comercial);
                    intent.putExtra("direccion",direccion);
                    intent.putExtra("fecha",fecha);
                    activity.startActivity(intent);
                    Toast ts = Toast.makeText(context, "Cheques Descargados Correctamente", Toast.LENGTH_LONG);
                    ts.show();
                }else{
                    Toast ts = Toast.makeText(context, "No se puede descargar la informacion solicitada", Toast.LENGTH_LONG);
                    ts.show();
                }
            }
        }
    }
}
