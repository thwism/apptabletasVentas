package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Banco {
    public static final String TABLE_NAME = "TBL_BANCO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idbanco= "ban_idbanco";
    public static final String FIELD_codbanco= "ban_codbanco";
    public static final String FIELD_descripcion= "ban_descripcion";
    public static final String FIELD_bandvalida= "ban_bandvalida";
    public static final String FIELD_fechagrabado = "ban_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idbanco+" int unique,"+
            FIELD_codbanco+" text unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idbanco;
    public String codbanco;
    public String descripcion;
    public int bandvalida;
    public String fechagrabado;

    public Banco(int idbanco, String codbanco, String descripcion, int bandvalida, String fechagrabado) {
        this.idbanco = idbanco;
        this.codbanco = codbanco;
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

    public int getIdbanco() {
        return idbanco;
    }

    public void setIdbanco(int idbanco) {
        this.idbanco = idbanco;
    }

    public String getCodbanco() {
        return codbanco;
    }

    public void setCodbanco(String codbanco) {
        this.codbanco = codbanco;
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
        return "Banco{" +
                "id=" + id +
                ", idbanco=" + idbanco +
                ", codbanco='" + codbanco + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
