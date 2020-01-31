package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
public class Cheque implements Serializable {
    private String nombre_banco;
    private String titular_cheque;
    private String num_cheque;
    private String fecha_vencimiento;
    private double valor_cheque;
    private String trans_ingreso;


    public Cheque(String nombre_banco, String titular_cheque, String num_cheque, String fecha_vencimiento, double valor_cheque, String trans_ingreso) {
        this.nombre_banco = nombre_banco;
        this.titular_cheque = titular_cheque;
        this.num_cheque = num_cheque;
        this.fecha_vencimiento = fecha_vencimiento;
        this.valor_cheque = valor_cheque;
        this.trans_ingreso = trans_ingreso;

    }

    public String getNombre_banco() {
        return nombre_banco;
    }

    public void setNombre_banco(String nombre_banco) {
        this.nombre_banco = nombre_banco;
    }

    public String getTitular_cheque() {
        return titular_cheque;
    }

    public void setTitular_cheque(String titular_cheque) {
        this.titular_cheque = titular_cheque;
    }

    public String getNum_cheque() {
        return num_cheque;
    }

    public void setNum_cheque(String num_cheque) {
        this.num_cheque = num_cheque;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public double getValor_cheque() {
        return valor_cheque;
    }

    public void setValor_cheque(double valor_cheque) {
        this.valor_cheque = valor_cheque;
    }

    public String getTrans_ingreso() {
        return trans_ingreso;
    }

    public void setTrans_ingreso(String trans_ingreso) {
        this.trans_ingreso = trans_ingreso;
    }

    @Override
    public String toString() {
        return "Cheque{" +
                "nombre_banco='" + nombre_banco + '\'' +
                ", titular_cheque='" + titular_cheque + '\'' +
                ", num_cheque='" + num_cheque + '\'' +
                ", fecha_vencimiento='" + fecha_vencimiento + '\'' +
                ", valor_cheque='" + valor_cheque + '\'' +
                ", trans_ingreso='" + trans_ingreso + '\'' +
                '}';
    }
}

