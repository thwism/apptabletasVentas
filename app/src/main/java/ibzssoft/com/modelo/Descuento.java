package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Descuento {
    public static final String TABLE_NAME = "TBL_DESCUENTO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_identificador = "des_codDescuento";
    public static final String FIELD_porcentaje = "des_Porcentaje";
    public static final String FIELD_estado = "des_estado";
    public static final String FIELD_cliente_id = "des_cliente_id";
    public static final String FIELD_vendedor_id= "des_vendedor_id";
    public static final String FIELD_inventario_id = "des_inventario_id";
    public static final String FIELD_fecha_grabado = "des_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_identificador+" text not null,"+
            FIELD_porcentaje+" double not null,"+
            FIELD_estado+" integer not null,"+
            FIELD_cliente_id+" text not null,"+
            FIELD_vendedor_id+" text not null,"+
            FIELD_inventario_id+" text not null,"+
            FIELD_fecha_grabado+" datetime not null,"
            +"FOREIGN KEY("+FIELD_cliente_id+") REFERENCES "+ Cliente.TABLE_NAME+"("+ Cliente.FIELD_idprovcli+"),"
            +"FOREIGN KEY("+FIELD_inventario_id+") REFERENCES "+ IVInventario.TABLE_NAME+"("+ IVInventario.FIELD_identificador+")"
            +" )";

    private int id;
    private String identificador;
    private double porcentaje;
    private int estado;
    private String cliente_id;
    private String inventario_id;
    private String vendedor_id;
    private String fecha_grabado;

    public Descuento(String identificador, double porcentaje, int estado, String cliente_id, String inventario_id,String vendedor_id, String fecha_grabado) {
        this.identificador = identificador;
        this.porcentaje = porcentaje;
        this.estado = estado;
        this.cliente_id = cliente_id;
        this.inventario_id = inventario_id;
        this.fecha_grabado = fecha_grabado;
        this.vendedor_id = vendedor_id;
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

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getInventario_id() {
        return inventario_id;
    }

    public void setInventario_id(String inventario_id) {
        this.inventario_id = inventario_id;
    }

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
    }

    public String getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(String vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    @Override
    public String toString() {
        return "Descuento{" +
                "id=" + id +
                ", identificador='" + identificador + '\'' +
                ", porcentaje=" + porcentaje +
                ", estado=" + estado +
                ", cliente_id='" + cliente_id + '\'' +
                ", inventario_id='" + inventario_id + '\'' +
                ", vendedor_id='" + vendedor_id + '\'' +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                '}';
    }
}
