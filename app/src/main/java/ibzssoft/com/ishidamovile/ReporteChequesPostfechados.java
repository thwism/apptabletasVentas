package ibzssoft.com.ishidamovile;

import android.content.res.Resources;
import android.database.Cursor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.modelo.Cheque;
import ibzssoft.com.modelo.json.Item;
import ibzssoft.com.storage.DBSistemaGestion;

public class ReporteChequesPostfechados extends AppCompatActivity {
    private TextView facturaNro;
    private TextView facturaNombres;
    private TextView facturaCedula;
    private TextView facturaAlterno;
    private TextView facturaFecha;
    private TextView facturaDireccion;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion,layoutCeldaNro;
    private TableLayout tabla;
    private TextView [] titular,banco,transaccion,numero,vencimiento,valor;
    private static final int LIMITE = 100;
    private Resources resources;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_cheques_postfechados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        items = (ArrayList<Item>) getIntent().getExtras().getSerializable("cheques");
        cargarDatos(getIntent().getStringExtra("factura"));
        agregarFilasTabla((ArrayList<Cheque>)getIntent().getExtras().getSerializable("cheques"));
    }
    public void inicializarComponentes(){
        facturaNro= (TextView)findViewById(R.id.facturaNro);
        facturaNombres= (TextView)findViewById(R.id.facturaNombres);
        facturaCedula= (TextView)findViewById(R.id.facturaCedula);
        facturaAlterno= (TextView)findViewById(R.id.facturaAlterno);
        facturaFecha= (TextView)findViewById(R.id.facturaFecha);
        facturaDireccion= (TextView)findViewById(R.id.facturaDireccion);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,40,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaNro= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.chequesTableLayout);
        titular=new TextView[LIMITE];banco=new TextView[LIMITE];numero=new TextView[LIMITE];vencimiento=new TextView[LIMITE];
        valor=new TextView[LIMITE];
        transaccion=new TextView[LIMITE];
        resources=this.getResources();
    }

    public void cargarDatos(String transaccion){
        try{
            DBSistemaGestion helper = new DBSistemaGestion(this);
            Cursor cursor = helper.consultarCarteraClienteFactura(transaccion);
            facturaNro.setText(getIntent().getStringExtra("factura"));
            facturaNombres.setText(getIntent().getStringExtra("nombres"));
            facturaAlterno.setText(getIntent().getStringExtra("comercial"));
            facturaDireccion.setText(getIntent().getStringExtra("direccion"));
            facturaCedula.setText(getIntent().getStringExtra("cedula"));
            String date = getIntent().getStringExtra("fecha");
            Date f1 = new ParseDates().changeStringToDateSimple(date);
            facturaFecha.setText(new ParseDates().changeDateToStringSimple1(f1));
            cursor.close();
            helper.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void agregarFilasTabla(ArrayList<Cheque> items){
        int pst=0;
        do{
            TableRow fila= new TableRow(this);
            fila.setLayoutParams(layoutFila);
            if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

            layoutCeldaDescripcion.span=5;
            titular[pst]= new TextView(this);
            titular[pst].setEnabled(false);
            titular[pst].setFocusable(false);
            titular[pst].setTextSize(12);
            titular[pst].setMaxLines(1);
            titular[pst].setText(items.get(pst).getTitular_cheque());
            titular[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            titular[pst].setLayoutParams(layoutCeldaDescripcion);
            titular[pst].setGravity(Gravity.LEFT);
            titular[pst].setPadding(5, 5, 5, 5);


            layoutCeldas.span=1;
            banco[pst]= new TextView(this);
            banco[pst].setTextSize(12);
            banco[pst].setText(String.valueOf(items.get(pst).getNombre_banco()));
            banco[pst].setEnabled(false);
            banco[pst].setFocusable(false);
            banco[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            banco[pst].setLayoutParams(layoutCeldas);
            banco[pst].setGravity(Gravity.CENTER);
            banco[pst].setPadding(5, 5, 5, 5);

            transaccion[pst]= new TextView(this);
            transaccion[pst].setTextSize(12);
            transaccion[pst].setText(String.valueOf(items.get(pst).getTrans_ingreso()));
            transaccion[pst].setEnabled(false);
            transaccion[pst].setFocusable(false);
            transaccion[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            transaccion[pst].setLayoutParams(layoutCeldas);
            transaccion[pst].setGravity(Gravity.CENTER);
            transaccion[pst].setPadding(5, 5, 5, 5);

            numero[pst]= new TextView(this);
            numero[pst].setTextSize(12);
            numero[pst].setFocusable(false);
            numero[pst].setEnabled(false);
            numero[pst].setText(String.valueOf(items.get(pst).getNum_cheque()));
            numero[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            numero[pst].setLayoutParams(layoutCeldas);
            numero[pst].setGravity(Gravity.CENTER);
            numero[pst].setPadding(5, 5, 5, 5);


            vencimiento[pst]= new TextView(this);
            vencimiento[pst].setTextSize(12);
            vencimiento[pst].setFocusable(false);
            String date = items.get(pst).getFecha_vencimiento();
            Date f1 = new ParseDates().changeStringToDateSimpleFormat(date);
            vencimiento[pst].setText(new ParseDates().changeDateToStringSimple1(f1));
            vencimiento[pst].setEnabled(false);
            vencimiento[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            vencimiento[pst].setLayoutParams(layoutCeldas);
            vencimiento[pst].setGravity(Gravity.CENTER);
            vencimiento[pst].setPadding(5, 5, 5, 5);

            valor[pst]= new TextView(this);
            valor[pst].setTextSize(12);
            valor[pst].setFocusable(false);
            valor[pst].setText(String.valueOf(items.get(pst).getValor_cheque()));
            valor[pst].setEnabled(false);
            valor[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            valor[pst].setLayoutParams(layoutCeldas);
            valor[pst].setGravity(Gravity.CENTER);
            valor[pst].setPadding(5, 5, 5, 5);



            fila.addView(titular[pst]);
            fila.addView(banco[pst]);
            fila.addView(transaccion[pst]);
            fila.addView(numero[pst]);
            fila.addView(vencimiento[pst]);
            fila.addView(valor[pst]);
            tabla.addView(fila);

            pst++;
        }while (pst<items.size());
    }

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
