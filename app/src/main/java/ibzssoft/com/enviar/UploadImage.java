package ibzssoft.com.enviar;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.ValidateReferencia;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Banco;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class UploadImage {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;

    public UploadImage(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_upload_image),context.getString(R.string.pref_ws_upload_image));
    }

    public void ejecutartarea(String base64, String iditem, int posicion_galeria){
        RecibirImagenesTask taskRecibirBodegas= new RecibirImagenesTask();
        taskRecibirBodegas.execute(base64,iditem,String.valueOf(posicion_galeria));
    }

    private class RecibirImagenesTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Actualizando imagenes");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
            HttpParams httpParameters = new BasicHttpParams();
            HttpPost del = new HttpPost("http://"+ip+":"+port+url+ws);
            JsonObject innerObject = new JsonObject();
            innerObject.addProperty("img", params[0]);
            innerObject.addProperty("iditem", params[1]);
            innerObject.addProperty("posicion", params[2]);
            int timeoutConnection = 30000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 30000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            del.setHeader("Accept", "application/json");
            del.setHeader("Content-type", "application/json");
            try
            {
                StringEntity se = new StringEntity(innerObject.toString());
                del.setEntity(se);
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject obj = new JSONObject(respStr);
                int response = obj.getInt("estado");
                if(response==1){
                    byte[] photo= Base64.decode(params[0], Base64.DEFAULT);
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                    Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
                    String path = this.savebitmap(context,bitmap);
                    helper.modificarImagenesProducto(params[1],path,Integer.parseInt(params[2]));
                    result = true;
                }
                helper.close();
                Thread.sleep(50);
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
            }
            return result;
        }

        public String savebitmap(Context context,Bitmap bmp) throws IOException {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator +context.getString(R.string.title_folder_root)+ File.separator +new GeneradorClaves().generarClave()+".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
            return f.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    new Alertas(context,"Datos exportados correctamente","La imagen fue subida correctamente al sistema Sii4A").mostrarMensaje();
                }else{
                    Toast.makeText(context,"No es posible exportar la imagen",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
