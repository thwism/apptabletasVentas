package ibzssoft.com.modelo;

/**
 * Created by root on 66/60/66.
 */
public class IVGrupo6 {
    public static final String TABLE_NAME = "TBL_IVGRUPO6";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo6= "ivg6_idgrupo6";
    public static final String FIELD_codgrupo6= "ivg6_codgrupo6";
    public static final String FIELD_descripcion= "ivg6_descripcion";
    public static final String FIELD_bandvalida= "ivg6_BandValida";
    public static final String FIELD_fechagrabado = "ivg6_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo6+" int unique,"+
            FIELD_codgrupo6+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo6;
    private String codgrupo6;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo6(String idgrupo6, String codgrupo6, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo6 = idgrupo6;
        this.codgrupo6 = codgrupo6;
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

    public String getIdgrupo6() {
        return idgrupo6;
    }

    public void setIdgrupo6(String idgrupo6) {
        this.idgrupo6 = idgrupo6;
    }

    public String getCodgrupo6() {
        return codgrupo6;
    }

    public void setCodgrupo6(String codgrupo6) {
        this.codgrupo6 = codgrupo6;
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
        return "IVGrupo6{" +
                "id=" + id +
                ", idgrupo6='" + idgrupo6 + '\'' +
                ", codgrupo6='" + codgrupo6 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
