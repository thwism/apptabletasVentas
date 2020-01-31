package ibzssoft.com.modelo;

/**
 * Created by root on 15/10/15.
 */
public class PCKardex {
    public static final String TABLE_NAME = "TBL_PCKARDEX";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idcartera= "pck_idcartera";
    public static final String FIELD_idasignado= "pck_idasignado";
    public static final String FIELD_fechavenci= "pck_fechavenci";
    public static final String FIELD_valor= "pck_valor";
    public static final String FIELD_pagado= "pck_pagado";
    public static final String FIELD_saldoxvence= "pck_saldoxvence";
    public static final String FIELD_saldovencido= "pck_saldovencido";
    public static final String FIELD_dvencidos= "pck_dvencidos";
    public static final String FIELD_plazo= "pck_plazo";
    public static final String FIELD_fechaemision= "pck_fechaemision";
    public static final String FIELD_idprovcli= "pck_idprovcli";
    public static final String FIELD_ruc= "pck_ruc";
    public static final String FIELD_nombrecliente= "pck_nombrecliente";
    public static final String FIELD_comercialcliente= "pck_comercialcliente";
    public static final String FIELD_direccioncliente= "pck_direccioncliente";
    public static final String FIELD_codforma= "pck_codforma";
    public static final String FIELD_transid= "pck_transid";
    public static final String FIELD_idvendedor= "pck_idvendedor";
    public static final String FIELD_idcobrador= "pck_idcobrador";
    public static final String FIELD_trans= "pck_trans";
    public static final String FIELD_guid="pck_guid";
    public static final String FIELD_cantcheque="pck_cantcheques";
    public static final String FIELD_idruta= "pck_idruta";
    public static final String FIELD_doc="pck_doc";
    public static final String FIELD_ordencuota="pck_ordencuota";
    public static final String FIELD_ruta="pck_ruta";
    public static final String FIELD_banco_id="pck_banco_id";
    public static final String FIELD_forma_pago="pck_forma_pago";
    public static final String FIELD_numero_cheque="pck_numero_cheque";
    public static final String FIELD_numero_cuenta="pck_numero_cuenta";
    public static final String FIELD_titular="pck_titular";
    public static final String FIELD_pago_fecha_vencimiento="pck_pago_fecha_vencimiento";
    public static final String FIELD_band_generado="pck_band_generado";
    public static final String FIELD_band_respaldado="pck_band_respaldado";
    public static final String FIELD_renta="pck_renta";
    public static final String FIELD_renta_base="pck_renta_base";
    public static final String FIELD_iva="pck_iva";
    public static final String FIELD_iva_base="pck_iva_base";
    public static final String FIELD_num_ser_estab="pck_num_ser_estab";
    public static final String FIELD_num_ser_punto="pck_num_ser_punto";
    public static final String FIELD_num_ser_secuencial="pck_num_ser_secuen";
    public static final String FIELD_autorizacion="pck_autorizacion";
    public static final String FIELD_caducidad="pck_caducidad";
    public static final String FIELD_band_cobrado="pck_cobrado";
    public static final String FIELD_observacion="pck_observacion";
    public static final String FIELD_fechaultimopago="pck_fechaultimopago";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idcartera+" text unique,"+
            FIELD_idasignado+" text,"+
            FIELD_fechavenci+" text,"+
            FIELD_valor+" double,"+
            FIELD_pagado+" double,"+
            FIELD_saldoxvence+" double,"+
            FIELD_saldovencido+" double,"+
            FIELD_dvencidos+" int not null,"+
            FIELD_plazo+" int,"+
            FIELD_fechaemision+" text,"+
            FIELD_idprovcli+" text,"+
            FIELD_ruc+" text,"+
            FIELD_nombrecliente+" text,"+
            FIELD_comercialcliente+" text,"+
            FIELD_direccioncliente+" text,"+
            FIELD_codforma+" text,"+
            FIELD_transid+" text,"+
            FIELD_idvendedor+" text,"+
            FIELD_idcobrador+" text,"+
            FIELD_trans+" text,"+
            FIELD_guid+" text,"+
            FIELD_cantcheque+" integer,"+
            FIELD_idruta+" text,"+
            FIELD_doc+" text,"+
            FIELD_ordencuota+" int,"+
            FIELD_ruta+" text,"+
            FIELD_banco_id+" text,"+
            FIELD_forma_pago+" text,"+
            FIELD_numero_cheque+" text,"+
            FIELD_numero_cuenta+" text,"+
            FIELD_titular+" text,"+
            FIELD_pago_fecha_vencimiento+" text,"+
            FIELD_band_generado+" int,"+
            FIELD_band_respaldado+" int,"+
            FIELD_renta+" double,"+
            FIELD_renta_base+" double,"+
            FIELD_iva+" text,"+
            FIELD_iva_base+" text,"+
            FIELD_num_ser_estab+" text,"+
            FIELD_num_ser_punto+" text,"+
            FIELD_num_ser_secuencial+" text,"+
            FIELD_autorizacion+" text,"+
            FIELD_caducidad+" text,"+
            FIELD_band_cobrado+" int,"+
            FIELD_observacion+" text,"+
            FIELD_fechaultimopago+" datetime,"
            +"FOREIGN KEY("+FIELD_idprovcli+") REFERENCES "+ Cliente.TABLE_NAME+"("+ Cliente.FIELD_idprovcli+"),"
            +"FOREIGN KEY("+FIELD_transid+") REFERENCES "+ Transaccion.TABLE_NAME+"("+ Transaccion.FIELD_identificador+")"
            +")";

