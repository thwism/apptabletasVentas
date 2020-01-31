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
import ibzssoft.com.ishidamovile.Cliente_Detalle;
import ibzssoft.com.ishidamovile.PedidosPedientes;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.PedidoPendiente;
import ibzssoft.com.recibir.RecibirItemsPendientes;

/**
 * Created by root on 03/12/15.
 */
public class CatalogoPedidosPendientesAdapter extends RecyclerView.Adapter<ViewHolderPedidoPendiente> {

    private final List<PedidoPendiente> mModels;
    private final LayoutInflater mInflater;
    private Context context;

    public CatalogoPedidosPendientesAdapter(Context context, List<PedidoPendiente> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderPedidoPendiente onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_pedido_pendiente_cardview, parent, false);
        return new ViewHolderPedidoPendiente(itemView,context);
    }


    @Override
    public void onBindViewHolder(ViewHolderPedidoPendiente holder, int position) {
        PedidoPendiente model = mModels.get(position);
        holder.bind(model);
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                RecibirItemsPendientes recibirItemsPendientes= new RecibirItemsPendientes(context,mModels.get(position).getIdprovcli());
                recibirItemsPendientes.ejecutartarea();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mModels.size();
    }
    public void animateTo(List<PedidoPendiente> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<PedidoPendiente> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final PedidoPendiente model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<PedidoPendiente> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PedidoPendiente model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(List<PedidoPendiente> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PedidoPendiente model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    public PedidoPendiente removeItem(int position) {
        final PedidoPendiente model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, PedidoPendiente model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PedidoPendiente model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setFilter(List<PedidoPendiente> pedidos) {
        mModels.clear();
        mModels.addAll(pedidos);
        notifyDataSetChanged();
    }
}