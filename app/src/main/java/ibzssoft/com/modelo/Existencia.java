package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Existencia {
    public static final String TABLE_NAME = "TBL_EXISTENCIA";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_existencia = "exi_existencia";
    public static final String FIELD_bodega_id = "exi_bodega_id";
    public static final String FIELD_inventario_id = "exi_inventario_id";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_existencia+" double not null,"+
            FIELD_bodega_id+" text not null,"+
            FIELD_inventario_id+" text not null,"
            +"FOREIGN KEY("+FIELD_inventario_id+") REFERENCES "+ IVInventario.TABLE_NAME+"("+ IVInventario.FIELD_identificador+"),"
            +"FOREIGN KEY("+FIELD_bodega_id+") REFERENCES "+ Bodega.TABLE_NAME+"("+ Bodega.FIELD_idbodega+")"
            +" )";

    private int id;
    private double existencia;
    private String bodega_id;
    private String inventario_id;

    public Existencia(double existencia, String bodega_id, String inventario_id) {
        this.existencia = existencia;
        this.bodega_id = bodega_id;
        this.inventario_id = inventario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getExistencia() {
        return existencia;
    }

    public void setExistencia(double existencia) {
        this.existencia = existencia;
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

    @Override
    public String toString() {
        return "Existencia{" +
                "id=" + id +
                ", existencia=" + existencia +
                ", bodega_id='" + bodega_id + '\'' +
                ", inventario_id='" + inventario_id + '\'' +
                '}';
    }
}
