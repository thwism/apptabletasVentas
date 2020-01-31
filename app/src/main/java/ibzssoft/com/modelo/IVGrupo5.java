package ibzssoft.com.modelo;

/**
 * Created by root on 55/50/55.
 */
public class IVGrupo5 {
    public static final String TABLE_NAME = "TBL_IVGRUPO5";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo5= "ivg5_idgrupo5";
    public static final String FIELD_codgrupo5= "ivg5_codgrupo5";
    public static final String FIELD_descripcion= "ivg5_descripcion";
    public static final String FIELD_bandvalida= "ivg5_BandValida";
    public static final String FIELD_fechagrabado = "ivg5_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo5+" int unique,"+
            FIELD_codgrupo5+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo5;
    private String codgrupo5;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo5(String idgrupo5, String codgrupo5, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo5 = idgrupo5;
        this.codgrupo5 = codgrupo5;
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

    public String getIdgrupo5() {
        return idgrupo5;
    }

    public void setIdgrupo5(String idgrupo5) {
        this.idgrupo5 = idgrupo5;
    }

    public String getCodgrupo5() {
        return codgrupo5;
    }

    public void setCodgrupo5(String codgrupo5) {
        this.codgrupo5 = codgrupo5;
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
        return "IVGrupo5{" +
                "id=" + id +
                ", idgrupo5='" + idgrupo5 + '\'' +
                ", codgrupo5='" + codgrupo5 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
