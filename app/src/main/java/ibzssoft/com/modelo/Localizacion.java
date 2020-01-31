package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Usuario-pc on 13/01/2017.
 */
public class Localizacion implements Serializable{
    private double latitud;
    private double longitud;


    public Localizacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "Localizacion{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
