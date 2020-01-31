package ibzssoft.com.modelo;

/**
 * Created by root on 14/10/15.
 */
public class GNTrans {
    public static final String TABLE_NAME = "TBL_GNTrans";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_codtrans = "gnt_codtrans";
    public static final String FIELD_idbodegapre= "gnt_idbodegapre";
    public static final String FIELD_descripcion = "gnt_Descripcion";
    public static final String FIELD_nombretrans= "gnt_nombretrans";
    public static final String FIELD_preciopcgrupo= "gnt_preciopcgrupo";
    public static final String FIELD_numfilas= "gnt_numfilas";
    public static final String FIELD_maxdocs= "gnt_maxdocs";
    public static final String FIELD_opciones= "gnt_opciones";
    public static final String FIELD_badobservacion= "gnt_badobservacion";

    public static final String FIELD_numdiasvencidos= "gnt_numdiasvencidos";
    public static final String FIELD_diasgracia= "gnt_diasgracia";
    public static final String FIELD_bandobservacion= "gnt_bandobservacion";
    public static final String FIELD_codpantalla= "gnt_codpantalla";
    public static final String FIELD_fecha_grabado = "gnt_FechaGrabado";
    public static final String FIELD_modulo = "gnt_modulo";
    public static final String FIELD_idclientepre = "gnt_idclientepre";
    public static final String FIELD_cantidadpre = "gnt_cantidadpre";
    public static final String FIELD_preciopre = "gnt_preciopre";
    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_codtrans+" text unique,"+
            FIELD_idbodegapre+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_nombretrans+" text,"+
            FIELD_preciopcgrupo+" text,"+
            FIELD_numfilas+" text,"+
            FIELD_maxdocs+" text,"+
            FIELD_opciones+" text,"+
            FIELD_bandobservacion+" text,"+
            FIELD_numdiasvencidos+" text,"+
            FIELD_diasgracia+" text,"+
            FIELD_codpantalla+" text,"+
            FIELD_modulo+" text,"+
            FIELD_idclientepre+" text,"+
            FIELD_cantidadpre+" int,"+
            FIELD_preciopre+" int,"+
            FIELD_fecha_grabado+" datetime not null"
            +" )";

    public int id;
    public String codtrans;
    public String idbodegapre;
    public String descripcion;
    public String nombretrans;
    public String preciopcgrupo;
    public String numfilas;
    public String maxdocs;
    public String opciones;
    public String bandobservacion;
    public String numdiasvencidos;
    public String diasgracia;
    public String codpantalla;
    public String modulo;
    public String fechagrabado;
    public String idclientepre;
    public String preciopre;
    public String cantidadpre;

    public GNTrans(String codtrans, String idbodegapre, String descripcion, String nombretrans, String preciopcgrupo, String numfilas, String maxdocs, String opciones, String bandobservacion, String numdiasvencidos,String diasgracia,String codpantalla,String modulo, String fechagrabado) {
        this.codtrans = codtrans;
        this.idbodegapre = idbodegapre;
        this.descripcion = descripcion;
        this.nombretrans = nombretrans;
        this.preciopcgrupo = preciopcgrupo;
        this.numfilas = numfilas;
        this.maxdocs = maxdocs;
        this.opciones = opciones;
        this.bandobservacion = bandobservacion;
        this.numdiasvencidos = numdiasvencidos;
        this.codpantalla=codpantalla;
        this.diasgracia = diasgracia;
        this.modulo = modulo;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodtrans() {
        return codtrans;
    }

    public void setCodtrans(String codtrans) {
        this.codtrans = codtrans;
    }

    public String getIdbodegapre() {
        return idbodegapre;
    }

    public void setIdbodegapre(String idbodegapre) {
        this.idbodegapre = idbodegapre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombretrans() {
        return nombretrans;
    }

    public void setNombretrans(String nombretrans) {
        this.nombretrans = nombretrans;
    }


    public String getPreciopcgrupo() {
        return preciopcgrupo;
    }

    public void setPreciopcgrupo(String preciopcgrupo) {
        this.preciopcgrupo = preciopcgrupo;
    }

    public String getNumfilas() {
        return numfilas;
    }

    public void setNumfilas(String numfilas) {
        this.numfilas = numfilas;
    }

    public String getMaxdocs() {
        return maxdocs;
    }

    public void setMaxdocs(String maxdocs) {
        this.maxdocs = maxdocs;
    }

    public String getOpciones() {
        return opciones;
    }

    public void setOpciones(String opciones) {
        this.opciones = opciones;
    }

    public String getBandobservacion() {
        return bandobservacion;
    }

    public void setBandobservacion(String bandobservacion) {
        this.bandobservacion = bandobservacion;
    }

    public String getNumdiasvencidos() {
        return numdiasvencidos;
    }

    public void setNumdiasvencidos(String numdiasvencidos) {
        this.numdiasvencidos = numdiasvencidos;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    public String getDiasgracia() {
        return diasgracia;
    }

    public void setDiasgracia(String diasgracia) {
        this.diasgracia = diasgracia;
    }

    public String getCodpantalla() {
        return codpantalla;
    }

    public void setCodpantalla(String codpantalla) {
        this.codpantalla = codpantalla;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getCantidadpre() {
        return cantidadpre;
    }

    public void setCantidadpre(String cantidadpre) {
        this.cantidadpre = cantidadpre;
    }

    public String getPreciopre() {
        return preciopre;
    }

    public void setPreciopre(String preciopre) {
        this.preciopre = preciopre;
    }

    public String getIdclientepre() {
        return idclientepre;
    }

    public void setIdclientepre(String idclientepre) {
        this.idclientepre = idclientepre;
    }

    @Override
    public String toString() {
        return "GNTrans{" +
                "id=" + id +
                ", codtrans='" + codtrans + '\'' +
                ", idbodegapre='" + idbodegapre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nombretrans='" + nombretrans + '\'' +
                ", preciopcgrupo='" + preciopcgrupo + '\'' +
                ", numfilas='" + numfilas + '\'' +
                ", maxdocs='" + maxdocs + '\'' +
                ", opciones='" + opciones + '\'' +
                ", bandobservacion='" + bandobservacion + '\'' +
                ", numdiasvencidos='" + numdiasvencidos + '\'' +
                ", diasgracia='" + diasgracia + '\'' +
                ", codpantalla='" + codpantalla + '\'' +
                ", modulo='" + modulo + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                ", idclientepre='" + idclientepre + '\'' +
                ", preciopre='" + preciopre + '\'' +
                ", cantidadpre='" + cantidadpre + '\'' +
                '}';
    }
}
