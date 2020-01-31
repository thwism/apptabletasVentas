package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;


public class CargandoDatos extends AppCompatActivity {
    public boolean estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando_datos);
        cargarPreferencias();
        if(estado){
            Intent intent= new Intent(CargandoDatos.this,Splash.class);
            startActivity(intent);
            finish();
        }else{
            CargarDatos cargar= new CargarDatos();
            cargar.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cargando_datos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void cargarPreferencias(){
        SharedPreferences preferencias=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        estado=preferencias.getBoolean("isLoad",false);
    }

    private void guardarPreferencias(boolean valor,String path){
        SharedPreferences preferencias=getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferencias.edit();
        editor.putBoolean("isLoad",valor);
        editor.putString("directorio", path);
        editor.commit();
    }

    public String crearDirectorio(String carpeta){
        File file = new File(Environment.getExternalStorageDirectory()+"/"+carpeta+"/");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directorio creado!!");
            } else {
                System.out.println("No se puede crear el directorio");
            }
        }
        return Environment.getExternalStorageDirectory()+"/"+carpeta+"/";
    }

  /*  public String crearSubDirectorio(String carpeta){
        File file = new File(Environment.getExternalStorageDirectory()+"/"+getResources().getString(R.string.title_folder_root)+"/"+carpeta+"/");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directorio creado!!");
            } else {
                System.out.println("No se puede crear el directorio");
            }
        }
        return Environment.getExternalStorageDirectory()+"/"+carpeta+"/";
    }
*/
    private class CargarDatos extends AsyncTask<Void,Void,Void> {

        ProgressDialog progress;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String time=sdf.format(new Date());
      ArrayList<Grupo>grupos=new ArrayList<>(Arrays.asList(
              new Grupo("IAZ","Grupo Ishida y Asociados",time)
      ));

        ArrayList<Usuario>usuarios=new ArrayList<>(Arrays.asList(
                new Usuario("IA","IAZ","ISHIDA & ASOCIADOS",1,1,"111",time)
        ));

      ArrayList<Vendedor>vendedores=new ArrayList<>(Arrays.asList(
              new Vendedor(80,"IA","IA","ISHIDA & ASOCIADOS",1,1,1,"","","","",time)
      ));

        @Override
        protected void onPreExecute(){
            progress=new ProgressDialog(CargandoDatos.this);
            progress.setTitle("Cargando");
            progress.setMessage("Guardando Registros..");
            progress.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            String path=crearDirectorio(getResources().getString(R.string.title_folder_root));
            /*creando sub directorios*/
            guardarPreferencias(true,path);
            if(progress.isShowing()){
                progress.dismiss();
                ExtraerConfiguraciones ext = new ExtraerConfiguraciones(CargandoDatos.this);
                ext.update(getString(R.string.key_empresa_nro_compilacion),getString(R.string.pref_nro_compilacion_actual));
                Intent intent= new Intent(CargandoDatos.this,Splash.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            DBSistemaGestion helper= new DBSistemaGestion(CargandoDatos.this);
            for(Grupo gru:grupos) {
                try {
                    helper.crearGrupo(gru);
                    System.out.println("Grupo creado: "+gru.toString());
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            for(Usuario usr:usuarios) {
                try {
                    helper.crearUsuario(usr);
                    System.out.println("Usuario creado: "+usr.toString());
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            for(Vendedor vend:vendedores) {
                try {
                    System.out.println("Vendedor creado: "+vend.toString());
                    helper.crearVendedor(vend);
                    Thread.sleep(50);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            helper.close();
            return null;
        }
    }

}
