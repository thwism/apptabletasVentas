package ibzssoft.com.eliminar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;

import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarTransaccion {
    private Context context;
    private String id_trans;
    private int num_trans;
    private String cod_trans;
    private int  position;

    public EliminarTransaccion(Context context, String id_trans, int num_trans, String cod_trans, int position) {
        this.context = context;
        this.id_trans = id_trans;
        this.num_trans = num_trans;
        this.cod_trans = cod_trans;
        this.position = position;
    }

    public boolean ejecutarTarea(){
        EliminarTransaccionTask delTransTask= new EliminarTransaccionTask();
        delTransTask.execute(id_trans);
        return true;
    }

    class EliminarTransaccionTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        private String transaccion_id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            transaccion_id="";
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Transaccion");
            progress.setMessage("Espere...");
            progress.show();
        }

        public boolean refresh(){
            Activity activity = (Activity)context;
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("opcion", position);
            activity.startActivity(intent);
            activity.finish();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    Toast ts= Toast.makeText(context, R.string.delete_trans_success,Toast.LENGTH_SHORT);
                    ts.show();
                        /*Eliminar encabezado transaccion*/
                    DBSistemaGestion helper= new DBSistemaGestion(context);
                    helper.eliminarTransaccion(transaccion_id);
                    helper.close();
                    /**reload activity**/
                   refresh();
                }else{
                    Toast ts= Toast.makeText(context, R.string.delete_trans_fail,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            transaccion_id=params[0];
            try{
                String file = "sdcard/" + context.getResources().getString(R.string.title_folder_root) + "/" + cod_trans + "_" + num_trans + ".pdf";
                System.out.println("Archivo a eliminar: "+file);
                File adjunto = new File(file);
                if(adjunto.exists()){
                    adjunto.delete();
                }
                Thread.sleep(500);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
