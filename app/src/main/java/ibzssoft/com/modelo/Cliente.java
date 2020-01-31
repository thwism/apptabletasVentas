package ibzssoft.com.modelo;

/**
 * Created by root on 14/10/15.
 */
public class Cliente {
    public static final String TABLE_NAME = "TBL_CLIENTE";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idprovcli= "cli_idprovcli";
    public static final String FIELD_ruc= "cli_ruc";
    public static final String FIELD_nombre = "cli_nombre";
    public static final String FIELD_nombrealterno= "cli_nombrealterno";
    public static final String FIELD_direccion1= "cli_direccion1";
    public static final String FIELD_direccion2= "cli_direccion2";
    public static final String FIELD_telefono1= "cli_telefono1";
    public static final String FIELD_telefono2= "cli_telefono2";
    public static final String FIELD_fax= "cli_fax";
    public static final String FIELD_email= "cli_email";
    public static final String FIELD_banco= "cli_banco";
    public static final String FIELD_numcuenta= "cli_numcuenta";
    public static final String FIELD_direcbanco= "cli_direcbanco";
    public static final String FIELD_bandproveedor= "cli_bandproveedor";
    public static final String FIELD_bandcliente= "cli_bandcliente";
    public static final String FIELD_estado= "cli_estado";
    public static final String FIELD_idparroquia= "cli_idparroquia";
    public static final String FIELD_idgrupo1= "cli_idgrupo1";
    public static final String FIELD_idgrupo2= "cli_idgrupo2";
    public static final String FIELD_idgrupo3= "cli_idgrupo3";
    public static final String FIELD_idgrupo4= "cli_idgrupo4";
    public static final String FIELD_idvendedor= "cli_idvendedor";
    public static final String FIELD_idcobrador= "cli_idcobrador";
    public static final String FIELD_idprovincia= "cli_idprovincia";
    public static final String FIELD_idcanton= "cli_idcanton";
    public static final String FIELD_diasplazo= "cli_diasplazo";
    public static final String FIELD_observacion= "cli_observacion";
    public static final String FIELD_numprecio= "cli_numprecio";
    public static final String FIELD_numserie= "cli_numserie";
    public static final String FIELD_telefono3= "cli_telefono3";
    public static final String FIELD_posgooglemaps= "cli_posgooglemaps";
    public static final String FIELD_fechagrabado= "cli_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idprovcli+" text unique,"+
            FIELD_ruc+" text not null,"+
            FIELD_nombre+" text not null,"+
            FIELD_nombrealterno+" text,"+
            FIELD_direccion1+" text,"+
            FIELD_direccion2+" text,"+
            FIELD_telefono1+" text,"+
            FIELD_telefono2+" text,"+
            FIELD_fax+" text,"+
            FIELD_email+" text,"+
            FIELD_banco+" text,"+
            FIELD_numcuenta+" text,"+
            FIELD_direcbanco+" text,"+
            FIELD_bandproveedor+" int,"+
            FIELD_bandcliente+" int,"+
            FIELD_estado+" int,"+
            FIELD_idparroquia+" text,"+
            FIELD_idgrupo1+" text,"+
            FIELD_idgrupo2+" text,"+
            FIELD_idgrupo3+" text,"+
            FIELD_idgrupo4+" text,"+
            FIELD_idvendedor+" text,"+
            FIELD_idcobrador+" text,"+
            FIELD_idprovincia+" text,"+
            FIELD_idcanton+" text,"+
            FIELD_diasplazo+" int,"+
            FIELD_observacion+" text,"+
            FIELD_numprecio+" text,"+
            FIELD_numserie+" text,"+
            FIELD_posgooglemaps+" text,"+
            FIELD_telefono3+" text,"+
            FIELD_fechagrabado+" text not null,"
            +"FOREIGN KEY("+FIELD_idparroquia+") REFERENCES "+ Parroquia.TABLE_NAME+"("+Parroquia.FIELD_idparroquia+"),"
            +"FOREIGN KEY("+FIELD_idcanton+") REFERENCES "+ Canton.TABLE_NAME+"("+Canton.FIELD_idcanton+"),"
            +"FOREIGN KEY("+FIELD_idprovincia+") REFERENCES "+ Provincia.TABLE_NAME+"("+Canton.FIELD_idcanton+"),"
            +"FOREIGN KEY("+FIELD_idgrupo1+") REFERENCES "+ PCGrupo1.TABLE_NAME+"("+PCGrupo1.FIELD_idgrupo1+"),"
            +"FOREIGN KEY("+FIELD_idgrupo2+") REFERENCES "+ PCGrupo2.TABLE_NAME+"("+PCGrupo2.FIELD_idgrupo2+"),"
            +"FOREIGN KEY("+FIELD_idgrupo3+") REFERENCES "+ PCGrupo3.TABLE_NAME+"("+PCGrupo3.FIELD_idgrupo3+"),"
            +"FOREIGN KEY("+FIELD_idgrupo4+") REFERENCES "+ PCGrupo4.TABLE_NAME+"("+PCGrupo4.FIELD_idgrupo4+"),"
            +"FOREIGN KEY("+FIELD_idvendedor+") REFERENCES "+ Vendedor.TABLE_NAME+"("+Vendedor.FIELD_idvendedor+")"
            +" )";

