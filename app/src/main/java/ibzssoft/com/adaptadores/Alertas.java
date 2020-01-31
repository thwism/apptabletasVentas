package ibzssoft.com.adaptadores;

import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Usuario-pc on 03/02/2017.
 */
public class Alertas {
        private Context context;
        private String mensaje;
        private String descripcion;


        public Alertas(Context context, String mensaje, String descripcion) {
            this.context=context;
            this.mensaje = mensaje;
            this.descripcion = descripcion;
        }
    public void mostrarMensaje(){
        android.support.v7.app.AlertDialog.Builder quitDialog
                = new android.support.v7.app.AlertDialog.Builder(context);
        quitDialog.setTitle(this.mensaje);
        quitDialog.setMessage(this.descripcion);
        quitDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }});
        quitDialog.show();
    }

}
