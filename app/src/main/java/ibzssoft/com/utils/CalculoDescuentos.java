package ibzssoft.com.utils;

import android.content.Context;
import android.database.Cursor;

import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by Usuario-pc on 19/06/2017.
 */
public class CalculoDescuentos {
    private Context context;
    private String clienteId;
    private String productoId;

    public CalculoDescuentos(Context context, String clienteId, String productoId) {
        this.context = context;
        this.clienteId = clienteId;
        this.productoId = productoId;
    }

    public double obtenerPorcentajeDescuento(){
        double result = 0.0;
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor = helper.consultarDescuentoItem(clienteId, productoId);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getDouble(cursor.getColumnIndex(Descuento.FIELD_porcentaje));
            }
        }
        cursor.close();
        helper.close();
        return result;
    }

    public double obtenerPrecioReal(double precio, double porcentaje){
        double result = 0.0;
        double porc = porcentaje / 100;
        double desc = precio * porc;
        result = precio-desc;
        return result;
    }
}
