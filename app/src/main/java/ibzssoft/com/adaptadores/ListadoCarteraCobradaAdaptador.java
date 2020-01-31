package ibzssoft.com.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.KardexTransaccion;

/**
 * Created by root on 17/08/15.
 */
public class ListadoCarteraCobradaAdaptador extends SimpleCursorAdapter{
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoCarteraCobradaAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);
        return v;
    }

    @Override
    public void bindView(View v, final Context context, Cursor c) {
        super.bindView(v, context, c);
        final String doc= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_trans));
        final String ref= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_numdocref));
        final String valor= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_valor));
        final String debe= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_debe));
        final String haber= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_haber));
        final String descripcion= c.getString(c.getColumnIndex(KardexTransaccion.FIELD_descripcion));

        String femi= new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(c.getString(c.getColumnIndex(KardexTransaccion.FIELD_fechatrans))));
        String fvenci= new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(c.getString(c.getColumnIndex(KardexTransaccion.FIELD_fechavenci))));

        TextView trans_text = (TextView) v.findViewById(R.id.transaccion);
        TextView ref_text = (TextView) v.findViewById(R.id.referencia);
        TextView emision_text = (TextView) v.findViewById(R.id.emision);
        TextView venci_text = (TextView) v.findViewById(R.id.vencimiento);
        TextView desc_text = (TextView) v.findViewById(R.id.descripcion);
        TextView valor_text = (TextView) v.findViewById(R.id.valor);
        TextView debe_text = (TextView) v.findViewById(R.id.debe);
        TextView haber_text = (TextView) v.findViewById(R.id.haber);

        if (doc!=null) {
            trans_text.setText(doc);
            ref_text.setText(ref);
            emision_text.setText(femi);
            venci_text.setText(fvenci);
            valor_text.setText(valor);
            debe_text.setText(debe);
            haber_text.setText(haber);
            desc_text.setText(descripcion);
        }
    }
}
