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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.IVKardex;

/**
 * Created by root on 18/11/15.
 */
public class RecibirIVKardex {
    private Context context;
    private String f1;
    private String ip;
    private String port;

    public RecibirIVKardex(Context context, String f1,String ip, String port) {
        this.context = context;
        this.f1 = f1;
        this.ip = ip;
        this.port = port;
    }

    public void ejecutartarea(){
        RecibirIVKardexsTask taskRecibirIVKardexs= new RecibirIVKardexsTask();
        taskRecibirIVKardexs.execute(f1,ip,port);
    }

    private class RecibirIVKardexsTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        int nItems=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando IVKardexs");
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
            String ip_param=params[0];
            String port_param=params[1];
            String fecha1=params[2].replace(" ","%20");
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = new HttpGet("http://"+ip_param+":"+port_param+"/EnlaceMovil/sincronizacion/send/send_ivkardex?pr1="+fecha1);
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson= new Gson();
                nItems=respJSON.length();
                int count=0;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    IVKardex cli=gson.fromJson(obj.toString(), IVKardex.class);
                    count++;
                    if(helper.existeIVKardex(cli.getIdentificador())){
                        helper.modificarIVKardex(cli);
                    }else helper.crearIVKardex(cli);
                    int progreso=(count*100)/nItems;
                    publishProgress(progreso);
                    //System.out.println("IVKardex recibido: "+cli.toString());
                }
                helper.close();
                result=true;
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
            }
        }
    }
}
