package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import ibzssoft.com.ishidamovile.Configurar_GNTrans;
import ibzssoft.com.ishidamovile.R;


import ibzssoft.com.modelo.Acceso;
import ibzssoft.com.modelo.Empresa;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.Permiso;
import ibzssoft.com.modelo.PermisoTrans;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;

/**
 * Created by root on 17/08/15.
 */
public class ListadoPermisosAdaptador extends SimpleCursorAdapter implements Filterable {
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoPermisosAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        String nombre = c.getString(c.getColumnIndex(GNTrans.FIELD_nombretrans));
        String codigo = c.getString(c.getColumnIndex(GNTrans.FIELD_codtrans));
        //String group= c.getString(c.getColumnIndex(Grupo.FIELD_codgrupo));
        TextView nom_text = (TextView) v.findViewById(R.id.filaPermisoNomTrans);
        TextView cod_text = (TextView) v.findViewById(R.id.filaPermisoCodigo);
        TextView group_text = (TextView) v.findViewById(R.id.filaPermisoGroup);
        if (codigo!= null) {
            nom_text.setText(nombre);cod_text.setText(codigo);
          //  group_text.setText(group);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = super.getView(position, convertView, parent);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur=getCursor();
                if(cur.moveToPosition(position)){
                    Intent intent= new Intent(context, Configurar_GNTrans.class);
                    intent.putExtra("gntrans", cur.getString(cur.getColumnIndexOrThrow(GNTrans.FIELD_codtrans)));
                    context.startActivity(intent);
                }
            }
        });
        return view;
    }
}
