package ibzssoft.com.modelo;

/**
 * Created by root on 44/40/45.
 */
public class PCGrupo4 {
    public static final String TABLE_NAME = "TBL_PCGRUPO4";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo4= "pcg4_idgrupo4";
    public static final String FIELD_codgrupo4= "pcg4_codgrupo4";
    public static final String FIELD_descripcion = "pcg4_descripcion";
    public static final String FIELD_preciosdisponibles= "pcg4_preciosdispoibles";
    public static final String FIELD_bandvalida= "pcg4_bandvalida";
    public static final String FIELD_fechagrabado = "pcg4_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idgrupo4+" int unique,"+
            FIELD_codgrupo4+" text not null,"+
            FIELD_descripcion+" text,"+
            FIELD_preciosdisponibles+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idgrupo4;
    public String codgrupo4;
    public String descripcion;
    public int bandvalida;
    public String preciosdisponibles;
    public String fechagrabado;

    public PCGrupo4(int idgrupo4, String codgrupo4, String descripcion, int bandvalida, String preciosdisponibles, String fechagrabado) {
        this.idgrupo4 = idgrupo4;
        this.codgrupo4 = codgrupo4;
        this.descripcion = descripcion;
        this.bandvalida = bandvalida;
        this.preciosdisponibles = preciosdisponibles;
        this.fechagrabado = fechagrabado;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    public String getPreciosdisponibles() {
        return preciosdisponibles;
    }

    public void setPreciosdisponibles(String preciosdisponibles) {
        this.preciosdisponibles = preciosdisponibles;
    }

    public int getBandvalida() {
        return bandvalida;
    }

    public void setBandvalida(int bandvalida) {
        this.bandvalida = bandvalida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodgrupo4() {
        return codgrupo4;
    }

    public void setCodgrupo4(String codgrupo4) {
        this.codgrupo4 = codgrupo4;
    }

    public int getIdgrupo4() {
        return idgrupo4;
    }

    public void setIdgrupo4(int idgrupo4) {
        this.idgrupo4 = idgrupo4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PCGrupo4{" +
                "id=" + id +
                ", idgrupo4=" + idgrupo4 +
                ", codgrupo4='" + codgrupo4 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", preciosdisponibles='" + preciosdisponibles + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
