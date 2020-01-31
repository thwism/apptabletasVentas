package ibzssoft.com.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.PCKardex;


/**
 * Created by root on 04/12/15.
 */
public class ViewHolderDescuento extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private String desc_id;
    private TextView desc_cliente;
    private TextView desc_producto;
    private TextView desc_coditem;
    private TextView desc_porcentaje;
    private Context context;
    private ItemClickListener clickListener;
    /*Headers*/

    public ViewHolderDescuento(View view, Context context) {
        super(view);

            desc_cliente= (TextView) itemView.findViewById(R.id.cliente_descuento);
            desc_producto= (TextView) itemView.findViewById(R.id.producto_descuento);
            desc_porcentaje= (TextView) itemView.findViewById(R.id.porcentaje_descuento);
            desc_coditem= (TextView) itemView.findViewById(R.id.cod_prod_desc);
        this.context=context;
        //view.setOnClickListener(this);
        //view.setOnCreateContextMenuListener(this);
    }

    public String redondearNumero(double numero) {
        String clave = "0.00";
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
    }
    public void bind(Descuento model) {
        //System.out.println("mostrando descuentos: "+model.getCliente_id()+" ; "+model.getPorcentaje());
        desc_cliente.setText(model.getCliente_id());
        desc_producto.setText(model.getInventario_id());
        desc_porcentaje.setText(redondearNumero(model.getPorcentaje()));
        desc_coditem.setText(model.getFecha_grabado());
        desc_id=model.getIdentificador();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
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
