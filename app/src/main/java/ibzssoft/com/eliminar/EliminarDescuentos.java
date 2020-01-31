package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.recibir.RecibirProvincias;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarDescuentos {
    private Context context;

    public EliminarDescuentos(Context context) {
        this.context = context;
    }

    public boolean ejecutarTarea(){
        EliminarDescuentosTransTask delTransTask= new EliminarDescuentosTransTask();
        delTransTask.execute();
        return true;
    }

    class EliminarDescuentosTransTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Descuentos");
            progress.setMessage("Espere...");
            progress.show();
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(context);
                ext.update(context.getString(R.string.key_sinc_clientes),context.getString(R.string.pref_sinc_clientes_default));
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try{
                DBSistemaGestion helper= new DBSistemaGestion(context);
                helper.vaciarDescuentosAll();
                helper.close();
                Thread.sleep(500);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
