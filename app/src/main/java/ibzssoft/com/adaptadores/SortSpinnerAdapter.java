package ibzssoft.com.adaptadores;


import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.SortItem;

/**
 * Simple arrayAdapter for sort type selection.
 */
public class SortSpinnerAdapter extends ArrayAdapter<SortItem> {

    private List<SortItem> sortItemList = new ArrayList<>();

    /**
     * Creates an adapter for sort type selection.
     *
     * @param activity activity context.
     */
    public SortSpinnerAdapter(Activity activity) {
        super(activity, R.layout.spinner_item_sort);
        this.setDropDownViewResource(R.layout.spinner_item_sort_dropdown);

        SortItem newest = new SortItem("nro", activity.getString(R.string.sort_nro));
        sortItemList.add(newest);
        SortItem popularity = new SortItem("cliente", activity.getString(R.string.sort_cliente));
        sortItemList.add(popularity);
        SortItem sortTrans = new SortItem("trans", activity.getString(R.string.sort_trans));
        sortItemList.add(sortTrans);
        SortItem sortEnv = new SortItem("creacion", activity.getString(R.string.sort_creacion));
        sortItemList.add(sortEnv);
        SortItem sortFecha = new SortItem("fecha", activity.getString(R.string.sort_fecha));
        sortItemList.add(sortFecha);

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