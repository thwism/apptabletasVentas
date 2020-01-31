package ibzssoft.com.ishidamovile.reportes;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ibzssoft.com.adaptadores.ListadoMisPermisosAdaptador;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.ItemPendiente;
import ibzssoft.com.storage.DBSistemaGestion;

public class ItemsPendientes extends AppCompatActivity {

    private String cliente;
    private ListadoMisPermisosAdaptador adapter;
    private TextView txtNombre,txtAlterno,txtCedula;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion;
    private static final int LIMITE=100;
    private Resources resources;
    private TableLayout tabla;
    private TextView [] fechas,trans,codigos,descripciones,existencias,cantidades,entregados,saldos,estados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_pendientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        cliente = getIntent().getStringExtra("cliente");
        cargarDatos(cliente);
        agregarFilasTabla((ArrayList<ItemPendiente>) getIntent().getExtras().getSerializable("items"));
    }
    public void inicializarComponentes(){
        txtCedula= (TextView)findViewById(R.id.infoPPendienteRUC);
        txtAlterno= (TextView)findViewById(R.id.infoPPendienteAlterno);
        txtNombre= (TextView)findViewById(R.id.infoPPendienteNombre);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.infoItemPendienteTableLayout);
        resources=this.getResources();
        fechas = new TextView[LIMITE];trans = new TextView[LIMITE];codigos= new TextView[LIMITE];descripciones= new TextView[LIMITE];
        existencias= new TextView[LIMITE];cantidades = new TextView[LIMITE];entregados = new TextView[LIMITE];saldos = new TextView[LIMITE];
        estados= new TextView[LIMITE];
    }

    public void cargarDatos(String cliente){
        try{
            DBSistemaGestion helper = new DBSistemaGestion(this);
            Cursor cursor = helper.obtenerCliente(cliente);
            if(cursor.moveToFirst()){
                txtAlterno.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)));
                txtCedula.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)));
                txtNombre.setText(cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)));
            }
            cursor.close();
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void agregarFilasTabla(ArrayList<ItemPendiente> items){
        int pst=0;
        do{
            TableRow fila= new TableRow(this);
            fila.setLayoutParams(layoutFila);
            if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

            layoutCeldaDescripcion.span=2;
            descripciones[pst]= new TextView(this);
            descripciones[pst].setTextSize(11);
            descripciones[pst].setMaxLines(1);
            descripciones[pst].setText(items.get(pst).getDescripcion());
            descripciones[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            descripciones[pst].setLayoutParams(layoutCeldaDescripcion);
            descripciones[pst].setGravity(Gravity.LEFT);
            descripciones[pst].setPadding(5, 5, 5, 5);

            layoutCeldas.span = 1 ;
            cantidades[pst]= new TextView(this);
            cantidades[pst].setTextSize(11);
            cantidades[pst].setText(String.valueOf(items.get(pst).getSolicita()));
            cantidades[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            cantidades[pst].setLayoutParams(layoutCeldas);
            cantidades[pst].setGravity(Gravity.CENTER);
            cantidades[pst].setPadding(5, 5, 5, 5);


            fechas[pst]= new TextView(this);
            fechas[pst].setTextSize(11);
            fechas[pst].setText(new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimpleFormat(items.get(pst).getFecha())));
            fechas[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            fechas[pst].setLayoutParams(layoutCeldas);
            fechas[pst].setGravity(Gravity.CENTER);
            fechas[pst].setPadding(5, 5, 5, 5);



            trans[pst]= new TextView(this);
            trans[pst].setTextSize(11);
            trans[pst].setText(String.valueOf(items.get(pst).getTrans()));
            trans[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            trans[pst].setLayoutParams(layoutCeldas);
            trans[pst].setGravity(Gravity.CENTER);
            trans[pst].setPadding(5, 5, 5, 5);

            codigos[pst]= new TextView(this);
            codigos[pst].setTextSize(11);
            codigos[pst].setText(String.valueOf(items.get(pst).getCodigo()));
            codigos[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            codigos[pst].setLayoutParams(layoutCeldas);
            codigos[pst].setGravity(Gravity.CENTER);
            codigos[pst].setPadding(5, 5, 5, 5);

            existencias[pst]= new TextView(this);
            existencias[pst].setTextSize(11);
            existencias[pst].setText(String.valueOf(items.get(pst).getExistencia()));
            existencias[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            existencias[pst].setLayoutParams(layoutCeldas);
            existencias[pst].setGravity(Gravity.CENTER);
            existencias[pst].setPadding(5, 5, 5, 5);

            entregados[pst]= new TextView(this);
            entregados[pst].setTextSize(11);
            entregados[pst].setText(String.valueOf(items.get(pst).getEntrega()));
            entregados[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            entregados[pst].setLayoutParams(layoutCeldas);
            entregados[pst].setGravity(Gravity.CENTER);
            entregados[pst].setPadding(5, 5, 5, 5);

            saldos[pst]= new TextView(this);
            saldos[pst].setTextSize(11);
            saldos[pst].setText(String.valueOf(items.get(pst).getSaldo()));
            saldos[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            saldos[pst].setLayoutParams(layoutCeldas);
            saldos[pst].setGravity(Gravity.CENTER);
            saldos[pst].setPadding(5, 5, 5, 5);

            estados[pst]= new TextView(this);
            estados[pst].setTextSize(11);
            estados[pst].setText(String.valueOf(items.get(pst).getEstado()));
            estados[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            estados[pst].setLayoutParams(layoutCeldas);
            estados[pst].setGravity(Gravity.CENTER);
            estados[pst].setPadding(5, 5, 5, 5);

            fila.setPadding(0,5,0,5);
            fila.addView(fechas[pst]);
            fila.addView(trans[pst]);
            fila.addView(codigos[pst]);
            fila.addView(descripciones[pst]);
            fila.addView(existencias[pst]);
            fila.addView(cantidades[pst]);
            fila.addView(entregados[pst]);
            fila.addView(saldos[pst]);
            fila.addView(estados[pst]);
            tabla.addView(fila);

            pst++;
        }while (pst<items.size());
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
