package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Canton {
    public static final String TABLE_NAME = "TBL_CANTON";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idcanton= "can_idcanton";
    public static final String FIELD_descripcion= "can_descripcion";
    public static final String FIELD_idprovincia= "can_idprovincia";
    public static final String FIELD_bandvalida = "can_bandvalida";
    public static final String FIELD_fechagrabado = "can_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idcanton+" int unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_idprovincia+" int not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime  not null,"
            +"FOREIGN KEY("+FIELD_idprovincia+") REFERENCES "+ Provincia.TABLE_NAME+"("+ Provincia.FIELD_idprovincia+")"
            +" )";

    public int id;
    public int idcanton;
    public String descripcion;
    public int idprovincia;
    public int bandvalida;
    public String fechagrabado;

    public Canton(int idcanton, String descripcion, int idprovincia, int bandvalida, String fechagrabado) {
        this.idcanton = idcanton;
        this.descripcion = descripcion;
        this.idprovincia = idprovincia;
        this.bandvalida = bandvalida;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdcanton() {
        return idcanton;
    }

    public void setIdcanton(int idcanton) {
        this.idcanton = idcanton;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(int idprovincia) {
        this.idprovincia = idprovincia;
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
        return "Canton{" +
                "id=" + id +
                ", idcanton=" + idcanton +
                ", descripcion='" + descripcion + '\'' +
                ", idprovincia=" + idprovincia +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
