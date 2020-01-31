package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class PCGrupo1 {
    public static final String TABLE_NAME = "TBL_PCGRUPO1";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo1= "pcg1_idgrupo1";
    public static final String FIELD_codgrupo1= "pcg1_codgrupo1";
    public static final String FIELD_descripcion = "pcg1_descripcion";
    public static final String FIELD_preciosdisponibles= "pcg1_preciosdisponibles";
    public static final String FIELD_bandvalida= "pcg1_bandvalida";
    public static final String FIELD_fechagrabado = "pcg1_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idgrupo1+" int unique,"+
            FIELD_codgrupo1+" text not null,"+
            FIELD_descripcion+" text,"+
            FIELD_preciosdisponibles+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idgrupo1;
    public String codgrupo1;
    public String descripcion;
    public int bandvalida;
    public String preciosdisponibles;
    public String fechagrabado;

    public PCGrupo1(int idgrupo1, String codgrupo1, String descripcion, int bandvalida, String preciosdisponibles, String fechagrabado) {
        this.idgrupo1 = idgrupo1;
        this.codgrupo1 = codgrupo1;
        this.descripcion = descripcion;
        this.bandvalida = bandvalida;
        this.preciosdisponibles = preciosdisponibles;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdgrupo1() {
        return idgrupo1;
    }

    public void setIdgrupo1(int idgrupo1) {
        this.idgrupo1 = idgrupo1;
    }

    public String getCodgrupo1() {
        return codgrupo1;
    }

    public void setCodgrupo1(String codgrupo1) {
        this.codgrupo1 = codgrupo1;
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

    public String getPreciosdisponibles() {
        return preciosdisponibles;
    }

    public void setPreciosdisponibles(String preciosdisponibles) {
        this.preciosdisponibles = preciosdisponibles;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "PCGrupo1{" +
                "id=" + id +
                ", idgrupo1=" + idgrupo1 +
                ", codgrupo1='" + codgrupo1 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", preciosdisponibles='" + preciosdisponibles + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
