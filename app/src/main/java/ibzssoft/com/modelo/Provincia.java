package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Provincia {
    public static final String TABLE_NAME = "TBL_PROVINCIA";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idprovincia= "pro_idprovincia";
    public static final String FIELD_descripcion = "pro_descripcion";
    public static final String FIELD_bandvalida= "pro_bandvalida";
    public static final String FIELD_fechagrabado= "pro_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idprovincia+" int unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime not null"
            +")";

    public int id;
    public int idprovincia;
    public String descripcion;
    public int bandvalida;
    public String fechagrabado;

    public Provincia(int idprovincia, String descripcion, int bandvalida, String fechagrabado) {
        this.idprovincia = idprovincia;
        this.descripcion = descripcion;
        this.bandvalida = bandvalida;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(int idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getBandvalida() {
        return bandvalida;
    }

    public void setBandvalida(int bandvalida) {
        this.bandvalida = bandvalida;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "Provincia{" +
                "id=" + id +
                ", idprovincia=" + idprovincia +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
