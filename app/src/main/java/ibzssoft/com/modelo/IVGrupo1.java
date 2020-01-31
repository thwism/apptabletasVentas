package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class IVGrupo1 {
    public static final String TABLE_NAME = "TBL_IVGRUPO1";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo1= "ivg1_idgrupo1";
    public static final String FIELD_codgrupo1= "ivg1_codgrupo1";
    public static final String FIELD_descripcion= "ivg1_descripcion";
    public static final String FIELD_bandvalida= "ivg1_BandValida";
    public static final String FIELD_fechagrabado = "ivg1_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo1+" int unique,"+
            FIELD_codgrupo1+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo1;
    private String codgrupo1;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo1(String idgrupo1, String codgrupo1, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo1 = idgrupo1;
        this.codgrupo1 = codgrupo1;
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

    public String getIdgrupo1() {
        return idgrupo1;
    }

    public void setIdgrupo1(String idgrupo1) {
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

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "IVGrupo1{" +
                "id=" + id +
                ", idgrupo1='" + idgrupo1 + '\'' +
                ", codgrupo1='" + codgrupo1 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
