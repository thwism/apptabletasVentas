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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.json.Item;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirOfertaAprobacion {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String ws2;
    private int  posicion;

    public RecibirOfertaAprobacion(Context context, int posicion) {
        this.context = context;
        this.posicion = posicion;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_ofertaaprobacion),context.getString(R.string.pref_ws_ofertaaprobacion));
        ws2=extraerConfiguraciones.get(context.getString(R.string.key_ws_facturas),context.getString(R.string.pref_ws_facturas));
    }

    public void ejecutartarea(){
        RecibirTransaccionesTask taskRecibirTrans= new RecibirTransaccionesTask();
        taskRecibirTrans.execute();
    }

    private class RecibirTransaccionesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Transacciones para Aprobación");
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
            boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
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
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Transaccion per=gson.fromJson(obj.toString(), Transaccion.class);
                    if(helper.existeTransaccion(per.getId_trans())){
                        helper.eliminarIVKardexTrans(per.getId_trans());
                        helper.eliminarPCKardexTrans(per.getId_trans());
                        helper.modificarTransaccion(per);
                    } else helper.crearTransaccion(per);
                    /*Descargar Detalles de la Oferta*/
                    httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                    HttpGet del1 = new HttpGet("http://"+ip+":"+port+url+ws2+"/"+per.getReferencia().replace(" ","%20"));
                    del1.setHeader("content-type", "application/json");
                    HttpResponse resp1 = httpClient.execute(del1);
                    String respStr1 = EntityUtils.toString(resp1.getEntity());
                    JSONArray respJSON1 = new JSONArray(respStr1);
                    Gson gson1= new Gson();
                    progress.setMax(respJSON1.length());
                    if(respJSON1.length()>0){
                        for(int j=0; j<respJSON1.length(); j++){
                            JSONObject obj1 = respJSON1.getJSONObject(j);
                            Item per1 = gson1.fromJson(obj1.toString(), Item.class);
                            String transid = per1.getTransid();
                            String bodid = per1.getIdbodega();
                            String iviid = per1.getIdinventario();
                            String idpadre = per1.getIdpadre();
                            int descpromo = 0;
                            double pre_real = per1.getValor();
                            int numprecio = per1.getNum_precio();
                            double cnt = per1.getCantidad();
                            double prc = per1.getDescuento();
                            String nota = per1.getNota();
                            double cos_pro = per1.getPromedio();
                            double porcentaje = prc / 100;
                            double precio_total = cnt * pre_real;
                            double descuento = precio_total * porcentaje;
                            double pre_uni = pre_real / (1-porcentaje);
                            double tot = cnt * pre_real;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            String fecha = sdf.format(new Date());
                            if (porcentaje > 0.0) {
                                descpromo = 1;
                            }
                            IVKardex ivKardex = new IVKardex(
                                    new GeneradorClaves().generarClave(),
                                    cnt,
                                    pre_uni,
                                    0.0,
                                    cnt * pre_real,
                                    pre_real,
                                    nota,
                                    prc,
                                    (pre_real* cnt * porcentaje),
                                    transid,
                                    bodid,
                                    iviid,
                                    idpadre,
                                    null,
                                    descpromo,
                                    numprecio,
                                    cos_pro, 0,
                                    fecha);
                            helper.crearIVKardex(ivKardex);
                        }
                    }
                    /*Fin descarga detalles*/
                    count++;
                    publishProgress(count);
                }
                helper.close();
                Thread.sleep(50);
                result = true;
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
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("opcion",posicion);
                    activity.startActivity(intent);
                    activity.finish();
                    Toast.makeText(context,"Transacciones descargadas correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
