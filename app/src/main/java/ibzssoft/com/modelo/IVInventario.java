package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class IVInventario {
    public static final String TABLE_NAME = "TBL_IVINVENTARIO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_identificador = "ivi_codInventario";
    public static final String FIELD_descripcion = "ivi_Descripcion";
    public static final String FIELD_cod_item= "ivi_CodItem";
    public static final String FIELD_cod_alterno = "ivi_CodAlterno";
    public static final String FIELD_observacion= "ivi_observacion";
    public static final String FIELD_precio1= "ivi_Precio1";
    public static final String FIELD_precio2 = "ivi_Precio2";
    public static final String FIELD_precio3= "ivi_Precio3";
    public static final String FIELD_precio4 = "ivi_Precio4";
    public static final String FIELD_precio5 = "ivi_Precio5";
    public static final String FIELD_precio6 = "ivi_Precio6";
    public static final String FIELD_precio7 = "ivi_Precio7";
    public static final String FIELD_cod_moneda = "ivi_CodMoneda";
    public static final String FIELD_porc_iva = "ivi_PorcentajeIVA";
    public static final String FIELD_band_iva = "ivi_BandIVA";
    public static final String FIELD_estado= "ivi_estado";
    public static final String FIELD_presentacion= "ivi_presentacion";
    public static final String FIELD_unidad= "ivi_unidad";
    public static final String FIELD_ivg1 = "ivi_ivg1_id";
    public static final String FIELD_ivg2 = "ivi_ivg2_id";
    public static final String FIELD_ivg3 = "ivi_ivg3_id";
    public static final String FIELD_ivg4 = "ivi_ivg4_id";
    public static final String FIELD_ivg5 = "ivi_ivg5_id";
    public static final String FIELD_ivg6 = "ivi_ivg6_id";

    public static final String FIELD_img1 = "ivi_img1_id";
    public static final String FIELD_img2 = "ivi_img2_id";
    public static final String FIELD_img3 = "ivi_img3_id";

    public static final String FIELD_fecha_grabado = "ivi_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_identificador+" text unique,"+
            FIELD_descripcion+" text not null,"+
            FIELD_cod_item+" text not null,"+
            FIELD_cod_alterno+" text,"+
            FIELD_observacion+" text,"+
            FIELD_precio1+" double,"+
            FIELD_precio2+" double,"+
            FIELD_precio3+" double,"+
            FIELD_precio4+" double,"+
            FIELD_precio5+" double,"+
            FIELD_precio6+" double,"+
            FIELD_precio7+" double,"+
            FIELD_cod_moneda+" text not null,"+
            FIELD_porc_iva+" double,"+
            FIELD_band_iva+" int,"+
            FIELD_estado+" int not null,"+
            FIELD_presentacion+" text,"+
            FIELD_unidad+" text,"+
            FIELD_fecha_grabado+" datetime not null,"+
            FIELD_ivg1 +" text,"+
            FIELD_ivg2 +" text,"+
            FIELD_ivg3 +" text,"+
            FIELD_ivg4 +" text,"+
            FIELD_ivg5 +" text,"+
            FIELD_ivg6 +" text,"+
            FIELD_img1 +" text,"+
            FIELD_img2 +" text,"+
            FIELD_img3 +" text,"
            +" FOREIGN KEY("+FIELD_ivg1+") REFERENCES "+ IVGrupo1.TABLE_NAME+"("+ IVGrupo1.FIELD_idgrupo1+"),"
            +" FOREIGN KEY("+FIELD_ivg2+") REFERENCES "+ IVGrupo2.TABLE_NAME+"("+ IVGrupo2.FIELD_idgrupo2+"),"
            +" FOREIGN KEY("+FIELD_ivg3+") REFERENCES "+ IVGrupo3.TABLE_NAME+"("+ IVGrupo3.FIELD_idgrupo3+"),"
            +" FOREIGN KEY("+FIELD_ivg4+") REFERENCES "+ IVGrupo4.TABLE_NAME+"("+ IVGrupo4.FIELD_idgrupo4+"),"
            +" FOREIGN KEY("+FIELD_ivg5+") REFERENCES "+ IVGrupo5.TABLE_NAME+"("+ IVGrupo5.FIELD_idgrupo5+"),"
            +" FOREIGN KEY("+FIELD_ivg6+") REFERENCES "+ IVGrupo6.TABLE_NAME+"("+ IVGrupo6.FIELD_idgrupo6+")"
            +" )";

    public int id;
    public String identificador;
    public String cod_item;
    public String descripcion;
    public String cod_alterno;
    public String observacion;
    public double precio1;
    public double precio2;
    public double precio3;
    public double precio4;
    public double precio5;
    public double precio6;
    public double precio7;
    public String cod_moneda;
    public double porc_iva;
    public int band_iva;
    public String ivg1;
    public String ivg2;
    public String ivg3;
    public String ivg4;
    public String ivg5;
    public String ivg6;
    public String img1;
    public String img2;
    public String img3;
    public int estado;
    public String presentacion;
    public String unidad;
    public String fecha_grabado;

    public IVInventario(String identificador, String cod_item, String descripcion, String cod_alterno, String observacion, double precio1, double precio2, double precio3, double precio4,double precio5,double precio6,double precio7, String cod_moneda, double porc_iva, int band_iva, String ivg1, String ivg2, String ivg3, String ivg4, String ivg5, String ivg6, int estado, String presentacion, String unidad, String fecha_grabado) {
        this.identificador = identificador;
        this.cod_item = cod_item;
        this.descripcion = descripcion;
        this.cod_alterno = cod_alterno;
        this.observacion = observacion;
        this.precio1 = precio1;
        this.precio2 = precio2;
        this.precio3 = precio3;
        this.precio4 = precio4;
        this.precio5 = precio5;
        this.precio6 = precio6;
        this.precio7 = precio7;
        this.cod_moneda = cod_moneda;
        this.porc_iva = porc_iva;
        this.band_iva = band_iva;
        this.ivg1 = ivg1;
        this.ivg2 = ivg2;
        this.ivg3 = ivg3;
        this.ivg4 = ivg4;
        this.ivg5 = ivg5;
        this.ivg6 = ivg6;
        this.estado = estado;
        this.presentacion = presentacion;
        this.unidad = unidad;
        this.fecha_grabado = fecha_grabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCod_alterno() {
        return cod_alterno;
    }

    public void setCod_alterno(String cod_alterno) {
        this.cod_alterno = cod_alterno;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
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

    public String getCod_moneda() {
        return cod_moneda;
    }

    public void setCod_moneda(String cod_moneda) {
        this.cod_moneda = cod_moneda;
    }

    public double getPorc_iva() {
        return porc_iva;
    }

    public void setPorc_iva(double porc_iva) {
        this.porc_iva = porc_iva;
    }

    public int getBand_iva() {
        return band_iva;
    }

    public void setBand_iva(int band_iva) {
        this.band_iva = band_iva;
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

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
    }

    public double getPrecio7() {
        return precio7;
    }

    public void setPrecio7(double precio7) {
        this.precio7 = precio7;
    }

    public double getPrecio6() {
        return precio6;
    }

    public void setPrecio6(double precio6) {
        this.precio6 = precio6;
    }

    public double getPrecio5() {
        return precio5;
    }

    public void setPrecio5(double precio5) {
        this.precio5 = precio5;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    @Override
    public String toString() {
        return "IVInventario{" +
                "id=" + id +
                ", identificador='" + identificador + '\'' +
                ", cod_item='" + cod_item + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", cod_alterno='" + cod_alterno + '\'' +
                ", observacion='" + observacion + '\'' +
                ", precio1=" + precio1 +
                ", precio2=" + precio2 +
                ", precio3=" + precio3 +
                ", precio4=" + precio4 +
                ", precio5=" + precio5 +
                ", precio6=" + precio6 +
                ", precio7=" + precio7 +
                ", cod_moneda='" + cod_moneda + '\'' +
                ", porc_iva=" + porc_iva +
                ", band_iva=" + band_iva +
                ", ivg1='" + ivg1 + '\'' +
                ", ivg2='" + ivg2 + '\'' +
                ", ivg3='" + ivg3 + '\'' +
                ", ivg4='" + ivg4 + '\'' +
                ", ivg5='" + ivg5 + '\'' +
                ", ivg6='" + ivg6 + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2+ '\'' +
                ", img3='" + img3 + '\'' +
                ", estado=" + estado +
                ", presentacion='" + presentacion + '\'' +
                ", unidad='" + unidad + '\'' +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                '}';
    }
}
