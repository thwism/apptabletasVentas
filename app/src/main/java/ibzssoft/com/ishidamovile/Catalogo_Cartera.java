package ibzssoft.com.ishidamovile;

import android.app.SearchManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.CatalogoCarteraAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.storage.DBSistemaGestion;

public class Catalogo_Cartera extends Fragment implements android.support.v7.widget.SearchView.OnQueryTextListener {
    private RecyclerView mRecyclerView;
    private CatalogoCarteraAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private StaggeredGridLayoutManager mGridLayoutManager;
    private ArrayList<PCKardex> pckardexs;
    /*Totales*/
    private TextView countResults;
    private TextView txtVFact;
    private TextView txtVCan;
    private TextView txtSVen;
    private TextView txtSVenci;
    private int conf_descarga_cartera;
    private String[] rutas;
    private String[] accesos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_catalogo__cartera, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_cartera);
        mLayoutManager = new LinearLayoutManager(getActivity());
        pckardexs = new ArrayList<>();
        txtVFact = (TextView) view.findViewById(R.id.catCarteraVFact);
        txtVCan = (TextView) view.findViewById(R.id.catCarteraVCan);
        txtSVen = (TextView) view.findViewById(R.id.catCarteraSVen);
        txtSVenci = (TextView) view.findViewById(R.id.catCarteraSVenci);
        countResults = (TextView) view.findViewById(R.id.catCarteraResults);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.cargarPreferenciasCatalogo();
        this.consultarClientes();
        mAdapter = new CatalogoCarteraAdapter(getActivity(), pckardexs);
        countResults.setText(String.valueOf(mAdapter.getItemCount()));
        mGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Metodos de filtrado
     */
    public void cargarPreferenciasCatalogo() {
        ExtraerConfiguraciones extraerConfiguraciones = new ExtraerConfiguraciones(getActivity());
        String conf = extraerConfiguraciones.get(getString(R.string.key_conf_descarga_cartera), "0");
        conf_descarga_cartera = Integer.parseInt(conf);
        rutas = extraerConfiguraciones.get(getString(R.string.key_act_rut), getString(R.string.pref_act_rut_default)).split(",");
        accesos = extraerConfiguraciones.get(getString(R.string.key_act_acc), getString(R.string.pref_act_acc_default)).split(",");
    }


    public void consultarClientes() {
        DBSistemaGestion helper = new DBSistemaGestion(getActivity());
        Cursor cursor = null;
        switch (conf_descarga_cartera) {
            case 0:
                cursor = helper.consultarCarteraxVendedor(accesos, false);
                break;
            case 1:
                cursor = helper.consultarCarteraxRutas(rutas, false);
                break;
            case 2:
                cursor = helper.consultarCarteraxCobrador(accesos, false);
                break;
        }
        cargarListado(cursor);
        helper.close();
    }

    public void cargarListado(Cursor cur) {
        double vfacts = 0.d;
        double vcans = 0.d;
        double svenc = 0.d;
        double svenci = 0.d;

        PCKardex pcKardex = null;
        if (cur.moveToFirst()) {
            do {
                pcKardex = new PCKardex(
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
                svenc += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldoxvence));
                svenci += cur.getDouble(cur.getColumnIndex(PCKardex.FIELD_saldovencido));
                pckardexs.add(pcKardex);
            } while (cur.moveToNext());
            txtSVen.setText(redondearNumero(svenc));
            txtSVenci.setText(redondearNumero(svenci));
            txtVCan.setText(redondearNumero(vcans));
            txtVFact.setText(redondearNumero(vfacts));
        }
        cur.close();
    }

    public String redondearNumero(double numero) {
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",", ".");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_catalogo_cartera, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(pckardexs);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<PCKardex> filteredModelList = filter(pckardexs, query);
        mAdapter.animateTo(filteredModelList);
        countResults.setText(this.redondearNumero(mAdapter.getItemCount()));
        txtSVen.setText(this.redondearNumero(mAdapter.getSaldoVencer()));
        txtSVenci.setText(this.redondearNumero(mAdapter.getSaldoVencido()));
        txtVCan.setText(this.redondearNumero(mAdapter.getValorCancelado()));
        txtVFact.setText(this.redondearNumero(mAdapter.getValorFacturas()));
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<PCKardex> filter(List<PCKardex> models, String query) {
        query = query.toLowerCase();
        final List<PCKardex> filteredModelList = new ArrayList<>();
        for (PCKardex model : models) {
            final String text = model.getNombrecli().toLowerCase();
            final String text2 = model.getTrans().toLowerCase();
            if (text.contains(query) || text2.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
