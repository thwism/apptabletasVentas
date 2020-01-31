package ibzssoft.com.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;


/**
 * Created by root on 28/10/15.
 */
public class ListadoPreciosAdapter extends ArrayAdapter<String> {
    private String []indices;
    public ListadoPreciosAdapter(Context context, String[] objects,String[] indices) {
        super(context, 0, objects);
        this.indices=indices;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String valor=getItem(position);
        String indice=indices[position];
        if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fila_precio, parent, false);
        }
        TextView precio = (TextView) convertView.findViewById(R.id.txtValorPrecio1);
        TextView nro_precio= (TextView) convertView.findViewById(R.id.txtNumeroPrecio);
        nro_precio.setText(getContext().getResources().getString(R.string.tittle_precio)+" "+(indice)+": ");
        precio.setText(valor);
        return convertView;
    }
}
