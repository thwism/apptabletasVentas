package ibzssoft.com.modelo;

/**
 * Created by Ricardo on 27/11/2016.
 */
public class Actualizacion {
    public String modulo;
    public String fecha;

    public Actualizacion(String modulo, String fecha) {
        this.modulo = modulo;
        this.fecha = fecha;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Actualizacion{" +
                "modulo='" + modulo + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
