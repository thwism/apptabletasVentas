package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class KardexTransaccion {
    public static final String TABLE_NAME = "TBL_KARDEXTRANSACCION";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_transid= "ktr_TransID";
    public static final String FIELD_idprovcli= "ktr_IdProvCli";
    public static final String FIELD_nombre= "ktr_Nombre";
    public static final String FIELD_tipo_origen= "ktr_TIDOrigen";
    public static final String FIELD_fechatrans= "ktr_FechaTrans";
    public static final String FIELD_horatrans= "ktr_HoraTrans";
    public static final String FIELD_codtrans= "ktr_CodTrans";
    public static final String FIELD_trans= "ivk_Trans";
    public static final String FIELD_numdocref= "ivk_NumDocRef";
    public static final String FIELD_descripcion= "ivk_Descripcion";
    public static final String FIELD_valor= "ivk_Valor";
    public static final String FIELD_doc= "ivk_Doc";
    public static final String FIELD_debe= "ivk_Debe";
    public static final String FIELD_haber= "ivk_Haber";
    public static final String FIELD_saldo= "ivk_Saldo";
    public static final String FIELD_cotizacion= "ivk_Cotizacion";
    public static final String FIELD_fechavenci= "ivk_FechaVenci";
    public static final String FIELD_estado= "ivk_Estado";
    public static final String FIELD_orden= "ivk_Orden";
    public static final String FIELD_idorigen= "ivk_IdOrigen";
    public static final String FIELD_idcobrador= "ivk_idCobrador";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_transid+" text not null,"+
            FIELD_idprovcli+" text not null,"+
            FIELD_nombre+" text,"+
            FIELD_tipo_origen+" text,"+
            FIELD_fechatrans+" datetime,"+
            FIELD_horatrans+" datetime,"+
            FIELD_codtrans+" text,"+
            FIELD_trans+" text,"+
            FIELD_numdocref+" text,"+
            FIELD_descripcion+" text,"+
            FIELD_valor+" double,"+
            FIELD_doc+" text,"+
            FIELD_debe+" double,"+
            FIELD_haber+" double,"+
            FIELD_saldo+" double,"+
            FIELD_cotizacion+" text,"+
            FIELD_fechavenci+" datetime,"+
            FIELD_estado+" int,"+
            FIELD_orden+" int,"+
            FIELD_idorigen+" text,"+
            FIELD_idcobrador+" text,"
            +"FOREIGN KEY("+FIELD_idcobrador+") REFERENCES "+ KardexTransaccion.TABLE_NAME+"("+ Vendedor.FIELD_idvendedor+"))";

    private int id;
    private String TransID;
    private String idProvCli;
    private String Nombre;
    private String TIDOrigen;
    private String FechaTrans;
    private String HoraTrans;
    private String CodTrans;
    private String Trans;
    private String NumDocRef;
    private String Descripcion;
    private double Valor;
    private String Doc;
    private double Debe;
    private double Haber;
    private double Saldo;
    private String Cotizacion;
    private String FechaVenci;
    private int Estado;
    private int Orden;
    private String IdOrigen;
    private String idCobrador;

    public KardexTransaccion(String transID, String idProvCli, String nombre, String TIDOrigen, String fechaTrans, String horaTrans, String codTrans, String trans, String numDocRef, String descripcion, double valor, String doc, double debe, double haber, double saldo, String cotizacion, String fechaVenci, int estado, int orden, String idOrigen, String idCobrador) {
        TransID = transID;
        this.idProvCli = idProvCli;
        Nombre = nombre;
        this.TIDOrigen = TIDOrigen;
        FechaTrans = fechaTrans;
        HoraTrans = horaTrans;
        CodTrans = codTrans;
        Trans = trans;
        NumDocRef = numDocRef;
        Descripcion = descripcion;
        Valor = valor;
        Doc = doc;
        Debe = debe;
        Haber = haber;
        Saldo = saldo;
        Cotizacion = cotizacion;
        FechaVenci = fechaVenci;
        Estado = estado;
        Orden = orden;
        IdOrigen = idOrigen;
        this.idCobrador = idCobrador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransID() {
        return TransID;
    }

    public void setTransID(String transID) {
        TransID = transID;
    }

    public String getIdProvCli() {
        return idProvCli;
    }

    public void setIdProvCli(String idProvCli) {
        this.idProvCli = idProvCli;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTIDOrigen() {
        return TIDOrigen;
    }

    public void setTIDOrigen(String TIDOrigen) {
        this.TIDOrigen = TIDOrigen;
    }

    public String getFechaTrans() {
        return FechaTrans;
    }

    public void setFechaTrans(String fechaTrans) {
        FechaTrans = fechaTrans;
    }

    public String getHoraTrans() {
        return HoraTrans;
    }

    public void setHoraTrans(String horaTrans) {
        HoraTrans = horaTrans;
    }

    public String getCodTrans() {
        return CodTrans;
    }

    public void setCodTrans(String codTrans) {
        CodTrans = codTrans;
    }

    public String getTrans() {
        return Trans;
    }

    public void setTrans(String trans) {
        Trans = trans;
    }

    public String getNumDocRef() {
        return NumDocRef;
    }

    public void setNumDocRef(String numDocRef) {
        NumDocRef = numDocRef;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getDoc() {
        return Doc;
    }

    public void setDoc(String doc) {
        Doc = doc;
    }

    public double getDebe() {
        return Debe;
    }

    public void setDebe(double debe) {
        Debe = debe;
    }

    public double getHaber() {
        return Haber;
    }

    public void setHaber(double haber) {
        Haber = haber;
    }

    public double getSaldo() {
        return Saldo;
    }

    public void setSaldo(double saldo) {
        Saldo = saldo;
    }

    public String getCotizacion() {
        return Cotizacion;
    }

    public void setCotizacion(String cotizacion) {
        Cotizacion = cotizacion;
    }

    public String getFechaVenci() {
        return FechaVenci;
    }

    public void setFechaVenci(String fechaVenci) {
        FechaVenci = fechaVenci;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    public String getIdOrigen() {
        return IdOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        IdOrigen = idOrigen;
    }

    public String getIdCobrador() {
        return idCobrador;
    }

    public void setIdCobrador(String idCobrador) {
        this.idCobrador = idCobrador;
    }

    @Override
    public String toString() {
        return "KardexTransaccion{" +
                "id=" + id +
                ", TransID='" + TransID + '\'' +
                ", idProvCli='" + idProvCli + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", TIDOrigen='" + TIDOrigen + '\'' +
                ", FechaTrans='" + FechaTrans + '\'' +
                ", HoraTrans='" + HoraTrans + '\'' +
                ", CodTrans='" + CodTrans + '\'' +
                ", Trans='" + Trans + '\'' +
                ", NumDocRef='" + NumDocRef + '\'' +
                ", Descripcion='" + Descripcion + '\'' +
                ", Valor=" + Valor +
                ", Doc='" + Doc + '\'' +
                ", Debe=" + Debe +
                ", Haber=" + Haber +
                ", Saldo=" + Saldo +
                ", Cotizacion='" + Cotizacion + '\'' +
                ", FechaVenci='" + FechaVenci + '\'' +
                ", Estado=" + Estado +
                ", Orden=" + Orden +
                ", IdOrigen='" + IdOrigen + '\'' +
                ", idCobrador='" + idCobrador + '\'' +
                '}';
    }
}
