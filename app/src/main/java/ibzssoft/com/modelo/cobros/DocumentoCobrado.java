package ibzssoft.com.modelo.cobros;

/**
 * Created by Usuario-pc on 28/12/2016.
 */
public class DocumentoCobrado {
    int indice;
    String documento;
    String codigo;
    String letra;
    String emision;
    int plazo;
    String vencimiento;
    int dv;
    double valor;
    double pagado;
    double saldo;
    double interes;
    double cobrado;
    String observacion;
    String forma;
    String pckid;
    String idvendedor;
    String idcobrador ;
    String ultpago ;

    public DocumentoCobrado(int indice, String documento, String codigo, String letra, String emision, int plazo, String vencimiento, int dv, double valor, double pagado, double saldo, double interes, double cobrado, String observacion,String forma, String pckid,String idvendedor,String idcobrador,String ultpago) {
        this.indice = indice;
        this.documento = documento;
        this.codigo = codigo;
        this.letra = letra;
        this.emision = emision;
        this.plazo = plazo;
        this.vencimiento = vencimiento;
        this.dv = dv;
        this.valor = valor;
        this.pagado = pagado;
        this.saldo = saldo;
        this.interes = interes;
        this.cobrado = cobrado;
        this.observacion = observacion;
        this.forma = forma;
        this.pckid = pckid;
        this.idvendedor=idvendedor;
        this.idcobrador=idcobrador;
        this.ultpago=ultpago;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public int getDv() {
        return dv;
    }

    public void setDv(int dv) {
        this.dv = dv;
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

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }


    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getCobrado() {
        return cobrado;
    }

    public void setCobrado(double cobrado) {
        this.cobrado = cobrado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getPckid() {
        return pckid;
    }

    public void setPckid(String pckid) {
        this.pckid = pckid;
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

    public String getUltpago() {
        return ultpago;
    }

    public void setUltpago(String ultpago) {
        this.ultpago = ultpago;
    }

    @Override
    public String toString() {
        return "DocumentoCobrado{" +
                "indice=" + indice +
                ", documento='" + documento + '\'' +
                ", codigo='" + codigo + '\'' +
                ", letra='" + letra + '\'' +
                ", emision='" + emision + '\'' +
                ", plazo=" + plazo +
                ", vencimiento='" + vencimiento + '\'' +
                ", dv=" + dv +
                ", valor=" + valor +
                ", pagado=" + pagado +
                ", saldo=" + saldo +
                ", interes=" + interes +
                ", cobrado=" + cobrado +
                ", observacion='" + observacion + '\'' +
                ", forma='" + forma + '\'' +
                ", pckid='" + pckid + '\'' +
                ", idvendedor='" + idvendedor + '\'' +
                ", idcobrador='" + idcobrador + '\'' +
                ", ultpago='" + ultpago + '\'' +
                '}';
    }
}
