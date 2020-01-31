package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.Producto_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.Vendedor_Detalle;
import ibzssoft.com.modelo.Vendedor;


/**
 * Created by root on 03/12/15.
 */
public class CatalogoVendedoresAdapter extends RecyclerView.Adapter<ViewHolderVendedor> {

    private final List<Vendedor> mModels;
    private final LayoutInflater mInflater;
    private Context context;

    public CatalogoVendedoresAdapter(Context context, List<Vendedor> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderVendedor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_usuarios, parent, false);
        return new ViewHolderVendedor(itemView,context);
    }

    @Override
    public void onBindViewHolder(ViewHolderVendedor holder, int position) {
        final Vendedor model = mModels.get(position);
        holder.bind(model);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, Vendedor_Detalle.class);
                Activity activity = (Activity) context;
                intent.putExtra("vendedor", mModels.get(position).getIdvendedor());
                activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<Vendedor> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Vendedor> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Vendedor model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(List<Vendedor> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Vendedor model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<Vendedor> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Vendedor model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public Vendedor removeItem(int position) {
        final Vendedor model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Vendedor model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Vendedor model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}