package ibzssoft.com.ishidamovile;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;


public class ReporteExistencia extends AppCompatActivity {
    private String orden_bodegas;
    private TextView codigo;
    private TextView alterno;
    private TextView descripcion;
    private TextView presentacion;
    private TextView unidad;
    private TextView total;
    private TableLayout tabla;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaBodega;
    private TextView [] bodegas;
    private TextView [] cantidades;
    private static final int LIMIT= 100;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existencia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        System.out.println("parametros recibidos: "+ getIntent().getStringExtra("item"));
        cargarInformacion(getIntent().getStringExtra("item"));
        cargarDetallesExistencia(getIntent().getStringExtra("item"));
   }

    public void inicializarComponentes(){
        codigo = (TextView)findViewById(R.id.infoExiCodigo);
        alterno= (TextView)findViewById(R.id.infoExiAlterno);
        descripcion = (TextView)findViewById(R.id.infoExiDescripcion);
        presentacion = (TextView)findViewById(R.id.infoExiPresentacion);
        unidad = (TextView)findViewById(R.id.infoExiUnidad);
        total = (TextView)findViewById(R.id.infoExiTotal);
        tabla= (TableLayout)findViewById(R.id.infoExiTableLayout);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 50,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaBodega = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        cantidades = new  TextView [LIMIT];
        bodegas = new  TextView [LIMIT];
        resources=this.getResources();
        cargarPreferenciasBodegas();

    }
    public void cargarInformacion(String id_producto){
        DBSistemaGestion helper = new DBSistemaGestion(ReporteExistencia.this);
        Cursor cursor = helper.obtenerItem(id_producto);
        if(cursor.moveToFirst()){
            codigo.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)));
            alterno.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_alterno)));
            descripcion.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)));
            presentacion.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_presentacion)));
            unidad.setText(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_unidad)));
        }
        cursor.close();
        helper.close();
    }
    public void cargarPreferenciasBodegas() {
        ExtraerConfiguraciones extraerConfiguraciones = new ExtraerConfiguraciones(ReporteExistencia.this);
        this.orden_bodegas=extraerConfiguraciones.get(getString(R.string.key_act_bod),getString(R.string.pref_act_bod_default));
    }
    public void cargarDetallesExistencia(String id_item){
        DBSistemaGestion helper= new DBSistemaGestion(getApplicationContext());
        Cursor cursor=helper.obtenerExistenciaItem(id_item, this.orden_bodegas.split(","));
        TableRow fila;
        int pst=0;
        double tot = 0.d;
        if(cursor.moveToFirst()){
            do{
                fila = new TableRow(this);
                fila.setLayoutParams(layoutFila);
                if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

                layoutCeldas.span=1;
                TextView nro = new TextView(this);
                nro.setTextSize(14);
                nro.setText(String.valueOf(pst+1));
                nro.setTextColor(getResources().getColor(R.color.textColorPrimary));
                nro.setLayoutParams(layoutCeldas);
                nro.setGravity(Gravity.CENTER);
                nro.setPadding(5, 5, 5, 5);

                bodegas[pst]= new TextView(this);
                bodegas[pst].setMaxLines(1);
                layoutCeldaBodega.span = 5;
                bodegas[pst].setLayoutParams(layoutCeldas);
                bodegas[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                bodegas[pst].setPadding(5,5,5,5);
                bodegas[pst].setText(cursor.getString(cursor.getColumnIndex(Bodega.FIELD_codbodega)));


                cantidades[pst]= new TextView(this);
                cantidades[pst].setMaxLines(1);
                layoutCeldas.span = 1;
                cantidades[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                cantidades[pst].setLayoutParams(layoutCeldas);
                cantidades[pst].setPadding(5,5,5,5);
                cantidades[pst].setText(cursor.getString(cursor.getColumnIndex(Existencia.FIELD_existencia)));

                tot += cursor.getDouble(cursor.getColumnIndex(Existencia.FIELD_existencia));
                total.setText(redondearNumero(tot));
                fila.setPadding(2, 0, 2, 0);
                fila.addView(nro);
                fila.addView(bodegas[pst]);
                fila.addView(cantidades[pst]);
                tabla.addView(fila);
                pst++;
            }while (cursor.moveToNext());
        }else{
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);
            TextView info= new TextView(this);
            info.setText("PRODUCTO SIN STOCK");
            info.setLayoutParams(layoutCeldas);
            info.setGravity(Gravity.CENTER);
            fila.setPadding(10, 20, 10, 20);
            fila.addView(info);
            tabla.addView(fila);
        }
        cursor.close();
        helper.close();
    }

    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero);
    }
    /*Metodos Secundarios*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
}
