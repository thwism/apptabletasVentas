package ibzssoft.com.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import ibzssoft.com.adaptadores.CategorySpinnerAdapter;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.interfaces.FilterDialogInterface;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.modelo.filtros.FilterType;
import ibzssoft.com.modelo.filtros.Filters;
import timber.log.Timber;

public class FilterDialogFragment extends DialogFragment {

    private FilterDialogInterface filterDialogInterface;
    private String cat1,cat2,cat3,cat4,cat5,cat6;
    private String []configs;

    public static FilterDialogFragment newInstance(FilterDialogInterface filterDialogInterface, String [] configs) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        if (filterDialogInterface == null) {
            return null;
        }
        filterDialogFragment.configs = configs;
        filterDialogFragment.filterDialogInterface = filterDialogInterface;
        return filterDialogFragment;
    }

    public void cargarPreferenciasEtiquetas() {
        ExtraerConfiguraciones extraerConfiguraciones = new ExtraerConfiguraciones(getActivity());
        this.cat1 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g1), getActivity().getString(R.string.pref_etiqueta_grupo1_inventario_default));
        this.cat2 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g2), getActivity().getString(R.string.pref_etiqueta_grupo2_inventario_default));
        this.cat3 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g3), getActivity().getString(R.string.pref_etiqueta_grupo3_inventario_default));
        this.cat4 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g4), getActivity().getString(R.string.pref_etiqueta_grupo4_inventario_default));
        this.cat5 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g5), getActivity().getString(R.string.pref_etiqueta_grupo5_inventario_default));
        this.cat6 = extraerConfiguraciones.get(getActivity().getString(R.string.key_g6), getActivity().getString(R.string.pref_etiqueta_grupo6_inventario_default));
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

        this.cargarPreferenciasEtiquetas();
        Timber.d("%s - OnCreateView", this.getClass().getSimpleName());
        View view = inflater.inflate(R.layout.dialog_filters, container, false);
        TextView tittle1 = (TextView) view.findViewById(R.id.list_item_filter_select_title_1);
        TextView tittle2 = (TextView) view.findViewById(R.id.list_item_filter_select_title_2);
        TextView tittle3 = (TextView) view.findViewById(R.id.list_item_filter_select_title_3);
        TextView tittle4 = (TextView) view.findViewById(R.id.list_item_filter_select_title_4);
        TextView tittle5 = (TextView) view.findViewById(R.id.list_item_filter_select_title_5);
        TextView tittle6 = (TextView) view.findViewById(R.id.list_item_filter_select_title_6);
        final Spinner spinner1 = (Spinner) view.findViewById(R.id.list_item_filter_select_1);
        final Spinner spinner2 = (Spinner) view.findViewById(R.id.list_item_filter_select_2);
        final Spinner spinner3 = (Spinner) view.findViewById(R.id.list_item_filter_select_3);
        final Spinner spinner4 = (Spinner) view.findViewById(R.id.list_item_filter_select_4);
        final Spinner spinner5 = (Spinner) view.findViewById(R.id.list_item_filter_select_5);
        final Spinner spinner6 = (Spinner) view.findViewById(R.id.list_item_filter_select_6);
        tittle1.setText(cat1);tittle2.setText(cat2);tittle3.setText(cat3);
        tittle4.setText(cat4);tittle5.setText(cat5);tittle6.setText(cat6);
        final CategorySpinnerAdapter adapter1 = new CategorySpinnerAdapter(getActivity(), IVGrupo1.TABLE_NAME,cat1);
        CategorySpinnerAdapter adapter2 = new CategorySpinnerAdapter(getActivity(), IVGrupo2.TABLE_NAME,cat2);
        CategorySpinnerAdapter adapter3 = new CategorySpinnerAdapter(getActivity(), IVGrupo3.TABLE_NAME,cat3);
        CategorySpinnerAdapter adapter4 = new CategorySpinnerAdapter(getActivity(), IVGrupo4.TABLE_NAME,cat4);
        CategorySpinnerAdapter adapter5 = new CategorySpinnerAdapter(getActivity(), IVGrupo5.TABLE_NAME,cat5);
        CategorySpinnerAdapter adapter6 = new CategorySpinnerAdapter(getActivity(), IVGrupo6.TABLE_NAME,cat6);
        spinner1.setAdapter(adapter1);spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);spinner4.setAdapter(adapter4);
        spinner5.setAdapter(adapter5);spinner6.setAdapter(adapter6);
        /*Enable and Disable for Configurations*/
        for(String conf: configs){
            int result = Integer.parseInt(conf);
            switch (result){
                case 1:tittle1.setVisibility(View.VISIBLE);spinner1.setVisibility(View.VISIBLE);break;
                case 2:tittle2.setVisibility(View.VISIBLE);spinner2.setVisibility(View.VISIBLE);break;
                case 3:tittle3.setVisibility(View.VISIBLE);spinner3.setVisibility(View.VISIBLE);break;
                case 4:tittle4.setVisibility(View.VISIBLE);spinner4.setVisibility(View.VISIBLE);break;
                case 5:tittle5.setVisibility(View.VISIBLE);spinner5.setVisibility(View.VISIBLE);break;
                case 6:tittle6.setVisibility(View.VISIBLE);spinner6.setVisibility(View.VISIBLE);break;
            }
        }
        Button btnApply = (Button) view.findViewById(R.id.filter_btn_apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // aplicar filtros para la busqueda
                 SortItem sp1= (SortItem) spinner1.getSelectedItem();SortItem sp2= (SortItem) spinner2.getSelectedItem();SortItem sp3= (SortItem) spinner3.getSelectedItem();
                 SortItem sp4= (SortItem) spinner4.getSelectedItem();SortItem sp5= (SortItem) spinner5.getSelectedItem();SortItem sp6= (SortItem) spinner6.getSelectedItem();
                 String filterUrl = sp1.getValue()+";"+sp2.getValue()+";"+sp3.getValue()+";"+sp4.getValue()+";"+sp5.getValue()+";"+sp6.getValue();
                 filterDialogInterface.onFilterSelected(filterUrl);
                dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.filter_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancelar busqueda con filtro
                filterDialogInterface.onFilterCancelled();
                dismiss();
            }
        });
        return view;
    }
}
