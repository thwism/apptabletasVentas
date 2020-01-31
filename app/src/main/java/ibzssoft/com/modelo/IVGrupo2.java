package ibzssoft.com.modelo;

/**
 * Created by root on 23/20/25.
 */
public class IVGrupo2 {
    public static final String TABLE_NAME = "TBL_IVGRUPO2";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo2= "ivg2_idgrupo2";
    public static final String FIELD_codgrupo2= "ivg2_codgrupo2";
    public static final String FIELD_descripcion= "ivg2_descripcion";
    public static final String FIELD_bandvalida= "ivg2_BandValida";
    public static final String FIELD_fechagrabado = "ivg2_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo2+" int unique,"+
            FIELD_codgrupo2+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo2;
    private String codgrupo2;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo2(String idgrupo2, String codgrupo2, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo2 = idgrupo2;
        this.codgrupo2 = codgrupo2;
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

    public String getIdgrupo2() {
        return idgrupo2;
    }

    public void setIdgrupo2(String idgrupo2) {
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

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "IVGrupo2{" +
                "id=" + id +
                ", idgrupo2='" + idgrupo2 + '\'' +
                ", codgrupo2='" + codgrupo2 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
