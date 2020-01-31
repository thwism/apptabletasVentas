package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import ibzssoft.com.ishidamovile.Cliente_Detalle;
import ibzssoft.com.ishidamovile.Producto_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.vista.Item;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by root on 03/12/15.
 */
public class CatalogoProductosAdapter extends RecyclerView.Adapter<ViewHolderProducto> {

    private final List<Item> mModels;
    private final LayoutInflater mInflater;
    private Context context;

    public CatalogoProductosAdapter(Context context, List<Item> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderProducto onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_item_cardview, parent, false);
        return new ViewHolderProducto(itemView,context);
    }

    @Override
    public void onBindViewHolder(ViewHolderProducto holder, int position) {
        final Item model = mModels.get(position);
        holder.bind(model);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
            Intent intent = new Intent(context, Producto_Detalle.class);
            Activity activity = (Activity) context;
            intent.putExtra("item", mModels.get(position).getIdentificador());
            activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<Item> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Item> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Item model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<Item> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Item model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<Item> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Item model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public Item removeItem(int position) {
        final Item model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Item model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Item model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setFilter(List<Item> items) {
        mModels.clear();
        mModels.addAll(items);
        notifyDataSetChanged();
    }
}