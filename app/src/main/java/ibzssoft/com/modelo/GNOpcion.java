package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class GNOpcion {
    public static final String TABLE_NAME = "TBL_GNOPCION";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgnopcion= "gno_idgnopcion";
    public static final String FIELD_codigo= "gno_codigo";
    public static final String FIELD_valor= "gno_valor";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idgnopcion+" int unique,"+
            FIELD_codigo+" text unique,"+
            FIELD_valor+" text not null"
            +")";

    public int id;
    public int idgnopcion;
    public String codigo;
    public String valor;

    public GNOpcion(int idgnopcion, String codigo, String valor) {
        this.idgnopcion = idgnopcion;
        this.codigo = codigo;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdgnopcion() {
        return idgnopcion;
    }

    public void setIdgnopcion(int idgnopcion) {
        this.idgnopcion = idgnopcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "GNOpcion{" +
                "id=" + id +
                ", idgnopcion=" + idgnopcion +
                ", codigo='" + codigo + '\'' +
                ", valor='" + valor + '\'' +
                '}';
    }
}
