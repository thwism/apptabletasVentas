package ibzssoft.com.modelo;

/**
 * Created by root on 15/10/15.
 */
public class TSFormaCobroPago {
    public static final String TABLE_NAME = "TBL_TSFORMACOBROPAGO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idforma = "tsf_idforma";
    public static final String FIELD_codforma= "tsf_codforma";
    public static final String FIELD_nombreforma = "tsf_nombreforma";
    public static final String FIELD_plazo= "tsf_plazo";
    public static final String FIELD_bandvalida= "tsf_bandvalida";
    public static final String FIELD_fechagrabado= "tsf_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idforma+" int unique,"+
            FIELD_codforma+" text unique,"+
            FIELD_nombreforma+" text not null,"+
            FIELD_plazo+" int not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_fechagrabado+" datetime not null"
            +")";

    private int id;
    private int idforma;
    private String codforma;
    private String nombreforma;
    private int plazo;
    private int bandvalida;
    private String fechagrabado;

    public TSFormaCobroPago(int idforma, String codforma, String nombreforma, int plazo, int bandvalida, String fechagrabado) {
        this.idforma = idforma;
        this.codforma = codforma;
        this.nombreforma = nombreforma;
        this.plazo = plazo;
        this.bandvalida = bandvalida;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdforma() {
        return idforma;
    }

    public void setIdforma(int idforma) {
        this.idforma = idforma;
    }

    public String getCodforma() {
        return codforma;
    }

    public void setCodforma(String codforma) {
        this.codforma = codforma;
    }

    public String getNombreforma() {
        return nombreforma;
    }

    public void setNombreforma(String nombreforma) {
        this.nombreforma = nombreforma;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
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
        return "TSFormaCobroPago{" +
                "id=" + id +
                ", idforma=" + idforma +
                ", codforma='" + codforma + '\'' +
                ", nombreforma='" + nombreforma + '\'' +
                ", plazo=" + plazo +
                ", bandvalida=" + bandvalida +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
