package ibzssoft.com.enviar;

import java.util.List;

/**
 * Created by Ricardo on 05/12/2016.
 */
public class Transaccion_Serialize_Envio {

        private String id_trans;
        private String identificador;
        private String descripcion;
        private int numTransaccion;
        private String fecha_trans;
        private String hora_trans;
        private String cliente_id;
        private String vendedor_id;


        private String banco_id;
        private String forma_pago;
        private String numero_cheque;
        private String numero_cuenta;
        private String titular;
        private String cheque_fecha_vencimiento;
        private int band_generado;

        private String iva;
        private String iva_base;
        private String renta;
        private String renta_base;
        private String establecimiento;
        private String punto;
        private String secuencial;
        private String autorizacion;
        private String caducidad;


        private List<IVKardex_Serialize_Envio> ivkardex;
        private List<PKardex_Envio>pckardex;



        public String getId_trans() {
            return id_trans;
        }

        public void setId_trans(String id_trans) {
            this.id_trans = id_trans;
        }

        public String getIdentificador() {
            return identificador;
        }

        public void setIdentificador(String identificador) {
            this.identificador = identificador;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public int getNumTransaccion() {
            return numTransaccion;
        }

        public void setNumTransaccion(int numTransaccion) {
            this.numTransaccion = numTransaccion;
        }

        public String getFecha_trans() {
            return fecha_trans;
        }

        public void setFecha_trans(String fecha_trans) {
            this.fecha_trans = fecha_trans;
        }

        public String getHora_trans() {
            return hora_trans;
        }

        public void setHora_trans(String hora_trans) {
            this.hora_trans = hora_trans;
        }

        public String getCliente_id() {
            return cliente_id;
        }

        public void setCliente_id(String cliente_id) {
            this.cliente_id = cliente_id;
        }

        public String getVendedor_id() {
            return vendedor_id;
        }

        public void setVendedor_id(String vendedor_id) {
            this.vendedor_id = vendedor_id;
        }

        public String getBanco_id() {
            return banco_id;
        }

        public void setBanco_id(String banco_id) {
            this.banco_id = banco_id;
        }

        public String getForma_pago() {
            return forma_pago;
        }

        public void setForma_pago(String forma_pago) {
            this.forma_pago = forma_pago;
        }

        public String getNumero_cheque() {
            return numero_cheque;
        }

        public void setNumero_cheque(String numero_cheque) {
            this.numero_cheque = numero_cheque;
        }

        public String getNumero_cuenta() {
            return numero_cuenta;
        }

        public void setNumero_cuenta(String numero_cuenta) {
            this.numero_cuenta = numero_cuenta;
        }

        public String getTitular() {
            return titular;
        }

        public void setTitular(String titular) {
            this.titular = titular;
        }

        public String getCheque_fecha_vencimiento() {
            return cheque_fecha_vencimiento;
        }

        public void setCheque_fecha_vencimiento(String cheque_fecha_vencimiento) {
            this.cheque_fecha_vencimiento = cheque_fecha_vencimiento;
        }

        public int getBand_generado() {
            return band_generado;
        }

        public void setBand_generado(int band_generado) {
            this.band_generado = band_generado;
        }

        public String getIva() {
            return iva;
        }

        public void setIva(String iva) {
            this.iva = iva;
        }

        public String getRenta() {
            return renta;
        }

        public void setRenta(String renta) {
            this.renta = renta;
        }

        public String getEstablecimiento() {
            return establecimiento;
        }

        public void setEstablecimiento(String establecimiento) {
            this.establecimiento = establecimiento;
        }

        public String getPunto() {
            return punto;
        }

        public void setPunto(String punto) {
            this.punto = punto;
        }

        public String getSecuencial() {
            return secuencial;
        }

        public void setSecuencial(String secuencial) {
            this.secuencial = secuencial;
        }

        public String getAutorizacion() {
            return autorizacion;
        }

        public void setAutorizacion(String autorizacion) {
            this.autorizacion = autorizacion;
        }

        public String getCaducidad() {
            return caducidad;
        }

        public void setCaducidad(String caducidad) {
            this.caducidad = caducidad;
        }

        public List<IVKardex_Serialize_Envio> getIvkardex() {
            return ivkardex;
        }

        public void setIvkardex(List<IVKardex_Serialize_Envio> ivkardex) {
            this.ivkardex = ivkardex;
        }

        public List<PKardex_Envio> getPckardex() {
            return pckardex;
        }

        public void setPckardex(List<PKardex_Envio> pckardex) {
            this.pckardex = pckardex;
        }

        public String getIva_base() {
            return iva_base;
        }

        public void setIva_base(String iva_base) {
            this.iva_base = iva_base;
        }

        public String getRenta_base() {
            return renta_base;
        }

        public void setRenta_base(String renta_base) {
            this.renta_base = renta_base;
        }

        @Override
        public String toString() {
            return "Transaccion[" + id_trans + "|" + identificador + "|" + descripcion + "|"
                    + numTransaccion + "|" + fecha_trans + "|" + hora_trans + "|"
                    + cliente_id + "|" + vendedor_id + "|" + banco_id + "|"
                    + forma_pago + "|" + numero_cheque + "|" + numero_cuenta + "|"
                    + titular + "|" + cheque_fecha_vencimiento + "|"
                    + band_generado + "|" + iva + "|" + iva_base + "|" + renta
                    + "|" + renta_base + "|" + establecimiento + "|" + punto + "|"
                    + secuencial + "|" + autorizacion + "|" + caducidad + "|IVKardex:"
                    + ivkardex + "|PCKardex:" + pckardex + "]";
        }
    }
