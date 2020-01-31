package ibzssoft.com.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.ShopSpinnerAdapter;
import ibzssoft.com.dialogs.ClienteDialogFragment;
import ibzssoft.com.interfaces.ClienteDialogInterface;
import ibzssoft.com.ishidamovile.Configuraciones;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ShoppingCar;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.Shop;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.MsgUtils;
import timber.log.Timber;

/**
 * Created by Usuario-pc on 17/04/2017.
 */
public class Settings_Fragment extends Fragment  implements View.OnClickListener{
    private String empresa,grupo;
    private String [] accesos;
    private int busqueda_clientes;
    private Shop trans_selected;
    private TextView params,param_client;
    /**
     * Spinner offering all available shops.
     **/
    private Spinner spinShopSelection;
    private List<Shop> shopList;
    /**
     * Button for start activity shoppingcar
     */
    private Button start;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        this.extraerParametros();
        this.trans_selected = null;
        spinShopSelection = (Spinner) view.findViewById(R.id.settings_shop_selection_spinner);
        start = (Button) view.findViewById(R.id.cart_order);
        start.setOnClickListener(this);
        this.param_client = (TextView)view.findViewById(R.id.param_client);
        this.params = (TextView)view.findViewById(R.id.params);
        consultarMisTransacciones();
        setSpinShops(shopList);
        LinearLayout licensesLayout = (LinearLayout) view.findViewById(R.id.settings_licenses_layout);
        licensesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClienteDialogFragment df = ClienteDialogFragment.newInstance(new ClienteDialogInterface() {
                    @Override
                    public void onClienteSelected(Shop results) {
                        if(results!=null&&!results.getClienteid().isEmpty()){
                            trans_selected = results;
                            params.setText(trans_selected.toString());
                            //param_client.setText(trans_selected.getClientename());
                            param_client.setText(results.toString());
                        }
                    }

                    @Override
                    public void onClienteCancelled() {

                    }
                },accesos,busqueda_clientes,trans_selected);
                if (df != null)
                    df.show(getFragmentManager(), "clienteDialogFragment");
                else {
                    MsgUtils.showToast(getActivity(), MsgUtils.TOAST_TYPE_INTERNAL_ERROR, null, MsgUtils.ToastLength.SHORT);
                }
            }
        });

        return view;
    }

    public void extraerParametros(){
        this.empresa = getArguments().getString("empresa").toString();
        this.grupo= getArguments().getString("grupo").toString();
        this.accesos= getArguments().getString("accesos").toString().split(",");
        this.busqueda_clientes= getArguments().getInt("busqueda_clientes",0);
    }

    public void consultarMisTransacciones(){
        shopList = new ArrayList<>();
        DBSistemaGestion helper = new DBSistemaGestion(getActivity());
        Cursor cursor = helper.consultarPermisos(grupo, empresa, "", false, false,new String[]{"IV"});
        if(cursor.moveToFirst()){
            do{
                Shop shop= new Shop();
                shop.setCodtrans(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_codtrans)));
                shop.setNombretrans(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_nombretrans)));
                shop.setIdbodegapre(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_idbodegapre)));
                shop.setNumfilas(cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_numfilas)));
                shop.setMaxdocs(cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_maxdocs)));
                shop.setDiasgracia(cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_diasgracia)));
                shop.setOpciones(cursor.getInt(cursor.getColumnIndex(GNTrans.FIELD_opciones)));
                if(cursor.getString(cursor.getColumnIndex(GNTrans.FIELD_preciopcgrupo)).equals("S"))
                    shop.setPreciopcgrupo(1);
                else shop.setPreciopcgrupo(0);
                shopList.add(shop);
            }while(cursor.moveToNext());
        }
        cursor.close();
        helper.close();
    }

    /**
     * Prepare spinner with shops and pre-select already selected one.
     *
     * @param shops list of shops received from server.
     */
    private void setSpinShops(List<Shop> shops) {
        ShopSpinnerAdapter adapterLanguage = new ShopSpinnerAdapter(getActivity(), shops, false);
        spinShopSelection.setAdapter(adapterLanguage);
        spinShopSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Shop selectedShop = (Shop) parent.getItemAtPosition(position);
                trans_selected = selectedShop;
                trans_selected.setClienteid(null);
                trans_selected.setClientename(null);
                param_client.setText(getString(R.string.License_details_for_open_source_software));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                trans_selected = null;
                Timber.d("Nothing selected");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cart_order:
                try{
                    if(this.trans_selected!=null&&!this.trans_selected.getClienteid().isEmpty()){
                        Intent intent= new Intent(getContext(),ShoppingCar.class);
                        intent.putExtra("shop",trans_selected);
                        startActivity(intent);
                    }else
                        new Alertas(getContext(),"Alerta","Por favor seleccione un cliente").mostrarMensaje();
                }catch (Exception e){
                    new Alertas(getContext(),"Error","Se ha producido un error verifique que se haya seleccionado un cliente").mostrarMensaje();
                }
                break;
        }
    }
}
