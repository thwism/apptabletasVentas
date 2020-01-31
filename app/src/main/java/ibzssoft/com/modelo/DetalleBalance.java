package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class DetalleBalance implements Serializable{
    private String IdCuenta;
    private String CodCuenta;
    private String NombreCuenta;
    private String Nombre;
    private String Valor;
    private String Nivel;
    private String IdCuentaSuma;
    private String TipoCuenta;
    private String BandTotal;

    public DetalleBalance(String idCuenta, String codCuenta, String nombreCuenta, String nombre, String valor, String nivel, String idCuentaSuma, String tipoCuenta, String bandTotal) {
        IdCuenta = idCuenta;
        CodCuenta = codCuenta;
        NombreCuenta = nombreCuenta;
        Nombre = nombre;
        Valor = valor;
        Nivel = nivel;
        IdCuentaSuma = idCuentaSuma;
        TipoCuenta = tipoCuenta;
        BandTotal = bandTotal;
    }

    public String getIdCuenta() {
        return IdCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        IdCuenta = idCuenta;
    }

    public String getCodCuenta() {
        return CodCuenta;
    }

    public void setCodCuenta(String codCuenta) {
        CodCuenta = codCuenta;
    }

    public String getNombreCuenta() {
        return NombreCuenta;
    }

    public void setNombreCuenta(String nombreCuenta) {
        NombreCuenta = nombreCuenta;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getNivel() {
        return Nivel;
    }

    public void setNivel(String nivel) {
        Nivel = nivel;
    }

    public String getIdCuentaSuma() {
        return IdCuentaSuma;
    }

    public void setIdCuentaSuma(String idCuentaSuma) {
        IdCuentaSuma = idCuentaSuma;
    }

    public String getTipoCuenta() {
        return TipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        TipoCuenta = tipoCuenta;
    }

    public String getBandTotal() {
        return BandTotal;
    }

    public void setBandTotal(String bandTotal) {
        BandTotal = bandTotal;
    }

    @Override
    public String toString() {
        return "DetalleBalance{" +
                "IdCuenta='" + IdCuenta + '\'' +
                ", CodCuenta='" + CodCuenta + '\'' +
                ", NombreCuenta='" + NombreCuenta + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", Valor='" + Valor + '\'' +
                ", Nivel='" + Nivel + '\'' +
                ", IdCuentaSuma='" + IdCuentaSuma + '\'' +
                ", TipoCuenta='" + TipoCuenta + '\'' +
                ", BandTotal='" + BandTotal + '\'' +
                '}';
    }
}
