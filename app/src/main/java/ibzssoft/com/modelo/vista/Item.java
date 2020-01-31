package ibzssoft.com.modelo.vista;

/**
 * Created by Usuario-pc on 06/04/2017.
 */
public class Item {
    private String identificador;
    private String cod_item;
    private String descripcion;
    private String presentacion;
    private String ruta_img1;
    private String ruta_img3;
    private String ruta_img2;

    private String ivg1;
    private String ivg2;
    private String ivg3;
    private String ivg4;
    private String ivg5;
    private String ivg6;

    private double precio1;
    private double precio2;
    private double precio3;
    private double precio4;
    private double precio5;
    private double precio6;
    private double precio7;

    private double precio_final;
    private double precio_lista;
    public Item() {
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getCod_item() {
        return cod_item;
    }

    public void setCod_item(String cod_item) {
        this.cod_item = cod_item;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getRuta_img1() {
        return ruta_img1;
    }

    public void setRuta_img1(String ruta_img1) {
        this.ruta_img1 = ruta_img1;
    }

    public String getIvg1() {
        return ivg1;
    }

    public void setIvg1(String ivg1) {
        this.ivg1 = ivg1;
    }

    public String getIvg2() {
        return ivg2;
    }

    public void setIvg2(String ivg2) {
        this.ivg2 = ivg2;
    }

    public String getIvg3() {
        return ivg3;
    }

    public void setIvg3(String ivg3) {
        this.ivg3 = ivg3;
    }

    public String getIvg4() {
        return ivg4;
    }

    public void setIvg4(String ivg4) {
        this.ivg4 = ivg4;
    }

    public String getIvg5() {
        return ivg5;
    }

    public void setIvg5(String ivg5) {
        this.ivg5 = ivg5;
    }

    public String getIvg6() {
        return ivg6;
    }

    public void setIvg6(String ivg6) {
        this.ivg6 = ivg6;
    }

    public String getRuta_img3() {
        return ruta_img3;
    }

    public void setRuta_img3(String ruta_img3) {
        this.ruta_img3 = ruta_img3;
    }

    public String getRuta_img2() {
        return ruta_img2;
    }

    public void setRuta_img2(String ruta_img2) {
        this.ruta_img2 = ruta_img2;
    }

    public double getPrecio1() {
        return precio1;
    }

    public void setPrecio1(double precio1) {
        this.precio1 = precio1;
    }

    public double getPrecio2() {
        return precio2;
    }

    public void setPrecio2(double precio2) {
        this.precio2 = precio2;
    }

    public double getPrecio3() {
        return precio3;
    }

    public void setPrecio3(double precio3) {
        this.precio3 = precio3;
    }

    public double getPrecio4() {
        return precio4;
    }

    public void setPrecio4(double precio4) {
        this.precio4 = precio4;
    }

    public double getPrecio5() {
        return precio5;
    }

    public void setPrecio5(double precio5) {
        this.precio5 = precio5;
    }

    public double getPrecio6() {
        return precio6;
    }

    public void setPrecio6(double precio6) {
        this.precio6 = precio6;
    }

    public double getPrecio7() {
        return precio7;
    }

    public void setPrecio7(double precio7) {
        this.precio7 = precio7;
    }

    public double getPrecio_final() {
        return precio_final;
    }

    public void setPrecio_final(double precio_final) {
        this.precio_final = precio_final;
    }

    public double getPrecio_lista() {
        return precio_lista;
    }

    public void setPrecio_lista(double precio_lista) {
        this.precio_lista = precio_lista;
    }

    @Override
    public String toString() {
        return "Item{" +
                "identificador='" + identificador + '\'' +
                ", cod_item='" + cod_item + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", presentacion='" + presentacion + '\'' +
                ", ruta_img1='" + ruta_img1 + '\'' +
                ", ruta_img3='" + ruta_img3 + '\'' +
                ", ruta_img2='" + ruta_img2 + '\'' +
                ", ivg1='" + ivg1 + '\'' +
                ", ivg2='" + ivg2 + '\'' +
                ", ivg3='" + ivg3 + '\'' +
                ", ivg4='" + ivg4 + '\'' +
                ", ivg5='" + ivg5 + '\'' +
                ", ivg6='" + ivg6 + '\'' +
                ", precio1=" + precio1 +
                ", precio2=" + precio2 +
                ", precio3=" + precio3 +
                ", precio4=" + precio4 +
                ", precio5=" + precio5 +
                ", precio6=" + precio6 +
                ", precio7=" + precio7 +
                ", precio_final=" + precio_final +
                ", precio_lista=" + precio_lista +
                '}';
    }
}
