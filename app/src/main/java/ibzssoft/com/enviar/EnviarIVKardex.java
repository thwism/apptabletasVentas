package ibzssoft.com.enviar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.ishidamovile.R;

/**
 * Created by root on 23/11/15.
 */
public class EnviarIVKardex {
    private Context context;
    private  String ip;
    private  String port;
    private String id_trans;
    public EnviarIVKardex(Context context, String ip, String port,String id_trans) {
        this.context=context;
        this.ip=ip;
        this.port=port;
        this.id_trans=id_trans;
    }
    public boolean ejecutarTarea(){

        EnviarIVKardexTask sendIVKTask= new EnviarIVKardexTask();
        sendIVKTask.execute(ip,port,id_trans);
        return true;
    }
    class EnviarIVKardexTask extends AsyncTask<String,Integer,Boolean>{
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Enviando IVKardex");
            progress.setCancelable(false);
            progress.setMessage("Espere...");
            progress.show();
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    EnviarPCKardex enviarPCKardex= new EnviarPCKardex(context,ip,port,id_trans);
                    enviarPCKardex.ejecutarTarea();
                }else{
                    Toast ts= Toast.makeText(context,R.string.send_trans_fail,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            DBSistemaGestion helper= new DBSistemaGestion(context);
            String paramIP=params[0];
            String paramPort=params[1];
            String id_trans_param=params[2];
            boolean result=false;
            String respStr=null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://"+paramIP+":"+paramPort+"/EnlaceMovil/sincronizacion/recieve/registrar_ivkardex");
            post.setHeader("content-type", "application/json; charset=UTF-8");
            Gson gson = new Gson();
            Cursor cursor=helper.consultarIVKardex(id_trans_param);
            List<IVKardex> array = new ArrayList();
            try{
                if(cursor.moveToFirst()){
                    do{
                        IVKardex ivKardex= new IVKardex(
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_identificador)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cantidad)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_real_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_precio_total)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento_real)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_trans_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_bodega_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_inventario_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_keypadre)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio)),
                                cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_pro)),
                                cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_band_aprobado)),
                                cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_fecha_grabado))
                        );
                        array.add(ivKardex);

                    }while (cursor.moveToNext());
                }

                /*Ejecutando metodo POST*/
                String respJSON=gson.toJson(array);
                System.out.println("IVKArdex a Enviar:" +respJSON.toString());
                StringEntity entity = new StringEntity(respJSON.toString(),"UTF-8");
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(10000);
                if(respStr.equals("OK")){
                    result=true;
                }else{
                    result=false;
                }
                helper.close();
            }catch (Exception e){
                e.printStackTrace();
                result=false;
            }
            return result;
        }
    }
}
