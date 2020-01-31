package ibzssoft.com.adaptadores;


import android.app.Activity;
import android.database.Cursor;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCGrupo1;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.storage.DBSistemaGestion;
import timber.log.Timber;

/**
 * Simple arrayAdapter for sort type selection.
 */
public class SectorSpinnerAdapter extends ArrayAdapter<SortItem> {

    private List<SortItem> sortItemList = new ArrayList<>();
    /**
     * Creates an adapter for sort type selection.
     *
     * @param activity activity context.
     */
    public SectorSpinnerAdapter(Activity activity) {
        super(activity, R.layout.spinner_item_sort);
        this.setDropDownViewResource(R.layout.spinner_item_sort_dropdown);
        this.consultarPCGrupo1(activity);
    }

    public void consultarPCGrupo1(Activity activity){
        try{
            DBSistemaGestion helper = new DBSistemaGestion(activity);
            Cursor cursor = helper.consultarSectores();
            if(cursor.moveToFirst()){
                do{
                    sortItemList.add(new SortItem(cursor.getString(cursor.getColumnIndex(PCGrupo1.FIELD_idgrupo1)),cursor.getString(cursor.getColumnIndex(PCGrupo1.FIELD_descripcion))));
                }while (cursor.moveToNext());
            }
            cursor.close();
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