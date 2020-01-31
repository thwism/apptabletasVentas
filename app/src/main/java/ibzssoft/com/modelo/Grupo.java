package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Grupo {
    public static final String TABLE_NAME = "TBL_GRUPO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_codgrupo = "gru_codGrupo";
    public static final String FIELD_descripcion  = "gru_descripcion";
    public static final String FIELD_fechagrabado = "gru_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_codgrupo+" text unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public String codgrupo;
    public String descripcion;
    public String fechagrabado;

    public Grupo(String codgrupo, String descripcion, String fechagrabado) {
        this.codgrupo = codgrupo;
        this.descripcion = descripcion;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(String codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }
    @Override
    public String toString() {
        return "Grupo{" +
                "id=" + id +
                ", codgrupo='" + codgrupo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
