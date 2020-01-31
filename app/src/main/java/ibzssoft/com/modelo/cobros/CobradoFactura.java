package ibzssoft.com.modelo.cobros;

/**
 * Created by Usuario-pc on 28/12/2016.
 */
public class CobradoFactura {
    String documento;
    double total_cobrado;

    public CobradoFactura(String documento, double total_cobrado) {
        this.documento = documento;
        this.total_cobrado = total_cobrado;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public double getTotal_cobrado() {
        return total_cobrado;
    }

    public void setTotal_cobrado(double total_cobrado) {
        this.total_cobrado = total_cobrado;
    }

    @Override
    public String toString() {
        return "CobradoFactura{" +
                "documento='" + documento + '\'' +
                ", total_cobrado=" + total_cobrado +
                '}';
    }
}