    private int id;
    private String idcartera;
    private String idasinado;
    private String fechavenci;
    private double valor;
    private double pagado;
    private double saldoxvence;
    private double saldovencido;
    private int plazo;
    private int dvencidos;
    private String fechaemision;
    private String idprovcli;
    private String ruc;
    private String nombrecli;
    private String comercialcli;
    private String direccioncli;
    private String codforma;
    private String transid;
    private String idvendedor;
    private String idcobrador;
    private String trans;
    private String guid;
    private int cantcheque;
    private String idruta;
    private String doc;
    private String ordencuota;
    private String ruta;
    private String banco_id;
    private String forma_pago;
    private String numero_cheque;
    private String numero_cuenta;
    private String titular;
    private String pago_fecha_vencimiento;
    private int band_generado;
    private int band_respaldado;
    private String renta;
    private String renta_base;
    private String iva;
    private String iva_base;
    private String num_ser_estab;
    private String num_ser_punto;
    private String num_ser_secuencial;
    private String autorizacion;
    private String caducidad;
    private int band_cobrado;
    private String observacion;
    private String fechaultimopago;

    public PCKardex(String idcartera, String idasinado, String fechavenci, double valor, double pagado, double saldoxvence, double saldovencido, int plazo, int dvencidos, String fechaemision, String idprovcli, String ruc, String nombrecli, String comercialcli, String direccioncli, String codforma, String transid, String idvendedor, String idcobrador, String trans, String guid, int cantcheque, String idruta, String doc, String ordencuota, String ruta, String banco_id, String forma_pago, String numero_cheque, String numero_cuenta, String titular, String pago_fecha_vencimiento, int band_generado, int band_respaldado, String renta, String renta_base, String iva, String iva_base, String num_ser_estab, String num_ser_punto, String num_ser_secuencial, String autorizacion, String caducidad, int band_cobrado, String observacion,String fechaultimopago) {
        this.idcartera = idcartera;
        this.idasinado = idasinado;
        this.fechavenci = fechavenci;
        this.valor = valor;
        this.pagado = pagado;
        this.saldoxvence = saldoxvence;
        this.saldovencido = saldovencido;
        this.plazo = plazo;
        this.dvencidos = dvencidos;
        this.fechaemision = fechaemision;
        this.idprovcli = idprovcli;
        this.ruc = ruc;
        this.nombrecli = nombrecli;
        this.comercialcli = comercialcli;
        this.direccioncli = direccioncli;
        this.codforma = codforma;
        this.transid = transid;
        this.idvendedor = idvendedor;
        this.idcobrador = idcobrador;
        this.trans = trans;
        this.guid = guid;
        this.cantcheque = cantcheque;
        this.idruta = idruta;
        this.doc = doc;
        this.ordencuota = ordencuota;
        this.ruta = ruta;
        this.banco_id = banco_id;
        this.forma_pago = forma_pago;
        this.numero_cheque = numero_cheque;
        this.numero_cuenta = numero_cuenta;
        this.titular = titular;
        this.pago_fecha_vencimiento = pago_fecha_vencimiento;
        this.band_generado = band_generado;
        this.band_respaldado = band_respaldado;
        this.renta = renta;
        this.renta_base = renta_base;
        this.iva = iva;
        this.iva_base = iva_base;
        this.num_ser_estab = num_ser_estab;
        this.num_ser_punto = num_ser_punto;
        this.num_ser_secuencial = num_ser_secuencial;
        this.autorizacion = autorizacion;
        this.caducidad = caducidad;
        this.band_cobrado = band_cobrado;
        this.observacion = observacion;
        this.fechaultimopago= fechaultimopago;
    }

