package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PedidoPendiente;
import ibzssoft.com.recibir.RecibirItemsPendientes;

/**
 * Created by root on 04/12/15.
 */
public class ViewHolderPedidoPendiente extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private TextView nombres;
    private TextView alterno;
    private TextView items;
    private Context context;
    private String cliente;
    private ItemClickListener clickListener;

    public ViewHolderPedidoPendiente(View view, Context context) {
        super(view);
        nombres= (TextView) itemView.findViewById(R.id.filaPPendienteNombre);
        alterno= (TextView) itemView.findViewById(R.id.filaPPendienteAlterno);
        items= (TextView) itemView.findViewById(R.id.filaPPendienteItems);
        this.context=context;
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }

    public void bind(PedidoPendiente model) {
        cliente = model.getIdprovcli();
        nombres.setText(model.getNombres());
        alterno.setText(model.getNombre_alterno());
        items.setText(String.valueOf(model.getItems()));
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem1 = menu.add(0,R.id.menu_item_pendiente_ver,1,"Ver Pedido Pendiente");
        menuItem1.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Menu Item Clicked!
        switch (item.getItemId()){
            case R.id.menu_item_pendiente_ver:
                RecibirItemsPendientes recibirItemsPendientes = new RecibirItemsPendientes(context,cliente);
                recibirItemsPendientes.ejecutartarea();
                break;
            default:
                break;
        }
        return true;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getPosition(), false);
    }
}
