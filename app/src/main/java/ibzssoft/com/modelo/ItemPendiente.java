package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 19/08/2016.
 */
public class ItemPendiente implements Serializable {
    private int id;
    private String codigo;
    private String fecha;
    private String  trans;
    private String descripcion;
    private double existencia;
    private double solicita;
    private int entrega;
    private int modifica;
    private double saldo;
    private String estado;

    public ItemPendiente(String codigo, String fecha, String trans, String descripcion, double existencia, double solicita, int entrega, int modifica, double saldo, String estado) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.trans = trans;
        this.descripcion = descripcion;
        this.existencia = existencia;
        this.solicita = solicita;
        this.entrega = entrega;
        this.modifica = modifica;
        this.saldo = saldo;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getExistencia() {
        return existencia;
    }

    public void setExistencia(double existencia) {
        this.existencia = existencia;
    }

    public double getSolicita() {
        return solicita;
    }

    public void setSolicita(double solicita) {
        this.solicita = solicita;
    }

    public int getEntrega() {
        return entrega;
    }

    public void setEntrega(int entrega) {
        this.entrega = entrega;
    }

    public int getModifica() {
        return modifica;
    }

    public void setModifica(int modifica) {
        this.modifica = modifica;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ItemPendiente{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", fecha='" + fecha + '\'' +
                ", trans='" + trans + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", existencia=" + existencia +
                ", solicita=" + solicita +
                ", entrega=" + entrega +
                ", modifica=" + modifica +
                ", saldo=" + saldo +
                ", estado='" + estado + '\'' +
                '}';
    }
}
