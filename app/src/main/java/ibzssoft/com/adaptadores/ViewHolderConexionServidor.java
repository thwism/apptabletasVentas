package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.InfoItemList;


/**
 * Created by root on 04/12/15.
 */
public class ViewHolderConexionServidor extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

    private Context context;
    private TextView tittle;
    private  ItemClickListener clickListener;

    public ViewHolderConexionServidor(View view, Context context) {
        super(view);
        tittle = (TextView) itemView.findViewById(R.id.list_item);
        this.context=context;
    }

    public void bind(InfoItemList model) {
        tittle.setText(model.getTitle());
        System.out.println("Titulo: "+model.getTitle());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem1=menu.add(0, R.id.menu_cliente_ver, 1, "Ver Cliente");
        MenuItem menuItem2=menu.add(0, R.id.menu_cliente_ver_cartera, 2, "Ver Estado de Cuenta");
        MenuItem menuItem3=menu.add(0, R.id.menu_cliente_ver_descuentos,3, "Ver Descuentos");
        MenuItem menuItem4=menu.add(0, R.id.menu_cliente_ver_promedio,4, "Ver Promedio de Pagos");
        MenuItem menuItem5=menu.add(0, R.id.menu_cliente_ver_historial,5, "Ver Historial de Compras");
        MenuItem menuItem6=menu.add(0, R.id.menu_cliente_ver_ubicacion,6, "Ver Ubicacion en Google Maps");

        menuItem1.setOnMenuItemClickListener(this);
        menuItem2.setOnMenuItemClickListener(this);
        menuItem3.setOnMenuItemClickListener(this);
        menuItem4.setOnMenuItemClickListener(this);
        menuItem5.setOnMenuItemClickListener(this);
        menuItem6.setOnMenuItemClickListener(this);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cliente_ver:

                break;
            case R.id.menu_cliente_ver_descuentos:

                break;
            case R.id.menu_cliente_ver_promedio:

                break;
            case R.id.menu_cliente_ver_cartera:

                break;
            case R.id.menu_cliente_ver_historial:

                break;
            case R.id.menu_cliente_ver_ubicacion:

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
