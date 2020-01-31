package ibzssoft.com.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.recibir.RecibirFacturas;


/**
 * Created by root on 17/08/15.
 */
public class ListadoCarteraAdaptador extends SimpleCursorAdapter{
    /*Instancia DB*/
    private Context context;
    private int layout;
    public ListadoCarteraAdaptador(Context context, int layout, Cursor c, String[] from, int[] to) {
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
        final String doc= c.getString(c.getColumnIndex(PCKardex.FIELD_trans));
        final String nom= c.getString(c.getColumnIndex(PCKardex.FIELD_nombrecliente));
        final String com= c.getString(c.getColumnIndex(PCKardex.FIELD_comercialcliente));
        final String dir= c.getString(c.getColumnIndex(PCKardex.FIELD_direccioncliente));
        final String ruc= c.getString(c.getColumnIndex(PCKardex.FIELD_ruc));
        final String emi= c.getString(c.getColumnIndex(PCKardex.FIELD_fechaemision));

        String femi= new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(c.getString(c.getColumnIndex(PCKardex.FIELD_fechaemision))));
        String fvenci= new ParseDates().changeDateToStringSimpleYear(new ParseDates().changeStringToDateSimple(c.getString(c.getColumnIndex(PCKardex.FIELD_fechavenci))));
        String saldo_vencer= c.getString(c.getColumnIndex(PCKardex.FIELD_saldoxvence));
        String saldo_venci= c.getString(c.getColumnIndex(PCKardex.FIELD_saldovencido));
        String dvencido = c.getString(c.getColumnIndex(PCKardex.FIELD_dvencidos));
        String cheques= c.getString(c.getColumnIndex(PCKardex.FIELD_cantcheque));
        TextView trans_text = (TextView) v.findViewById(R.id.transaccion);
        TextView emision_text = (TextView) v.findViewById(R.id.emision);
        TextView venci_text = (TextView) v.findViewById(R.id.vencimiento);
        //TextView pagado_text = (TextView) v.findViewById(R.id.valor_cancelado);
        TextView saldo_vencer_text = (TextView) v.findViewById(R.id.saldo_vencer);
        TextView saldo_venci_text = (TextView) v.findViewById(R.id.saldo_vencido);
        TextView dv_text = (TextView) v.findViewById(R.id.dias_vencidos);
        TextView ch_text = (TextView) v.findViewById(R.id.cheques);

        if (doc!=null) {
            trans_text.setText(doc);
            emision_text.setText(femi);
            venci_text.setText(fvenci);
            saldo_vencer_text.setText(saldo_vencer);
//            pagado_text.setText(pagado);
            saldo_venci_text.setText(saldo_venci);
            dv_text.setText(dvencido);
            ch_text.setText(cheques);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecibirFacturas recibirFacturas = new RecibirFacturas(context,doc,nom, ruc, com, dir,emi);
                recibirFacturas.ejecutartarea();
            }
        });
    }
}
