package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarPCGrupo1 {
    private Context context;

    public EliminarPCGrupo1(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarPCGrupo1TransTask delTransTask= new EliminarPCGrupo1TransTask();
        delTransTask.execute();
        return true;
    }

    class EliminarPCGrupo1TransTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando PCGrupo 1");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                EliminarPCGrupo2 eliminarPCGrupo2= new EliminarPCGrupo2(context);
                eliminarPCGrupo2.ejecutarTarea();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarPCGrupo1();
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
