package ibzssoft.com.enviar;

/**
 * Created by Ricardo on 05/12/2016.
 */
public class IVKardex_Serialize_Envio {
    public double cantidad;
    public double precio_total;
    public double precio_real_total;
    public double descuento;
    public String bodega_id;
    public String inventario_id;
    public String padre_id;
    public int desc_promo;
    public int num_precio;
    public String desc_sol;
    public int aprobado;

    public IVKardex_Serialize_Envio(double cantidad, double precio_total, double precio_real_total, double descuento, String bodega_id, String inventario_id, String padre_id, int desc_promo, int num_precio,String desc_sol,int aprobado) {
        this.cantidad = cantidad;
        this.precio_total = precio_total;
        this.precio_real_total = precio_real_total;
        this.descuento = descuento;
        this.bodega_id = bodega_id;
        this.inventario_id = inventario_id;
        this.padre_id = padre_id;
        this.desc_promo = desc_promo;
        this.num_precio = num_precio;
        this.desc_sol = desc_sol;
        this.aprobado= aprobado;
    }

    public double getCantidad() {
        return cantidad;
    }
    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio_total() {
        return precio_total;
    }
    public void setPrecio_total(double precio_total) {
        this.precio_total = precio_total;
    }
    public double getPrecio_real_total() {
        return precio_real_total;
    }
    public void setPrecio_real_total(double precio_real_total) {
        this.precio_real_total = precio_real_total;
    }
    public double getDescuento() {
        return descuento;
    }
    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
    public String getBodega_id() {
        return bodega_id;
    }
    public void setBodega_id(String bodega_id) {
        this.bodega_id = bodega_id;
    }
    public String getInventario_id() {
        return inventario_id;
    }
    public void setInventario_id(String inventario_id) {
        this.inventario_id = inventario_id;
    }
    public String getPadre_id() {
        return padre_id;
    }
    public void setPadre_id(String padre_id) {
        this.padre_id = padre_id;
    }
    public int getDesc_promo() {
        return desc_promo;
    }
    public void setDesc_promo(int desc_promo) {
        this.desc_promo = desc_promo;
    }
    public int getNum_precio() {
        return num_precio;
    }
    public void setNum_precio(int num_precio) {
        this.num_precio = num_precio;
    }

    public String getDesc_sol() {
        return desc_sol;
    }

    public void setDesc_sol(String desc_sol) {
        this.desc_sol = desc_sol;
    }

    public int getAprobado() {
        return aprobado;
    }

    public void setAprobado(int aprobado) {
        this.aprobado = aprobado;
    }

    @Override
    public String toString() {
        return  "{"+cantidad + "|"
                + precio_total + "|" + precio_real_total + "|" + descuento
                + "|"+ bodega_id + "|" + inventario_id + "|"
                + padre_id + "|" + desc_promo + "|" + num_precio + "|" +desc_sol+ "|"+aprobado+ "|"
                + "}";
    }

}

