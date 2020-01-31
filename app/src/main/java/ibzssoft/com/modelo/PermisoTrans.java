package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class PermisoTrans {
    public static final String TABLE_NAME = "TBL_PERMISOTRANS";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idpermiso = "codPermisoTrans";
    //public static final String FIELD_codtrans= "pert_codtrans";
    public static final String FIELD_codtrans= "pert_estado";
    public static final String FIELD_crear= "pert_crear";
    public static final String FIELD_ver= "pert_ver";
    public static final String FIELD_modificar= "pert_modificar";
    public static final String FIELD_eliminar= "pert_eliminar";


    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idpermiso+" text not null,"+
            FIELD_codtrans+" text not null,"+
            FIELD_ver+" int not null,"+
            FIELD_crear+" int not null,"+
            FIELD_modificar+" int not null,"+
            FIELD_eliminar+" int not null,"+
            " FOREIGN KEY("+FIELD_idpermiso+") REFERENCES "+ Permiso.TABLE_NAME+"("+Permiso.FIELD_idpermiso+"),"+
            " FOREIGN KEY("+FIELD_codtrans+") REFERENCES "+ GNTrans.TABLE_NAME+"("+GNTrans.FIELD_codtrans+")" +")";

    public int id;
    public String idpermiso;
    public String codtrans;
    public int ver;
    public int crear;
    public int modificar;
    public int eliminar;

    public PermisoTrans(String idpermiso, String codtrans, int ver, int crear, int modificar, int eliminar) {
        this.idpermiso = idpermiso;
        this.codtrans = codtrans;
        this.ver = ver;
        this.crear = crear;
        this.modificar = modificar;
        this.eliminar = eliminar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdpermiso() {
        return idpermiso;
    }

    public void setIdpermiso(String idpermiso) {
        this.idpermiso = idpermiso;
    }

    public String getCodtrans() {
        return codtrans;
    }

    public void setCodtrans(String codtrans) {
        this.codtrans = codtrans;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getCrear() {
        return crear;
    }

    public void setCrear(int crear) {
        this.crear = crear;
    }

    public int getModificar() {
        return modificar;
    }

    public void setModificar(int modificar) {
        this.modificar = modificar;
    }

    public int getEliminar() {
        return eliminar;
    }

    public void setEliminar(int eliminar) {
        this.eliminar = eliminar;
    }

    @Override
    public String toString() {
        return "PermisoTrans{" +
                "id=" + id +
                ", idpermiso='" + idpermiso + '\'' +
                ", codtrans='" + codtrans + '\'' +
                ", ver=" + ver +
                ", crear=" + crear +
                ", modificar=" + modificar +
                ", eliminar=" + eliminar +
                '}';
    }
}
