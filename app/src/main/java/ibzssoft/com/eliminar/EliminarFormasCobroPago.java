package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarFormasCobroPago {
    private Context context;

    public EliminarFormasCobroPago(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarFormasCobroPagoTask delTransTask= new EliminarFormasCobroPagoTask();
        delTransTask.execute();
        return true;
    }

    class EliminarFormasCobroPagoTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Formas de cobro");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                EliminarGNTrans eliminarGNTrans= new EliminarGNTrans(context);
                eliminarGNTrans.ejecutarTarea();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarFormasPagoCobro();
                helper.close();
                Thread.sleep(50);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
