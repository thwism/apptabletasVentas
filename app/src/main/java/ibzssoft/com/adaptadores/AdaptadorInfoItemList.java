package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.InfoItemList;

public class AdaptadorInfoItemList extends BaseAdapter{
    private Activity activity; //Activity desde el cual se hace referencia al llenado de la lista
    private ArrayList<InfoItemList> arrayItems; // Lista de items
    // Constructor con parámetros que recibe la Acvity y los datos de los items.
    public AdaptadorInfoItemList(Activity activity, ArrayList<InfoItemList> listaItems){
        super();
        this.activity = activity;
        this.arrayItems = new ArrayList<InfoItemList>(listaItems);
    }
    // Retorna el número de items de la lista
    @Override
    public int getCount() {
        return arrayItems.size();
    }
    // Retorna el objeto TitularItems de la lista
    @Override
    public Object getItem(int position) {
        return arrayItems.get(position);
    }
    // Retorna la posición del item en la lista
    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
    Clase estática que contiene los elementos de la lista
      */
    public static class Fila
    {
        TextView txtTitle;
        TextView txtDescription;
        ImageView img;
    }
    // Método que retorna la vista formateada
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fila view = new Fila();
        LayoutInflater inflator = activity.getLayoutInflater();
        InfoItemList itm = arrayItems.get(position);
       /*
       Condicional para recrear la vista y no distorcionar el número de elementos
         */
        if(convertView==null)
        {
            convertView = inflator.inflate(R.layout.info_conexion_item, parent, false);
            view.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            view.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            view.img = (ImageView)convertView.findViewById(R.id.imgItem);
            convertView.setTag(view);
        }
        else
        {
            view = (Fila)convertView.getTag();
        }
        // Se asigna el dato proveniente del objeto TitularItems
        view.txtTitle.setText(itm.getTitle());
        view.txtDescription.setText(itm.getDescription());
        view.img.setImageResource(itm.getImg());
        // Retornamos la vista
        return convertView;
    }
}
