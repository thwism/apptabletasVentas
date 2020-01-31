package ibzssoft.com.modelo.json;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class Item implements Serializable{
    private String transid;
    private String idinventario;
    private String idbodega;
    private String descripcion;
    private double cantidad;
    private double valor;
    private int iva;
    private double total;
    private String nota;
    private double descuento;
    private double promedio;
    private int num_precio;
    private String idpadre;

    public Item(String transid, String idinventario, String idbodega, String descripcion, double cantidad, double valor, int iva, double total, String nota, double descuento, double promedio,int num_precio, String idpadre) {
        this.transid = transid;
        this.idinventario = idinventario;
        this.idbodega = idbodega;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.valor = valor;
        this.iva = iva;
        this.total = total;
        this.nota = nota;
        this.descuento = descuento;
        this.promedio = promedio;
        this.num_precio=num_precio;
        this.idpadre = idpadre;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getIdinventario() {
        return idinventario;
    }

    public void setIdinventario(String idinventario) {
        this.idinventario = idinventario;
    }

    public String getIdbodega() {
        return idbodega;
    }

    public void setIdbodega(String idbodega) {
        this.idbodega = idbodega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public String getIdpadre() {
        return idpadre;
    }

    public void setIdpadre(String idpadre) {
        this.idpadre = idpadre;
    }

    public int getNum_precio() {
        return num_precio;
    }

    public void setNum_precio(int num_precio) {
        this.num_precio = num_precio;
    }

    @Override
    public String toString() {
        return "Item{" +
                "transid='" + transid + '\'' +
                ", idinventario='" + idinventario + '\'' +
                ", idbodega='" + idbodega + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", valor=" + valor +
                ", iva=" + iva +
                ", total=" + total +
                ", nota=" + nota +
                ", descuento=" + descuento +
                ", promedio=" + promedio +
                ", num_precio=" + num_precio +
                ", idpadre='" + idpadre + '\'' +
                '}';
    }
}
