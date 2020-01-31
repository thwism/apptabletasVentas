package ibzssoft.com.modelo.json;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Usuario-pc on 07/04/2017.
 */
public class RecargoDescuento implements Serializable {

    private String transid;
    private String codrecargo;
    private double valor;
    private int signo;
    private String  descripcion;

    public RecargoDescuento(String transid, String codrecargo, double valor, int signo, String descripcion) {
        this.transid = transid;
        this.codrecargo = codrecargo;
        this.valor = valor;
        this.signo = signo;
        this.descripcion = descripcion;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getCodrecargo() {
        return codrecargo;
    }

    public void setCodrecargo(String codrecargo) {
        this.codrecargo = codrecargo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getSigno() {
        return signo;
    }

    public void setSigno(int signo) {
        this.signo = signo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "RecargoDescuento{" +
                "transid='" + transid + '\'' +
                ", codrecargo='" + codrecargo + '\'' +
                ", valor=" + valor +
                ", signo=" + signo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
