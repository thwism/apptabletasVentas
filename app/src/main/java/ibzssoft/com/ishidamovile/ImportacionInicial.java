package ibzssoft.com.ishidamovile;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.recibir.RecibirBanco;
import ibzssoft.com.recibir.RecibirBodega;
import ibzssoft.com.recibir.RecibirCanton;
import ibzssoft.com.recibir.RecibirEmpresas;
import ibzssoft.com.recibir.RecibirFormasCobro;
import ibzssoft.com.recibir.RecibirGNTrans;
import ibzssoft.com.recibir.RecibirGrupos;
import ibzssoft.com.recibir.RecibirParroquia;
import ibzssoft.com.recibir.RecibirPermisos;
import ibzssoft.com.recibir.RecibirPermisosTrans;
import ibzssoft.com.recibir.RecibirProvincias;
import ibzssoft.com.recibir.RecibirUsuarios;
import ibzssoft.com.recibir.RecibirVendedores;

public class ImportacionInicial extends AppCompatActivity implements View.OnClickListener{
    private boolean estado=false;
    private String ip_conf,port_conf,url_conf;
    private EditText ip,port,url;
    private Button btnGuardar;
    private LinearLayout contenedorExtraerBD,contenedorConexion;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_importacion_inicial);
        cargarPreferenciasIniciales();
        if(estado!=true){
            crearDialog();
            btnGuardar.setOnClickListener(this);
        }else{
            Intent intent = new Intent(ImportacionInicial.this,SeleccionarEmpresa.class);
            intent.putExtra("usuario",getIntent().getStringExtra("usuario"));
            intent.putExtra("grupo",getIntent().getStringExtra("grupo"));
            intent.putExtra("vendedor",getIntent().getStringExtra("vendedor"));
            intent.putExtra("supervisor", getIntent().getIntExtra("supervisor", 0));
            intent.putExtra("accesos",getIntent().getStringExtra("accesos"));
            intent.putExtra("rutas",getIntent().getStringExtra("rutas"));
            intent.putExtra("lineas",getIntent().getStringExtra("lineas"));
            intent.putExtra("bodegas",getIntent().getStringExtra("bodegas"));
            startActivity(intent);
            finish();
        }
    }
    private void animar(boolean mostrar,LinearLayout layout)
    {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar)
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
         else
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layout.setLayoutAnimation(controller);
        layout.startAnimation(animation);
    }

    public void mostrarContenedorExtraerConfiguracion(){
        if(contenedorExtraerBD.getVisibility()==View.GONE){
            animar(false, contenedorExtraerBD);
            contenedorExtraerBD.setVisibility(View.VISIBLE);
            port.setEnabled(false);
            port.setFocusable(false);
            ip.setEnabled(false);
            ip.setFocusable(false);
            url.setEnabled(false);
            url.setFocusable(false);
        }
    }
    public void ocultarContenedorConexion(){
        if(contenedorExtraerBD.getVisibility()==View.VISIBLE){
            animar(false,contenedorConexion);
            contenedorConexion.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confServiButtonEstablecer:
                    /*TareaProbarConexion taskTestConection = new TareaProbarConexion();
                    taskTestConection.execute(ip.getText().toString(), port.getText().toString(),url.getText().toString());*/
                break;
            case R.id.confServiButtonAceptar:
                    cargarPreferenciasConexion();
                    importarBD(ip_conf, port_conf,url_conf,"1999-01-01 00:00:00");
                    alertDialog.dismiss();
                break;
            default:
                break;
        }
    }
    public void cargarPreferenciasConexion() {
        SharedPreferences preferencias=this.getSharedPreferences("conf_servi", Context.MODE_PRIVATE);
        ip_conf=preferencias.getString("ip_servidor", "127.0.0.0");
        port_conf=preferencias.getString("puerto_servidor", "0000");
        url_conf=preferencias.getString("url_servidor", "Enlace");
    }

    public boolean importarBD(String ip_param,String port_param,String url_param,String ultMod){
        try{
                RecibirProvincias recibirProvincia= new RecibirProvincias(this);
                recibirProvincia.ejecutartarea();
                guardarPreferenciasIniciales(true);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private void guardarPreferencias(boolean valor,String ip,String puerto,String artifact){
        SharedPreferences preferencias=getSharedPreferences("conf_servi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferencias.edit();
        editor.putBoolean("isLoad", valor);
        editor.putString("ip_servidor", ip);
        editor.putString("puerto_servidor", puerto);
        editor.putString("url_servidor", artifact);
        editor.commit();
    }

    public void crearDialog(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.importacion_inicial, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Importacion Inicial");
        /*Cargar Listado*/
        btnGuardar=(Button)promptsView.findViewById(R.id.confServiButtonAceptar);
        ip=(EditText)promptsView.findViewById(R.id.confServiIP);
        //ip.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
        port=(EditText)promptsView.findViewById(R.id.confServiPort);
        port.setInputType(InputType.TYPE_CLASS_NUMBER);
        url=(EditText)promptsView.findViewById(R.id.confServiURL);
        contenedorExtraerBD=(LinearLayout)promptsView.findViewById(R.id.contenedorExtraerBD);
        contenedorConexion=(LinearLayout)promptsView.findViewById(R.id.contenedorConectarServidor);

        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void cargarPreferenciasIniciales(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(ImportacionInicial.this);
        String result = extraerConfiguraciones.get(getString(R.string.key_conf_ini),getString(R.string.pref_conf_ini_default));
        if(result.equals("1")){estado=true;}
    }

    private void guardarPreferenciasIniciales(boolean valor){
        SharedPreferences preferencias=getSharedPreferences("configuracion_inicial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferencias.edit();
        editor.putBoolean("isLoad", valor);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ImportacionInicial.this,Login.class);
        startActivity(intent);
        finish();
    }
}
