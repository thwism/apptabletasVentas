package ibzssoft.com.modelo;

/**
 * Created by root on 33/30/35.
 */
public class IVGrupo3 {
    public static final String TABLE_NAME = "TBL_IVGRUPO3";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idgrupo3= "ivg3_idgrupo3";
    public static final String FIELD_codgrupo3= "ivg3_codgrupo3";
    public static final String FIELD_descripcion= "ivg3_descripcion";
    public static final String FIELD_bandvalida= "ivg3_BandValida";
    public static final String FIELD_fechagrabado = "ivg3_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement,"+
            FIELD_idgrupo3+" int unique,"+
            FIELD_codgrupo3+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_bandvalida+" integer not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    private int id;
    private String idgrupo3;
    private String codgrupo3;
    private String descripcion;
    private int bandvalida;
    private String fechagrabado;

    public IVGrupo3(String idgrupo3, String codgrupo3, String descripcion, int bandvalida, String fechagrabado) {
        this.idgrupo3 = idgrupo3;
        this.codgrupo3 = codgrupo3;
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

    public String getIdgrupo3() {
        return idgrupo3;
    }

    public void setIdgrupo3(String idgrupo3) {
        this.idgrupo3 = idgrupo3;
    }

    public String getCodgrupo3() {
        return codgrupo3;
    }

    public void setCodgrupo3(String codgrupo3) {
        this.codgrupo3 = codgrupo3;
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
        return "IVGrupo3{" +
                "id=" + id +
                ", idgrupo3='" + idgrupo3 + '\'' +
                ", codgrupo3='" + codgrupo3 + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
