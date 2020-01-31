package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.recibir.RecibirFacturas;

/**
 * Created by root on 03/12/15.
 */
public class CatalogoDescuentoAdapter extends RecyclerView.Adapter<ViewHolderDescuento> {

    private final List<Descuento> mModels;
    private final LayoutInflater mInflater;
    private Context context;

    public CatalogoDescuentoAdapter(Context context, List<Descuento> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderDescuento onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_descuento_general, parent, false);
        return new ViewHolderDescuento(itemView,context);
    }


    @Override
    public void onBindViewHolder(ViewHolderDescuento holder, int position) {
        Descuento model = mModels.get(position);
        holder.bind(model);
    }



    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<Descuento> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Descuento> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Descuento model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Descuento> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Descuento model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<Descuento> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Descuento model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public Descuento removeItem(int position) {
        final Descuento model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Descuento model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Descuento model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    public void setFilter(List<Descuento> descuentos) {
        mModels.clear();
        mModels.addAll(descuentos);
        notifyDataSetChanged();
    }
}