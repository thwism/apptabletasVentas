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
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.json.Item;
import ibzssoft.com.storage.DBSistemaGestion;


public class RecibirTransaccionesConEstado {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String codigoTrans;
    private String numTrans;


    public RecibirTransaccionesConEstado(Context context, String codigoTrans, String numTrans) {
        this.context = context;
        this.codigoTrans = codigoTrans;
        this.numTrans = numTrans;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion() {
        ExtraerConfiguraciones extraerConfiguraciones = new ExtraerConfiguraciones(context);
        ip = extraerConfiguraciones.get(context.getString(R.string.key_conf_ip), context.getString(R.string.pref_ip_default));
        port = extraerConfiguraciones.get(context.getString(R.string.key_conf_port), context.getString(R.string.pref_port_default));
        url = extraerConfiguraciones.get(context.getString(R.string.key_conf_url), context.getString(R.string.pref_url_default));
        ws = extraerConfiguraciones.get(context.getString(R.string.key_ws_get_estados_trans), context.getString(R.string.pref_ws_get_estados_trans));

    }

    public void ejecutarTarea() {
        RecibirTransaccionesTask taskRecibirTrans = new RecibirTransaccionesTask();
        taskRecibirTrans.execute();
    }

    private class RecibirTransaccionesTask extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            DBSistemaGestion helper = new DBSistemaGestion(context);
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://" + ip + ":" + port + url + ws + "/" + codigoTrans + "/" + numTrans);
            del.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson = new Gson();
                String numReferencia = null;
                int count = 0;
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    numReferencia = obj.getString("CodTrans") + "-" + obj.getString("NumTrans");
                    if (helper.buscarTransaccionPorReferencia(numReferencia)) {
                        helper.actualizarEstadoTransaccion(numReferencia, Integer.parseInt(obj.getString("estado")));
                    }

                    count++;
                    System.out.println("Lo que recibe de la consulta al servicio web: " + count);
                }
                helper.close();
                Thread.sleep(50);
                result = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {

            Toast.makeText(context, "Estados de las transacciones descargados correctamente.", Toast.LENGTH_SHORT).show();


        }
    }
}
