package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Empresa {
    public static final String TABLE_NAME = "TBL_EMPRESA";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idempresa= "emp_idempresa";
    public static final String FIELD_nombrebase= "emp_nombrebase";
    public static final String FIELD_nombreempresa= "emp_nombreempresa";
    public static final String FIELD_direccion1 = "emp_direccion1";
    public static final String FIELD_telefono1= "emp_telefono1";
    public static final String FIELD_etiquetagrupo1= "emp_etiquetagrupo1";
    public static final String FIELD_etiquetagrupo2= "emp_etiquetagrupo2";
    public static final String FIELD_etiquetagrupo3= "emp_etiquetagrupo3";
    public static final String FIELD_etiquetagrupo4= "emp_etiquetagrupo4";
    public static final String FIELD_etiquetagrupo5= "emp_etiquetagrupo5";
    public static final String FIELD_etiquetagrupo6= "emp_etiquetagrupo6";
    public static final String FIELD_etiquetapcgrupo1= "emp_etiquetapcgrupo1";
    public static final String FIELD_etiquetapcgrupo2= "emp_etiquetapcgrupo2";
    public static final String FIELD_etiquetapcgrupo3= "emp_etiquetapcgrupo3";
    public static final String FIELD_etiquetapcgrupo4= "emp_etiquetapcgrupo4";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idempresa+" text unique,"+
            FIELD_nombreempresa+" text not null,"+
            FIELD_nombrebase+" text not null,"+
            FIELD_direccion1+" text,"+
            FIELD_telefono1+" text,"+
            FIELD_etiquetagrupo1+" text,"+
            FIELD_etiquetagrupo2+" text,"+
            FIELD_etiquetagrupo3+" text,"+
            FIELD_etiquetagrupo4+" text,"+
            FIELD_etiquetagrupo5+" text,"+
            FIELD_etiquetagrupo6+" text,"+
            FIELD_etiquetapcgrupo1+" text,"+
            FIELD_etiquetapcgrupo2+" text,"+
            FIELD_etiquetapcgrupo3+" text,"+
            FIELD_etiquetapcgrupo4+" text"
            +" )";

    public int id;
    public String idempresa;
    public String nombrebase;
    public String nombreempresa;
    public String direccion1;
    public String telefono1;
    public String etiquetagrupo1;
    public String etiquetagrupo2;
    public String etiquetagrupo3;
    public String etiquetagrupo4;
    public String etiquetagrupo5;
    public String etiquetagrupo6;
    public String etiquetapcgrupo1;
    public String etiquetapcgrupo2;
    public String etiquetapcgrupo3;
    public String etiquetapcgrupo4;

    public Empresa(String idempresa, String nombrebase, String nombreempresa, String direccion1, String telefono1, String etiquetagrupo1, String etiquetagrupo2, String etiquetagrupo3, String etiquetagrupo4, String etiquetagrupo5, String etiquetagrupo6, String etiquetapcgrupo1, String etiquetapcgrupo2, String etiquetapcgrupo3, String etiquetapcgrupo4) {
        this.idempresa = idempresa;
        this.nombrebase = nombrebase;
        this.nombreempresa = nombreempresa;
        this.direccion1 = direccion1;
        this.telefono1 = telefono1;
        this.etiquetagrupo1 = etiquetagrupo1;
        this.etiquetagrupo2 = etiquetagrupo2;
        this.etiquetagrupo3 = etiquetagrupo3;
        this.etiquetagrupo4 = etiquetagrupo4;
        this.etiquetagrupo5 = etiquetagrupo5;
        this.etiquetagrupo6 = etiquetagrupo6;
        this.etiquetapcgrupo1 = etiquetapcgrupo1;
        this.etiquetapcgrupo2 = etiquetapcgrupo2;
        this.etiquetapcgrupo3 = etiquetapcgrupo3;
        this.etiquetapcgrupo4 = etiquetapcgrupo4;
    }

    public String getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(String idempresa) {
        this.idempresa = idempresa;
    }

    public String getNombrebase() {
        return nombrebase;
    }

    public void setNombrebase(String nombrebase) {
        this.nombrebase = nombrebase;
    }

    public String getNombreempresa() {
        return nombreempresa;
    }

    public void setNombreempresa(String nombreempresa) {
        this.nombreempresa = nombreempresa;
    }

    public String getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(String direccion1) {
        this.direccion1 = direccion1;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getEtiquetagrupo1() {
        return etiquetagrupo1;
    }

    public void setEtiquetagrupo1(String etiquetagrupo1) {
        this.etiquetagrupo1 = etiquetagrupo1;
    }

    public String getEtiquetagrupo2() {
        return etiquetagrupo2;
    }

    public void setEtiquetagrupo2(String etiquetagrupo2) {
        this.etiquetagrupo2 = etiquetagrupo2;
    }

    public String getEtiquetagrupo3() {
        return etiquetagrupo3;
    }

    public void setEtiquetagrupo3(String etiquetagrupo3) {
        this.etiquetagrupo3 = etiquetagrupo3;
    }

    public String getEtiquetagrupo4() {
        return etiquetagrupo4;
    }

    public void setEtiquetagrupo4(String etiquetagrupo4) {
        this.etiquetagrupo4 = etiquetagrupo4;
    }

    public String getEtiquetagrupo5() {
        return etiquetagrupo5;
    }

    public void setEtiquetagrupo5(String etiquetagrupo5) {
        this.etiquetagrupo5 = etiquetagrupo5;
    }

    public String getEtiquetagrupo6() {
        return etiquetagrupo6;
    }

    public void setEtiquetagrupo6(String etiquetagrupo6) {
        this.etiquetagrupo6 = etiquetagrupo6;
    }

    public String getEtiquetapcgrupo1() {
        return etiquetapcgrupo1;
    }

    public void setEtiquetapcgrupo1(String etiquetapcgrupo1) {
        this.etiquetapcgrupo1 = etiquetapcgrupo1;
    }

    public String getEtiquetapcgrupo2() {
        return etiquetapcgrupo2;
    }

    public void setEtiquetapcgrupo2(String etiquetapcgrupo2) {
        this.etiquetapcgrupo2 = etiquetapcgrupo2;
    }

    public String getEtiquetapcgrupo3() {
        return etiquetapcgrupo3;
    }

    public void setEtiquetapcgrupo3(String etiquetapcgrupo3) {
        this.etiquetapcgrupo3 = etiquetapcgrupo3;
    }

    public String getEtiquetapcgrupo4() {
        return etiquetapcgrupo4;
    }

    public void setEtiquetapcgrupo4(String etiquetapcgrupo4) {
        this.etiquetapcgrupo4 = etiquetapcgrupo4;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", idempresa='" + idempresa + '\'' +
                ", nombrebase='" + nombrebase + '\'' +
                ", nombreempresa='" + nombreempresa + '\'' +
                ", direccion1='" + direccion1 + '\'' +
                ", telefono1='" + telefono1 + '\'' +
                ", etiquetagrupo1='" + etiquetagrupo1 + '\'' +
                ", etiquetagrupo2='" + etiquetagrupo2 + '\'' +
                ", etiquetagrupo3='" + etiquetagrupo3 + '\'' +
                ", etiquetagrupo4='" + etiquetagrupo4 + '\'' +
                ", etiquetagrupo5='" + etiquetagrupo5 + '\'' +
                ", etiquetagrupo6='" + etiquetagrupo6 + '\'' +
                ", etiquetapcgrupo1='" + etiquetapcgrupo1 + '\'' +
                ", etiquetapcgrupo2='" + etiquetapcgrupo2 + '\'' +
                ", etiquetapcgrupo3='" + etiquetapcgrupo3 + '\'' +
                ", etiquetapcgrupo4='" + etiquetapcgrupo4 + '\'' +
                '}';
    }
}
