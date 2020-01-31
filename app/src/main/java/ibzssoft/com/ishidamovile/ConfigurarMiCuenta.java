package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ibzssoft.com.storage.DBSistemaGestion;

public class ConfigurarMiCuenta extends Fragment{
    private String usuario;
    private TextView msg;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_configurar_mi_cuenta, container, false);
        msg= (TextView)view.findViewById(R.id.msg);
        this.extraerParametros();
        this.OpenCategroyDialogBox();
        return view;
    }

    public void extraerParametros(){
        this.usuario = getArguments().getString("usuario").toString();
    }
    private void OpenCategroyDialogBox() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.content_cambio_clave, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Digite nueva contraseña");
        alert.setView(promptView);

        final EditText input1 = (EditText) promptView.findViewById(R.id.userInputDialog);
        final EditText input2 = (EditText) promptView.findViewById(R.id.userInputDialog2);
        alert.setPositiveButton("Cambiar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(validarCoincidencia(input1.getText().toString(),input2.getText().toString())){
                    ChangePassTask changeTask= new ChangePassTask();
                    changeTask.execute(input1.getText().toString(),usuario);
                    msg.setText("La contraseña se ha cambiado exitosamente!!");
                }else{
                    msg.setText("Las contraseñas no coinciden");
                    Toast.makeText(getActivity(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        msg.setText("Proceso cancelado");
                    }
                });
        AlertDialog alert1 = alert.create();
        alert1.show();

    }
    /**
     * Tarea asincrona para cambiar clave
     */
    private class ChangePassTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(getActivity());
            progress.setTitle("Cambiando Clave de Autenticación..");
            progress.setMessage("Espere...");
            progress.show();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result=false;
            DBSistemaGestion helper= new DBSistemaGestion(getActivity());
            String new_pass=params[0];
            String user_id=params[1];
            try
            {
                helper.modificarPassword(user_id,new_pass);
                helper.close();
                Thread.sleep(1000);
                result=true;
            }
            catch(Exception ex)
            {
                Log.e("ChangePassword", "Error!", ex);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s.equals(true)){
                    Toast.makeText(getActivity(),"Clave cambiada correctamente",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public boolean validarCoincidencia(String pass1,String pass2) {
        if(pass1.equals(pass2)){
            return true;
        }
        return false;
    }
}
