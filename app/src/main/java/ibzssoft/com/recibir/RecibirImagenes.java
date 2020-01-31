package ibzssoft.com.recibir;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.GeneradorClaves;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.json.Imagen;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 18/11/15.
 */
public class RecibirImagenes {
    private Context context;
    private String ultMod;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String [] lineas;

    public RecibirImagenes(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_imagenes),context.getString(R.string.pref_ws_imagenes));
        lineas=extraerConfiguraciones.get(context.getString(R.string.key_act_lin),context.getString(R.string.pref_act_lin_default)).split(",");
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_productos),context.getString(R.string.pref_sinc_productos_default));
    }

    public void ejecutartarea(){
        RecibirImagenesProductosTask taskRecibirProductos= new RecibirImagenesProductosTask();
        taskRecibirProductos.execute();
    }

    private class RecibirImagenesProductosTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Descargando Imagenes de Productos");
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
            DBSistemaGestion helper= new DBSistemaGestion(context);
            Boolean result=false;
            String fecha1=ultMod.replace(" ", "%20");
            for(int i =0; i<lineas.length; i++){
                HttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+lineas[i]+"/"+fecha1);
                System.out.println("http://"+ip+":"+port+url+ws+"/"+lineas[i]+"/"+fecha1);
                del.setHeader("content-type", "application/json");
                try
                {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());
                    JSONArray respJSON = new JSONArray(respStr);
                    Gson gson= new Gson();
                    progress.setMax(respJSON.length());
                    for(int j=0; j<respJSON.length(); j++) {
                        JSONObject obj = respJSON.getJSONObject(j);
                        Imagen cli=gson.fromJson(obj.toString(), Imagen.class);
                    /*Imagen 1*/
                        if(cli.getImg1()!=null){
                            byte[] photo= Base64.decode(cli.getImg1(), Base64.DEFAULT);
                            ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                            Bitmap bitmap= BitmapFactory.decodeStream(imageStream);
                            String path = this.savebitmap(context,bitmap);
                            cli.setImg1(path);
                        }
                    /*Imagen 2*/
                        if(cli.getImg2()!=null){
                            byte[] photo2= Base64.decode(cli.getImg2(), Base64.DEFAULT);
                            ByteArrayInputStream image2Stream = new ByteArrayInputStream(photo2);
                            Bitmap bitmap2= BitmapFactory.decodeStream(image2Stream);
                            String path2 = this.savebitmap(context,bitmap2);
                            cli.setImg2(path2);
                        }
                    /*Imagen 3*/
                        if(cli.getImg3()!=null){
                            byte[] photo3= Base64.decode(cli.getImg3(), Base64.DEFAULT);
                            ByteArrayInputStream image3Stream = new ByteArrayInputStream(photo3);
                            Bitmap bitmap3= BitmapFactory.decodeStream(image3Stream);
                            String path3 = this.savebitmap(context,bitmap3);
                            cli.setImg3(path3);
                        }
                        System.out.println("Imagen recibido: "+cli.toString());
                        helper.actualizarImagenesProducto(cli);
                    }//fin recorrer imagenes
                    result=true;
                }catch(Exception ex){
                    Log.e("ServicioRest", "Error!", ex);
                }
            }
            helper.close();
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    ExtraerConfiguraciones extraerConfiguraciones=new ExtraerConfiguraciones(context);
                    extraerConfiguraciones.update(context.getString(R.string.key_sinc_productos),new ParseDates().getNowDateString());
                    Toast ts= Toast.makeText(context, R.string.info_success_import,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        /*public String getImageUri(Context inContext, Bitmap inImage) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            System.out.println("Nombre imagen: "+new GeneradorClaves().generarClave()+".png");
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, new GeneradorClaves().generarClave()+".png", null);

            return path;
        }*/

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
    }
}