    private int id;
    private String idprovcli;
    private String ruc;
    private String nombre;
    private String nombrealterno;
    private String direccion1;
    private String direccion2;
    private String telefono1;
    private String telefono2;
    private String fax;
    private String email;
    private String banco;
    private String numcuenta;
    private String direcbanco;
    private int bandproveedor;
    private int bandcliente;
    private int  estado;
    private String idparroquia;
    private String idgrupo1;
    private String idgrupo2;
    private String idgrupo3;
    private String idgrupo4;
    private String idvendedor;
    private String idcobrador;
    private String idprovincia;
    private String idcanton;
    private String diasplazo;
    private String observacion;
    private String numprecio;
    private String numserie;
    private String telefono3;
    private String posgooglemaps;
    private String fechagrabado;

    public Cliente(String idprovcli, String ruc, String nombre, String nombrealterno, String direccion1, String direccion2, String telefono1, String telefono2, String fax, String email, String banco, String numcuenta, String direcbanco, int bandproveedor, int bandcliente, int estado, String idparroquia, String idgrupo1, String idgrupo2, String idgrupo3, String idgrupo4, String idvendedor, String idcobrador, String idprovincia, String idcanton, String diasplazo, String observacion, String numprecio, String numserie, String telefono3, String posgooglemaps, String fechagrabado) {
        this.idprovcli = idprovcli;
        this.ruc = ruc;
        this.nombre = nombre;
        this.nombrealterno = nombrealterno;
        this.direccion1 = direccion1;
        this.direccion2 = direccion2;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.fax = fax;
        this.email = email;
        this.banco = banco;
        this.numcuenta = numcuenta;
        this.direcbanco = direcbanco;
        this.bandproveedor = bandproveedor;
        this.bandcliente = bandcliente;
        this.estado = estado;
        this.idparroquia = idparroquia;
        this.idgrupo1 = idgrupo1;
        this.idgrupo2 = idgrupo2;
        this.idgrupo3 = idgrupo3;
        this.idgrupo4 = idgrupo4;
        this.idvendedor = idvendedor;
        this.idcobrador = idcobrador;
        this.idprovincia = idprovincia;
        this.idcanton = idcanton;
        this.diasplazo = diasplazo;
        this.observacion = observacion;
        this.numprecio = numprecio;
        this.numserie = numserie;
        this.telefono3 = telefono3;
        this.posgooglemaps = posgooglemaps;
        this.fechagrabado = fechagrabado;
    }

    public Cliente() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdprovcli() {
        return idprovcli;
    }

