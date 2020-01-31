package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class IVKardex {
    public static final String TABLE_NAME = "TBL_IVKARDEX";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_identificador = "ivk_CodIVKardex";
    public static final String FIELD_cantidad = "ivk_Cantidad";
    public static final String FIELD_cos_total = "ivk_CostoTotal";
    public static final String FIELD_cos_real_total = "ivk_CostoRealTotal";
    public static final String FIELD_precio_total = "ivk_PrecioTotal";
    public static final String FIELD_pre_real_total = "ivk_PrecioRealTotal";
    public static final String FIELD_desc_sol= "ivk_DescuentoSolicitado";
    public static final String FIELD_descuento = "ivk_Descuento";
    public static final String FIELD_descuento_real = "ivk_Descuento_Real";
    public static final String FIELD_trans_id = "ivk_trans_id";
    public static final String FIELD_bodega_id = "ivk_bodega_id";
    public static final String FIELD_inventario_id = "ivk_inventario_id";
    public static final String FIELD_padre_id = "ivk_padre_id";
    public static final String FIELD_desc_promo = "ivk_desc_promo";
    public static final String FIELD_keypadre= "ivk_keypadre";
    public static final String FIELD_num_precio = "ivk_num_precio";
    public static final String FIELD_cos_pro = "ivk_cos_pro";
    public static final String FIELD_band_aprobado = "ivk_bandAprobado";
    public static final String FIELD_fecha_grabado = "ivk_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_identificador+" text unique,"+
            FIELD_cantidad+" double not null,"+
            FIELD_cos_total+" double not null,"+
            FIELD_cos_real_total+" double not null,"+
            FIELD_precio_total+" double not null,"+
            FIELD_pre_real_total+" double not null,"+
            FIELD_desc_sol+" text,"+
            FIELD_descuento+" double not null,"+
            FIELD_descuento_real+" double not null,"+
            FIELD_fecha_grabado+" datetime not null,"+
            FIELD_bodega_id+" text not null,"+
            FIELD_trans_id+" text not null,"+
            FIELD_inventario_id+" text not null,"+
            FIELD_padre_id+" text,"+
            FIELD_keypadre+" text,"+
            FIELD_desc_promo+" int,"+
            FIELD_num_precio+" int,"+
            FIELD_cos_pro+" double,"+
            FIELD_band_aprobado+" int not null,"
            +"FOREIGN KEY("+FIELD_trans_id+") REFERENCES "+ Transaccion.TABLE_NAME+"("+ Transaccion.FIELD_ID_Trans+"),"
            +"FOREIGN KEY("+FIELD_bodega_id+") REFERENCES "+ Bodega.TABLE_NAME+"("+ Bodega.FIELD_idbodega+"),"
            +"FOREIGN KEY("+FIELD_inventario_id+") REFERENCES "+ IVInventario.TABLE_NAME+"("+ IVInventario.FIELD_identificador+")"
            +"FOREIGN KEY("+FIELD_padre_id+") REFERENCES "+ IVInventario.TABLE_NAME+"("+ IVInventario.FIELD_identificador+")"
            +" )";

    private int id;
    private String identificador;
    private double cantidad;
    private double costo_total;
    private double costo_real_total;
    private double precio_total;
    private double precio_real_total;
    private String  desc_sol;
    private double descuento;
    private double descuento_real;
    private String trans_id;
    private String bodega_id;
    private String inventario_id;
    private String padre_id;
    private String keypadre;
    private String fecha_grabado;
    private int desc_promo;
    private int num_precio;
    private double cos_pro;
    private int  bandaprobado;

    public IVKardex(String identificador, double cantidad, double costo_total, double costo_real_total, double precio_total, double precio_real_total,String desc_sol, double descuento,double descuento_real, String trans_id, String bodega_id, String inventario_id, String padre_id,String keypadre,int desc_promo,int num_precio,double cos_pro,int bandaprobado,String fecha_grabado){
        this.identificador = identificador;
        this.cantidad = cantidad;
        this.costo_total = costo_total;
        this.costo_real_total = costo_real_total;
        this.precio_total = precio_total;
        this.precio_real_total = precio_real_total;
        this.desc_sol = desc_sol;
        this.descuento = descuento;
        this.trans_id = trans_id;
        this.bodega_id = bodega_id;
        this.inventario_id = inventario_id;
        this.padre_id=padre_id;
        this.keypadre = keypadre;
        this.desc_promo=desc_promo;
        this.fecha_grabado = fecha_grabado;
        this.num_precio=num_precio;
        this.cos_pro=cos_pro;
        this.descuento_real=descuento_real;
        this.bandaprobado=bandaprobado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCosto_total() {
        return costo_total;
    }

    public void setCosto_total(double costo_total) {
        this.costo_total = costo_total;
    }

    public double getCosto_real_total() {
        return costo_real_total;
    }

    public void setCosto_real_total(double costo_real_total) {
        this.costo_real_total = costo_real_total;
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

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
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

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
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

    public double getDescuento_real() {
        return descuento_real;
    }

    public void setDescuento_real(double descuento_real) {
        this.descuento_real = descuento_real;
    }

    public String getKeypadre() {
        return keypadre;
    }

    public void setKeypadre(String keypadre) {
        this.keypadre = keypadre;
    }

    public double getCos_pro() {
        return cos_pro;
    }

    public void setCos_pro(double cos_pro) {
        this.cos_pro = cos_pro;
    }

    public String getDesc_sol() {
        return desc_sol;
    }

    public void setDesc_sol(String desc_sol) {
        this.desc_sol = desc_sol;
    }

    public int getBandaprobado() {
        return bandaprobado;
    }

    public void setBandaprobado(int bandaprobado) {
        this.bandaprobado = bandaprobado;
    }

    @Override
    public String toString() {
        return "IVKardex{" +
                "id=" + id +
                ", identificador='" + identificador + '\'' +
                ", cantidad=" + cantidad +
                ", costo_total=" + costo_total +
                ", costo_real_total=" + costo_real_total +
                ", precio_total=" + precio_total +
                ", precio_real_total=" + precio_real_total +
                ", desc_sol=" + desc_sol +
                ", descuento=" + descuento +
                ", descuento_real=" + descuento_real +
                ", trans_id='" + trans_id + '\'' +
                ", bodega_id='" + bodega_id + '\'' +
                ", inventario_id='" + inventario_id + '\'' +
                ", padre_id='" + padre_id + '\'' +
                ", keypadre='" + keypadre + '\'' +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                ", desc_promo=" + desc_promo +
                ", num_precio=" + num_precio +
                ", cos_pro=" + cos_pro +
                ", bandaprobado=" + bandaprobado +
                '}';
    }
}
