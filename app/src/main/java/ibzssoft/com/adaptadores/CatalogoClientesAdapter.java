package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ibzssoft.com.ishidamovile.Cliente_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 03/12/15.
 */
public class CatalogoClientesAdapter extends RecyclerView.Adapter<ViewHolderCliente> {

    private final List<Cliente> mModels;
    private final LayoutInflater mInflater;
    private Context context;
    private String usuario;

    public CatalogoClientesAdapter(Context context, List<Cliente> models,String usuario) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
        this.usuario=usuario;
    }


    @Override
    public ViewHolderCliente onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_cliente_cardview, parent, false);
        return new ViewHolderCliente(itemView,context,usuario);
    }


    @Override
    public void onBindViewHolder(ViewHolderCliente holder, int position) {
        Cliente model = mModels.get(position);
        holder.bind(model);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                    //Toast.makeText(context, "#" + position + " - " + mModels.get(position), Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(context, Cliente_Detalle.class);
                    Activity activity=(Activity)context;
                    intent.putExtra("vendedor",usuario);
                    intent.putExtra("cliente",mModels.get(position).getIdprovcli());
                    activity.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<Cliente> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Cliente> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Cliente model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Cliente> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Cliente model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<Cliente> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Cliente model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public Cliente removeItem(int position) {
        final Cliente model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Cliente model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Cliente model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
    public void setFilter(List<Cliente> clientes) {
        mModels.clear();
        mModels.addAll(clientes);
        notifyDataSetChanged();
    }
}