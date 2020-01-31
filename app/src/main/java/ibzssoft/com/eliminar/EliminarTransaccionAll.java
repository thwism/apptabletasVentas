package ibzssoft.com.eliminar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarTransaccionAll {
    private Context context;
    private String id_trans;
    private int  position;

    public EliminarTransaccionAll(Context context, String id_trans, int position) {
        this.context = context;
        this.id_trans = id_trans;
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
            Intent intent = activity.getIntent();
            intent.putExtra("posicion", position);
            activity.finish();
            context.startActivity(intent);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                try{
                    DBSistemaGestion helper= new DBSistemaGestion(context);
                    helper.eliminarTransaccionCatalogo(transaccion_id);
                    helper.close();
                    Toast ts= Toast.makeText(context, R.string.delete_trans_success_all,Toast.LENGTH_SHORT);
                    ts.show();
                    refresh();
                }catch (Exception e){
                    Toast ts= Toast.makeText(context, R.string.delete_trans_fail,Toast.LENGTH_SHORT);
                    ts.show();
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            transaccion_id=params[0];
            try{
                Thread.sleep(1000);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
