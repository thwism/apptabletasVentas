package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarPCGrupo3 {
    private Context context;

    public EliminarPCGrupo3(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarPCGrupo3TransTask delTransTask= new EliminarPCGrupo3TransTask();
        delTransTask.execute();
        return true;
    }

    class EliminarPCGrupo3TransTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando PCGrupo 3");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                EliminarPCGrupo4 eliminarPCGrupo4= new EliminarPCGrupo4(context);
                eliminarPCGrupo4.ejecutarTarea();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarPCGrupo3();
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
