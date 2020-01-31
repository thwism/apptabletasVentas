package ibzssoft.com.modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shop implements Serializable {
    private String codtrans;
    private String idbodegapre;
    private String nombretrans;
    private String precios;
    private String clienteid;
    private String clientename;
    private int preciopcgrupo;
    private int numfilas;
    private int maxdocs;
    private int diasgracia;
    private int opciones;

    public Shop() {
    }

    public Shop(String codtrans, String nombretrans) {
        this.codtrans= codtrans;
        this.nombretrans = nombretrans;
    }

    public String getClienteid() {
        return clienteid;
    }

    public void setClienteid(String clienteid) {
        this.clienteid = clienteid;
    }

    public String getClientename() {
        return clientename;
    }

    public void setClientename(String clientename) {
        this.clientename = clientename;
    }

    public String getPrecios() {
        return precios;
    }

    public void setPrecios(String precios) {
        this.precios = precios;
    }

    public String getCodtrans() {
        return codtrans;
    }

    public void setCodtrans(String codtrans) {
        this.codtrans = codtrans;
    }

    public String getIdbodegapre() {
        return idbodegapre;
    }

    public void setIdbodegapre(String idbodegapre) {
        this.idbodegapre = idbodegapre;
    }

    public String getNombretrans() {
        return nombretrans;
    }

    public void setNombretrans(String nombretrans) {
        this.nombretrans = nombretrans;
    }

    public int getPreciopcgrupo() {
        return preciopcgrupo;
    }

    public void setPreciopcgrupo(int preciopcgrupo) {
        this.preciopcgrupo = preciopcgrupo;
    }

    public int getNumfilas() {
        return numfilas;
    }

    public void setNumfilas(int numfilas) {
        this.numfilas = numfilas;
    }

    public int getMaxdocs() {
        return maxdocs;
    }

    public void setMaxdocs(int maxdocs) {
        this.maxdocs = maxdocs;
    }

    public int getDiasgracia() {
        return diasgracia;
    }

    public void setDiasgracia(int diasgracia) {
        this.diasgracia = diasgracia;
    }

    public int getOpciones() {
        return opciones;
    }

    public void setOpciones(int opciones) {
        this.opciones = opciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (preciopcgrupo != shop.preciopcgrupo) return false;
        if (numfilas != shop.numfilas) return false;
        if (maxdocs != shop.maxdocs) return false;
        if (diasgracia != shop.diasgracia) return false;
        if (opciones != shop.opciones) return false;
        if (codtrans != null ? !codtrans.equals(shop.codtrans) : shop.codtrans != null)
            return false;
        if (idbodegapre != null ? !idbodegapre.equals(shop.idbodegapre) : shop.idbodegapre != null)
            return false;
        if (nombretrans != null ? !nombretrans.equals(shop.nombretrans) : shop.nombretrans != null)
            return false;
        if (precios != null ? !precios.equals(shop.precios) : shop.precios != null) return false;
        if (clienteid != null ? !clienteid.equals(shop.clienteid) : shop.clienteid != null)
            return false;
        return clientename != null ? clientename.equals(shop.clientename) : shop.clientename == null;

    }

    @Override
    public int hashCode() {
        int result = codtrans != null ? codtrans.hashCode() : 0;
        result = 31 * result + (idbodegapre != null ? idbodegapre.hashCode() : 0);
        result = 31 * result + (nombretrans != null ? nombretrans.hashCode() : 0);
        result = 31 * result + (precios != null ? precios.hashCode() : 0);
        result = 31 * result + (clienteid != null ? clienteid.hashCode() : 0);
        result = 31 * result + (clientename != null ? clientename.hashCode() : 0);
        result = 31 * result + preciopcgrupo;
        result = 31 * result + numfilas;
        result = 31 * result + maxdocs;
        result = 31 * result + diasgracia;
        result = 31 * result + opciones;
        return result;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "codtrans='" + codtrans + '\'' +
                ", idbodegapre='" + idbodegapre + '\'' +
                ", nombretrans='" + nombretrans + '\'' +
                ", precios='" + precios + '\'' +
                ", clienteid='" + clienteid + '\'' +
                ", clientename='" + clientename + '\'' +
                ", preciopcgrupo=" + preciopcgrupo +
                ", numfilas=" + numfilas +
                ", maxdocs=" + maxdocs +
                ", diasgracia=" + diasgracia +
                ", opciones=" + opciones +
                '}';
    }
}
