package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class Promedio implements Serializable{
    private String transaccion;
    private String fecha_emision;
    private String plazo;
    private String fecha_vencimiento;
    private double valor;
    private String fecha_pago;
    private double saldo;
    private int dias_mora;
    private int dias_credito;

    public Promedio(String transaccion, String fecha_emision, String plazo, String fecha_vencimiento, double valor, String fecha_pago, double saldo, int dias_mora, int dias_credito) {
        this.transaccion = transaccion;
        this.fecha_emision = fecha_emision;
        this.plazo = plazo;
        this.fecha_vencimiento = fecha_vencimiento;
        this.valor = valor;
        this.fecha_pago = fecha_pago;
        this.saldo = saldo;
        this.dias_mora = dias_mora;
        this.dias_credito = dias_credito;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(String fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getDias_mora() {
        return dias_mora;
    }

    public void setDias_mora(int dias_mora) {
        this.dias_mora = dias_mora;
    }

    public int getDias_credito() {
        return dias_credito;
    }

    public void setDias_credito(int dias_credito) {
        this.dias_credito = dias_credito;
    }

    @Override
    public String toString() {
        return "Promedio{" +
                "transaccion='" + transaccion + '\'' +
                ", fecha_emision='" + fecha_emision + '\'' +
                ", plazo='" + plazo + '\'' +
                ", fecha_vencimiento='" + fecha_vencimiento + '\'' +
                ", valor=" + valor +
                ", fecha_pago='" + fecha_pago + '\'' +
                ", saldo=" + saldo +
                ", dias_mora=" + dias_mora +
                ", dias_credito=" + dias_credito +
                '}';
    }
}
