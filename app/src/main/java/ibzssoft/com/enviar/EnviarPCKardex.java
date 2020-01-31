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
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.ishidamovile.R;

/**
 * Created by root on 23/11/15.
 */
public class EnviarPCKardex{
    private Context context;
    private  String ip;
    private  String port;
    private String id_trans;
    public EnviarPCKardex(Context context, String ip, String port,String id_trans) {
        this.context=context;
        this.ip=ip;
        this.port=port;
        this.id_trans=id_trans;
    }
    public boolean ejecutarTarea(){
        System.out.println("Ejecutando Tarea ENviar Kardex");
        EnviarPCKardexTask  sendPCKTask= new EnviarPCKardexTask ();
        sendPCKTask.execute(ip,port,id_trans);
        return true;
    }
    class EnviarPCKardexTask extends AsyncTask<String,Integer,Boolean>{
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Enviando Kardex");
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
                    Toast ts= Toast.makeText(context, R.string.send_trans_success,Toast.LENGTH_SHORT);
                    ts.show();
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
            HttpParams httpParams = new BasicHttpParams();
            ConnManagerParams.setTimeout(httpParams, 10000);
            HttpConnectionParams.setConnectionTimeout(httpParams, 50000);
            HttpConnectionParams.setSoTimeout(httpParams, 50000);

            HttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost("http://"+paramIP+":"+paramPort+"/EnlaceMovil/sincronizacion/recieve/registrar_pckardex");
            post.setHeader("content-type", "application/json; charset=UTF-8");
            Gson gson = new Gson();
            Cursor cur=helper.consultarPCKardex(id_trans_param);
            List<PCKardex> array= new ArrayList<>();
            try{
                if(cur.moveToFirst()){
                    do{
                        /*PCKardex pck=new PCKardex(
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_identificador)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_id_asignado)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_plazo)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_fecha_venci)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_fecha_emi)),
                                cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_valor)),
                                cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_valor_cancelado)),
                                cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldo_vencer)),
                                cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldo_vencido)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_dias_vencidos)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_fecha_grabado)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_cliente_id)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_forma_cobro_id)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_trans_id)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_trans)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_vendedor_id)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_guid)),

                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_banco_id)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_forma_pago)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_numero_cheque)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_numero_cuenta)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_titular)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_pago_fecha_vencimiento)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_generado)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_respaldado)),
                /*campos retencion*/
                          /*      cur.getString(cur.getColumnIndex(PCKardex.FIELD_iva)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_iva_base)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_renta)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_renta_base)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_estab)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_punto)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_secuencial)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_autorizacion)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_caducidad)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_cobrado)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_cantidad_cheques)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_letra)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_id_ruta)),
                                cur.getInt(cur.getColumnIndex(PCKardex.FIELD_orden_letra)),
                                cur.getString(cur.getColumnIndex(PCKardex.FIELD_observacion)));*/
                        //array.add(pck);
                    }while (cur.moveToNext());
                }
                helper.close();
                //Ejecutando metodo POST
                String respJSON=gson.toJson(array);
                System.out.println("PCKardex a Enviar:" + respJSON.toString());
                StringEntity entity = new StringEntity(respJSON.toString(),"UTF-8");
                post.setEntity(entity);
                HttpResponse resp = httpClient.execute(post);
                respStr = EntityUtils.toString(resp.getEntity());
                Thread.sleep(10000);
                System.out.println("Respuesta del servidor: "+respStr);
                if(respStr.equals("FAILED")){
                    result=false;
                }else{
                    result=true;
                }
            }catch (Exception e){
                e.printStackTrace();
                result=false;
            }
            return result;
        }
    }
}
