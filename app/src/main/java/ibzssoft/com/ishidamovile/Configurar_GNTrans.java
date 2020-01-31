package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.storage.DBSistemaGestion;

public class Configurar_GNTrans extends AppCompatActivity {
    private EditText carteta_estado,cod_trans,nom_trans,desc_trans,filas_trans,bodega,num_max_docs,cantidad_pre,precio_pre,cliente_pre;
    private CheckBox precioPCG;
    private String gntrans;
    private ArrayList<String> transactions;
    private GNTrans gnTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar__gntrans);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        cargarConfiguracion();

     /*guardar cambios*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.coordinator), "Guardar Cambios", Snackbar.LENGTH_LONG).setAction("Si", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ModificarTransaccion modificarTransaccion= new ModificarTransaccion();
                        modificarTransaccion.execute();
                    }
                }).show();
            }
        });
    }
            public void inicializarComponentes() {
                carteta_estado  = (EditText) findViewById(R.id.conf_trans_estado);
                cod_trans = (EditText) findViewById(R.id.conf_trans_codigo);
                nom_trans = (EditText) findViewById(R.id.conf_trans_nombre);
                desc_trans = (EditText) findViewById(R.id.conf_trans_desc);
                filas_trans = (EditText) findViewById(R.id.conf_trans_limite);
                bodega = (EditText) findViewById(R.id.conf_trans_bodega);
                num_max_docs = (EditText) findViewById(R.id.conf_trans_max_docs);
                precioPCG = (CheckBox) findViewById(R.id.conf__trans_precio_pcg);
                cantidad_pre = (EditText) findViewById(R.id.conf_trans_cantidad_pre);
                precio_pre = (EditText) findViewById(R.id.conf_trans_precio_pre);
                cliente_pre = (EditText) findViewById(R.id.conf_trans_cliente_pre);

                transactions = new ArrayList<String>();
                gntrans = getIntent().getStringExtra("gntrans");
            }

            public boolean cargarConfiguracion() {
                DBSistemaGestion helper = new DBSistemaGestion(getApplicationContext());
                Cursor cursor = helper.consultarGNTrans(gntrans);
                    if (cursor.moveToFirst()) {
                        gnTrans = new GNTrans(
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_codtrans)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idbodegapre)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_descripcion)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_nombretrans)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_preciopcgrupo)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_numfilas)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_maxdocs)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_opciones)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_bandobservacion)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_numdiasvencidos)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_diasgracia)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_codpantalla)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_modulo)),
                                cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_fecha_grabado))
                        );
                        cod_trans.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_codtrans)));
                        nom_trans.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_nombretrans)));
                        desc_trans.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_descripcion)));
                        filas_trans.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_numfilas)));
                        num_max_docs.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_maxdocs)));
                        cliente_pre.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idclientepre)));
                        precio_pre.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_preciopre)));
                        cantidad_pre.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_cantidadpre)));
                        bodega.setText(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                        int estado = cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_opciones));
                        if(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S")){
                            precioPCG.setChecked(true);
                        }
                            switch (estado){
                                case 0:
                                    carteta_estado.setText("Deshabilitado");
                                    break;
                                case 1:
                                    carteta_estado.setText("Advertir");
                                    break;
                                case 2:
                                    carteta_estado.setText("Advertir");
                                    break;
                                case 3:
                                    carteta_estado.setText("Bloquear");
                                    break;
                            }
                        }
                helper.close();
                return true;
            }

            public boolean guardarCambios() {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String time = sdf.format(new Date());
                    DBSistemaGestion helper = new DBSistemaGestion(Configurar_GNTrans.this);
                    gnTrans.setNombretrans(nom_trans.getText().toString());
                    gnTrans.setDescripcion(desc_trans.getText().toString());
                    gnTrans.setNumfilas(filas_trans.getText().toString());
                    gnTrans.setIdbodegapre(bodega.getText().toString());
                    helper.modificarGNTrans(gnTrans);
                    helper.close();

                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            class ModificarTransaccion extends AsyncTask<Void, Boolean, Boolean> {
                private ProgressDialog progress;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progress = new ProgressDialog(Configurar_GNTrans.this);
                    progress.setTitle("Guardando Configuracion");
                    progress.setMessage("Espere...");
                    progress.show();
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    if (progress.isShowing()) {
                        progress.dismiss();
                        if (aBoolean) {
                            finish();
                            startActivity(getIntent());
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.info_save_configuration, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.info_unsave_configuration, Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }
                }


                @Override
                protected Boolean doInBackground(Void... params) {
                    boolean result = false;
                    try {
                        result = guardarCambios();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
