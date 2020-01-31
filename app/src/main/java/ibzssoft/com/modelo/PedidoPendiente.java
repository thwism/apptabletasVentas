package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class PedidoPendiente implements Serializable{
    public static final String TABLE_NAME = "TBL_PEDIDOPENDIENTE";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idprovcli= "ppe_idprovcli";
    public static final String FIELD_ruc_ci= "ppe_ruc_ci";
    public static final String FIELD_nombres= "ppe_nombres";
    public static final String FIELD_nombre_alterno= "ppe_nombre_alterno";
    public static final String FIELD_items= "ppe_items";
    public static final String FIELD_vendedor_id= "ppe_vendedor_id";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idprovcli+" text,"+
            FIELD_ruc_ci+" text,"+
            FIELD_nombres+" text,"+
            FIELD_nombre_alterno+" text,"+
            FIELD_items+" integer,"+
            FIELD_vendedor_id+" text )";

    private int id;
    private String idprovcli;
    private String ruc_ci;
    private String nombres;
    private String nombre_alterno;
    private int items;
    private String vendedor_id;

    public PedidoPendiente(String idprovcli, String ruc_ci, String nombres, String nombre_alterno, int items, String vendedor_id) {
        this.idprovcli = idprovcli;
        this.ruc_ci = ruc_ci;
        this.nombres = nombres;
        this.nombre_alterno = nombre_alterno;
        this.items = items;
        this.vendedor_id = vendedor_id;
    }

    public String getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(String vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getNombre_alterno() {
        return nombre_alterno;
    }

    public void setNombre_alterno(String nombre_alterno) {
        this.nombre_alterno = nombre_alterno;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getRuc_ci() {
        return ruc_ci;
    }

    public void setRuc_ci(String ruc_ci) {
        this.ruc_ci = ruc_ci;
    }

    public String getIdprovcli() {
        return idprovcli;
    }

    public void setIdprovcli(String idprovcli) {
        this.idprovcli = idprovcli;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PedidoPendiente{" +
                "id=" + id +
                ", idprovcli='" + idprovcli + '\'' +
                ", ruc_ci='" + ruc_ci + '\'' +
                ", nombres='" + nombres + '\'' +
                ", nombre_alterno='" + nombre_alterno + '\'' +
                ", items=" + items +
                ", vendedor_id='" + vendedor_id + '\'' +
                '}';
    }
}