    public void setIdprovcli(String idprovcli) {
        this.idprovcli = idprovcli;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombrealterno() {
        return nombrealterno;
    }

    public void setNombrealterno(String nombrealterno) {
        this.nombrealterno = nombrealterno;
    }

    public String getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(String direccion1) {
        this.direccion1 = direccion1;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumcuenta() {
        return numcuenta;
    }

    public void setNumcuenta(String numcuenta) {
        this.numcuenta = numcuenta;
    }

    public String getDirecbanco() {
        return direcbanco;
    }

    public void setDirecbanco(String direcbanco) {
        this.direcbanco = direcbanco;
    }

    public int getBandproveedor() {
        return bandproveedor;
    }

    public void setBandproveedor(int bandproveedor) {
        this.bandproveedor = bandproveedor;
    }

    public int getBandcliente() {
        return bandcliente;
    }

    public void setBandcliente(int bandcliente) {
        this.bandcliente = bandcliente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getIdparroquia() {
        return idparroquia;
    }

    public void setIdparroquia(String idparroquia) {
        this.idparroquia = idparroquia;
    }

    public String getIdgrupo1() {
        return idgrupo1;
    }

    public void setIdgrupo1(String idgrupo1) {
        this.idgrupo1 = idgrupo1;
    }

    public String getIdgrupo2() {
        return idgrupo2;
    }

    public void setIdgrupo2(String idgrupo2) {
        this.idgrupo2 = idgrupo2;
    }

    public String getIdgrupo3() {
        return idgrupo3;
    }

    public void setIdgrupo3(String idgrupo3) {
        this.idgrupo3 = idgrupo3;
    }

    public String getIdgrupo4() {
        return idgrupo4;
    }

    public void setIdgrupo4(String idgrupo4) {
        this.idgrupo4 = idgrupo4;
    }

    public String getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(String idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getIdcobrador() {
        return idcobrador;
    }

    public void setIdcobrador(String idcobrador) {
        this.idcobrador = idcobrador;
    }

    public String getIdprovincia() {
        return idprovincia;
    }

    public void setIdprovincia(String idprovincia) {
        this.idprovincia = idprovincia;
    }

    public String getIdcanton() {
        return idcanton;
    }

    public void setIdcanton(String idcanton) {
        this.idcanton = idcanton;
    }

    public String getDiasplazo() {
        return diasplazo;
    }

    public void setDiasplazo(String diasplazo) {
        this.diasplazo = diasplazo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumprecio() {
        return numprecio;
    }

    public void setNumprecio(String numprecio) {
        this.numprecio = numprecio;
    }

    public String getNumserie() {
        return numserie;
    }

    public void setNumserie(String numserie) {
        this.numserie = numserie;
    }

    public String getTelefono3() {
        return telefono3;
    }

    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
    }

    public String getPosgooglemaps() {
        return posgooglemaps;
    }

    public void setPosgooglemaps(String posgooglemaps) {
        this.posgooglemaps = posgooglemaps;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", idprovcli='" + idprovcli + '\'' +
                ", ruc='" + ruc + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombrealterno='" + nombrealterno + '\'' +
                ", direccion1='" + direccion1 + '\'' +
                ", direccion2='" + direccion2 + '\'' +
                ", telefono1='" + telefono1 + '\'' +
                ", telefono2='" + telefono2 + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", banco='" + banco + '\'' +
                ", numcuenta='" + numcuenta + '\'' +
                ", direcbanco='" + direcbanco + '\'' +
                ", bandproveedor=" + bandproveedor +
                ", bandcliente=" + bandcliente +
                ", estado=" + estado +
                ", idparroquia='" + idparroquia + '\'' +
                ", idgrupo1='" + idgrupo1 + '\'' +
                ", idgrupo2='" + idgrupo2 + '\'' +
                ", idgrupo3='" + idgrupo3 + '\'' +
                ", idgrupo4='" + idgrupo4 + '\'' +
                ", idvendedor='" + idvendedor + '\'' +
                ", idcobrador='" + idcobrador + '\'' +
                ", idprovincia='" + idprovincia + '\'' +
                ", idcanton='" + idcanton + '\'' +
                ", diasplazo='" + diasplazo + '\'' +
                ", observacion='" + observacion + '\'' +
                ", numprecio='" + numprecio + '\'' +
                ", numserie='" + numserie + '\'' +
                ", telefono3='" + telefono3 + '\'' +
                ", posgooglemaps='" + posgooglemaps + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
