package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.Producto_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Vendedor;

/**
 * Created by root on 04/12/15.
 */
public class ViewHolderVendedor extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private int id_vendedor;
    private TextView codigo_text;
    private TextView nombres_text;
    private TextView usuarios_text ;
    private Context context;
    private ItemClickListener clickListener;

    public ViewHolderVendedor(View view, Context context) {
        super(view);
        nombres_text= (TextView) itemView.findViewById(R.id.text1);
        usuarios_text= (TextView) itemView.findViewById(R.id.text2);
        this.context=context;
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }

    public void bind(Vendedor model) {
       id_vendedor=model.getIdvendedor();
       nombres_text.setText(model.getNombre());
        usuarios_text.setText(model.getCodusuario());
       if(model.getNombre().isEmpty()!=true) nombres_text.setText(model.getNombre());
       if(model.getCodusuario().isEmpty()!=true) usuarios_text.setText(model.getCodusuario());
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
