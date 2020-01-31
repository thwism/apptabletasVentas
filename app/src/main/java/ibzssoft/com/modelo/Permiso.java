package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Permiso {
    public static final String TABLE_NAME = "TBL_PERMISO";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_idpermiso = "per_codPermiso";
    public static final String FIELD_codempresa= "per_codempresa";
    public static final String FIELD_codgrupo= "per_codgrupo";


    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_idpermiso+" text unique,"+
            FIELD_codempresa+" text not null,"+
            FIELD_codgrupo+" text not null,"+
            " FOREIGN KEY("+FIELD_codgrupo+") REFERENCES "+ Grupo.TABLE_NAME+"("+Grupo.FIELD_codgrupo+"),"+
            " FOREIGN KEY("+FIELD_codempresa+") REFERENCES "+ Empresa.TABLE_NAME+"("+Empresa.FIELD_idempresa+")"
            +")";

    public int id;
    public String idpermiso;
    public String codgrupo;
    public String codempresa;

    public Permiso(String idpermiso, String codgrupo, String codempresa) {
        this.idpermiso = idpermiso;
        this.codgrupo = codgrupo;
        this.codempresa = codempresa;
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

    public String getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(String codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getCodempresa() {
        return codempresa;
    }

    public void setCodempresa(String codempresa) {
        this.codempresa = codempresa;
    }

    @Override
    public String toString() {
        return "Permiso{" +
                "id=" + id +
                ", idpermiso='" + idpermiso + '\'' +
                ", codgrupo='" + codgrupo + '\'' +
                ", codempresa='" + codempresa + '\'' +
                '}';
    }
}
