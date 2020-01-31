package ibzssoft.com.ishidamovile.reportes;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ibzssoft.com.adaptadores.CatalogoCarteraAdapter;
import ibzssoft.com.adaptadores.CatalogoCarteraPersonalAdapter;
import ibzssoft.com.ishidamovile.GenerarReporteEstadoCuentaPDF;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;

public class EstadoCuenta extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CatalogoCarteraPersonalAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<PCKardex> pckardexs;
    /*Totales*/
    private TextView txtVFact;
    private TextView txtVCan;
    private TextView txtSVen;
    private TextView txtSVenci;
    private TextView txtResults;
    private TextView txtNombres;
    private TextView txtComercial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_cuenta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtNombres= (TextView)findViewById(R.id.estadoNombres);
        txtComercial=(TextView)findViewById(R.id.estadoComercial);
        txtNombres.setText("Estado de Cuenta :: "+getIntent().getStringExtra("descripcion"));
        if(getIntent().getStringExtra("comercial").isEmpty()){
            txtComercial.setVisibility(View.GONE);
        }else{
            txtComercial.setText(getIntent().getStringExtra("comercial"));
            txtComercial.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Estado de Cuenta :: "+getIntent().getStringExtra("descripcion"));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_cartera_personal);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pckardexs= new ArrayList<>();
        txtVFact = (TextView)findViewById(R.id.estadoVFact);
        txtVCan = (TextView)findViewById(R.id.estadoVCan);
        txtSVen = (TextView)findViewById(R.id.estadoSVen);
        txtSVenci = (TextView)findViewById(R.id.estadoSVenci);
        txtResults= (TextView)findViewById(R.id.estadoResults);
        consultarCliente();
        mAdapter = new CatalogoCarteraPersonalAdapter(this, pckardexs,getIntent().getStringExtra("vendedor"));
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        txtResults.setText(String.valueOf(mAdapter.getItemCount()));
    }
        public void consultarCliente(){
        DBSistemaGestion helper= new DBSistemaGestion(this);
        Cursor cursor=helper.consultarCarteraPersonal(getIntent().getStringExtra("cliente"));
        cargarListado(cursor);
        helper.close();
    }
    public void cargarListado(Cursor cur){
        double vfacts = 0.d;
        double vcans = 0.d;
        double svenc = 0.d;
        double svenci = 0.d;
        PCKardex pcKardex=null;
        if(cur.moveToFirst()){
            do{
                pcKardex=  new PCKardex(
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idcartera)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idasignado)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_fechavenci)),
                        cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_valor)),
                        cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_pagado)),
                        cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldoxvence)),
                        cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldovencido)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_plazo)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_dvencidos)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_fechaemision)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idprovcli)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_ruc)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_nombrecliente)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_comercialcliente)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_direccioncliente)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_codforma)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_transid)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idvendedor)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idcobrador)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_trans)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_guid)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_cantcheque)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_idruta)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_doc)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_ordencuota)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_ruta)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_banco_id)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_forma_pago)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_numero_cheque)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_numero_cuenta)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_titular)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_pago_fecha_vencimiento)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_generado)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_respaldado)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_renta)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_renta_base)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_iva)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_iva_base)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_estab)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_punto)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_num_ser_secuencial)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_autorizacion)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_caducidad)),
                        cur.getInt(cur.getColumnIndex(PCKardex.FIELD_band_cobrado)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_observacion)),
                        cur.getString(cur.getColumnIndex(PCKardex.FIELD_fechaultimopago))
                );
                vcans += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_pagado));
                vfacts += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_valor));
                svenc+= cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldoxvence));
                svenci+= cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldovencido));

                pckardexs.add(pcKardex);
            }while (cur.moveToNext());
            txtSVen.setText(redondearNumero(svenc));
            txtSVenci.setText(redondearNumero(svenci));
            txtVCan.setText(redondearNumero(vcans));
            txtVFact.setText(redondearNumero(vfacts));
        }
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
        getMenuInflater().inflate(R.menu.menu_estado_cuenta_personal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_change_exportPDF:
                try{
                    GenerarReporteEstadoCuentaPDF ext = new GenerarReporteEstadoCuentaPDF(this,getIntent().getStringExtra("cliente") );
                    ext.ejecutarProceso();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
