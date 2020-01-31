package ibzssoft.com.ishidamovile.cobro.visualizar;

/**
 * Created by Usuario-pc on 15/12/2016.
 */
public class CobroDetalle{
    private String transaccion;
    private String valor;
    private String pagado;
    private String saldo;

    public CobroDetalle(String transaccion, String valor, String pagado, String saldo) {
        this.transaccion = transaccion;
        this.valor = valor;
        this.pagado = pagado;
        this.saldo = saldo;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "CobroDetalle{" +
                "transaccion='" + transaccion + '\'' +
                ", valor='" + valor + '\'' +
                ", pagado='" + pagado + '\'' +
                ", saldo='" + saldo + '\'' +
                '}';
    }
}
