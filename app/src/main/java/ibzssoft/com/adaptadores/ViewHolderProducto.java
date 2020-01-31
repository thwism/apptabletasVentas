package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.Producto_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ReporteExistencia;
import ibzssoft.com.modelo.vista.Item;

/**
 * Created by root on 04/12/15.
 */
public class ViewHolderProducto extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private String id_producto;
    private TextView codigo_text;
    private TextView alterno_text;
    private TextView descripcion_text ;
    private TextView presentacion_text ;
    private Context context;
    private ItemClickListener clickListener;

    public ViewHolderProducto(View view, Context context) {
        super(view);
        codigo_text= (TextView) itemView.findViewById(R.id.filaProductoCodigo);
        descripcion_text= (TextView) itemView.findViewById(R.id.filaProductoDescripcion);
        presentacion_text= (TextView) itemView.findViewById(R.id.filaProductoPresent);
        this.context=context;
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);

    }

    public void bind(Item model) {
        id_producto=model.getIdentificador();
        codigo_text.setText(model.getCod_item());
        descripcion_text.setText(model.getDescripcion());
        presentacion_text.setText(model.getPresentacion());
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem1 = menu.add(0,R.id.menu_producto_ver_producto,1,"Ver Producto");
        MenuItem menuItem2= menu.add(0,R.id.menu_producto_ver_existencia,2,"Consultar Existencias");
        MenuItem menuItem3= menu.add(0,R.id.menu_producto_export_img,2,"Exportar Imagenes");
        menuItem1.setOnMenuItemClickListener(this);
        menuItem2.setOnMenuItemClickListener(this);
        menuItem3.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Menu Item Clicked!
        switch (item.getItemId()){
            case R.id.menu_producto_ver_producto:
                    Intent intent = new Intent(context, Producto_Detalle.class);
                    Activity activity = (Activity) context;
                    intent.putExtra("item", id_producto);
                    activity.startActivity(intent);
                break;
            case R.id.menu_producto_ver_existencia:
                    Intent intent2 = new Intent(context, ReporteExistencia.class);
                    Activity activity2 = (Activity) context;
                    intent2.putExtra("item", id_producto);
                    activity2.startActivity(intent2);
                break;
            case R.id.menu_producto_export_img:
                MainActivity.getInstance().chooseImage("1",1);
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
