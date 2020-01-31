package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class Detalle implements Serializable{
    private String codigo;
    private double cantidad;
    private double cantidad_total;
    private String transaccion;
    private String descripcion;
    private String fecha;
    private double precio;

    public Detalle(String codigo, double cantidad, double cantidad_total, String transaccion, String descripcion, String fecha, double precio) {
        this.codigo = codigo;
        this.cantidad = cantidad;
        this.cantidad_total = cantidad_total;
        this.transaccion = transaccion;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(double cantidad_total) {
        this.cantidad_total = cantidad_total;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Detalle{" +
                "codigo='" + codigo + '\'' +
                ", cantidad=" + cantidad +
                ", cantidad_total=" + cantidad_total +
                ", transaccion='" + transaccion + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", precio=" + precio +
                '}';
    }
}
