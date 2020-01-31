package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarEmpresas {
    private Context context;

    public EliminarEmpresas(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarEmpresasTask delTransTask= new EliminarEmpresasTask();
        delTransTask.execute();
        return true;
    }

    class EliminarEmpresasTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Empresas");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarEmpresas();
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
