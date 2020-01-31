package ibzssoft.com.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import ibzssoft.com.ishidamovile.R;


import ibzssoft.com.modelo.Empresa;

/**
 * Created by root on 17/08/15.
 */
public class ListadoEmpresasAdaptador extends SimpleCursorAdapter implements Filterable {
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoEmpresasAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
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
        int ind_est1 = c.getColumnIndex(Empresa.FIELD_idempresa);
        int ind_est2= c.getColumnIndex(Empresa.FIELD_nombreempresa);


        String desc1= c.getString(ind_est1);
        String desc2= c.getString(ind_est2);
        TextView id_text = (TextView) v.findViewById(R.id.filaEmpresaId);
        TextView desc_text = (TextView) v.findViewById(R.id.filaEmpresaDesc);
        if (id_text!= null) {
            id_text.setText(desc1);desc_text.setText(desc2);
        }
    }

}
