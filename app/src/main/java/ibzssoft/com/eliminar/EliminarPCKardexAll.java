package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarPCKardexAll {
    private Context context;
    private String id_trans;
    private int  position;

    public EliminarPCKardexAll(Context context, String id_trans, int position) {
        this.context = context;
        this.id_trans = id_trans;
        this.position = position;
    }

    public void ejecutarTarea(){
        EliminarDetallesTransaccionPCKTask asyn=new EliminarDetallesTransaccionPCKTask();
        asyn.execute();
    }

    public void eliminarDetallesAnterioresPCKardex(String id_trans){
        DBSistemaGestion helper= new DBSistemaGestion(context);
        Cursor cursor=helper.consultarPCKardexTransaccionCatalogo(id_trans);
        if(cursor.moveToFirst()){
            do{
                System.out.println("PCKardex a eliminar: "+cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_idcartera))+" posicion: "+position);
                helper.eliminarPCKardex(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_idcartera)));
            }while(cursor.moveToNext());
        }
        helper.close();
    }

    /*Tarea asincrona*/
    class EliminarDetallesTransaccionPCKTask extends AsyncTask<Void,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Detalles PCKardex");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    EliminarTransaccionAll async= new EliminarTransaccionAll(context,id_trans,position);
                    async.ejecutarTarea();
                }else{
                    Toast ts= Toast.makeText(context, R.string.delete_trans_fail,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                eliminarDetallesAnterioresPCKardex(id_trans);
                Thread.sleep(500);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}
