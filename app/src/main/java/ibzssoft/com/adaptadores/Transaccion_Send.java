package ibzssoft.com.adaptadores;

import java.util.List;

import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;

/**
 * Created by root on 19/04/16.
 */
public class Transaccion_Send {
    private Transaccion transaccion;
    private List<PCKardex> pckardex;
    private List<IVKardex> ivkardex;

    public Transaccion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    public List<PCKardex> getPckardex() {
        return pckardex;
    }

    public void setPckardex(List<PCKardex> pckardex) {
        this.pckardex = pckardex;
    }

    public List<IVKardex> getIvkardex() {
        return ivkardex;
    }

    public void setIvkardex(List<IVKardex> ivkardex) {
        this.ivkardex = ivkardex;
    }

    @Override
    public String toString() {
        return "Transaccion {" +
                "transaccion=" + transaccion +
                ", pckardex=" + pckardex +
                ", ivkardex=" + ivkardex +
                '}';
    }
}
