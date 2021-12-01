package ibzssoft.com.modelo;

/**
 * Created by root on 14/10/15.
 */
public class Transaccion {

    public static final String TABLE_NAME = "TBL_TRANSACCION";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_ID_Trans = "tra_IDTrans";
    public static final String FIELD_identificador = "tra_CodTrans";
    public static final String FIELD_numTransaccion = "tra_NumTransaccion";
    public static final String FIELD_fecha_trans = "tra_FechaTransaccion";
    public static final String FIELD_hora_trans = "tra_HoraTransaccion";
    public static final String FIELD_descripcion = "tra_Descripcion";
    public static final String FIELD_band_enviado = "tra_Band_enviado";
    public static final String FIELD_vendedor_id = "tra_vendedor_id";
    public static final String FIELD_referencia= "tra_Referencia";
    public static final String FIELD_cliente_id = "tra_cliente_id";
    public static final String FIELD_forma_cobro_id= "tra_forma_cobro_id";
    public static final String FIELD_fecha_grabado = "tra_fecha_grabado";
    public static final String FIELD_fecha_envio= "tra_fecha_envio";
    public static final String FIELD_estado= "tra_estado";
    public static final String TRIGGER_UPDATE_FECHA_ENVIO = "trigger_update_fecha_envio";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_ID_Trans+" text unique,"+
            FIELD_identificador+" text not null,"+
            FIELD_descripcion+" text,"+
            FIELD_numTransaccion+" integer not null,"+
            FIELD_fecha_trans+ " date not null,"+
            FIELD_hora_trans+ " time not null,"+
            FIELD_referencia+ " text,"+
            FIELD_band_enviado+ " integer not null,"+
            FIELD_vendedor_id+ " text not null,"+
            FIELD_cliente_id+ " text not null,"+
            FIELD_forma_cobro_id+ " text,"+
            FIELD_fecha_grabado+" datetime not null,"+
            FIELD_fecha_envio+" datetime default null," +
            FIELD_estado+" integer default 0, "
            +"FOREIGN KEY("+FIELD_cliente_id+") REFERENCES "+ Cliente.TABLE_NAME+"("+Cliente.FIELD_idprovcli+"),"
            +"FOREIGN KEY("+FIELD_vendedor_id+") REFERENCES "+ Vendedor.TABLE_NAME+"("+Vendedor.FIELD_idvendedor+")"
            +"FOREIGN KEY("+FIELD_forma_cobro_id+") REFERENCES "+ TSFormaCobroPago.TABLE_NAME+"("+TSFormaCobroPago.FIELD_idforma+")"
            +" )";

    private int id;
    private String id_trans;
    private String identificador;
    private String descripcion;
    private int numTransaccion;
    private String fecha_trans;
    private String hora_trans;
    private int band_enviado;
    private String cliente_id;
    private String forma_cobro_id;
    private String vendedor_id;
    private String referencia;
    private String fecha_grabado;
    private String fecha_envio;
    private int estado;
    /**
     * estado de cuenta
     * cuales son las facturas
     * detalle de la factura
     *
     *
     * @param id_trans
     * @param identificador
     * @param descripcion
     * @param numTransaccion
     * @param fecha_trans
     * @param hora_trans
     * @param band_enviado
     * @param vendedor_id
     * @param cliente_id
     * @param forma_cobro_id
     * @param fecha_grabado
     */

    public Transaccion(String id_trans,String identificador, String descripcion,int numTransaccion, String fecha_trans, String hora_trans, int band_enviado, String vendedor_id, String cliente_id, String forma_cobro_id,String referencia,String fecha_grabado) {
        this.id_trans=id_trans;
        this.identificador = identificador;
        this.descripcion = descripcion;
        this.numTransaccion = numTransaccion;
        this.fecha_trans = fecha_trans;
        this.hora_trans = hora_trans;
        this.band_enviado = band_enviado;
        this.vendedor_id=vendedor_id;
        this.cliente_id = cliente_id;
        this.forma_cobro_id=forma_cobro_id;
        this.referencia=referencia;
        this.fecha_grabado = fecha_grabado;
    }

    public Transaccion(String id_trans,String identificador, String descripcion,int numTransaccion, String fecha_trans, String hora_trans, int band_enviado, String vendedor_id, String cliente_id, String forma_cobro_id,String referencia,String fecha_grabado, int estado) {
        this.id_trans=id_trans;
        this.identificador = identificador;
        this.descripcion = descripcion;
        this.numTransaccion = numTransaccion;
        this.fecha_trans = fecha_trans;
        this.hora_trans = hora_trans;
        this.band_enviado = band_enviado;
        this.vendedor_id=vendedor_id;
        this.cliente_id = cliente_id;
        this.forma_cobro_id=forma_cobro_id;
        this.referencia=referencia;
        this.fecha_grabado = fecha_grabado;
        this.estado = estado;
    }

    public Transaccion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_trans() {
        return id_trans;
    }

    public void setId_trans(String id_trans) {
        this.id_trans = id_trans;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getNumTransaccion() {
        return numTransaccion;
    }

    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }

    public String getFecha_trans() {
        return fecha_trans;
    }

    public void setFecha_trans(String fecha_trans) {
        this.fecha_trans = fecha_trans;
    }

    public String getHora_trans() {
        return hora_trans;
    }

    public void setHora_trans(String hora_trans) {
        this.hora_trans = hora_trans;
    }


    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getForma_cobro_id() {
        return forma_cobro_id;
    }

    public void setForma_cobro_id(String forma_cobro_id) {
        this.forma_cobro_id = forma_cobro_id;
    }

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
    }

    public String getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(String vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getBand_enviado() {
        return band_enviado;
    }

    public void setBand_enviado(int band_enviado) {
        this.band_enviado = band_enviado;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id=" + id +
                ", id_trans='" + id_trans + '\'' +
                ", identificador='" + identificador + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", numTransaccion=" + numTransaccion +
                ", fecha_trans='" + fecha_trans + '\'' +
                ", hora_trans='" + hora_trans + '\'' +
                ", band_enviado=" + band_enviado +
                ", cliente_id='" + cliente_id + '\'' +
                ", forma_cobro_id='" + forma_cobro_id + '\'' +
                ", vendedor_id='" + vendedor_id + '\'' +
                ", referencia='" + referencia + '\'' +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                ", fecha_envio='" + fecha_envio + '\'' +
                ", estado=" + estado +
                '}';
    }
}
