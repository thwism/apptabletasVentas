package ibzssoft.com.modelo;

import java.io.Serializable;

/**
 * Created by Ricardo on 08/07/2016.
 */
@SuppressWarnings("serial")
public class DetalleCierre implements Serializable{
    private String FechaTrans;
    private String codUsuario;
    private String Cliente;
    private String CodTrans;
    private String NumSerieEstablecimiento;
    private String NumSeriePunto;
    private String NumTrans;
    private String AutorizacionSRI;
    private String FechaCaducidadSRI;
    private String CodForma;
    private String NombreForma;
    private String NumLetra;
    private double Valor;
    private String nombre;

    public DetalleCierre(String fechaTrans, String codUsuario, String cliente, String codTrans, String numSerieEstablecimiento, String numSeriePunto, String numTrans, String autorizacionSRI, String fechaCaducidadSRI, String codForma, String nombreForma, String numLetra, double valor, String nombre) {
        FechaTrans = fechaTrans;
        this.codUsuario = codUsuario;
        Cliente = cliente;
        CodTrans = codTrans;
        NumSerieEstablecimiento = numSerieEstablecimiento;
        NumSeriePunto = numSeriePunto;
        NumTrans = numTrans;
        AutorizacionSRI = autorizacionSRI;
        FechaCaducidadSRI = fechaCaducidadSRI;
        CodForma = codForma;
        NombreForma = nombreForma;
        NumLetra = numLetra;
        Valor = valor;
        this.nombre = nombre;
    }

    public String getFechaTrans() {
        return FechaTrans;
    }

    public void setFechaTrans(String fechaTrans) {
        FechaTrans = fechaTrans;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getCodTrans() {
        return CodTrans;
    }

    public void setCodTrans(String codTrans) {
        CodTrans = codTrans;
    }

    public String getNumSerieEstablecimiento() {
        return NumSerieEstablecimiento;
    }

    public void setNumSerieEstablecimiento(String numSerieEstablecimiento) {
        NumSerieEstablecimiento = numSerieEstablecimiento;
    }

    public String getNumSeriePunto() {
        return NumSeriePunto;
    }

    public void setNumSeriePunto(String numSeriePunto) {
        NumSeriePunto = numSeriePunto;
    }

    public String getNumTrans() {
        return NumTrans;
    }

    public void setNumTrans(String numTrans) {
        NumTrans = numTrans;
    }

    public String getAutorizacionSRI() {
        return AutorizacionSRI;
    }

    public void setAutorizacionSRI(String autorizacionSRI) {
        AutorizacionSRI = autorizacionSRI;
    }

    public String getFechaCaducidadSRI() {
        return FechaCaducidadSRI;
    }

    public void setFechaCaducidadSRI(String fechaCaducidadSRI) {
        FechaCaducidadSRI = fechaCaducidadSRI;
    }

    public String getCodForma() {
        return CodForma;
    }

    public void setCodForma(String codForma) {
        CodForma = codForma;
    }

    public String getNombreForma() {
        return NombreForma;
    }

    public void setNombreForma(String nombreForma) {
        NombreForma = nombreForma;
    }

    public String getNumLetra() {
        return NumLetra;
    }

    public void setNumLetra(String numLetra) {
        NumLetra = numLetra;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "DetalleCierre{" +
                "FechaTrans='" + FechaTrans + '\'' +
                ", codUsuario='" + codUsuario + '\'' +
                ", Cliente='" + Cliente + '\'' +
                ", CodTrans='" + CodTrans + '\'' +
                ", NumSerieEstablecimiento='" + NumSerieEstablecimiento + '\'' +
                ", NumSeriePunto='" + NumSeriePunto + '\'' +
                ", NumTrans='" + NumTrans + '\'' +
                ", AutorizacionSRI='" + AutorizacionSRI + '\'' +
                ", FechaCaducidadSRI='" + FechaCaducidadSRI + '\'' +
                ", CodForma='" + CodForma + '\'' +
                ", NombreForma='" + NombreForma + '\'' +
                ", NumLetra='" + NumLetra + '\'' +
                ", Valor=" + Valor +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
