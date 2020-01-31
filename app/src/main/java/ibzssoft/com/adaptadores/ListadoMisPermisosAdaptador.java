package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.Configurar_GNTrans;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Empresa;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.Permiso;
import ibzssoft.com.modelo.PermisoTrans;
import ibzssoft.com.modelo.Usuario;

/**
 * Created by root on 17/08/15.
 */
public class ListadoMisPermisosAdaptador extends SimpleCursorAdapter implements Filterable {
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoMisPermisosAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
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
       // String usr = c.getString(c.getColumnIndex(Usuario.FIELD_identificador));
        String modi = c.getString(c.getColumnIndex(GNTrans.FIELD_fecha_grabado));
        String group= c.getString(c.getColumnIndex(Grupo.FIELD_codgrupo));
        String emp= c.getString(c.getColumnIndex(Empresa.FIELD_nombreempresa));

        TextView nom_text = (TextView) v.findViewById(R.id.filaPermisoNomTrans);
        TextView cod_text = (TextView) v.findViewById(R.id.filaPermisoCodigo);
//        TextView mod_text = (TextView) v.findViewById(R.id.filaPermisoModificacion);
        TextView group_text = (TextView) v.findViewById(R.id.filaPermisoGroup);
  //      TextView emp_text = (TextView) v.findViewById(R.id.filaPermisoEmp);
        if (codigo!= null) {
            nom_text.setText(nombre);cod_text.setText(codigo);
            //mod_text.setText("Utl. Mod: "+modi);
            group_text.setText(group);//emp_text.setText(emp);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View view = super.getView(position, convertView, parent);
        return view;
    }
}
