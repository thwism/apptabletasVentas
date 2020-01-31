package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarIVGrupo3 {
    private Context context;

    public EliminarIVGrupo3(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarGrupo3TransTask delTransTask= new EliminarGrupo3TransTask();
        delTransTask.execute();
        return true;
    }

    class EliminarGrupo3TransTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Grupo 3");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                EliminarIVGrupo4 eliminarIVGrupo4= new EliminarIVGrupo4(context);
                eliminarIVGrupo4.ejecutarTarea();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarIVGrupo3();
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
