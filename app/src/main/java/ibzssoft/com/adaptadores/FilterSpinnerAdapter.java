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
public class FilterSpinnerAdapter extends ArrayAdapter<SortItem> {

    private List<SortItem> sortItemList = new ArrayList<>();

    /**
     * Creates an adapter for sort type selection.
     *
     * @param activity activity context.
     */
    public FilterSpinnerAdapter(Activity activity) {
        super(activity, R.layout.spinner_item_sort);
        this.setDropDownViewResource(R.layout.spinner_item_sort_dropdown);

        //        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SortItem newest = new SortItem("all", activity.getString(R.string.filter_all));
        sortItemList.add(newest);
        SortItem popularity = new SortItem("send", activity.getString(R.string.filter_send));
        sortItemList.add(popularity);
        SortItem priceDesc = new SortItem("nosend", activity.getString(R.string.filter_no_send));
        sortItemList.add(priceDesc);
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