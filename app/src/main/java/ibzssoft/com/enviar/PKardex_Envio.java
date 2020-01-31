package ibzssoft.com.enviar;

/**
 * Created by Ricardo on 05/12/2016.
 */
public class PKardex_Envio {
    private String id_asignado;
    private String tsf_id;
    private double valor_cancelado;
    private String observacion;
    private String idvendedor;
    private String idcobrador;
    /*Campos para el cobro*/

    public String getId_asignado() {
        return id_asignado;
    }
    public void setId_asignado(String id_asignado) {
        this.id_asignado = id_asignado;
    }


    public double getValor_cancelado() {
        return valor_cancelado;
    }
    public void setValor_cancelado(double valor_cancelado) {
        this.valor_cancelado = valor_cancelado;
    }

    public String getTsf_id() {
        return tsf_id;
    }
    public void setTsf_id(String tsf_id) {
        this.tsf_id = tsf_id;
    }


    public String getObservacion() {
        return observacion;
    }
    public void setObservacion(String observacion) {
        this.observacion = observacion;
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


    @Override
    public String toString() {
        return "{" + id_asignado + " | " + tsf_id + " | " + valor_cancelado
                + " | " + observacion + " | " + idvendedor+ " | " + idcobrador+ "}";
    }
}
