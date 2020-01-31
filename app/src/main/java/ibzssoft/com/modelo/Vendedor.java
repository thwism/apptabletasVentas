package ibzssoft.com.modelo;

public class Vendedor {

    public static final String TABLE_NAME = "TBL_VENDEDOR";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idvendedor = "ven_idvendedor";
    public static final String FIELD_codvendedor = "ven_codvendedor";
    public static final String FIELD_codusuario= "ven_codusuario";
    public static final String FIELD_nombre= "ven_nombre";
    public static final String FIELD_bandvalida= "ven_bandvalida";
    public static final String FIELD_bandvendedor= "ven_bandvendedor";
    public static final String FIELD_bandcobrador= "ven_bandcobrador";
    public static final String FIELD_lineas= "ven_lineas";
    public static final String FIELD_vendedores= "ven_vendedores";
    public static final String FIELD_ordenbodegas= "ven_ordenbodegas";
    public static final String FIELD_rutastablet= "ven_rutastablet";
    public static final String FIELD_fechagrabado= "ven_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idvendedor+" int unique,"+
            FIELD_codvendedor+" text not null,"+
            FIELD_codusuario+" text not null,"+
            FIELD_nombre+" text,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_bandvendedor+" int,"+
            FIELD_bandcobrador+" int,"+
            FIELD_lineas+" text,"+
            FIELD_vendedores+" text,"+
            FIELD_ordenbodegas+" text,"+
            FIELD_rutastablet+" text,"+
            FIELD_fechagrabado+" datetime not null,"
            +"FOREIGN KEY("+FIELD_codusuario+") REFERENCES "+ Usuario.TABLE_NAME+" ("+ Usuario.FIELD_codusuario+")"
            +")";

    private int id;
    private int idvendedor;
    private String codvendedor;
    private String codusuario;
    private String nombre;
    private int bandvalida;
    private int bandvendedor;
    private int bandcobrador;
    private String lineas;
    private String vendedores;
    private String ordenbodegas;
    private String rutastablet;
    private String fechagrabado;

    public Vendedor(int idvendedor, String codvendedor, String codusuario, String nombre, int bandvalida, int bandvendedor, int bandcobrador, String lineas, String vendedores, String ordenbodegas, String rutastablet, String fechagrabado) {
        this.idvendedor = idvendedor;
        this.codvendedor = codvendedor;
        this.codusuario = codusuario;
        this.nombre = nombre;
        this.bandvalida = bandvalida;
        this.bandvendedor = bandvendedor;
        this.bandcobrador = bandcobrador;
        this.lineas = lineas;
        this.vendedores = vendedores;
        this.ordenbodegas = ordenbodegas;
        this.rutastablet = rutastablet;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(int idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(String codvendedor) {
        this.codvendedor = codvendedor;
    }

    public String getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(String codusuario) {
        this.codusuario = codusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getBandvalida() {
        return bandvalida;
    }

    public void setBandvalida(int bandvalida) {
        this.bandvalida = bandvalida;
    }

    public int getBandvendedor() {
        return bandvendedor;
    }

    public void setBandvendedor(int bandvendedor) {
        this.bandvendedor = bandvendedor;
    }

    public int getBandcobrador() {
        return bandcobrador;
    }

    public void setBandcobrador(int bandcobrador) {
        this.bandcobrador = bandcobrador;
    }

    public String getLineas() {
        return lineas;
    }

    public void setLineas(String lineas) {
        this.lineas = lineas;
    }

    public String getVendedores() {
        return vendedores;
    }

    public void setVendedores(String vendedores) {
        this.vendedores = vendedores;
    }

    public String getOrdenbodegas() {
        return ordenbodegas;
    }

    public void setOrdenbodegas(String ordenbodegas) {
        this.ordenbodegas = ordenbodegas;
    }

    public String getRutastablet() {
        return rutastablet;
    }

    public void setRutastablet(String rutastablet) {
        this.rutastablet = rutastablet;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", idvendedor=" + idvendedor +
                ", codvendedor='" + codvendedor + '\'' +
                ", codusuario='" + codusuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", bandvalida=" + bandvalida +
                ", bandvendedor=" + bandvendedor +
                ", bandcobrador=" + bandcobrador +
                ", lineas='" + lineas + '\'' +
                ", vendedores='" + vendedores + '\'' +
                ", ordenbodegas='" + ordenbodegas + '\'' +
                ", rutastablet='" + rutastablet + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
