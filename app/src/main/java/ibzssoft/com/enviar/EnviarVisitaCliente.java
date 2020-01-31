package ibzssoft.com.enviar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 19/04/16.
 */
public class EnviarVisitaCliente {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String transid;
    private String base;
    private String codemp;
    private int opcion;

    public EnviarVisitaCliente(String transid, Context context, int opcion) {
        this.context=context;
        this.transid=transid;
        this.opcion = opcion;
        cargarPreferenciasConexion();
    }
    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_visitas),context.getString(R.string.pref_ws_visita));
        base=extraerConfiguraciones.get(context.getString(R.string.key_empresa_base),context.getString(R.string.pref_base_empresa_default));
        codemp=extraerConfiguraciones.get(context.getString(R.string.key_empresa_codigo),context.getString(R.string.pref_codigo_empresa_default));
    }
    public boolean ejecutarTarea(){
        EnviarVisitaTask sendTransTask= new EnviarVisitaTask();
        sendTransTask.execute("Ricardo","Orellana");
        return true;
    }

    private class EnviarVisitaTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setTitle("Enviando Visita Generada");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost del = new HttpPost("http://"+ip+":"+port+url+ws);
            JsonObject innerObject = new JsonObject();
            innerObject.addProperty("datos", generarTramaEnvio());
            System.out.println("http://"+ip+":"+port+url+ws);
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
                String response = obj.getString("estado");
                System.out.println("Response: "+response);
                if(response.equals("OK")) {
                    result = true;
                    modificarTransaccion(transid,"VISITA");
                }
                helper.close();
                Thread.sleep(1000);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                Log.e("ServicioRest", "Error!", ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    refresh();
                }else{
                    new Alertas(context,"Error ", "La visita no puede ser enviada.").mostrarMensaje();
                }
            }
        }
    }

    public boolean modificarTransaccion(String trans,String referencia){
        DBSistemaGestion helper= new DBSistemaGestion(context);
        helper.marcarTransaccionComoEnviado(trans,referencia);
        helper.close();
        return true;
    }
    public boolean refresh(){
        Activity activity = (Activity)context;
        Intent intent = activity.getIntent();
        intent.putExtra("opcion", opcion);
        activity.finish();
        context.startActivity(intent);
        return true;
    }
    public String generarTramaEnvio(){
        String trama = "";
        try{
            List<IVKardex_Serialize_Envio> array1 = new ArrayList();
            List<PKardex_Envio> array2= new ArrayList();
            List<PCKardex> array3= new ArrayList();

            DBSistemaGestion helper= new DBSistemaGestion(context);
            Cursor cursor=helper.consultarIVKardex(transid);
            if(cursor.moveToFirst()){
                do{
                    IVKardex_Serialize_Envio ivKardex= new IVKardex_Serialize_Envio(
                            cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cantidad)),
                            cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_precio_total)),
                            cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_cos_total)),
                            cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_descuento)),
                            cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_bodega_id)),
                            cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_inventario_id)),
                            cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id)),
                            cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_desc_promo)),
                            cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio)),
                            cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)),
                            cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_band_aprobado))
                    );
                    System.out.println("Cargando IVKArdex: "+ivKardex.toString());
                    array1.add(ivKardex);
                }while (cursor.moveToNext());
            }

            Cursor cursor1=helper.consultarPCKardex(transid);
            if(cursor1.moveToFirst()){
                do{
                    PKardex_Envio pcka= new PKardex_Envio();
                    PCKardex pck= new PCKardex();
                    pcka.setId_asignado(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idasignado)));
                    pcka.setTsf_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_codforma)));
                    pcka.setValor_cancelado(cursor1.getDouble(cursor1.getColumnIndex(PCKardex.FIELD_pagado)));
                    pcka.setIdcobrador(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idcobrador)));
                    pcka.setIdvendedor(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idvendedor)));
                    pcka.setObservacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_observacion)).replace("/",":"));

                    pck.setBanco_id(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_banco_id)));
                    pck.setForma_pago(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_forma_pago)));
                    pck.setTitular(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_titular)));
                    pck.setNumero_cuenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_numero_cuenta)));
                    pck.setIva(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva)));
                    pck.setIva_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_iva_base)));
                    pck.setRenta(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta)));
                    pck.setRenta_base(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_renta_base)));
                    pck.setNum_ser_estab(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                    pck.setNum_ser_punto(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_punto)));
                    pck.setNum_ser_secuencial(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_num_ser_estab)));
                    pck.setAutorizacion(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_autorizacion)));
                    pck.setCaducidad(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_caducidad)));
                    pck.setIdvendedor(cursor1.getString(cursor1.getColumnIndex(PCKardex.FIELD_idvendedor)));
                    System.out.println("Cargando PCKArdex: "+pcka.toString());
                    array2.add(pcka);
                    array3.add(pck);
                }while (cursor1.moveToNext());
            }
            cursor.close();
            cursor1.close();
            helper.close();
                /*Preparando Objeto para el envio*/
            Transaccion_Serialize_Envio trans_send = new Transaccion_Serialize_Envio();
            Cursor cursor2=helper.obtenerTransaccion(transid);
            if(cursor2.moveToFirst()){
                trans_send.setId_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                trans_send.setIdentificador(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_identificador)));
                trans_send.setNumTransaccion(cursor2.getInt(cursor2.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                trans_send.setFecha_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_fecha_trans)));
                trans_send.setHora_trans(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_hora_trans)));
                trans_send.setCliente_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_cliente_id)));
                trans_send.setForma_pago(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_forma_cobro_id)));
                trans_send.setDescripcion(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_descripcion)));
                trans_send.setVendedor_id(cursor2.getString(cursor2.getColumnIndex(Transaccion.FIELD_vendedor_id)));
            }
            cursor2.close();
            if(array3.size()>0){
                trans_send.setBanco_id(array3.get(0).getBanco_id());
                trans_send.setForma_pago(array3.get(0).getForma_pago());
                trans_send.setTitular(array3.get(0).getTitular());
                trans_send.setNumero_cheque(array3.get(0).getNumero_cheque());
                trans_send.setNumero_cuenta(array3.get(0).getNumero_cuenta());
                trans_send.setCheque_fecha_vencimiento(array3.get(0).getPago_fecha_vencimiento());
                trans_send.setIva(array3.get(0).getIva());
                trans_send.setIva_base(array3.get(0).getIva_base());
                trans_send.setRenta(array3.get(0).getRenta());
                trans_send.setRenta_base(array3.get(0).getRenta_base());
                trans_send.setEstablecimiento(array3.get(0).getNum_ser_estab());
                trans_send.setPunto(array3.get(0).getNum_ser_punto());
                trans_send.setSecuencial(array3.get(0).getNum_ser_secuencial());
                trans_send.setAutorizacion(array3.get(0).getAutorizacion());
                trans_send.setCaducidad(array3.get(0).getCaducidad());
            }
            trans_send.setIvkardex(array1);
            trans_send.setPckardex(array2);
            trama = "TRANSACCION;"+base+";trama1;2016-01-01%2012:00:00;"+codemp+";"+trans_send.toString();
            System.out.println(trama);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return trama;
    }
}