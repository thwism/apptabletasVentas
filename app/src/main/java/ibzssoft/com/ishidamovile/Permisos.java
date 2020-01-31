package ibzssoft.com.ishidamovile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ListadoPermisosAdaptador;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.Permiso;
import ibzssoft.com.storage.DBSistemaGestion;

public class Permisos extends AppCompatActivity {
    private String empresa="";
    private ListView listadoPermisos;
    private ListadoPermisosAdaptador adapter;
    private ArrayList<String> transactions;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cargarPreferencias();
        transactions = new ArrayList<String>();
        listadoPermisos = (ListView) findViewById(R.id.listadoPermisos);
        cargarListado();
    }
    public void cargarListado(){
        DBSistemaGestion helper=new DBSistemaGestion(getApplicationContext());
        Cursor cursor= helper.consultarPermisosGeneral();
        llenarListadoTransacciones(cursor);
        String[] from = new String[] {};
        int[] to = new int[] {};
        adapter=new ListadoPermisosAdaptador(this, R.layout.fila_permiso, cursor, from, to);
        listadoPermisos.setAdapter(adapter);
        helper.close();
    }

    public void llenarListadoTransacciones(Cursor cur){
        if(cur.moveToFirst()){
            do {
                transactions.add(cur.getString(cur.getColumnIndex(GNTrans.FIELD_codtrans)).toString());
            }while (cur.moveToNext());
        }
    }

    public void cargarPreferencias(){
        ExtraerConfiguraciones ext =new ExtraerConfiguraciones(Permisos.this);
        empresa = ext.get(getString(R.string.key_empresa_codigo),getString(R.string.pref_codigo_empresa_default));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }


}
