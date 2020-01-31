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
public class ComodinSpinnerAdapter extends ArrayAdapter<SortItem> {

    private List<SortItem> sortItemList = new ArrayList<>();

    /**
     * Creates an adapter for sort type selection.
     *
     * @param activity activity context.
     */
    public ComodinSpinnerAdapter(Activity activity) {
        super(activity, R.layout.spinner_item_sort);
        this.setDropDownViewResource(R.layout.spinner_item_sort_dropdown);

        SortItem newest = new SortItem("desc", activity.getString(R.string.sort_desc));
        sortItemList.add(newest);
        SortItem popularity = new SortItem("asc", activity.getString(R.string.sort_asc));
        sortItemList.add(popularity);

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