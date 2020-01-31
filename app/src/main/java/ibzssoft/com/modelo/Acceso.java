package ibzssoft.com.modelo;

public class Acceso {
    public static final String TABLE_NAME = "TBL_ACCESO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_identificador = "acc_codAcceso";
    public static final String FIELD_permiso_id = "acc_permiso_id";
    public static final String FIELD_usuario_id = "acc_usuario_id";
    public static final String FIELD_fecha_grabado = "acc_fecha_grabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_identificador+" text unique,"+
            FIELD_permiso_id+" text not null,"+
            FIELD_usuario_id+" text not null,"+
            FIELD_fecha_grabado+" datetime not null,"
            +"FOREIGN KEY("+FIELD_permiso_id+") REFERENCES "+ Permiso.TABLE_NAME+"("+ Permiso.FIELD_idpermiso+"),"
            +"FOREIGN KEY("+FIELD_usuario_id+") REFERENCES "+ Usuario.TABLE_NAME+"("+ Usuario.FIELD_codusuario+")"
            +" )";

    public int id;
    public String identificador;
    public String permiso_id;
    public String usuario_id;
    public String fecha_grabado;

    public Acceso(String identificador, String permiso_id, String usuario_id, String fecha_grabado) {
        this.identificador = identificador;
        this.permiso_id = permiso_id;
        this.usuario_id = usuario_id;
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

    public String getPermiso_id() {
        return permiso_id;
    }

    public void setPermiso_id(String permiso_id) {
        this.permiso_id = permiso_id;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getFecha_grabado() {
        return fecha_grabado;
    }

    public void setFecha_grabado(String fecha_grabado) {
        this.fecha_grabado = fecha_grabado;
    }

    @Override
    public String toString() {
        return "Acceso{" +
                "id=" + id +
                ", identificador='" + identificador + '\'' +
                ", permiso_id='" + permiso_id + '\'' +
                ", usuario_id='" + usuario_id + '\'' +
                ", fecha_grabado='" + fecha_grabado + '\'' +
                '}';
    }
}