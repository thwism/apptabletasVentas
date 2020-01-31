package ibzssoft.com.adaptadores;


import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Shop;

/**
 * Simple arrayAdapter for shop selection.
 */
public class ShopSpinnerAdapter extends ArrayAdapter<Shop> {
    private static final int layoutID = R.layout.list_item_shops;
    private final boolean viewTextWhite;
    private LayoutInflater layoutInflater;
    private List<Shop> shops;

    /**
     * Creates an adapter for shop selection.
     *
     * @param activity      activity context.
     * @param shops         list of items.
     * @param viewTextWhite true if text should be white.
     */
    public ShopSpinnerAdapter(Activity activity, List<Shop> shops, boolean viewTextWhite) {
        super(activity, layoutID, shops);
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.shops = shops;
        this.viewTextWhite = viewTextWhite;
    }

    public int getCount() {
        return shops.size();
    }

    public Shop getItem(int position) {
        return shops.get(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent, false);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, boolean dropdown) {
        View v = convertView;
        ListItemHolder holder;

        if (v == null) {
            v = layoutInflater.inflate(layoutID, parent, false);
            holder = new ListItemHolder();
            holder.shopName = (TextView) v.findViewById(R.id.shop_name);
            holder.shopDescription= (TextView) v.findViewById(R.id.shop_description);
            v.setTag(holder);
        } else {
            holder = (ListItemHolder) v.getTag();
        }

        Shop shop = shops.get(position);

        if (dropdown || !viewTextWhite) {
            holder.shopName.setTextColor(ContextCompat.getColor(getContext(), R.color.textPrimary));
        } else {
            holder.shopName.setTextColor(ContextCompat.getColor(getContext(), R.color.textIconColorPrimary));
        }

        if (shop != null) {
            holder.shopName.setText(shop.getCodtrans());
            holder.shopDescription.setText("["+shop.getNombretrans()+"]");
        }

        return v;
    }

    static class ListItemHolder {
        TextView shopName;
        TextView shopDescription;
    }
}