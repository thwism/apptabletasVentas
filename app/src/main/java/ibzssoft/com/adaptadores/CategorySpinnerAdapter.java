package ibzssoft.com.adaptadores;


import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

/**
 * Simple arrayAdapter for sort type selection.
 */
public class CategorySpinnerAdapter extends ArrayAdapter<SortItem> {

    private List<SortItem> sortItemList = new ArrayList<>();
    /**
     * Creates an adapter for sort type selection.
     *
     * @param activity activity context.
     */
    public CategorySpinnerAdapter(Activity activity, String tabla, String category) {
        super(activity, R.layout.spinner_item_product_category);
        this.setDropDownViewResource(R.layout.spinner_item_sort_dropdown);
        this.consultarIVGrupo(activity, tabla, category);
    }
    public void consultarIVGrupo(Activity activity, String tabla, String category){
        try{
            DBSistemaGestion helper = new DBSistemaGestion(activity);
            sortItemList = helper.consultarCategoriaItem(tabla,category);
            helper.close();
        }catch (Exception e){
            Timber.e(new RuntimeException(), "No se puede cargar las categorias.");
        }
    }

    public int getCount() {
        return sortItemList.size();
    }

    public SortItem getItem(int position) {
        return sortItemList.get(position);
    }


//    public View getCustomView(int position, View convertView, ViewGroup parent) {
//
//        TextView label = new TextView(context);
//        label.setSingleLine(true);
//        label.setEllipsize(TextUtils.TruncateAt.END);
//        label.setText(getItemDescription(position));
//
//        return label;
//    }
}