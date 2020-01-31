package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.recibir.RecibirFacturas;

/**
 * Created by root on 03/12/15.
 */
public class CatalogoCarteraPersonalAdapter extends RecyclerView.Adapter<ViewHolderCartera> {

    private final List<PCKardex> mModels;
    private final LayoutInflater mInflater;
    private Context context;


    public CatalogoCarteraPersonalAdapter(Context context, List<PCKardex> models, String usuario) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderCartera onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_cartera_personal, parent, false);
        return new ViewHolderCartera(itemView,context);
    }


    @Override
    public void onBindViewHolder(ViewHolderCartera holder, int position) {
        PCKardex model = mModels.get(position);
        holder.bind(model);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                RecibirFacturas recibirFacturas = new RecibirFacturas(context,mModels.get(position).getTrans(),mModels.get(position).getRuc(),mModels.get(position).getNombrecli(),mModels.get(position).getComercialcli(),mModels.get(position).getDireccioncli(),mModels.get(position).getFechaemision());
                recibirFacturas.ejecutartarea();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<PCKardex> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PCKardex> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final PCKardex model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PCKardex> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PCKardex model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<PCKardex> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PCKardex model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public PCKardex removeItem(int position) {
        final PCKardex model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, PCKardex model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PCKardex model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}