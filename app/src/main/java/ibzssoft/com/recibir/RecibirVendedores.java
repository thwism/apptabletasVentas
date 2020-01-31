package ibzssoft.com.recibir;

    import android.app.Activity;
    import android.support.v7.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.util.Log;

    import com.google.gson.Gson;

    import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
    import ibzssoft.com.adaptadores.ParseDates;
    import ibzssoft.com.ishidamovile.Login;
    import ibzssoft.com.ishidamovile.R;

    import org.apache.http.HttpResponse;
    import org.apache.http.client.HttpClient;
    import org.apache.http.client.methods.HttpGet;
    import org.apache.http.impl.client.DefaultHttpClient;
    import org.apache.http.protocol.HTTP;
    import org.apache.http.util.EntityUtils;
    import org.json.JSONArray;
    import org.json.JSONObject;
    import ibzssoft.com.storage.DBSistemaGestion;
    import ibzssoft.com.modelo.Vendedor;

/**
 * Created by root on 18/11/15.
 */
public class RecibirVendedores {
    private Context context;
    private String ip;
    private String port;
    private String url;
    private String ws;
    private String ultMod;

    public RecibirVendedores(Context context) {
        this.context = context;
        cargarPreferenciasConexion();
    }

    public void cargarPreferenciasConexion(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        ip=extraerConfiguraciones.get(context.getString(R.string.key_conf_ip),context.getString(R.string.pref_ip_default));
        port=extraerConfiguraciones.get(context.getString(R.string.key_conf_port),context.getString(R.string.pref_port_default));
        url=extraerConfiguraciones.get(context.getString(R.string.key_conf_url),context.getString(R.string.pref_url_default));
        ws=extraerConfiguraciones.get(context.getString(R.string.key_ws_vendedores),context.getString(R.string.pref_ws_vendedores));
        ultMod=extraerConfiguraciones.get(context.getString(R.string.key_sinc_inicial),context.getString(R.string.pref_sinc_inicial_default));
    }

    public void ejecutartarea(){
        RecibirVendedorsTask taskRecibirVendedors= new RecibirVendedorsTask();
        taskRecibirVendedors.execute();
    }

    public void confirmacion(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle("Importacion Inicial");
        alertDialogBuilder
                .setMessage("El proceso se ha completado exitosamente!")
                .setCancelable(false)
                .setPositiveButton("Salir",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.dismiss();
                        Activity activity = (Activity) context;
                        Intent intent = new Intent((Activity) context,Login.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class RecibirVendedorsTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setTitle("Descargando Catalogo de Vendedores");
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
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(context);
            String fecha1=ultMod.replace(" ", "%20");
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet del = new HttpGet("http://"+ip+":"+port+url+ws+"/"+fecha1);
            del.setHeader("content-type", "application/json");
            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                progress.setMax(respJSON.length());
                Gson gson= new Gson();
                int count = 0;
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    Vendedor usr=gson.fromJson(obj.toString(), Vendedor.class);
                    count++;
                    if (helper.existeVendedor(usr.getIdvendedor())){
                        helper.modificarVendedor(usr);
                        System.out.println("Vendedor modificado: "+usr.toString());
                    }else {
                        helper.crearVendedor(usr);
                        System.out.println("Vendedor creado: "+usr.toString());
                    }
                    publishProgress(count);
                }
                helper.close();
                Thread.sleep(100);
                result=true;
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
                if(s.equals(true)){
                    ExtraerConfiguraciones ext=new ExtraerConfiguraciones(context);
                    ext.update(context.getString(R.string.key_sinc_inicial),new ParseDates().getNowDateString());
                    ext.update(context.getString(R.string.key_conf_ini),"1");
                    ext.update(context.getString(R.string.key_act_user),context.getString(R.string.pref_act_user_default));
                    ext.update(context.getString(R.string.key_act_ven),context.getString(R.string.pref_act_ven_default));
                    ext.update(context.getString(R.string.key_act_gru),context.getString(R.string.pref_act_gru_default));
                    ext.update(context.getString(R.string.key_act_nom),context.getString(R.string.pref_act_nom_default));
                    ext.update(context.getString(R.string.key_act_sup),context.getString(R.string.pref_act_sup_default));
                    ext.update(context.getString(R.string.key_act_lin),context.getString(R.string.pref_act_lin_default));
                    ext.update(context.getString(R.string.key_act_rut),context.getString(R.string.pref_act_rut_default));
                    ext.update(context.getString(R.string.key_act_acc),context.getString(R.string.pref_act_acc_default));
                    ext.update(context.getString(R.string.key_act_bod),context.getString(R.string.pref_act_bod_default));
                    confirmacion();
                }
            }
        }
    }
}
