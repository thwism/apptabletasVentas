package ibzssoft.com.eliminar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 25/04/16.
 */
public class EliminarIVKardexAll{
    private Context context;
    private String id_trans;
    private int  position;

    public EliminarIVKardexAll(Context context, String id_trans, int position) {
        this.context = context;
        this.id_trans = id_trans;
        this.position = position;
    }

    public void ejecutarTarea(){
        EliminarDetallesTransaccionTask async1=new EliminarDetallesTransaccionTask();
        async1.execute();
    }
    public void eliminarDetallesAnterioresIVKardex(String id_trans){
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor1=helper.consultarIVKardexCatalogo(id_trans);
        if(cursor1.moveToFirst()){
            do{
                System.out.println("IVKardex a eliminar: "+cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador))+" posicion: "+position);
                helper.eliminarIVKardex(cursor1.getString(cursor1.getColumnIndex(IVKardex.FIELD_identificador)));
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        helper.close();
    }
    /*Tarea Asincrona*/
    class EliminarDetallesTransaccionTask extends AsyncTask<Void,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Detalles IVKardex");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    EliminarPCKardexAll asyncTask= new EliminarPCKardexAll(context,id_trans,position);
                    asyncTask.ejecutarTarea();
                }else{
                    Toast ts= Toast.makeText(context, R.string.delete_trans_fail,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                eliminarDetallesAnterioresIVKardex(id_trans);
                Thread.sleep(500);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
}

