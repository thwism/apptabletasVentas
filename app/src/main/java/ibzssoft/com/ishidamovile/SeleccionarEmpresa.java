package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ListadoEmpresasAdaptador;
import ibzssoft.com.modelo.Empresa;
import ibzssoft.com.storage.DBSistemaGestion;

public class SeleccionarEmpresa extends AppCompatActivity {
    private ListView listadoEmpresas;
    private ArrayList<String>empresas;
    private ListadoEmpresasAdaptador adapter;
    private String usuario;
    private String preferenciaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_empresa);
        listadoEmpresas=(ListView)findViewById(R.id.listadoEmpresas);
        empresas= new ArrayList<String>();
        cargarPreferencias();
        validarAccesos();
    }
    public void validarAccesos(){
        if(usuario.equals(getString(R.string.pref_act_user_default))){
            Intent intent = new Intent(SeleccionarEmpresa.this, MainActivity.class);
            guardarPreferencias();
            startActivity(intent);
            finish();
        }else{
            if(!usuario.equals(preferenciaUsuario)){
                cargarListado();
                listadoEmpresas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        cargarProceso(position);
                    }
                });
            }else{
                Intent intent = new Intent(SeleccionarEmpresa.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void cargarPreferencias(){
        usuario = getIntent().getStringExtra("usuario");
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(SeleccionarEmpresa.this);
        preferenciaUsuario = extraerConfiguraciones.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
    }

    public void cargarProceso(int posicion){
        Toast.makeText(getApplicationContext(),empresas.get(posicion).toString(), Toast.LENGTH_SHORT).show();
        CargarEmpresa taskLoadEmp= new CargarEmpresa();
        taskLoadEmp.execute(empresas.get(posicion).toString());
    }


    private void guardarPreferencias(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(SeleccionarEmpresa.this);
        ext.update(getString(R.string.key_act_est),getString(R.string.pref_act_est_loggin));
        ext.update(getString(R.string.key_act_user),getIntent().getStringExtra("usuario"));
        ext.update(getString(R.string.key_act_ven),getIntent().getStringExtra("vendedor"));
        ext.update(getString(R.string.key_act_gru),getIntent().getStringExtra("grupo"));
        ext.update(getString(R.string.key_act_nom),getIntent().getStringExtra("nombres"));
        ext.update(getString(R.string.key_act_sup),getIntent().getStringExtra("supervisor"));
        ext.update(getString(R.string.key_act_lin),getIntent().getStringExtra("lineas").trim());
        ext.update(getString(R.string.key_act_rut),getIntent().getStringExtra("rutas").trim());
        ext.update(getString(R.string.key_act_acc),getIntent().getStringExtra("accesos").trim());
        ext.update(getString(R.string.key_act_bod),getIntent().getStringExtra("bodegas").trim());
    }

    private void cargarListado() {
        DBSistemaGestion helper=new DBSistemaGestion(getApplicationContext());
        final Cursor cursor= helper.consultarEmpresas();
        String[] from = new String[] {};
        int[] to = new int[] {};
        adapter=new ListadoEmpresasAdaptador(this, R.layout.fila_empresa, cursor, from, to);
        listadoEmpresas.setAdapter(adapter);
        llenarListadoEmpresas(cursor);
        helper.close();
    }
    public void llenarListadoEmpresas(Cursor cur){
        if(cur.moveToFirst()){
            do{
                empresas.add(cur.getString(cur.getColumnIndex(Empresa.FIELD_idempresa)).toString());
            }while (cur.moveToNext());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
    public class CargarEmpresa extends AsyncTask<String,Void,Boolean>{
        ProgressDialog progress;
        String empresa="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(SeleccionarEmpresa.this);
            progress.setTitle("Cargando Empresa");
            progress.setMessage("Guardando preferencias..");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            empresa=params[0];
            boolean result=false;
            try {
                guardarPreferencias();
                Thread.sleep(1000);
                result= true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(progress.isShowing()){
                progress.dismiss();
                if(result){
                    Intent intent= new Intent(SeleccionarEmpresa.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
