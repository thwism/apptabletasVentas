package ibzssoft.com.modelo;

/**
 * Created by root on 44/40/45.
 */
public class IVGrupo4 {
    public static final String TABLE_NAME = "TBL_IVGRUPO4";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo4= "ivg4_idgrupo4";
    public static final String FIELD_codgrupo4= "ivg4_codgrupo4";
    public static final String FIELD_descripcion= "ivg4_descripcion";
    public static final String FIELD_bandvalida= "ivg4_BandValida";
    public static final String FIELD_fechagrabado = "ivg4_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo4+" int unique,"+
            FIELD_codgrupo4+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo4;
    private String codgrupo4;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo4(String idgrupo4, String codgrupo4, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo4 = idgrupo4;
        this.codgrupo4 = codgrupo4;
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

    public String getIdgrupo4() {
        return idgrupo4;
    }

    public void setIdgrupo4(String idgrupo4) {
        this.idgrupo4 = idgrupo4;
    }

    public String getCodgrupo4() {
        return codgrupo4;
    }

    public void setCodgrupo4(String codgrupo4) {
        this.codgrupo4 = codgrupo4;
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
        return "IVGrupo4{" +
                "id=" + id +
                ", idgrupo4='" + idgrupo4 + '\'' +
                ", codgrupo4='" + codgrupo4 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
