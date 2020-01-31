package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarGrupo {
    private Context context;

    public EliminarGrupo(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarGruposTask delTransTask= new EliminarGruposTask();
        delTransTask.execute();
        return true;
    }

    class EliminarGruposTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Grupos");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                /*EliminarPermisos eliminarPermisos= new EliminarPermisos(context);
                eliminarPermisos.ejecutarTarea();*/
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarGrupos();
                helper.close();
                Thread.sleep(1000);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
