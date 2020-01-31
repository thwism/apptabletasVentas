package ibzssoft.com.modelo;

/**
 * Created by root on 23/20/25.
 */
public class PCGrupo2 {
    public static final String TABLE_NAME = "TBL_PCGRUPO2";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo2= "pcg2_idgrupo2";
    public static final String FIELD_codgrupo2= "pcg2_codgrupo2";
    public static final String FIELD_descripcion = "pcg2_descripcion";
    public static final String FIELD_preciosdisponibles= "pcg2_preciosdisponibles";
    public static final String FIELD_bandvalida= "pcg2_bandvalida";
    public static final String FIELD_fechagrabado = "pcg2_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idgrupo2+" int unique,"+
            FIELD_codgrupo2+" text not null,"+
            FIELD_descripcion+" text,"+
            FIELD_preciosdisponibles+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idgrupo2;
    public String codgrupo2;
    public String descripcion;
    public int bandvalida;
    public String preciosdisponibles;
    public String fechagrabado;

    public PCGrupo2(int idgrupo2, String codgrupo2, String descripcion, int bandvalida, String preciosdisponibles, String fechagrabado) {
        this.idgrupo2 = idgrupo2;
        this.codgrupo2 = codgrupo2;
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

    public int getIdgrupo2() {
        return idgrupo2;
    }

    public void setIdgrupo2(int idgrupo2) {
        this.idgrupo2 = idgrupo2;
    }

    public String getCodgrupo2() {
        return codgrupo2;
    }

    public void setCodgrupo2(String codgrupo2) {
        this.codgrupo2 = codgrupo2;
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
        return "PCGrupo2{" +
                "id=" + id +
                ", idgrupo2=" + idgrupo2 +
                ", codgrupo2='" + codgrupo2 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", preciosdisponibles='" + preciosdisponibles + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
