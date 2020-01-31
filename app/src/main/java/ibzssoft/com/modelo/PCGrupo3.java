package ibzssoft.com.modelo;

/**
 * Created by root on 33/30/35.
 */
public class PCGrupo3 {
    public static final String TABLE_NAME = "TBL_PCGRUPO3";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo3= "pcg3_idgrupo3";
    public static final String FIELD_codgrupo3= "pcg3_codgrupo3";
    public static final String FIELD_descripcion = "pcg3_descripcion";
    public static final String FIELD_preciosdisponibles= "pcg3_bandvalida";
    public static final String FIELD_bandvalida= "pcg3_preciosdisponibles";
    public static final String FIELD_fechagrabado = "pcg3_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idgrupo3+" int unique,"+
            FIELD_codgrupo3+" text not null,"+
            FIELD_descripcion+" text,"+
            FIELD_preciosdisponibles+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idgrupo3;
    public String codgrupo3;
    public String descripcion;
    public int bandvalida;
    public String preciosdisponibles;
    public String fechagrabado;

    public PCGrupo3(int idgrupo3, String codgrupo3, String descripcion, int bandvalida, String preciosdisponibles, String fechagrabado) {
        this.idgrupo3 = idgrupo3;
        this.codgrupo3 = codgrupo3;
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

    public String getCodgrupo3() {
        return codgrupo3;
    }

    public void setCodgrupo3(String codgrupo3) {
        this.codgrupo3 = codgrupo3;
    }

    public int getIdgrupo3() {
        return idgrupo3;
    }

    public void setIdgrupo3(int idgrupo3) {
        this.idgrupo3 = idgrupo3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PCGrupo3{" +
                "id=" + id +
                ", idgrupo3=" + idgrupo3 +
                ", codgrupo3='" + codgrupo3 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", preciosdisponibles='" + preciosdisponibles + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
