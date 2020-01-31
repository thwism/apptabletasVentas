package ibzssoft.com.ishidamovile;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.modelo.json.Item;
import ibzssoft.com.modelo.json.RecargoDescuento;

public class FacturaDetalle extends AppCompatActivity {

    private TextView facturaNro;
    private TextView facturaNombres;
    private TextView facturaCedula;
    private TextView facturaAlterno;
    private TextView facturaFecha;
    private TextView facturaDireccion;
    //private TextView facturaSubtotalIva;
    //private TextView facturaSubtotal0;
    //private TextView facturaSubtotal;
    //private TextView facturaIVA;
    //private TextView facturaTotal;
    private TableRow.LayoutParams layoutFila,layoutCeldas,layoutCeldaDescripcion,layoutCeldaNro;
    private TableLayout tabla, tabla2;
    private TextView [] descripciones,cantidades,precios,totales;

    private TextView [] codigo,signo,valor,suman;
    private static final int LIMITE=100;
    private Resources resources;
    private ArrayList<Item> items;
    private ArrayList<RecargoDescuento> recargos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inicializarComponentes();
        items = (ArrayList<Item>) getIntent().getExtras().getSerializable("items");
        recargos = (ArrayList<RecargoDescuento>) getIntent().getExtras().getSerializable("recargo");
        cargarDatos();
        agregarFilasTabla((ArrayList<Item>)getIntent().getExtras().getSerializable("items"));
        double subtotal_fc= calcularSubtotal((ArrayList<Item>)getIntent().getExtras().getSerializable("items"));
        agregarFilaInicialRecargos(subtotal_fc);
        agregarFilasRecargos(subtotal_fc,(ArrayList<RecargoDescuento>)getIntent().getExtras().getSerializable("recargo"));
        double total_fc= calcularTotalFactura(subtotal_fc,(ArrayList<RecargoDescuento>)getIntent().getExtras().getSerializable("recargo"));
        agregarFilaTotalFactura(total_fc,getIntent().getIntExtra("filas",0)+1);
    }

    public void inicializarComponentes(){
        facturaNro= (TextView)findViewById(R.id.facturaNro);
        facturaNombres= (TextView)findViewById(R.id.facturaNombres);
        facturaCedula= (TextView)findViewById(R.id.facturaCedula);
        facturaAlterno= (TextView)findViewById(R.id.facturaAlterno);
        facturaFecha= (TextView)findViewById(R.id.facturaFecha);
        facturaDireccion= (TextView)findViewById(R.id.facturaDireccion);
        //facturaSubtotalIva= (TextView)findViewById(R.id.fac_subtotal_con_iva);
        //facturaSubtotal0= (TextView)findViewById(R.id.fac_subtotal_sin_iva);
        //facturaSubtotal= (TextView)findViewById(R.id.fac_subtotal_total);
        //facturaIVA= (TextView)findViewById(R.id.fac_iva);
        //facturaTotal= (TextView)findViewById(R.id.fac_total);
        layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,50,1);
        layoutCeldaDescripcion = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldaNro= new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        layoutCeldas = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT,1);
        tabla= (TableLayout)findViewById(R.id.facturaTableLayout);
        tabla2= (TableLayout)findViewById(R.id.facturaRecargosTableLayout);
        cantidades=new TextView[LIMITE];descripciones=new TextView[LIMITE];totales=new TextView[LIMITE];precios=new TextView[LIMITE];
        codigo=new TextView[LIMITE];signo=new TextView[LIMITE];valor=new TextView[LIMITE];suman=new TextView[LIMITE];
        resources=this.getResources();
    }

    public void cargarDatos(){
                String date = getIntent().getStringExtra("emision");
                Date f1 = new ParseDates().changeStringToDateSimple(date);
                facturaNro.setText(getIntent().getStringExtra("factura"));
                facturaNombres.setText(getIntent().getStringExtra("nombre"));
                facturaDireccion.setText(getIntent().getStringExtra("direccion"));
                facturaAlterno.setText(getIntent().getStringExtra("alterno"));
                facturaCedula.setText(getIntent().getStringExtra("ruc"));
                facturaFecha.setText(new ParseDates().changeDateToStringSimple1(f1));
    }

    public void agregarFilasTabla(ArrayList<Item> productos){
        int pst=0;
            do{
                TableRow fila= new TableRow(this);
                fila.setLayoutParams(layoutFila);
                if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

                layoutCeldaNro.span=1;
                TextView nro = new TextView(this);
                nro.setTextSize(14);
                nro.setText(String.valueOf(pst+1));
                nro.setTextColor(getResources().getColor(R.color.textColorPrimary));
                nro.setLayoutParams(layoutCeldaNro);
                nro.setGravity(Gravity.CENTER);
                nro.setPadding(5, 5, 5, 5);

                layoutCeldaDescripcion.span=5;
                descripciones[pst]= new TextView(this);
                descripciones[pst].setEnabled(false);
                descripciones[pst].setFocusable(false);
                descripciones[pst].setTextSize(14);
                descripciones[pst].setMaxLines(1);
                descripciones[pst].setText(productos.get(pst).getDescripcion());
                descripciones[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                descripciones[pst].setLayoutParams(layoutCeldaDescripcion);
                descripciones[pst].setGravity(Gravity.LEFT);
                descripciones[pst].setPadding(5, 5, 5, 5);


                cantidades[pst]= new TextView(this);
                cantidades[pst].setTextSize(14);
                cantidades[pst].setText(String.valueOf(productos.get(pst).getCantidad()));
                cantidades[pst].setEnabled(false);
                cantidades[pst].setFocusable(false);
                cantidades[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                cantidades[pst].setLayoutParams(layoutCeldas);
                cantidades[pst].setGravity(Gravity.CENTER);
                cantidades[pst].setPadding(5, 5, 5, 5);


                precios[pst]= new TextView(this);
                precios[pst].setTextSize(14);
                precios[pst].setFocusable(false);
                precios[pst].setEnabled(false);
                precios[pst].setText(String.valueOf(productos.get(pst).getValor()));
                precios[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                precios[pst].setLayoutParams(layoutCeldas);
                precios[pst].setGravity(Gravity.CENTER);
                precios[pst].setPadding(5, 5, 5, 5);


                totales[pst]= new TextView(this);
                totales[pst].setTextSize(14);
                totales[pst].setFocusable(false);
                totales[pst].setText(String.valueOf(productos.get(pst).getTotal()));
                totales[pst].setEnabled(false);
                totales[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
                totales[pst].setLayoutParams(layoutCeldas);
                totales[pst].setGravity(Gravity.CENTER);
                totales[pst].setPadding(5, 5, 5, 5);


                fila.setPadding(10,5, 10, 5);
                fila.addView(nro);
                fila.addView(descripciones[pst]);
                fila.addView(cantidades[pst]);
                fila.addView(precios[pst]);
                fila.addView(totales[pst]);
                tabla.addView(fila);

                pst++;
            }while (pst<productos.size());
    }


    public void agregarFilaInicialRecargos(double subtotal){

        TableRow fila= new TableRow(this);
        fila.setLayoutParams(layoutFila);

        fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

        codigo[0]= new TextView(this);
        codigo[0].setEnabled(false);
        codigo[0].setFocusable(false);
        codigo[0].setTextSize(14);
        codigo[0].setMaxLines(1);
        codigo[0].setText("SUBTOTAL");
        codigo[0].setTextColor(getResources().getColor(R.color.textColorPrimary));
        codigo[0].setLayoutParams(layoutCeldas);
        codigo[0].setGravity(Gravity.LEFT);
        codigo[0].setPadding(5, 5, 5, 5);

        signo[0]= new TextView(this);
        signo[0].setLayoutParams(layoutCeldas);

        valor[0]= new TextView(this);
        valor[0].setLayoutParams(layoutCeldas);

        layoutCeldas.span=1;
        suman[0]= new TextView(this);
        suman[0].setTextSize(14);
        suman[0].setFocusable(false);
        suman[0].setText(redondearNumero(subtotal));
        suman[0].setEnabled(false);
        suman[0].setTextColor(getResources().getColor(R.color.textColorPrimary));
        suman[0].setLayoutParams(layoutCeldas);
        suman[0].setGravity(Gravity.CENTER);
        suman[0].setPadding(5, 5, 5, 5);


        fila.setPadding(10,5, 10, 5);
        fila.addView(codigo[0]);
        fila.addView(signo[0]);
        fila.addView(valor[0]);
        fila.addView(suman[0]);
        tabla2.addView(fila);
    }

    public void agregarFilaTotalFactura(double sumatoria, int pst){

        TableRow fila= new TableRow(this);
        fila.setLayoutParams(layoutFila);

        codigo[pst]= new TextView(this);
        codigo[pst].setEnabled(false);
        codigo[pst].setFocusable(false);
        codigo[pst].setTextSize(14);
        codigo[pst].setMaxLines(1);
        codigo[pst].setText("TOTAL");
        codigo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
        codigo[pst].setLayoutParams(layoutCeldas);
        codigo[pst].setGravity(Gravity.LEFT);
        codigo[pst].setPadding(5, 5, 5, 5);

        signo[pst]= new TextView(this);
        signo[pst].setLayoutParams(layoutCeldas);

        valor[pst]= new TextView(this);
        valor[pst].setLayoutParams(layoutCeldas);

        layoutCeldas.span=1;
        suman[pst]= new TextView(this);
        suman[pst].setTextSize(14);
        suman[pst].setFocusable(false);
        suman[pst].setText(redondearNumero(sumatoria));
        suman[pst].setEnabled(false);
        suman[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
        suman[pst].setLayoutParams(layoutCeldas);
        suman[pst].setGravity(Gravity.CENTER);
        suman[pst].setPadding(5, 5, 5, 5);


        fila.setPadding(10,5, 10, 5);
        fila.addView(codigo[pst]);
        fila.addView(signo[pst]);
        fila.addView(valor[pst]);
        fila.addView(suman[pst]);
        tabla2.addView(fila);
    }

    public void agregarFilasRecargos(double subtotal, ArrayList<RecargoDescuento> recargosList){
        int pst=1;
        int ind=0;
        do{
            TableRow fila= new TableRow(this);
            fila.setLayoutParams(layoutFila);
            if ( pst % 2 == 0) fila.setBackgroundColor(resources.getColor(R.color.dividerColor));

            codigo[pst]= new TextView(this);
            codigo[pst].setEnabled(false);
            codigo[pst].setFocusable(false);
            codigo[pst].setTextSize(14);
            codigo[pst].setMaxLines(1);
            codigo[pst].setText(recargosList.get(ind).getCodrecargo());
            codigo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            codigo[pst].setLayoutParams(layoutCeldas);
            codigo[pst].setGravity(Gravity.LEFT);
            codigo[pst].setPadding(5, 5, 5, 5);


            signo[pst]= new TextView(this);
            signo[pst].setTextSize(14);
            if(recargosList.get(ind).getSigno()!=0){
                signo[pst].setText(getString(R.string.suma));
            }else signo[pst].setText(getString(R.string.resta));

            signo[pst].setEnabled(false);
            signo[pst].setFocusable(false);
            signo[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            signo[pst].setLayoutParams(layoutCeldas);
            signo[pst].setGravity(Gravity.CENTER);
            signo[pst].setPadding(5, 5, 5, 5);


            valor[pst]= new TextView(this);
            valor[pst].setTextSize(14);
            valor[pst].setFocusable(false);
            valor[pst].setEnabled(false);
            valor[pst].setText(String.valueOf(recargosList.get(ind).getValor()));
            valor[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            valor[pst].setLayoutParams(layoutCeldas);
            valor[pst].setGravity(Gravity.CENTER);
            valor[pst].setPadding(5, 5, 5, 5);


            suman[pst]= new TextView(this);
            suman[pst].setTextSize(14);
            suman[pst].setFocusable(false);

            /*Recalculo del subtotal */
            if(recargosList.get(ind).getSigno()!=0){
                subtotal+=recargosList.get(ind).getValor();//sumar
            }else subtotal-=recargosList.get(ind).getValor();//restar
            suman[pst].setText(redondearNumero(subtotal));
            suman[pst].setEnabled(false);
            suman[pst].setTextColor(getResources().getColor(R.color.textColorPrimary));
            suman[pst].setLayoutParams(layoutCeldas);
            suman[pst].setGravity(Gravity.CENTER);
            suman[pst].setPadding(5, 5, 5, 5);


            fila.setPadding(10,5, 10, 5);
            fila.addView(codigo[pst]);
            fila.addView(signo[pst]);
            fila.addView(valor[pst]);
            fila.addView(suman[pst]);
            tabla2.addView(fila);
            pst++;ind++;
        }while (ind<recargosList.size());
    }


    public double calcularSubtotal(ArrayList<Item> productos){
        double subtotal=0.d;
        for(int i=0; i<productos.size(); i++){
                subtotal+=Double.parseDouble(totales[i].getText().toString());
        }
        return subtotal;
    }

    public double calcularTotalFactura(double subtotal, ArrayList<RecargoDescuento> recargos){
        double suma_recargos=0.d;
        for(RecargoDescuento recargo: recargos){
            if(recargo.getSigno()!=0){
                suma_recargos+=recargo.getValor();
            }else suma_recargos-=recargo.getValor();
        }
        return (subtotal+suma_recargos);
    }

    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
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
