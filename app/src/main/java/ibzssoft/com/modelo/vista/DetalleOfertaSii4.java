package ibzssoft.com.modelo.vista;

/**
 * Created by MARIUXI on 25/1/2022.
 */

public class DetalleOfertaSii4 {
    private String codTrans;
    private String numTrans;
    private int idCliente;
    private int orden;
    private int idInventario;
    private String codInventario;
    private String descripcionItem;
    private double cantidad;
    private double costoTotal;
    private double costoRealTotal;
    private double precioTotal;
    private double precioRealTotal;
    private double iva;
    private double descuentoAplicado;
    private double descuentoSolicitado;
    private String telefonoV;
    private String emailV;
    private String formaPagoO;

    public DetalleOfertaSii4(String codTrans, String numTrans, int idCliente, int orden, int idInventario, String codInventario, String descripcionItem, double cantidad, double costoTotal, double costoRealTotal, double precioTotal, double precioRealTotal, double iva, double descuentoAplicado, double descuentoSolicitado, String telefonoV, String emailV, String formaPagoO) {
        this.codTrans = codTrans;
        this.numTrans = numTrans;
        this.idCliente = idCliente;
        this.orden = orden;
        this.idInventario = idInventario;
        this.codInventario = codInventario;
        this.descripcionItem = descripcionItem;
        this.cantidad = cantidad;
        this.costoTotal = costoTotal;
        this.costoRealTotal = costoRealTotal;
        this.precioTotal = precioTotal;
        this.precioRealTotal = precioRealTotal;
        this.iva = iva;
        this.descuentoAplicado = descuentoAplicado;
        this.descuentoSolicitado = descuentoSolicitado;
        this.telefonoV = telefonoV;
        this.emailV = emailV;
        this.formaPagoO = formaPagoO;
    }

    public String getCodTrans() {
        return codTrans;
    }

    public void setCodTrans(String codTrans) {
        this.codTrans = codTrans;
    }

    public String getNumTrans() {
        return numTrans;
    }

    public void setNumTrans(String numTrans) {
        this.numTrans = numTrans;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public String getCodInventario() {
        return codInventario;
    }

    public void setCodInventario(String codInventario) {
        this.codInventario = codInventario;
    }

    public String getDescripcionItem() {
        return descripcionItem;
    }

    public void setDescripcionItem(String descripcionItem) {
        this.descripcionItem = descripcionItem;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public double getCostoRealTotal() {
        return costoRealTotal;
    }

    public void setCostoRealTotal(double costoRealTotal) {
        this.costoRealTotal = costoRealTotal;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getPrecioRealTotal() {
        return precioRealTotal;
    }

    public void setPrecioRealTotal(double precioRealTotal) {
        this.precioRealTotal = precioRealTotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public double getDescuentoSolicitado() {
        return descuentoSolicitado;
    }

    public void setDescuentoSolicitado(double descuentoSolicitado) {
        this.descuentoSolicitado = descuentoSolicitado;
    }

    public String getTelefonoV() {
        return telefonoV;
    }

    public void setTelefonoV(String telefonoV) {
        this.telefonoV = telefonoV;
    }

    public String getEmailV() {
        return emailV;
    }

    public void setEmailV(String emailV) {
        this.emailV = emailV;
    }

    public String getFormaPagoO() {
        return formaPagoO;
    }

    public void setFormaPagoO(String formaPagoO) {
        this.formaPagoO = formaPagoO;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
