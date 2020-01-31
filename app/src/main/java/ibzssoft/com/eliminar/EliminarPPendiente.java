package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarPPendiente {
    private Context context;

    public EliminarPPendiente(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarGrupo7TransTask delTransTask= new EliminarGrupo7TransTask();
        delTransTask.execute();
        return true;
    }

    class EliminarGrupo7TransTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Pedidos Pendientes");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();

                EliminarDescuentos eliminarDescuentos= new EliminarDescuentos(context);
                eliminarDescuentos.ejecutarTarea();

            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarPedidoPendiente();
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
