package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Promocion {
    public static final String TABLE_NAME = "TBL_PROMOCION";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_id_inven_promo = "prm_id_inven_promo";
    public static final String FIELD_cantidad_min = "prm_cantidad_min";
    public static final String FIELD_cantidad_promo = "prm_cantidad_promo";
    public static final String FIELD_precio_promo = "prm_precio_promo";
    public static final String FIELD_inventario_id = "prm_inventario_id";
    public static final String FIELD_estado= "prm_estado";
    public static final String FIELD_fecha_grabado = "prm_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_id_inven_promo+" text not null,"+
            FIELD_cantidad_min+" double not null,"+
            FIELD_cantidad_promo+" double not null,"+
            FIELD_precio_promo+" double not null,"+
            FIELD_estado+" int not null,"+
            FIELD_inventario_id+" text not null,"+
            FIELD_fecha_grabado+" datetime not null,"
            +"FOREIGN KEY("+FIELD_inventario_id+") REFERENCES "+ IVInventario.TABLE_NAME+"("+ IVInventario.FIELD_identificador+")"
            +" )";

    private int id;
    private String id_inven_promo;
    private double cantidad_min;
    private double cantidad_promo;
    private double precio_promo;
    private String inventario_id;
    private int estado;
    private String fecha_grabado;

    public Promocion(String id_inven_promo, double cantidad_min, double cantidad_promo, double precio_promo, String inventario_id, int estado, String fecha_grabado) {
        this.id_inven_promo = id_inven_promo;
        this.cantidad_min = cantidad_min;
        this.cantidad_promo = cantidad_promo;
        this.precio_promo = precio_promo;
        this.inventario_id = inventario_id;
        this.estado = estado;
        this.fecha_grabado = fecha_grabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_inven_promo() {
        return id_inven_promo;
    }

    public void setId_inven_promo(String id_inven_promo) {
        this.id_inven_promo = id_inven_promo;
    }

    public double getCantidad_min() {
        return cantidad_min;
    }

    public void setCantidad_min(double cantidad_min) {
        this.cantidad_min = cantidad_min;
    }

    public double getCantidad_promo() {
        return cantidad_promo;
    }

    public void setCantidad_promo(double cantidad_promo) {
        this.cantidad_promo = cantidad_promo;
    }

    public double getPrecio_promo() {
        return precio_promo;
    }

    public void setPrecio_promo(double precio_promo) {
        this.precio_promo = precio_promo;
    }

    public String getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(String inventario_id) {
        this.inventario_id = inventario_id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
    }

    @Override
    public String toString() {
        return "Promocion{" +
                "id=" + id +
                ", id_inven_promo='" + id_inven_promo + '\'' +
                ", cantidad_min=" + cantidad_min +
                ", cantidad_promo=" + cantidad_promo +
                ", precio_promo=" + precio_promo +
                ", inventario_id='" + inventario_id + '\'' +
                ", estado=" + estado +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                '}';
    }
}
