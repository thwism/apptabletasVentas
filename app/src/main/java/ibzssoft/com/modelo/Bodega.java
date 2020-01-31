package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Bodega {
    public static final String TABLE_NAME = "TBL_BODEGA";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String   FIELD_idbodega= "bod_idbodega";
    public static final String FIELD_codbodega= "bod_codbodega";
    public static final String FIELD_nombre= "bod_nombre";
    public static final String FIELD_bandvalida= "bod_bandvalida";
    public static final String FIELD_fechagrabado = "bod_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idbodega+" int unique,"+
            FIELD_codbodega+" text not null,"+
            FIELD_nombre+" text,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime not null"
            +" )";

    public int id;
    public int idbodega;
    public String codbodega;
    public String nombre;
    public int bandvalida;
    public String fechagrabado;

    public Bodega(int idbodega, String codbodega,String nombre, int bandvalida, String fechagrabado) {
        this.idbodega = idbodega;
        this.codbodega = codbodega;
        this.nombre=nombre;
        this.bandvalida = bandvalida;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdbodega() {
        return idbodega;
    }

    public void setIdbodega(int idbodega) {
        this.idbodega = idbodega;
    }

    public String getCodbodega() {
        return codbodega;
    }

    public void setCodbodega(String codbodega) {
        this.codbodega = codbodega;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Bodega{" +
                "id=" + id +
                ", idbodega=" + idbodega +
                ", codbodega='" + codbodega + '\'' +
                ", nombre='" + nombre + '\'' +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
