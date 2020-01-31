package ibzssoft.com.modelo;
/**
 * Created by ricardo on 13/10/15.
 */
public class Parroquia {
    public static final String TABLE_NAME = "TBL_PARROQUIA";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idparroquia = "par_idparroquia";
    public static final String FIELD_descripcion = "par_descripcion";
    public static final String FIELD_idcanton = "par_idcanton";
    public static final String FIELD_bandvalida= "par_bandvalida";
    public static final String FIELD_fechagrabado= "par_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idparroquia+" int unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_idcanton+" int not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime not null,"
            +"FOREIGN KEY("+FIELD_idcanton+") REFERENCES "+ Canton.TABLE_NAME+"("+ Canton.FIELD_idcanton+")"
            +" )";

    public int id;
    public String idparroquia;
    public String descripcion;
    public String idcanton;
    public String bandvalida;
    public String fechagrabado;

    public Parroquia(String idparroquia, String descripcion, String idcanton, String bandvalida, String fechagrabado) {
        this.idparroquia = idparroquia;
        this.descripcion = descripcion;
        this.idcanton = idcanton;
        this.bandvalida = bandvalida;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdparroquia() {
        return idparroquia;
    }

    public void setIdparroquia(String idparroquia) {
        this.idparroquia = idparroquia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdcanton() {
        return idcanton;
    }

    public void setIdcanton(String idcanton) {
        this.idcanton = idcanton;
    }

    public String getBandvalida() {
        return bandvalida;
    }

    public void setBandvalida(String bandvalida) {
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
        return "Parroquia{" +
                "id=" + id +
                ", idparroquia='" + idparroquia + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", idcanton='" + idcanton + '\'' +
                ", bandvalida='" + bandvalida + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
