package ibzssoft.com.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;


import ibzssoft.com.modelo.TSFormaCobroPago;

/**
 * Created by root on 17/08/15.
 */
public class ListadoTSFAdaptador extends SimpleCursorAdapter{
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoTSFAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
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
        super.bindView(v, context, c);
        String tsf= c.getString(c.getColumnIndex(TSFormaCobroPago.FIELD_codforma));
        TextView tsf_text = (TextView) v.findViewById(R.id.txtValorForma);

        if (tsf!=null) {
            tsf_text.setText(tsf);
        }
    }
}