    public PCKardex() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdcartera() {
        return idcartera;
    }

    public void setIdcartera(String idcartera) {
        this.idcartera = idcartera;
    }

    public String getIdasinado() {
        return idasinado;
    }

    public void setIdasinado(String idasinado) {
        this.idasinado = idasinado;
    }

    public String getFechavenci() {
        return fechavenci;
    }

    public void setFechavenci(String fechavenci) {
        this.fechavenci = fechavenci;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getPagado() {
        return pagado;
    }

    public void setPagado(double pagado) {
        this.pagado = pagado;
    }

    public double getSaldoxvence() {
        return saldoxvence;
    }

    public void setSaldoxvence(double saldoxvence) {
        this.saldoxvence = saldoxvence;
    }

    public double getSaldovencido() {
        return saldovencido;
    }

    public void setSaldovencido(double saldovencido) {
        this.saldovencido = saldovencido;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public int getDvencidos() {
        return dvencidos;
    }

    public void setDvencidos(int dvencidos) {
        this.dvencidos = dvencidos;
    }

    public String getFechaemision() {
        return fechaemision;
    }

    public void setFechaemision(String fechaemision) {
        this.fechaemision = fechaemision;
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

    public String getNombrecli() {
        return nombrecli;
    }

    public void setNombrecli(String nombrecli) {
        this.nombrecli = nombrecli;
    }

    public String getComercialcli() {
        return comercialcli;
    }

    public void setComercialcli(String comercialcli) {
        this.comercialcli = comercialcli;
    }

    public String getDireccioncli() {
        return direccioncli;
    }

    public void setDireccioncli(String direccioncli) {
        this.direccioncli = direccioncli;
    }

    public String getCodforma() {
        return codforma;
    }

    public void setCodforma(String codforma) {
        this.codforma = codforma;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
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

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getCantcheque() {
        return cantcheque;
    }

    public void setCantcheque(int cantcheque) {
        this.cantcheque = cantcheque;
    }

    public String getIdruta() {
        return idruta;
    }

    public void setIdruta(String idruta) {
        this.idruta = idruta;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getOrdencuota() {
        return ordencuota;
    }

    public void setOrdencuota(String ordencuota) {
        this.ordencuota = ordencuota;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getBanco_id() {
        return banco_id;
    }

    public void setBanco_id(String banco_id) {
        this.banco_id = banco_id;
    }

    public String getForma_pago() {
        return forma_pago;
    }

    public void setForma_pago(String forma_pago) {
        this.forma_pago = forma_pago;
    }

    public String getNumero_cheque() {
        return numero_cheque;
    }

    public void setNumero_cheque(String numero_cheque) {
        this.numero_cheque = numero_cheque;
    }

    public String getNumero_cuenta() {
        return numero_cuenta;
    }

    public void setNumero_cuenta(String numero_cuenta) {
        this.numero_cuenta = numero_cuenta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getPago_fecha_vencimiento() {
        return pago_fecha_vencimiento;
    }

    public void setPago_fecha_vencimiento(String pago_fecha_vencimiento) {
        this.pago_fecha_vencimiento = pago_fecha_vencimiento;
    }

    public int getBand_generado() {
        return band_generado;
    }

    public void setBand_generado(int band_generado) {
        this.band_generado = band_generado;
    }

    public int getBand_respaldado() {
        return band_respaldado;
    }

    public void setBand_respaldado(int band_respaldado) {
        this.band_respaldado = band_respaldado;
    }

    public String getRenta() {
        return renta;
    }

    public void setRenta(String renta) {
        this.renta = renta;
    }

    public String getRenta_base() {
        return renta_base;
    }

    public void setRenta_base(String renta_base) {
        this.renta_base = renta_base;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getIva_base() {
        return iva_base;
    }

    public void setIva_base(String iva_base) {
        this.iva_base = iva_base;
    }

    public String getNum_ser_estab() {
        return num_ser_estab;
    }

    public void setNum_ser_estab(String num_ser_estab) {
        this.num_ser_estab = num_ser_estab;
    }

    public String getNum_ser_punto() {
        return num_ser_punto;
    }

    public void setNum_ser_punto(String num_ser_punto) {
        this.num_ser_punto = num_ser_punto;
    }

    public String getNum_ser_secuencial() {
        return num_ser_secuencial;
    }

    public void setNum_ser_secuencial(String num_ser_secuencial) {
        this.num_ser_secuencial = num_ser_secuencial;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public int getBand_cobrado() {
        return band_cobrado;
    }

    public void setBand_cobrado(int band_cobrado) {
        this.band_cobrado = band_cobrado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }


    public String getFechaultimopago() {
        return fechaultimopago;
    }

    public void setFechaultimopago(String fechaultimopago) {
        this.fechaultimopago = fechaultimopago;
    }

    @Override
    public String toString() {
        return "PCKardex{" +
                "id=" + id +
                ", idcartera='" + idcartera + '\'' +
                ", idasinado='" + idasinado + '\'' +
                ", fechavenci='" + fechavenci + '\'' +
                ", valor=" + valor +
                ", pagado=" + pagado +
                ", saldoxvence=" + saldoxvence +
                ", saldovencido=" + saldovencido +
                ", plazo=" + plazo +
                ", dvencidos=" + dvencidos +
                ", fechaemision='" + fechaemision + '\'' +
                ", idprovcli='" + idprovcli + '\'' +
                ", ruc='" + ruc + '\'' +
                ", nombrecli='" + nombrecli + '\'' +
                ", comercialcli='" + comercialcli + '\'' +
                ", direccioncli='" + direccioncli + '\'' +
                ", codforma='" + codforma + '\'' +
                ", transid='" + transid + '\'' +
                ", idvendedor='" + idvendedor + '\'' +
                ", idcobrador='" + idcobrador + '\'' +
                ", trans='" + trans + '\'' +
                ", guid='" + guid + '\'' +
                ", cantcheque=" + cantcheque +
                ", idruta='" + idruta + '\'' +
                ", doc='" + doc + '\'' +
                ", ordencuota='" + ordencuota + '\'' +
                ", ruta='" + ruta + '\'' +
                ", banco_id='" + banco_id + '\'' +
                ", forma_pago='" + forma_pago + '\'' +
                ", numero_cheque='" + numero_cheque + '\'' +
                ", numero_cuenta='" + numero_cuenta + '\'' +
                ", titular='" + titular + '\'' +
                ", pago_fecha_vencimiento='" + pago_fecha_vencimiento + '\'' +
                ", band_generado=" + band_generado +
                ", band_respaldado=" + band_respaldado +
                ", renta='" + renta + '\'' +
                ", renta_base='" + renta_base + '\'' +
                ", iva='" + iva + '\'' +
                ", iva_base='" + iva_base + '\'' +
                ", num_ser_estab='" + num_ser_estab + '\'' +
                ", num_ser_punto='" + num_ser_punto + '\'' +
                ", num_ser_secuencial='" + num_ser_secuencial + '\'' +
                ", autorizacion='" + autorizacion + '\'' +
                ", caducidad='" + caducidad + '\'' +
                ", band_cobrado=" + band_cobrado +
                ", observacion='" + observacion + '\'' +
                ", fechaultimopago='" + fechaultimopago + '\'' +
                '}';
    }
}
