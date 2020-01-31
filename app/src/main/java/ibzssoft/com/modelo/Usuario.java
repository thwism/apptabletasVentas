package ibzssoft.com.modelo;

/**
 * Created by root on 13/10/15.
 */
public class Usuario {
    public static final String TABLE_NAME = "TBL_USUARIO";
    // Labels Table Columns names
    public static final String FIELD_ID = "_id";
    public static final String FIELD_codusuario = "usr_codUsuario";
    public static final String FIELD_codgrupo = "usr_codgrupo";
    public static final String FIELD_nombreusuario = "usr_nombreusuario";
    public static final String FIELD_clave = "usr_clave";
    public static final String FIELD_bandsupervisor = "usr_bandsupervisor";
    public static final String FIELD_bandvalida = "usr_bandvalida";
    public static final String FIELD_fechagrabado= "usr_fechagrabado";

    public static final String CREATE_DB_TABLE="create table "+TABLE_NAME+"( "+
            FIELD_ID+" integer primary key autoincrement, "+
            FIELD_codusuario+" text unique,"+
            FIELD_nombreusuario+" text,"+
            FIELD_codgrupo+" text not null,"+
            FIELD_bandsupervisor+" int not null,"+
            FIELD_bandvalida+" int not null,"+
            FIELD_clave+" text not null,"+
            FIELD_fechagrabado+" datetime not null,"
            +" FOREIGN KEY("+FIELD_codgrupo+") REFERENCES "+ Grupo.TABLE_NAME+"("+ Grupo.FIELD_codgrupo+")"
            +" )";

    public int id;
    public String codusuario;
    public String codgrupo;
    public String nombreusuario;
    public int bandsupervisor;
    public int bandvalida;
    public String clave;
    public String fechagrabado;

    public Usuario(String codusuario, String codgrupo, String nombreusuario, int bandupervisor, int bandvalida, String clave, String fechagrabado) {
        this.codusuario = codusuario;
        this.codgrupo = codgrupo;
        this.nombreusuario = nombreusuario;
        this.bandsupervisor = bandupervisor;
        this.bandvalida = bandvalida;
        this.clave = clave;
        this.fechagrabado = fechagrabado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodusuario() {
        return codusuario;
    }

    public void setCodusuario(String codusuario) {
        this.codusuario = codusuario;
    }

    public String getCodgrupo() {
        return codgrupo;
    }

    public void setCodgrupo(String codgrupo) {
        this.codgrupo = codgrupo;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public int getBandSupervisor() {
        return bandsupervisor;
    }

    public void setBandSupervisor(int bandupervisor) {
        this.bandsupervisor = bandupervisor;
    }

    public int getBandvalida() {
        return bandvalida;
    }

    public void setBandvalida(int bandvalida) {
        this.bandvalida = bandvalida;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFechagrabado() {
        return fechagrabado;
    }

    public void setFechagrabado(String fechagrabado) {
        this.fechagrabado = fechagrabado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", codusuario='" + codusuario + '\'' +
                ", codgrupo='" + codgrupo + '\'' +
                ", nombreusuario='" + nombreusuario + '\'' +
                ", bandsupervisor=" + bandsupervisor +
                ", bandvalida=" + bandvalida +
                ", clave='" + clave + '\'' +
                ", fechagrabado='" + fechagrabado + '\'' +
                '}';
    }
}
