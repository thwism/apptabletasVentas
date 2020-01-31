package ibzssoft.com.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ibzssoft.com.adaptadores.CategorySpinnerAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ListadoCarteraAdaptador;
import ibzssoft.com.interfaces.ClienteDialogInterface;
import ibzssoft.com.interfaces.FilterDialogInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Shop;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.recibir.RecibirProvincias;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

public class ClienteDialogFragment extends DialogFragment {

    private ClienteDialogInterface clienteDialogInterface;
    private AutoCompleteTextView autoCompleteCliente;
    private String[] accesos;
    private int modo_busqueda;
    private Shop trans;
    private TextView nombres,razon, direccion,email,observacion;
    private CardView card_info_client;
    private int mSelectedItem;

    public static ClienteDialogFragment newInstance(ClienteDialogInterface clienteDialogInterface, String [] accesos, int modo_busqueda, Shop trans) {
        ClienteDialogFragment filterDialogFragment = new ClienteDialogFragment();
        if (clienteDialogInterface == null) {
            return null;
        }
        filterDialogFragment.trans = trans;
        filterDialogFragment.accesos = accesos;
        filterDialogFragment.modo_busqueda = modo_busqueda;
        filterDialogFragment.clienteDialogInterface = clienteDialogInterface;
        return filterDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullscreen);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = d.getWindow();
            window.setLayout(width, height);
            window.setWindowAnimations(R.style.alertDialogAnimation);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_cliente, container, false);
        this.autoCompleteCliente = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteCliente);
        this.nombres= (TextView) view.findViewById(R.id.nombres);
        this.razon=  (TextView) view.findViewById(R.id.razon);
        this.direccion= (TextView) view.findViewById(R.id.direccion);
        this.email= (TextView) view.findViewById(R.id.email);
        this.observacion= (TextView) view.findViewById(R.id.observacion);
        this.card_info_client = (CardView) view.findViewById(R.id.card_info_client);

        AutoCompleteClienteSelected adapter = new AutoCompleteClienteSelected(getContext());
        this.autoCompleteCliente.setAdapter(adapter);
        this.autoCompleteCliente.setDropDownWidth(500);
        this.autoCompleteCliente.setOnItemClickListener(adapter);

        Button btnApply = (Button) view.findViewById(R.id.filter_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //String filterUrl = trans.toString();
                 clienteDialogInterface.onClienteSelected(trans);
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.filter_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clienteDialogInterface.onClienteCancelled();
                dismiss();
            }
        });
        return view;
    }

    public void mostrarContenedorInformacion() {
        if (card_info_client.getVisibility() == View.GONE || card_info_client.getVisibility() == View.INVISIBLE) {
            animar(true, card_info_client);
            card_info_client.setVisibility(View.VISIBLE);
        }
    }
    private void animar(boolean mostrar, CardView layout) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_appear);
        } else {
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_disappear);
        }
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);
        layout.setLayoutAnimation(controller);
        layout.startAnimation(animation);
    }
    /*Auto text Adapter*/
    class AutoCompleteClienteSelected extends CursorAdapter implements AdapterView.OnItemClickListener {
        DBSistemaGestion dbSistemaGestion;
        public AutoCompleteClienteSelected(Context context) {
            super(getContext(), null);
            dbSistemaGestion = new DBSistemaGestion(context);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

            Cursor cursor = null;
            try {
                cursor = dbSistemaGestion.buscarClientes(
                        (constraint != null ? constraint.toString() : "@@@@"), accesos, modo_busqueda);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return cursor;
        }

        @Override
        public CharSequence convertToString(Cursor cursor) {
            final int columnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc);
            final int columnIndex2 = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre);
            final String str = cursor.getString(columnIndex) + " --> " + cursor.getString(columnIndex2);
            return str;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int itemColumnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_ruc);
            final int descColumnIndex = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre);
            final int descAlterno = cursor.getColumnIndexOrThrow(Cliente.FIELD_nombrealterno);
            TextView text1 = (TextView) view.findViewById(R.id.text1);
            text1.setText(cursor.getString(itemColumnIndex));
            TextView text2 = (TextView) view.findViewById(R.id.text2);
            text2.setText(cursor.getString(descColumnIndex));
            TextView text3 = (TextView) view.findViewById(R.id.text3);
            text3.setText(cursor.getString(descAlterno));
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.fila_autocomplete_item, parent, false);
            return view;
        }

        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            Cursor cursor = (Cursor) listView.getItemAtPosition(position);
            String idCliente = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_idprovcli));
            String itemNombre = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_nombre));
            String itemAlterno = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_nombrealterno));
            String itemDir = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_direccion1));
            String itemCor = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_email));
            String itemObs = cursor.getString(cursor.getColumnIndexOrThrow(Cliente.FIELD_observacion));
            trans.setClienteid(idCliente);
            trans.setClientename(itemNombre);
            autoCompleteCliente.setText(itemNombre);
            autoCompleteCliente.setSelection(itemNombre.length());
            autoCompleteCliente.clearFocus();
            nombres.setText(itemNombre);
            razon.setText(itemAlterno);
            direccion.setText(itemDir);
            email.setText(itemCor);
            observacion.setText(itemObs);
            mostrarContenedorInformacion();
            //cargar configuracion segun pcgrupo
            ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getContext());
            if (trans.getPreciopcgrupo()!=0) {
                int conf_num_pcg = ext.configuracionPreciosGrupos();
                cargarPrecio(idCliente, conf_num_pcg);
            }else {
                String price = ext.get(getString(R.string.key_act_list_price), null);
                trans.setPrecios(price);
            }
            cursor.close();
            switch (trans.getOpciones()){
                case 1:
                    //advertir
                    generarReporteCartera(trans.getClienteid());
                    break;
                case 2:
                    //bloquear
                    int docs = generarReporteCartera(trans.getClienteid());
                    if(docs>trans.getMaxdocs()){
                        android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getContext());
                        builder1.setTitle("Transaccion Bloqueada");
                        builder1.setMessage("Revise el numero de documentos vencidos");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton("Continuar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        android.support.v7.app.AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                    break;
            }
            dbSistemaGestion.close();
        }
    }

    /*
         Metodo para cargar precios
     */
    public boolean cargarPrecio(String cliente, int numgrupo) {
        try {
            DBSistemaGestion helper = new DBSistemaGestion(getContext());
            Cursor cursor1 = helper.consultarPCGrupo(numgrupo, cliente);
            if (cursor1.moveToFirst()) {
                this.trans.setPrecios(obtenerNumeroPrecio(cursor1.getString(1)));
            } else {
                Toast ts = Toast.makeText(getContext(), R.string.info_no_load_price, Toast.LENGTH_SHORT);
                ts.show();
            }
            helper.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String obtenerNumeroPrecio(String inf_price) {
        String price = "";
        for (int i = 0; i < inf_price.length(); i++) {
            System.out.println("Cadenas comparadas: " + inf_price.substring(i, i + 1));
            if (inf_price.substring(i, i + 1).equals("1")) {
                price += String.valueOf(i + 1) + ",";
            }
        }
        price = price.substring(0, price.length() - 1);
        System.out.println("Resultado final: " + price);
        return price;
    }
    /**
     *
     * @param cliente //generando reporte de cartera
     */
    public int generarReporteCartera(String cliente) {
        int result = 0;
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.cartera, null);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);
        /*Cargar Listado*/
        ListView listadoCartera = (ListView) promptsView.findViewById(R.id.vst_cartera_listado);
        String[] from = new String[]{};
        int[] to = new int[]{};
        DBSistemaGestion helper = new DBSistemaGestion(getContext());
        Cursor cursor = helper.consultarCarteraCliente(cliente, true, this.trans.getDiasgracia());//dias de gracia
        ListadoCarteraAdaptador adapter = new ListadoCarteraAdaptador(getContext(), R.layout.fila_cartera_cliente, cursor, from, to);
        listadoCartera.setAdapter(adapter);
        result=cursor.getCount();
        /*Validar existencia de deudas*/
        if (cursor.getCount() > 0) {
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setNegativeButton("Aceptar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.empty_cartera), Toast.LENGTH_SHORT).show();
        }
        helper.close();
        return result;
    }

}
