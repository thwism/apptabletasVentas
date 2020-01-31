package ibzssoft.com.modelo.product;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ibzssoft.com.modelo.Shop;

/**
 * Created by Usuario-pc on 06/04/2017.
 */
public class Product implements Serializable {
    @SerializedName("descripcion")
    private String descripcion;
    @SerializedName("productoid")
    private String productoId;
    private int cantidad;

    public Product() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Detalle{" +
                "descripcion='" + descripcion + '\'' +
                ", productoId='" + productoId + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
