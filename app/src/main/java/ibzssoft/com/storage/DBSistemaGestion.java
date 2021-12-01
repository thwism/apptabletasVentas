package ibzssoft.com.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.modelo.Acceso;
import ibzssoft.com.modelo.Banco;
import ibzssoft.com.modelo.Bodega;
import ibzssoft.com.modelo.Canton;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Descuento;
import ibzssoft.com.modelo.Empresa;
import ibzssoft.com.modelo.Existencia;
import ibzssoft.com.modelo.GNOpcion;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.IVGrupo1;
import ibzssoft.com.modelo.IVGrupo2;
import ibzssoft.com.modelo.IVGrupo3;
import ibzssoft.com.modelo.IVGrupo4;
import ibzssoft.com.modelo.IVGrupo5;
import ibzssoft.com.modelo.IVGrupo6;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.KardexTransaccion;
import ibzssoft.com.modelo.PCGrupo1;
import ibzssoft.com.modelo.PCGrupo2;
import ibzssoft.com.modelo.PCGrupo3;
import ibzssoft.com.modelo.PCGrupo4;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Parroquia;
import ibzssoft.com.modelo.PedidoPendiente;
import ibzssoft.com.modelo.Permiso;
import ibzssoft.com.modelo.PermisoTrans;
import ibzssoft.com.modelo.Promocion;
import ibzssoft.com.modelo.Provincia;
import ibzssoft.com.modelo.SortItem;
import ibzssoft.com.modelo.TSFormaCobroPago;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.modelo.json.Imagen;

public class DBSistemaGestion extends SQLiteOpenHelper {

    public static final String DB_Name = "MASTER";
    public static final int SCHEME_VERSION = 3;
    public SQLiteDatabase db;

    public DBSistemaGestion(Context context) {
        super(context, DB_Name, null, SCHEME_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Empresa.CREATE_DB_TABLE);
        db.execSQL(Grupo.CREATE_DB_TABLE);
        db.execSQL(GNTrans.CREATE_DB_TABLE);
        db.execSQL(GNOpcion.CREATE_DB_TABLE);
        db.execSQL(Permiso.CREATE_DB_TABLE);
        db.execSQL(PermisoTrans.CREATE_DB_TABLE);
        db.execSQL(Usuario.CREATE_DB_TABLE);
        db.execSQL(Provincia.CREATE_DB_TABLE);
        db.execSQL(Canton.CREATE_DB_TABLE);
        db.execSQL(Parroquia.CREATE_DB_TABLE);
        db.execSQL(PCGrupo1.CREATE_DB_TABLE);
        db.execSQL(PCGrupo2.CREATE_DB_TABLE);
        db.execSQL(PCGrupo3.CREATE_DB_TABLE);
        db.execSQL(PCGrupo4.CREATE_DB_TABLE);
        db.execSQL(Cliente.CREATE_DB_TABLE);
        db.execSQL(Vendedor.CREATE_DB_TABLE);
        db.execSQL(PedidoPendiente.CREATE_DB_TABLE);
        /*Inventario*/
        db.execSQL(IVGrupo1.CREATE_DB_TABLE);
        db.execSQL(IVGrupo2.CREATE_DB_TABLE);
        db.execSQL(IVGrupo3.CREATE_DB_TABLE);
        db.execSQL(IVGrupo4.CREATE_DB_TABLE);
        db.execSQL(IVGrupo5.CREATE_DB_TABLE);
        db.execSQL(IVGrupo6.CREATE_DB_TABLE);
        db.execSQL(IVInventario.CREATE_DB_TABLE);
        db.execSQL(Bodega.CREATE_DB_TABLE);
        db.execSQL(Promocion.CREATE_DB_TABLE);
        db.execSQL(Descuento.CREATE_DB_TABLE);
        db.execSQL(Existencia.CREATE_DB_TABLE);
        /*Transacciones*/
        db.execSQL(Transaccion.CREATE_DB_TABLE);
        db.execSQL(IVKardex.CREATE_DB_TABLE);
        db.execSQL(TSFormaCobroPago.CREATE_DB_TABLE);
        db.execSQL(Banco.CREATE_DB_TABLE);
        db.execSQL(PCKardex.CREATE_DB_TABLE);
        /*Reporte para cobro*/
        db.execSQL(KardexTransaccion.CREATE_DB_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String upgradeDatabase(double versiontablet, double versionactual) {
        db = this.getWritableDatabase();
        String results = "";
        if (versiontablet < versionactual) {
            String sql1 = "ALTER TABLE " + PCKardex.TABLE_NAME + " ADD COLUMN " + PCKardex.FIELD_fechaultimopago + " datetime";
            String sql2 = "ALTER TABLE " + Bodega.TABLE_NAME + " ADD COLUMN " + Bodega.FIELD_nombre + " text";
            String sql3 = "ALTER TABLE " + IVInventario.TABLE_NAME + " ADD COLUMN " + IVInventario.FIELD_img1 + " text";
            String sql4 = "ALTER TABLE " + IVInventario.TABLE_NAME + " ADD COLUMN " + IVInventario.FIELD_img2 + " text";
            String sql5 = "ALTER TABLE " + IVInventario.TABLE_NAME + " ADD COLUMN " + IVInventario.FIELD_img3 + " text";
            String sql6 = "ALTER TABLE " + GNTrans.TABLE_NAME + " ADD COLUMN " + GNTrans.FIELD_modulo + " text";
            String sql7 = "ALTER TABLE " + Transaccion.TABLE_NAME + " ADD COLUMN " + Transaccion.FIELD_fecha_envio + " datetime default null";
            String sql8 = "UPDATE " + Transaccion.TABLE_NAME + " SET " + Transaccion.FIELD_fecha_envio + " = CURRENT_TIMESTAMP";
            String sql9 = "ALTER TABLE " + GNTrans.TABLE_NAME + " ADD COLUMN " + GNTrans.FIELD_idclientepre + " text";
            String sql10 = "ALTER TABLE " + GNTrans.TABLE_NAME + " ADD COLUMN " + GNTrans.FIELD_cantidadpre + " int";
            String sql11 = "ALTER TABLE " + GNTrans.TABLE_NAME + " ADD COLUMN " + GNTrans.FIELD_preciopre + " int";

            String[] statements = new String[]{sql1, sql2, sql3, sql4, sql5, sql6, sql7, sql8, sql9, sql10, sql11};

            for (String str : statements) {
                try {
                    results += str + "\n";
                    System.out.println("Ejecutando: " + str);
                    db.execSQL(str);
                } catch (Exception e) {
                    continue;
                }
            }
        } else if (versionactual == versiontablet) {
            results += "Aplicacion Actualizada\n";
        }
        return results;
    }

    public ContentValues generarValoresEmpresa(Empresa emp) {
        ContentValues content = new ContentValues();
        content.put(Empresa.FIELD_idempresa, emp.getIdempresa());
        content.put(Empresa.FIELD_nombrebase, emp.getNombrebase());
        content.put(Empresa.FIELD_nombreempresa, emp.getNombreempresa());
        content.put(Empresa.FIELD_direccion1, emp.getDireccion1());
        content.put(Empresa.FIELD_telefono1, emp.getTelefono1());
        content.put(Empresa.FIELD_etiquetagrupo1, emp.getEtiquetagrupo1());
        content.put(Empresa.FIELD_etiquetagrupo2, emp.getEtiquetagrupo2());
        content.put(Empresa.FIELD_etiquetagrupo3, emp.getEtiquetagrupo3());
        content.put(Empresa.FIELD_etiquetagrupo4, emp.getEtiquetagrupo4());
        content.put(Empresa.FIELD_etiquetagrupo5, emp.getEtiquetagrupo5());
        content.put(Empresa.FIELD_etiquetagrupo6, emp.getEtiquetagrupo6());
        content.put(Empresa.FIELD_etiquetapcgrupo1, emp.getEtiquetapcgrupo1());
        content.put(Empresa.FIELD_etiquetapcgrupo2, emp.getEtiquetapcgrupo2());
        content.put(Empresa.FIELD_etiquetapcgrupo3, emp.getEtiquetapcgrupo3());
        content.put(Empresa.FIELD_etiquetapcgrupo4, emp.getEtiquetapcgrupo4());
        return content;
    }

    public ContentValues generarValoresKardexTransaccion(KardexTransaccion emp) {
        ContentValues content = new ContentValues();
        content.put(KardexTransaccion.FIELD_transid, emp.getTransID());
        content.put(KardexTransaccion.FIELD_idprovcli, emp.getIdProvCli());
        content.put(KardexTransaccion.FIELD_nombre, emp.getNombre());
        content.put(KardexTransaccion.FIELD_idorigen, emp.getTIDOrigen());
        content.put(KardexTransaccion.FIELD_fechatrans, emp.getFechaTrans());
        content.put(KardexTransaccion.FIELD_horatrans, emp.getHoraTrans());
        content.put(KardexTransaccion.FIELD_codtrans, emp.getCodTrans());
        content.put(KardexTransaccion.FIELD_trans, emp.getTrans());
        content.put(KardexTransaccion.FIELD_numdocref, emp.getNumDocRef());
        content.put(KardexTransaccion.FIELD_descripcion, emp.getDescripcion());
        content.put(KardexTransaccion.FIELD_valor, emp.getValor());
        content.put(KardexTransaccion.FIELD_doc, emp.getDoc());
        content.put(KardexTransaccion.FIELD_debe, emp.getDebe());
        content.put(KardexTransaccion.FIELD_haber, emp.getHaber());
        content.put(KardexTransaccion.FIELD_saldo, emp.getSaldo());
        content.put(KardexTransaccion.FIELD_cotizacion, emp.getCotizacion());
        content.put(KardexTransaccion.FIELD_fechavenci, emp.getFechaVenci());
        content.put(KardexTransaccion.FIELD_estado, emp.getEstado());
        content.put(KardexTransaccion.FIELD_orden, emp.getOrden());
        content.put(KardexTransaccion.FIELD_idorigen, emp.getIdOrigen());
        content.put(KardexTransaccion.FIELD_idcobrador, emp.getIdCobrador());
        return content;
    }

    public ContentValues generarValoresGrupo(Grupo gru, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Grupo.FIELD_codgrupo, gru.getCodgrupo());
        }
        content.put(Grupo.FIELD_descripcion, gru.getDescripcion());
        content.put(Grupo.FIELD_fechagrabado, gru.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresPermiso(Permiso per) {
        ContentValues content = new ContentValues();
        content.put(Permiso.FIELD_codempresa, per.getCodempresa());
        content.put(Permiso.FIELD_codgrupo, per.getCodgrupo());
        content.put(Permiso.FIELD_idpermiso, per.getIdpermiso());
        return content;
    }

    public ContentValues generarValoresPermisoTrans(PermisoTrans per) {
        ContentValues content = new ContentValues();
        content.put(PermisoTrans.FIELD_idpermiso, per.getIdpermiso());
        content.put(PermisoTrans.FIELD_codtrans, per.getCodtrans());
        content.put(PermisoTrans.FIELD_crear, per.getCrear());
        content.put(PermisoTrans.FIELD_eliminar, per.getEliminar());
        content.put(PermisoTrans.FIELD_ver, per.getVer());
        content.put(PermisoTrans.FIELD_modificar, per.getModificar());
        return content;
    }

    public ContentValues generarValoresTSFormaCobroPago(TSFormaCobroPago tsf, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(TSFormaCobroPago.FIELD_idforma, tsf.getIdforma());
            content.put(TSFormaCobroPago.FIELD_codforma, tsf.getCodforma());
        }
        content.put(TSFormaCobroPago.FIELD_nombreforma, tsf.getNombreforma());
        content.put(TSFormaCobroPago.FIELD_plazo, tsf.getPlazo());
        content.put(TSFormaCobroPago.FIELD_bandvalida, tsf.getBandvalida());
        content.put(TSFormaCobroPago.FIELD_fechagrabado, tsf.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresUsuario(Usuario usr, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Usuario.FIELD_codusuario, usr.getCodusuario());
        }

        content.put(Usuario.FIELD_codgrupo, usr.getCodgrupo());
        content.put(Usuario.FIELD_nombreusuario, usr.getNombreusuario());
        content.put(Usuario.FIELD_bandsupervisor, usr.getBandSupervisor());
        content.put(Usuario.FIELD_bandvalida, usr.getBandvalida());
        content.put(Usuario.FIELD_clave, usr.getClave());
        content.put(Usuario.FIELD_fechagrabado, usr.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresVendedor(Vendedor vend, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Vendedor.FIELD_idvendedor, vend.getIdvendedor());
            content.put(Vendedor.FIELD_codvendedor, vend.getCodvendedor());
        }
        content.put(Vendedor.FIELD_codusuario, vend.getCodusuario());
        content.put(Vendedor.FIELD_nombre, vend.getNombre());
        content.put(Vendedor.FIELD_bandvalida, vend.getBandvalida());
        content.put(Vendedor.FIELD_bandvendedor, vend.getBandvendedor());
        content.put(Vendedor.FIELD_bandcobrador, vend.getBandcobrador());
        content.put(Vendedor.FIELD_lineas, vend.getLineas());
        content.put(Vendedor.FIELD_vendedores, vend.getVendedores());
        content.put(Vendedor.FIELD_ordenbodegas, vend.getOrdenbodegas());
        content.put(Vendedor.FIELD_rutastablet, vend.getRutastablet());
        content.put(Vendedor.FIELD_fechagrabado, vend.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresAcceso(Acceso acceso) {
        ContentValues content = new ContentValues();
        content.put(Acceso.FIELD_identificador, acceso.getIdentificador());
        content.put(Acceso.FIELD_permiso_id, acceso.getPermiso_id());
        content.put(Acceso.FIELD_usuario_id, acceso.getUsuario_id());
        content.put(Acceso.FIELD_fecha_grabado, acceso.getFecha_grabado());
        return content;
    }

    public ContentValues generarValoresGNTrans(GNTrans gnt, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(GNTrans.FIELD_codtrans, gnt.getCodtrans());
        }
        content.put(GNTrans.FIELD_idbodegapre, gnt.getIdbodegapre());
        content.put(GNTrans.FIELD_descripcion, gnt.getDescripcion());
        content.put(GNTrans.FIELD_nombretrans, gnt.getNombretrans());
        content.put(GNTrans.FIELD_preciopcgrupo, gnt.getPreciopcgrupo());
        content.put(GNTrans.FIELD_numfilas, gnt.getNumfilas());
        content.put(GNTrans.FIELD_maxdocs, gnt.getMaxdocs());
        content.put(GNTrans.FIELD_opciones, gnt.getOpciones());
        content.put(GNTrans.FIELD_bandobservacion, gnt.getBandobservacion());
        content.put(GNTrans.FIELD_numdiasvencidos, gnt.getNumdiasvencidos());
        content.put(GNTrans.FIELD_diasgracia, gnt.getDiasgracia());
        content.put(GNTrans.FIELD_codpantalla, gnt.getCodpantalla());
        content.put(GNTrans.FIELD_fecha_grabado, gnt.getFechagrabado());
        content.put(GNTrans.FIELD_modulo, gnt.getModulo());
        content.put(GNTrans.FIELD_idclientepre, gnt.getIdclientepre());
        content.put(GNTrans.FIELD_cantidadpre, gnt.getCantidadpre());
        content.put(GNTrans.FIELD_preciopre, gnt.getPreciopre());
        return content;
    }

    public ContentValues generarValoresProvincia(Provincia pro, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Provincia.FIELD_idprovincia, pro.getIdprovincia());
        }
        content.put(Provincia.FIELD_descripcion, pro.getDescripcion());
        content.put(Provincia.FIELD_bandvalida, pro.getBandvalida());
        content.put(Provincia.FIELD_fechagrabado, pro.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresCanton(Canton can, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Canton.FIELD_idcanton, can.getIdcanton());
        }
        content.put(Canton.FIELD_descripcion, can.getDescripcion());
        content.put(Canton.FIELD_idprovincia, can.getIdprovincia());
        content.put(Canton.FIELD_bandvalida, can.getBandvalida());
        content.put(Canton.FIELD_fechagrabado, can.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresParroquia(Parroquia parroquia, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Parroquia.FIELD_idparroquia, parroquia.getIdparroquia());
        }
        content.put(Parroquia.FIELD_descripcion, parroquia.getDescripcion());
        content.put(Parroquia.FIELD_bandvalida, parroquia.getBandvalida());
        content.put(Parroquia.FIELD_idcanton, parroquia.getIdcanton());
        content.put(Parroquia.FIELD_fechagrabado, parroquia.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresUbicacion(String coordenadas) {
        ContentValues content = new ContentValues();
        content.put(Cliente.FIELD_posgooglemaps, coordenadas);
        return content;
    }

    public ContentValues generarValoresCliente(Cliente cli, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Cliente.FIELD_idprovcli, cli.getIdprovcli());
            content.put(Cliente.FIELD_ruc, cli.getRuc());
        }
        content.put(Cliente.FIELD_nombre, cli.getNombre());
        content.put(Cliente.FIELD_nombrealterno, cli.getNombrealterno());
        content.put(Cliente.FIELD_direccion1, cli.getDireccion1());
        content.put(Cliente.FIELD_direccion2, cli.getDireccion2());
        content.put(Cliente.FIELD_telefono1, cli.getTelefono1());
        content.put(Cliente.FIELD_telefono2, cli.getTelefono2());
        content.put(Cliente.FIELD_fax, cli.getFax());
        content.put(Cliente.FIELD_email, cli.getEmail());
        content.put(Cliente.FIELD_banco, cli.getBanco());
        content.put(Cliente.FIELD_numcuenta, cli.getNumcuenta());
        content.put(Cliente.FIELD_direcbanco, cli.getDirecbanco());
        content.put(Cliente.FIELD_bandproveedor, cli.getBandproveedor());
        content.put(Cliente.FIELD_bandcliente, cli.getBandcliente());
        content.put(Cliente.FIELD_estado, cli.getEstado());
        content.put(Cliente.FIELD_idparroquia, cli.getIdparroquia());
        content.put(Cliente.FIELD_idgrupo1, cli.getIdgrupo1());
        content.put(Cliente.FIELD_idgrupo2, cli.getIdgrupo2());
        content.put(Cliente.FIELD_idgrupo3, cli.getIdgrupo3());
        content.put(Cliente.FIELD_idgrupo4, cli.getIdgrupo4());
        content.put(Cliente.FIELD_idvendedor, cli.getIdvendedor());
        content.put(Cliente.FIELD_idcobrador, cli.getIdcobrador());
        content.put(Cliente.FIELD_idprovincia, cli.getIdprovincia());
        content.put(Cliente.FIELD_idcanton, cli.getIdcanton());
        content.put(Cliente.FIELD_diasplazo, cli.getDiasplazo());
        content.put(Cliente.FIELD_observacion, cli.getObservacion());
        content.put(Cliente.FIELD_numprecio, cli.getNumprecio());
        content.put(Cliente.FIELD_numserie, cli.getNumserie());
        content.put(Cliente.FIELD_telefono3, cli.getTelefono3());
        content.put(Cliente.FIELD_posgooglemaps, cli.getPosgooglemaps());
        content.put(Cliente.FIELD_fechagrabado, cli.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresTransaccion(Transaccion tran, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Transaccion.FIELD_ID_Trans, tran.getId_trans());
            content.put(Transaccion.FIELD_identificador, tran.getIdentificador());
        }
        content.put(Transaccion.FIELD_numTransaccion, tran.getNumTransaccion());
        content.put(Transaccion.FIELD_descripcion, tran.getDescripcion());
        content.put(Transaccion.FIELD_fecha_trans, tran.getFecha_trans());
        content.put(Transaccion.FIELD_hora_trans, tran.getHora_trans());
        content.put(Transaccion.FIELD_band_enviado, tran.getBand_enviado());
        content.put(Transaccion.FIELD_vendedor_id, tran.getVendedor_id());
        content.put(Transaccion.FIELD_cliente_id, tran.getCliente_id());
        content.put(Transaccion.FIELD_forma_cobro_id, tran.getForma_cobro_id());
        content.put(Transaccion.FIELD_referencia, tran.getReferencia());
        content.put(Transaccion.FIELD_fecha_grabado, tran.getFecha_grabado());
        content.put(Transaccion.FIELD_fecha_envio, new ParseDates().changeDateToStringSimpleNow());
        return content;
    }

    public ContentValues generarValoresIVKardex(IVKardex ivk, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVKardex.FIELD_identificador, ivk.getIdentificador());
        }
        content.put(IVKardex.FIELD_cantidad, ivk.getCantidad());
        content.put(IVKardex.FIELD_cos_total, ivk.getCosto_total());
        content.put(IVKardex.FIELD_cos_real_total, ivk.getCosto_real_total());
        content.put(IVKardex.FIELD_precio_total, ivk.getPrecio_total());
        content.put(IVKardex.FIELD_pre_real_total, ivk.getPrecio_real_total());
        content.put(IVKardex.FIELD_descuento, ivk.getDescuento());
        content.put(IVKardex.FIELD_descuento_real, ivk.getDescuento_real());
        content.put(IVKardex.FIELD_trans_id, ivk.getTrans_id());
        content.put(IVKardex.FIELD_bodega_id, ivk.getBodega_id());
        content.put(IVKardex.FIELD_inventario_id, ivk.getInventario_id());
        content.put(IVKardex.FIELD_padre_id, ivk.getPadre_id());
        content.put(IVKardex.FIELD_keypadre, ivk.getKeypadre());
        content.put(IVKardex.FIELD_desc_promo, ivk.getDesc_promo());
        content.put(IVKardex.FIELD_num_precio, ivk.getNum_precio());
        content.put(IVKardex.FIELD_desc_sol, ivk.getDesc_sol());
        content.put(IVKardex.FIELD_cos_pro, ivk.getCos_pro());
        content.put(IVKardex.FIELD_band_aprobado, ivk.getBandaprobado());
        content.put(IVKardex.FIELD_fecha_grabado, ivk.getFecha_grabado());
        return content;
    }


    public ContentValues generarValoresIVGrupo1(IVGrupo1 ivg1, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo1.FIELD_idgrupo1, ivg1.getIdgrupo1());
            content.put(IVGrupo1.FIELD_codgrupo1, ivg1.getCodgrupo1());
        }
        content.put(IVGrupo1.FIELD_descripcion, ivg1.getDescripcion());
        content.put(IVGrupo1.FIELD_bandvalida, ivg1.getBandvalida());
        content.put(IVGrupo1.FIELD_fechagrabado, ivg1.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresIVGrupo2(IVGrupo2 ivg2, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo2.FIELD_idgrupo2, ivg2.getIdgrupo2());
            content.put(IVGrupo2.FIELD_codgrupo2, ivg2.getCodgrupo2());
        }
        content.put(IVGrupo2.FIELD_descripcion, ivg2.getDescripcion());
        content.put(IVGrupo2.FIELD_bandvalida, ivg2.getBandvalida());
        content.put(IVGrupo2.FIELD_fechagrabado, ivg2.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresIVGrupo3(IVGrupo3 ivg3, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo3.FIELD_idgrupo3, ivg3.getIdgrupo3());
            content.put(IVGrupo3.FIELD_codgrupo3, ivg3.getCodgrupo3());
        }
        content.put(IVGrupo3.FIELD_descripcion, ivg3.getDescripcion());
        content.put(IVGrupo3.FIELD_bandvalida, ivg3.getBandvalida());
        content.put(IVGrupo3.FIELD_fechagrabado, ivg3.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresIVGrupo4(IVGrupo4 ivg4, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo4.FIELD_idgrupo4, ivg4.getIdgrupo4());
            content.put(IVGrupo4.FIELD_codgrupo4, ivg4.getCodgrupo4());
        }
        content.put(IVGrupo4.FIELD_descripcion, ivg4.getDescripcion());
        content.put(IVGrupo4.FIELD_bandvalida, ivg4.getBandvalida());
        content.put(IVGrupo4.FIELD_fechagrabado, ivg4.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresIVGrupo5(IVGrupo5 ivg5, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo5.FIELD_idgrupo5, ivg5.getIdgrupo5());
            content.put(IVGrupo5.FIELD_codgrupo5, ivg5.getCodgrupo5());
        }
        content.put(IVGrupo5.FIELD_descripcion, ivg5.getDescripcion());
        content.put(IVGrupo5.FIELD_bandvalida, ivg5.getBandvalida());
        content.put(IVGrupo5.FIELD_fechagrabado, ivg5.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresIVGrupo6(IVGrupo6 ivg6, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVGrupo6.FIELD_idgrupo6, ivg6.getIdgrupo6());
            content.put(IVGrupo6.FIELD_codgrupo6, ivg6.getCodgrupo6());
        }
        content.put(IVGrupo6.FIELD_descripcion, ivg6.getDescripcion());
        content.put(IVGrupo6.FIELD_bandvalida, ivg6.getBandvalida());
        content.put(IVGrupo6.FIELD_fechagrabado, ivg6.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresPCGrupo1(PCGrupo1 pcg1, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(PCGrupo1.FIELD_idgrupo1, pcg1.getIdgrupo1());
        }
        content.put(PCGrupo1.FIELD_codgrupo1, pcg1.getCodgrupo1());
        content.put(PCGrupo1.FIELD_preciosdisponibles, pcg1.getPreciosdisponibles());
        content.put(PCGrupo1.FIELD_descripcion, pcg1.getDescripcion());
        content.put(PCGrupo1.FIELD_bandvalida, pcg1.getBandvalida());
        content.put(PCGrupo1.FIELD_fechagrabado, pcg1.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresPCGrupo2(PCGrupo2 pcg2, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(PCGrupo2.FIELD_idgrupo2, pcg2.getIdgrupo2());
        }
        content.put(PCGrupo2.FIELD_codgrupo2, pcg2.getCodgrupo2());
        content.put(PCGrupo2.FIELD_preciosdisponibles, pcg2.getPreciosdisponibles());
        content.put(PCGrupo2.FIELD_descripcion, pcg2.getDescripcion());
        content.put(PCGrupo2.FIELD_bandvalida, pcg2.getBandvalida());
        content.put(PCGrupo2.FIELD_fechagrabado, pcg2.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresPCGrupo3(PCGrupo3 pcg3, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(PCGrupo3.FIELD_idgrupo3, pcg3.getIdgrupo3());
        }
        content.put(PCGrupo3.FIELD_codgrupo3, pcg3.getCodgrupo3());
        content.put(PCGrupo3.FIELD_preciosdisponibles, pcg3.getPreciosdisponibles());
        content.put(PCGrupo3.FIELD_descripcion, pcg3.getDescripcion());
        content.put(PCGrupo3.FIELD_bandvalida, pcg3.getBandvalida());
        content.put(PCGrupo3.FIELD_fechagrabado, pcg3.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresPCGrupo4(PCGrupo4 pcg4, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(PCGrupo4.FIELD_idgrupo4, pcg4.getIdgrupo4());
        }
        content.put(PCGrupo4.FIELD_codgrupo4, pcg4.getCodgrupo4());
        content.put(PCGrupo4.FIELD_preciosdisponibles, pcg4.getPreciosdisponibles());
        content.put(PCGrupo4.FIELD_descripcion, pcg4.getDescripcion());
        content.put(PCGrupo4.FIELD_bandvalida, pcg4.getBandvalida());
        content.put(PCGrupo4.FIELD_fechagrabado, pcg4.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresBodega(Bodega bodega, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Bodega.FIELD_idbodega, bodega.getIdbodega());
        }
        content.put(Bodega.FIELD_codbodega, bodega.getCodbodega());
        content.put(Bodega.FIELD_nombre, bodega.getNombre());
        content.put(Bodega.FIELD_bandvalida, bodega.getBandvalida());
        content.put(Bodega.FIELD_fechagrabado, bodega.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresBanco(Banco banco, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(Banco.FIELD_idbanco, banco.getIdbanco());
        }
        content.put(Banco.FIELD_descripcion, banco.getDescripcion());
        content.put(Banco.FIELD_codbanco, banco.getCodbanco());
        content.put(Banco.FIELD_bandvalida, banco.getBandvalida());
        content.put(Banco.FIELD_fechagrabado, banco.getFechagrabado());
        return content;
    }

    public ContentValues generarValoresGNOpcion(GNOpcion gno, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(GNOpcion.FIELD_codigo, gno.getCodigo());
            content.put(GNOpcion.FIELD_idgnopcion, gno.getIdgnopcion());
        }
        content.put(GNOpcion.FIELD_valor, gno.getValor());
        return content;
    }

    public ContentValues generarValoresIVInventario(IVInventario ivi, boolean modificacion) {
        ContentValues content = new ContentValues();
        if (!modificacion) {
            content.put(IVInventario.FIELD_identificador, ivi.getIdentificador());
        }
        content.put(IVInventario.FIELD_descripcion, ivi.getDescripcion());
        content.put(IVInventario.FIELD_cod_item, ivi.getCod_item());
        content.put(IVInventario.FIELD_cod_alterno, ivi.getCod_alterno());
        content.put(IVInventario.FIELD_observacion, ivi.getObservacion());
        content.put(IVInventario.FIELD_precio1, ivi.getPrecio1());
        content.put(IVInventario.FIELD_precio2, ivi.getPrecio2());
        content.put(IVInventario.FIELD_precio3, ivi.getPrecio3());
        content.put(IVInventario.FIELD_precio4, ivi.getPrecio4());
        content.put(IVInventario.FIELD_precio5, ivi.getPrecio5());
        content.put(IVInventario.FIELD_precio6, ivi.getPrecio6());
        content.put(IVInventario.FIELD_precio7, ivi.getPrecio7());
        content.put(IVInventario.FIELD_cod_moneda, ivi.getCod_moneda());
        content.put(IVInventario.FIELD_porc_iva, ivi.getPorc_iva());
        content.put(IVInventario.FIELD_band_iva, ivi.getBand_iva());
        content.put(IVInventario.FIELD_estado, ivi.getEstado());
        content.put(IVInventario.FIELD_presentacion, ivi.getPresentacion());
        content.put(IVInventario.FIELD_unidad, ivi.getUnidad());
        content.put(IVInventario.FIELD_ivg1, ivi.getIvg1());
        content.put(IVInventario.FIELD_ivg2, ivi.getIvg2());
        content.put(IVInventario.FIELD_ivg3, ivi.getIvg3());
        content.put(IVInventario.FIELD_ivg4, ivi.getIvg4());
        content.put(IVInventario.FIELD_ivg5, ivi.getIvg5());
        content.put(IVInventario.FIELD_ivg6, ivi.getIvg6());
        content.put(IVInventario.FIELD_img1, ivi.getImg1());
        content.put(IVInventario.FIELD_img2, ivi.getImg2());
        content.put(IVInventario.FIELD_img3, ivi.getImg3());
        content.put(IVInventario.FIELD_fecha_grabado, ivi.getFecha_grabado());
        return content;
    }

    public ContentValues generarValoresPCKardex(PCKardex pck) {
        ContentValues content = new ContentValues();
        content.put(PCKardex.FIELD_idcartera, pck.getIdcartera());
        content.put(PCKardex.FIELD_idasignado, pck.getIdasinado());
        content.put(PCKardex.FIELD_fechavenci, pck.getFechavenci());
        content.put(PCKardex.FIELD_valor, pck.getValor());
        content.put(PCKardex.FIELD_plazo, pck.getPlazo());
        content.put(PCKardex.FIELD_pagado, pck.getPagado());
        content.put(PCKardex.FIELD_saldovencido, pck.getSaldovencido());
        content.put(PCKardex.FIELD_saldoxvence, pck.getSaldoxvence());
        content.put(PCKardex.FIELD_dvencidos, pck.getDvencidos());
        content.put(PCKardex.FIELD_fechaemision, pck.getFechaemision());
        content.put(PCKardex.FIELD_idprovcli, pck.getIdprovcli());
        content.put(PCKardex.FIELD_ruc, pck.getRuc());
        content.put(PCKardex.FIELD_nombrecliente, pck.getNombrecli());
        content.put(PCKardex.FIELD_comercialcliente, pck.getComercialcli());
        content.put(PCKardex.FIELD_direccioncliente, pck.getDireccioncli());
        content.put(PCKardex.FIELD_codforma, pck.getCodforma());
        content.put(PCKardex.FIELD_transid, pck.getTransid());
        content.put(PCKardex.FIELD_idvendedor, pck.getIdvendedor());
        content.put(PCKardex.FIELD_idcobrador, pck.getIdcobrador());
        content.put(PCKardex.FIELD_trans, pck.getTrans());
        content.put(PCKardex.FIELD_guid, pck.getGuid());
        content.put(PCKardex.FIELD_cantcheque, pck.getCantcheque());
        content.put(PCKardex.FIELD_idruta, pck.getIdruta());
        content.put(PCKardex.FIELD_doc, pck.getDoc());
        content.put(PCKardex.FIELD_ordencuota, pck.getOrdencuota());
        content.put(PCKardex.FIELD_ruta, pck.getFechaemision());
        /*Campos para el cobro*/
        content.put(PCKardex.FIELD_banco_id, pck.getBanco_id());
        content.put(PCKardex.FIELD_forma_pago, pck.getForma_pago());
        content.put(PCKardex.FIELD_numero_cheque, pck.getNumero_cheque());
        content.put(PCKardex.FIELD_numero_cuenta, pck.getNumero_cuenta());
        content.put(PCKardex.FIELD_titular, pck.getTitular());
        content.put(PCKardex.FIELD_pago_fecha_vencimiento, pck.getPago_fecha_vencimiento());
        content.put(PCKardex.FIELD_band_generado, pck.getBand_generado());
        content.put(PCKardex.FIELD_band_respaldado, pck.getBand_respaldado());
        /*Campos para el cobro con retencion*/
        content.put(PCKardex.FIELD_iva, pck.getIva());
        content.put(PCKardex.FIELD_renta, pck.getRenta());
        content.put(PCKardex.FIELD_iva_base, pck.getIva_base());
        content.put(PCKardex.FIELD_renta_base, pck.getRenta_base());
        content.put(PCKardex.FIELD_num_ser_punto, pck.getNum_ser_punto());
        content.put(PCKardex.FIELD_num_ser_estab, pck.getNum_ser_estab());
        content.put(PCKardex.FIELD_num_ser_secuencial, pck.getNum_ser_secuencial());
        content.put(PCKardex.FIELD_autorizacion, pck.getAutorizacion());
        content.put(PCKardex.FIELD_caducidad, pck.getCaducidad());
        content.put(PCKardex.FIELD_band_cobrado, pck.getBand_cobrado());
        content.put(PCKardex.FIELD_observacion, pck.getObservacion());
        content.put(PCKardex.FIELD_fechaultimopago, pck.getFechaultimopago());
        return content;
    }

    public ContentValues generarValoresPromocion(Promocion pro) {
        ContentValues content = new ContentValues();
        content.put(Promocion.FIELD_cantidad_min, pro.getCantidad_min());
        content.put(Promocion.FIELD_cantidad_promo, pro.getCantidad_promo());
        content.put(Promocion.FIELD_id_inven_promo, pro.getId_inven_promo());
        content.put(Promocion.FIELD_precio_promo, pro.getPrecio_promo());
        content.put(Promocion.FIELD_estado, pro.getEstado());
        content.put(Promocion.FIELD_inventario_id, pro.getInventario_id());
        content.put(Promocion.FIELD_fecha_grabado, pro.getFecha_grabado());
        return content;
    }

    public ContentValues generarValoresExistencia(Existencia existencia) {
        ContentValues content = new ContentValues();
        content.put(Existencia.FIELD_bodega_id, existencia.getBodega_id());
        content.put(Existencia.FIELD_existencia, existencia.getExistencia());
        content.put(Existencia.FIELD_inventario_id, existencia.getInventario_id());
        return content;
    }

    public ContentValues generarValoresDescuento(Descuento descuento) {
        ContentValues content = new ContentValues();
        content.put(Descuento.FIELD_identificador, descuento.getIdentificador());
        content.put(Descuento.FIELD_estado, descuento.getEstado());
        content.put(Descuento.FIELD_cliente_id, descuento.getCliente_id());
        content.put(Descuento.FIELD_inventario_id, descuento.getInventario_id());
        content.put(Descuento.FIELD_vendedor_id, descuento.getVendedor_id());
        content.put(Descuento.FIELD_porcentaje, descuento.getPorcentaje());
        content.put(Descuento.FIELD_fecha_grabado, descuento.getFecha_grabado());
        return content;
    }

    public ContentValues generarValoresPedidoPendiente(PedidoPendiente pedidoPendiente) {
        ContentValues content = new ContentValues();
        content.put(PedidoPendiente.FIELD_idprovcli, pedidoPendiente.getIdprovcli());
        content.put(PedidoPendiente.FIELD_ruc_ci, pedidoPendiente.getRuc_ci());
        content.put(PedidoPendiente.FIELD_nombres, pedidoPendiente.getNombres());
        content.put(PedidoPendiente.FIELD_nombre_alterno, pedidoPendiente.getNombre_alterno());
        content.put(PedidoPendiente.FIELD_items, pedidoPendiente.getItems());
        content.put(PedidoPendiente.FIELD_vendedor_id, pedidoPendiente.getVendedor_id());
        return content;
    }

    public void crearEmpresa(Empresa emp) {
        db = this.getWritableDatabase();
        db.insert(emp.TABLE_NAME, null, generarValoresEmpresa(emp));
    }

    public void crearKardexTransaccion(KardexTransaccion emp) {
        db = this.getWritableDatabase();
        db.insert(emp.TABLE_NAME, null, generarValoresKardexTransaccion(emp));
    }

    public void crearGrupo(Grupo gru) {
        db = this.getWritableDatabase();
        db.insert(gru.TABLE_NAME, null, generarValoresGrupo(gru, false));
    }

    public void crearGNTrans(GNTrans gnt) {
        db = this.getWritableDatabase();
        db.insert(gnt.TABLE_NAME, null, generarValoresGNTrans(gnt, false));
    }

    public void crearProvincia(Provincia pro) {
        db = this.getWritableDatabase();
        db.insert(pro.TABLE_NAME, null, generarValoresProvincia(pro, false));
    }

    public void crearCanton(Canton can) {
        db = this.getWritableDatabase();
        db.insert(can.TABLE_NAME, null, generarValoresCanton(can, false));
    }

    public void crearParroquia(Parroquia par) {
        db = this.getWritableDatabase();
        db.insert(par.TABLE_NAME, null, generarValoresParroquia(par, false));
    }

    public void crearTSFormaCobroPago(TSFormaCobroPago tsf) {
        db = this.getWritableDatabase();
        db.insert(tsf.TABLE_NAME, null, generarValoresTSFormaCobroPago(tsf, false));
    }

    public void crearPermiso(Permiso per) {
        db = this.getWritableDatabase();
        db.insert(per.TABLE_NAME, null, generarValoresPermiso(per));
    }

    public void crearPermisoTrans(PermisoTrans per) {
        db = this.getWritableDatabase();
        db.insert(per.TABLE_NAME, null, generarValoresPermisoTrans(per));
    }

    public void crearVendedor(Vendedor vend) {
        db = this.getWritableDatabase();
        db.insert(vend.TABLE_NAME, null, generarValoresVendedor(vend, false));
    }

    public void crearUsuario(Usuario usr) {
        db = this.getWritableDatabase();
        db.insert(usr.TABLE_NAME, null, generarValoresUsuario(usr, false));
    }

    /*public void crearAcceso(Acceso acceso){
        db.insert(acceso.TABLE_NAME, null, generarValoresAcceso(acceso));
    }*/
    public void crearPCGrupo1(PCGrupo1 pcGrupo1) {
        db = this.getWritableDatabase();
        db.insert(pcGrupo1.TABLE_NAME, null, generarValoresPCGrupo1(pcGrupo1, false));
    }

    public void crearPCGrupo2(PCGrupo2 pcGrupo2) {
        db = this.getWritableDatabase();
        db.insert(pcGrupo2.TABLE_NAME, null, generarValoresPCGrupo2(pcGrupo2, false));
    }

    public void crearPCGrupo3(PCGrupo3 pcGrupo3) {
        db = this.getWritableDatabase();
        db.insert(pcGrupo3.TABLE_NAME, null, generarValoresPCGrupo3(pcGrupo3, false));
    }

    public void crearPCGrupo4(PCGrupo4 pcGrupo4) {
        db = this.getWritableDatabase();
        db.insert(pcGrupo4.TABLE_NAME, null, generarValoresPCGrupo4(pcGrupo4, false));
    }

    public void crearIVGrupo1(IVGrupo1 ivGrupo1) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo1.TABLE_NAME, null, generarValoresIVGrupo1(ivGrupo1, false));
    }

    public void crearIVGrupo2(IVGrupo2 ivGrupo2) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo2.TABLE_NAME, null, generarValoresIVGrupo2(ivGrupo2, false));
    }

    public void crearIVGrupo3(IVGrupo3 ivGrupo3) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo3.TABLE_NAME, null, generarValoresIVGrupo3(ivGrupo3, false));
    }

    public void crearIVGrupo4(IVGrupo4 ivGrupo4) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo4.TABLE_NAME, null, generarValoresIVGrupo4(ivGrupo4, false));
    }

    public void crearIVGrupo5(IVGrupo5 ivGrupo5) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo5.TABLE_NAME, null, generarValoresIVGrupo5(ivGrupo5, false));
    }

    public void crearIVGrupo6(IVGrupo6 ivGrupo6) {
        db = this.getWritableDatabase();
        db.insert(ivGrupo6.TABLE_NAME, null, generarValoresIVGrupo6(ivGrupo6, false));
    }

    public void crearCliente(Cliente cli) {
        db = this.getWritableDatabase();
        db.insert(cli.TABLE_NAME, null, generarValoresCliente(cli, false));
    }

    public void crearTransaccion(Transaccion tran) {
        db = this.getWritableDatabase();
        db.insert(tran.TABLE_NAME, null, generarValoresTransaccion(tran, false));
    }


    public void crearPCKardex(PCKardex pck) {
        db = this.getWritableDatabase();
        db.insert(pck.TABLE_NAME, null, generarValoresPCKardex(pck));
    }

    public void crearBodega(Bodega bod) {
        db = this.getWritableDatabase();
        db.insert(bod.TABLE_NAME, null, generarValoresBodega(bod, false));
    }

    public void crearBanco(Banco ban) {
        db = this.getWritableDatabase();
        db.insert(ban.TABLE_NAME, null, generarValoresBanco(ban, false));
    }

    public void crearGNOpcion(GNOpcion ban) {
        db = this.getWritableDatabase();
        db.insert(ban.TABLE_NAME, null, generarValoresGNOpcion(ban, false));
    }

    public void crearIVInventario(IVInventario ivi) {
        db = this.getWritableDatabase();
        // System.out.println("Producto insertado: "+ivi);
        db.insert(ivi.TABLE_NAME, null, generarValoresIVInventario(ivi, false));
    }

    public void actualizarImagenesProducto(Imagen ivi) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(IVInventario.FIELD_img1, ivi.getImg1());
        content.put(IVInventario.FIELD_img2, ivi.getImg2());
        content.put(IVInventario.FIELD_img3, ivi.getImg3());
        db.update(IVInventario.TABLE_NAME, content, IVInventario.FIELD_identificador + "=" + ivi.getCoditem(), null);
    }

    public void crearIVKardex(IVKardex ivk) {
        db = this.getWritableDatabase();
        db.insert(ivk.TABLE_NAME, null, generarValoresIVKardex(ivk, false));
    }

    public void crearPromocion(Promocion promocion) {
        db = this.getWritableDatabase();
        db.insert(promocion.TABLE_NAME, null, generarValoresPromocion(promocion));
    }

    public void crearExistencia(Existencia existencia) {
        db = this.getWritableDatabase();
        db.insert(Existencia.TABLE_NAME, null, generarValoresExistencia(existencia));
    }

    public void crearDescuento(Descuento descuento) {
        db = this.getWritableDatabase();
        db.insert(Descuento.TABLE_NAME, null, generarValoresDescuento(descuento));
    }

    public void crearPedidoPendiente(PedidoPendiente descuento) {
        db = this.getWritableDatabase();
        db.insert(PedidoPendiente.TABLE_NAME, null, generarValoresPedidoPendiente(descuento));
    }

    /*Verificar existencia de registros*/
    public boolean existeProvincia(int id_prov) {
        db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Provincia.TABLE_NAME + " als WHERE als." + Provincia.FIELD_idprovincia + "=" + id_prov, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean existeForma(int id_forma) {
        db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TSFormaCobroPago.TABLE_NAME + " als WHERE als." + TSFormaCobroPago.FIELD_idforma + "=" + id_forma, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeGNTrans(String id_forma) {
        db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + GNTrans.TABLE_NAME + " als WHERE als." + GNTrans.FIELD_codtrans + "=?", new String[]{id_forma});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeCanton(int id_prov) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Canton.TABLE_NAME + " als WHERE als." + Canton.FIELD_idcanton + "=" + id_prov, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeParroquia(String id_prov) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Parroquia.TABLE_NAME + " als WHERE als." + Parroquia.FIELD_idparroquia + "=" + id_prov, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeVendedor(int id_vendedor) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Vendedor.TABLE_NAME + " als WHERE als." + Vendedor.FIELD_idvendedor + "=" + id_vendedor, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean existeUsuario(String id_usr) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Usuario.TABLE_NAME + " als WHERE als." + Usuario.FIELD_codusuario + "=?", new String[]{id_usr});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean existeCliente(String id_client) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Cliente.TABLE_NAME + " als WHERE als." + Cliente.FIELD_idprovcli + "=?", new String[]{id_client});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeBanco(int id_client) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Banco.TABLE_NAME + " als WHERE als." + Banco.FIELD_idbanco + "=" + id_client, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeBodega(int id_client) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Bodega.TABLE_NAME + " als WHERE als." + Bodega.FIELD_idbodega + "=" + id_client, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existePCGrupo1(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + PCGrupo1.TABLE_NAME + " als WHERE als." + PCGrupo1.FIELD_codgrupo1 + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existePCGrupo2(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + PCGrupo2.TABLE_NAME + " als WHERE als." + PCGrupo2.FIELD_codgrupo2 + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existePCGrupo3(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + PCGrupo3.TABLE_NAME + " als WHERE als." + PCGrupo3.FIELD_codgrupo3 + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existePCGrupo4(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + PCGrupo4.TABLE_NAME + " als WHERE als." + PCGrupo4.FIELD_codgrupo4 + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo1(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo1.TABLE_NAME + " als WHERE als." + IVGrupo1.FIELD_idgrupo1 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeGrupo(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Grupo.TABLE_NAME + " als WHERE als." + Grupo.FIELD_codgrupo + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo2(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo2.TABLE_NAME + " als WHERE als." + IVGrupo2.FIELD_idgrupo2 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo3(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo3.TABLE_NAME + " als WHERE als." + IVGrupo3.FIELD_idgrupo3 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo4(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo4.TABLE_NAME + " als WHERE als." + IVGrupo4.FIELD_idgrupo4 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo5(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo5.TABLE_NAME + " als WHERE als." + IVGrupo5.FIELD_idgrupo5 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVGrupo6(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVGrupo6.TABLE_NAME + " als WHERE als." + IVGrupo6.FIELD_idgrupo6 + "=" + id_param, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVInventario(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVInventario.TABLE_NAME + " als WHERE als." + IVInventario.FIELD_identificador + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean existeTransaccion(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Transaccion.TABLE_NAME + " als WHERE als." + Transaccion.FIELD_ID_Trans + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean existeIVKardex(String id_param) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + IVKardex.TABLE_NAME + " als WHERE als." + IVKardex.FIELD_identificador + "=?", new String[]{id_param});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public int registroscartera() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + PCKardex.TABLE_NAME + " als ", null);
        return mCursor.getCount();
    }


    public boolean existePromocionItem(String item, double cantidad) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Promocion.TABLE_NAME + " pro,"
                + IVInventario.TABLE_NAME + " ivi"
                + " WHERE ivi." + IVInventario.FIELD_identificador + "=pro." + Promocion.FIELD_inventario_id
                + " AND ivi." + IVInventario.FIELD_identificador + " in ('" + item + "')"
                + " AND pro." + Promocion.FIELD_cantidad_min + " <= " + cantidad;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean existeDescuentoItem(String id_item, String id_cliente) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Descuento.TABLE_NAME + " desc,"
                + Cliente.TABLE_NAME + " cli,"
                + IVInventario.TABLE_NAME + " ivi"
                + " WHERE cli." + Cliente.FIELD_idprovcli + "=desc." + Descuento.FIELD_cliente_id
                + " AND ivi." + IVInventario.FIELD_identificador + "=desc." + Descuento.FIELD_inventario_id
                + " AND cli." + Cliente.FIELD_idprovcli + " in ('" + id_cliente + "')"
                + " AND ivi." + IVInventario.FIELD_identificador + " in ('" + id_item + "')";
        Cursor mCursor = db.rawQuery(selectQuery, null);
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }

    public boolean marcarCobradoPCKardex(String id_kardex) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(PCKardex.FIELD_band_cobrado, 1);
        System.out.println("Kardex Modificado: " + id_kardex);
        db.update(PCKardex.TABLE_NAME, content, PCKardex.FIELD_idcartera + " in ('" + id_kardex + "')", null);
        return true;
    }

    public boolean modificarCliente(Cliente cli) {
        db = this.getWritableDatabase();
        db.update(Cliente.TABLE_NAME, generarValoresCliente(cli, true), Cliente.FIELD_idprovcli + " in ('" + cli.getIdprovcli() + "')", null);
        return true;
    }

    public boolean modificarUbicacionGeografica(String id_cliente, String coordenadas) {
        db = this.getWritableDatabase();
        db.update(Cliente.TABLE_NAME, generarValoresUbicacion(coordenadas), Cliente.FIELD_idprovcli + " in ('" + id_cliente + "')", null);
        return true;
    }

    public boolean modificarImagenesProducto(String id_producto, String path, int posicion) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch (posicion) {
            case 0:
                contentValues.put(IVInventario.FIELD_img1, path);
                break;
            case 1:
                contentValues.put(IVInventario.FIELD_img2, path);
                break;
            case 2:
                contentValues.put(IVInventario.FIELD_img3, path);
                break;
        }
        db.update(IVInventario.TABLE_NAME, contentValues, IVInventario.FIELD_identificador + " in ('" + id_producto + "')", null);
        return true;
    }

    public boolean modificarUsuario(Usuario cli) {
        db = this.getWritableDatabase();
        db.update(Usuario.TABLE_NAME, generarValoresUsuario(cli, true), Usuario.FIELD_codusuario + " in ('" + cli.getCodusuario() + "')", null);
        return true;
    }

    public boolean modificarForma(TSFormaCobroPago cli) {
        db = this.getWritableDatabase();
        db.update(TSFormaCobroPago.TABLE_NAME, generarValoresTSFormaCobroPago(cli, true), TSFormaCobroPago.FIELD_idforma + " in ('" + cli.getIdforma() + "')", null);
        return true;
    }

    public boolean modificarGNTrans(GNTrans cli) {
        db = this.getWritableDatabase();
        db.update(GNTrans.TABLE_NAME, generarValoresGNTrans(cli, true), GNTrans.FIELD_codtrans + " in ('" + cli.getCodtrans() + "')", null);
        return true;
    }

    public boolean modificarBanco(Banco cli) {
        db = this.getWritableDatabase();
        db.update(Banco.TABLE_NAME, generarValoresBanco(cli, true), Banco.FIELD_idbanco + " in ('" + cli.getIdbanco() + "')", null);
        return true;
    }

    public boolean modificarGNOpcion(GNOpcion cli) {
        db = this.getWritableDatabase();
        db.update(GNOpcion.TABLE_NAME, generarValoresGNOpcion(cli, true), GNOpcion.FIELD_codigo + " in ('" + cli.getCodigo() + "')", null);
        return true;
    }

    public boolean modificarBodega(Bodega cli) {
        db = this.getWritableDatabase();
        db.update(Bodega.TABLE_NAME, generarValoresBodega(cli, true), Bodega.FIELD_idbodega + " in ('" + cli.getIdbodega() + "')", null);
        return true;
    }

    public boolean modificarProvincia(Provincia cli) {
        db = this.getWritableDatabase();
        db.update(Provincia.TABLE_NAME, generarValoresProvincia(cli, true), Provincia.FIELD_idprovincia + " in ('" + cli.getIdprovincia() + "')", null);
        return true;
    }

    public boolean modificarCanton(Canton cli) {
        db = this.getWritableDatabase();
        db.update(Canton.TABLE_NAME, generarValoresCanton(cli, true), Canton.FIELD_idcanton + " in ('" + cli.getIdcanton() + "')", null);
        return true;
    }

    public boolean modificarParroquia(Parroquia cli) {
        db = this.getWritableDatabase();
        db.update(Parroquia.TABLE_NAME, generarValoresParroquia(cli, true), Parroquia.FIELD_idparroquia + " in ('" + cli.getIdparroquia() + "')", null);
        return true;
    }

    public boolean modificarVendedor(Vendedor cli) {
        db = this.getWritableDatabase();
        db.update(Vendedor.TABLE_NAME, generarValoresVendedor(cli, true), Vendedor.FIELD_codvendedor + " in ('" + cli.getCodvendedor() + "')", null);
        return true;
    }

    public boolean modificarPCGrupo1(PCGrupo1 pcg1) {
        db = this.getWritableDatabase();
        db.update(PCGrupo1.TABLE_NAME, generarValoresPCGrupo1(pcg1, true), PCGrupo1.FIELD_idgrupo1 + " in ('" + pcg1.getIdgrupo1() + "')", null);
        return true;
    }

    public boolean modificarPCGrupo2(PCGrupo2 pcg2) {
        db = this.getWritableDatabase();
        db.update(PCGrupo2.TABLE_NAME, generarValoresPCGrupo2(pcg2, true), PCGrupo2.FIELD_idgrupo2 + " in ('" + pcg2.getIdgrupo2() + "')", null);
        return true;
    }

    public boolean modificarPCGrupo3(PCGrupo3 pcg3) {
        db = this.getWritableDatabase();
        db.update(PCGrupo3.TABLE_NAME, generarValoresPCGrupo3(pcg3, true), PCGrupo3.FIELD_idgrupo3 + " in ('" + pcg3.getIdgrupo3() + "')", null);
        return true;
    }

    public boolean modificarPCGrupo4(PCGrupo4 pcg4) {
        db = this.getWritableDatabase();
        db.update(PCGrupo4.TABLE_NAME, generarValoresPCGrupo4(pcg4, true), PCGrupo4.FIELD_idgrupo4 + " in ('" + pcg4.getIdgrupo4() + "')", null);
        return true;
    }

    public boolean modificarGrupo(Grupo gru) {
        db = this.getWritableDatabase();
        db.update(Grupo.TABLE_NAME, generarValoresGrupo(gru, true), Grupo.FIELD_codgrupo + " in ('" + gru.getCodgrupo() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo1(IVGrupo1 ivg1) {
        db = this.getWritableDatabase();
        db.update(IVGrupo1.TABLE_NAME, generarValoresIVGrupo1(ivg1, true), IVGrupo1.FIELD_idgrupo1 + " in ('" + ivg1.getIdgrupo1() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo2(IVGrupo2 ivg2) {
        db = this.getWritableDatabase();
        db.update(IVGrupo2.TABLE_NAME, generarValoresIVGrupo2(ivg2, true), IVGrupo2.FIELD_idgrupo2 + " in ('" + ivg2.getIdgrupo2() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo3(IVGrupo3 ivg3) {
        db = this.getWritableDatabase();
        db.update(IVGrupo3.TABLE_NAME, generarValoresIVGrupo3(ivg3, true), IVGrupo3.FIELD_idgrupo3 + " in ('" + ivg3.getIdgrupo3() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo4(IVGrupo4 ivg4) {
        db = this.getWritableDatabase();
        db.update(IVGrupo4.TABLE_NAME, generarValoresIVGrupo4(ivg4, true), IVGrupo4.FIELD_idgrupo4 + " in ('" + ivg4.getIdgrupo4() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo5(IVGrupo5 ivg5) {
        db = this.getWritableDatabase();
        db.update(IVGrupo5.TABLE_NAME, generarValoresIVGrupo5(ivg5, true), IVGrupo5.FIELD_idgrupo5 + " in ('" + ivg5.getIdgrupo5() + "')", null);
        return true;
    }

    public boolean modificarIVGrupo6(IVGrupo6 ivg6) {
        db = this.getWritableDatabase();
        db.update(IVGrupo6.TABLE_NAME, generarValoresIVGrupo6(ivg6, true), IVGrupo6.FIELD_idgrupo6 + " in ('" + ivg6.getIdgrupo6() + "')", null);
        return true;
    }

    public boolean modificarIVInventario(IVInventario ivi) {
        db = this.getWritableDatabase();
        db.update(IVInventario.TABLE_NAME, generarValoresIVInventario(ivi, true), IVInventario.FIELD_identificador + " in ('" + ivi.getIdentificador() + "')", null);
        return true;
    }

    public boolean modificarIVKardex(IVKardex ivi) {
        db = this.getWritableDatabase();
        db.update(IVKardex.TABLE_NAME, generarValoresIVKardex(ivi, true), IVKardex.FIELD_identificador + " in ('" + ivi.getIdentificador() + "')", null);
        return true;
    }


    public boolean modificarTransaccion(Transaccion tran) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Transaccion.FIELD_descripcion, tran.getDescripcion());
        content.put(Transaccion.FIELD_numTransaccion, tran.getNumTransaccion());
        content.put(Transaccion.FIELD_fecha_trans, tran.getFecha_trans());
        content.put(Transaccion.FIELD_hora_trans, tran.getHora_trans());
        content.put(Transaccion.FIELD_referencia, tran.getReferencia());
        content.put(Transaccion.FIELD_band_enviado, tran.getBand_enviado());
        content.put(Transaccion.FIELD_vendedor_id, tran.getVendedor_id());
        content.put(Transaccion.FIELD_cliente_id, tran.getCliente_id());
        content.put(Transaccion.FIELD_forma_cobro_id, tran.getForma_cobro_id());
        content.put(Transaccion.FIELD_fecha_grabado, tran.getFecha_grabado());
        System.out.println("Transaccion modificacion: " + tran.toString());
        db.update(Transaccion.TABLE_NAME, content, Transaccion.FIELD_ID_Trans + " in ('" + tran.getId_trans() + "')", null);
        return true;
    }

    public boolean marcarTransaccionComoEnviado(String transid, String referencia) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Transaccion.FIELD_band_enviado, 1);
        content.put(Transaccion.FIELD_fecha_envio, new ParseDates().changeDateToStringSimpleNow());
        content.put(Transaccion.FIELD_referencia, referencia);
        db.update(Transaccion.TABLE_NAME, content, Transaccion.FIELD_ID_Trans + " in ('" + transid + "')", null);
        return true;
    }

    public boolean modificarEmpresa(Empresa emp) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Empresa.FIELD_nombreempresa, emp.getNombreempresa());
        content.put(Empresa.FIELD_direccion1, emp.getDireccion1());
        content.put(Empresa.FIELD_telefono1, emp.getTelefono1());
        content.put(Empresa.FIELD_etiquetagrupo1, emp.getEtiquetagrupo1());
        content.put(Empresa.FIELD_etiquetagrupo2, emp.getEtiquetagrupo2());
        content.put(Empresa.FIELD_etiquetagrupo3, emp.getEtiquetagrupo3());
        content.put(Empresa.FIELD_etiquetagrupo4, emp.getEtiquetagrupo4());
        content.put(Empresa.FIELD_etiquetagrupo5, emp.getEtiquetagrupo5());
        content.put(Empresa.FIELD_etiquetagrupo6, emp.getEtiquetagrupo6());
        content.put(Empresa.FIELD_etiquetapcgrupo1, emp.getEtiquetapcgrupo1());
        content.put(Empresa.FIELD_etiquetapcgrupo2, emp.getEtiquetapcgrupo2());
        content.put(Empresa.FIELD_etiquetapcgrupo3, emp.getEtiquetapcgrupo3());
        content.put(Empresa.FIELD_etiquetapcgrupo4, emp.getEtiquetapcgrupo4());
        db.update(Empresa.TABLE_NAME, content, Empresa.FIELD_nombreempresa + " in ('" + emp.getNombreempresa() + "')", null);
        return true;
    }


    public boolean modificarPassword(String user_id, String pass) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Usuario.FIELD_clave, pass);
        db.update(Usuario.TABLE_NAME, content, Usuario.FIELD_codusuario + " in ('" + user_id + "')", null);
        return true;
    }


    public boolean modificarPermisoTrans(PermisoTrans permiso) {
        db = getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(PermisoTrans.FIELD_modificar, permiso.getModificar());
        content.put(PermisoTrans.FIELD_ver, permiso.getVer());
        content.put(PermisoTrans.FIELD_crear, permiso.getCrear());
        content.put(PermisoTrans.FIELD_eliminar, permiso.getEliminar());
        db.update(PermisoTrans.TABLE_NAME, content, PermisoTrans.FIELD_idpermiso + " in ('" + permiso.getIdpermiso() + "')", null);
        return true;
    }

    /***Eliminar Transaccion**/
    public boolean vaciarExistencias() {
        db = this.getWritableDatabase();
        int rows = db.delete(Existencia.TABLE_NAME, null, null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarKardexTransaccion() {
        db = this.getWritableDatabase();
        int rows = db.delete(KardexTransaccion.TABLE_NAME, null, null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPedidoPendiente() {
        db = this.getWritableDatabase();
        int rows = db.delete(PedidoPendiente.TABLE_NAME, null, null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarCartera() {
        db = this.getWritableDatabase();
        int rows = db.delete(PCKardex.TABLE_NAME, PCKardex.FIELD_band_generado + " = 0", null);
        if (rows > 0) {
            System.out.println("Rows deleted pckardex: " + rows);
        }
        return true;
    }


    public boolean vaciarGrupos() {
        db = this.getWritableDatabase();
        int rows = db.delete(Grupo.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarDescuentos(String idvendedor) {
        db = this.getWritableDatabase();
        System.out.println("Eliminando Descuentos de: " + idvendedor);
        int rows = db.delete(Descuento.TABLE_NAME, Descuento.FIELD_vendedor_id + " in (?)", new String[]{idvendedor});
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarDescuentosAll() {
        db = this.getWritableDatabase();
        int rows = db.delete(Descuento.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPermisos() {
        db = this.getWritableDatabase();
        int rows = db.delete(Permiso.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPermisosTrans() {
        db = this.getWritableDatabase();
        int rows = db.delete(PermisoTrans.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarGNTrans() {
        db = this.getWritableDatabase();
        int rows = db.delete(GNTrans.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarGNOpcion() {
        db = this.getWritableDatabase();
        int rows = db.delete(GNOpcion.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarEmpresas() {
        db = this.getWritableDatabase();
        int rows = db.delete(Empresa.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarFormasPagoCobro() {
        db = this.getWritableDatabase();
        int rows = db.delete(TSFormaCobroPago.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }


    public boolean vaciarAccesos() {
        db = this.getWritableDatabase();
        int rows = db.delete(Acceso.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarBancos() {
        db = this.getWritableDatabase();
        int rows = db.delete(Banco.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarBodegas() {
        db = this.getWritableDatabase();
        int rows = db.delete(Bodega.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public Bodega[] cargarBodegasTransferencia() {
        db = this.getReadableDatabase();
        String query = "select * from " + Bodega.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Bodega> spinnerContent = new ArrayList<Bodega>();
        spinnerContent.add(new Bodega(0, "", "SELECCIONE UNA BODEGA", 1, ""));
        if (cursor.moveToFirst()) {
            do {
                spinnerContent.add(new Bodega(cursor.getInt(cursor.getColumnIndex(Bodega.FIELD_idbodega)), cursor.getString(cursor.getColumnIndex(Bodega.FIELD_codbodega)), cursor.getString(cursor.getColumnIndex(Bodega.FIELD_nombre)), cursor.getInt(cursor.getColumnIndex(Bodega.FIELD_bandvalida)), cursor.getString(cursor.getColumnIndex(Bodega.FIELD_fechagrabado))));
            } while (cursor.moveToNext());
        }
        cursor.close();

        Bodega[] allSpinner = new Bodega[spinnerContent.size()];
        allSpinner = spinnerContent.toArray(allSpinner);
        return allSpinner;
    }

    public boolean vaciarCantones() {
        db = this.getWritableDatabase();
        int rows = db.delete(Canton.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarClientes() {
        db = this.getWritableDatabase();
        int rows = db.delete(Cliente.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo1() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo1.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo2() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo2.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo3() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo3.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo4() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo4.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo5() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo5.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVGrupo6() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVGrupo6.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPCGrupo1() {
        db = this.getWritableDatabase();
        int rows = db.delete(PCGrupo1.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPCGrupo2() {
        db = this.getWritableDatabase();
        int rows = db.delete(PCGrupo2.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPCGrupo3() {
        db = this.getWritableDatabase();
        int rows = db.delete(PCGrupo3.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPCGrupo4() {
        db = this.getWritableDatabase();
        int rows = db.delete(PCGrupo4.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVInventario() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVInventario.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarIVKardex() {
        db = this.getWritableDatabase();
        int rows = db.delete(IVKardex.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarPromocion() {
        db = this.getWritableDatabase();
        int rows = db.delete(Promocion.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarProvincias() {
        db = this.getWritableDatabase();
        int rows = db.delete(Provincia.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarParroquias() {
        db = this.getWritableDatabase();
        int rows = db.delete(Parroquia.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarTransacciones() {
        db = this.getWritableDatabase();
        int rows = db.delete(Transaccion.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean vaciarTSFormaCobroPago() {
        db = this.getWritableDatabase();
        int rows = db.delete(TSFormaCobroPago.TABLE_NAME, "1", null);
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }


    public boolean eliminarIVKardex(String id_ivkardex) {
        db = this.getWritableDatabase();
        db.delete(IVKardex.TABLE_NAME, IVKardex.FIELD_identificador + " = ?", new String[]{id_ivkardex});
        return true;
    }

    public boolean eliminarIVKardexTrans(String transid) {
        System.out.println("Eliminado detalles de: " + transid);
        db = this.getWritableDatabase();
        int rows = db.delete(IVKardex.TABLE_NAME, IVKardex.FIELD_trans_id + " = ?", new String[]{transid});
        if (rows > 0) {
            System.out.println("Rows deleted: " + rows);
        }
        return true;
    }

    public boolean eliminarPCKardexTrans(String transid) {
        db = this.getWritableDatabase();
        db.delete(PCKardex.TABLE_NAME, PCKardex.FIELD_transid + " = ?", new String[]{transid});
        return true;
    }

    public boolean eliminarPCKardex(String id_pckardex) {
        db = this.getWritableDatabase();
        db.delete(PCKardex.TABLE_NAME, PCKardex.FIELD_transid + " = ?", new String[]{id_pckardex});
        return true;
    }

    public boolean eliminarTransaccion(String id_trans) {
        db = this.getWritableDatabase();
        db.delete(Transaccion.TABLE_NAME, Transaccion.FIELD_ID_Trans + " = ?", new String[]{id_trans});
        return true;
    }

    public boolean eliminarTransaccionCatalogo(String catalogo) {
        db = this.getWritableDatabase();
        db.delete(Transaccion.TABLE_NAME, Transaccion.FIELD_identificador + " like '%" + catalogo.toUpperCase() + "%'", null);
        return true;
    }

    /*Funciones y Procedimientos*/
    public boolean login(String user, String pass) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Usuario.TABLE_NAME + " WHERE " + Usuario.FIELD_codusuario + " = ? AND " + Usuario.FIELD_clave + "=?", new String[]{user, pass});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public Cursor buscarUsuario(String user) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Usuario.TABLE_NAME + " usr,"
                + Grupo.TABLE_NAME + " gru,"
                + Vendedor.TABLE_NAME + " vend"
                + " WHERE gru." + Grupo.FIELD_codgrupo + " = usr." + Usuario.FIELD_codgrupo
                + " AND usr." + Usuario.FIELD_codusuario + "  = vend." + Vendedor.FIELD_codusuario
                + " AND usr." + Usuario.FIELD_codusuario + "  = '" + user + "'";
        Cursor mCursor = db.rawQuery(selectQuery, null);
        return mCursor;
    }


    public Cursor buscarEmpresa(String empresa) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Empresa.TABLE_NAME + " WHERE " + Empresa.FIELD_nombreempresa + "=?", new String[]{empresa});
        return mCursor;
    }

    public Cursor consultarEmpresas() {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Empresa.TABLE_NAME, null);
        return mCursor;
    }

    public Cursor consultarVendedoresAll() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Vendedor.TABLE_NAME + " vend"
                + " LEFT JOIN " + Usuario.TABLE_NAME + " usr ON vend." + Vendedor.FIELD_codusuario + "= usr." + Usuario.FIELD_codusuario
                + " LEFT JOIN " + Grupo.TABLE_NAME + "  gru ON gru." + Grupo.FIELD_codgrupo + "= usr." + Usuario.FIELD_codgrupo
                + " AND usr." + Usuario.FIELD_bandvalida + " = 1"
                + " ORDER BY usr." + Usuario.FIELD_nombreusuario + " ASC";
        Cursor mCursor = db.rawQuery(selectQuery, new String[]{});
        return mCursor;
    }

    public Cursor consultarCategorias() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + IVGrupo2.TABLE_NAME + " ivi"
                + " ORDER BY ivi." + IVGrupo2.FIELD_descripcion;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarFamilias() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + IVGrupo1.TABLE_NAME + " ivi"
                + " ORDER BY ivi." + IVGrupo1.FIELD_descripcion;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPermisos(String grupo_id, String empresa_id, String trans, boolean validate, boolean validate2, String[] modulos) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + GNTrans.TABLE_NAME + " gnt"
                + " INNER JOIN " + PermisoTrans.TABLE_NAME + " pert ON pert." + PermisoTrans.FIELD_codtrans + " = gnt." + GNTrans.FIELD_codtrans
                + " INNER JOIN " + Permiso.TABLE_NAME + " per ON pert." + PermisoTrans.FIELD_idpermiso + "=per." + Permiso.FIELD_idpermiso
                + " INNER JOIN " + Grupo.TABLE_NAME + " gru ON per." + Permiso.FIELD_codgrupo + "=gru." + Grupo.FIELD_codgrupo
                + " INNER JOIN " + Empresa.TABLE_NAME + " emp ON UPPER(per." + Permiso.FIELD_codempresa + ") = UPPER(emp." + Empresa.FIELD_idempresa + ")"
                + " WHERE UPPER(gru." + Grupo.FIELD_codgrupo + ")  = UPPER ('" + grupo_id + "')"
                + " AND UPPER(emp." + Empresa.FIELD_idempresa + ") = UPPER ('" + empresa_id + "')";
        if (modulos != null) {
            selectQuery += " AND (";
            for (int i = 0; i < modulos.length; i++) {
                selectQuery += "(gnt." + GNTrans.FIELD_modulo + " in ('" + modulos[i] + "'))";
                if (modulos.length != 1 && i != modulos.length - 1) {
                    selectQuery += " OR ";
                }
            }
            selectQuery += ")";
        }
        if (validate != false) {
            selectQuery += " AND UPPER(gnt." + GNTrans.FIELD_codtrans + ") = UPPER('" + trans + "')";
        }
        if (validate2 != false) {
            selectQuery += " AND (UPPER(gnt." + GNTrans.FIELD_nombretrans + ") like '%" + trans + "%' OR UPPER(gnt." + GNTrans.FIELD_descripcion + ") like '%" + trans + "%')";
        }
        System.out.println("Query ejecutado mis permisos: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPermisosGeneral() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + GNTrans.TABLE_NAME + " gnt"
                + " ORDER BY " + GNTrans.FIELD_codtrans;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraCliente(String id_cli, boolean vencida, int dias_gracia) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck.*"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " WHERE " + PCKardex.FIELD_idprovcli + " = ('" + id_cli + "')"
                + " AND " + PCKardex.FIELD_band_generado + " = 0"
                + " AND " + PCKardex.FIELD_band_cobrado + "  = 0";
        if (vencida) {
            selectQuery += " AND " + PCKardex.FIELD_dvencidos + "-" + dias_gracia + " > 0";
        }
        selectQuery += " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraCobradaCliente(String id_cli) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck.*"
                + " FROM " + KardexTransaccion.TABLE_NAME + " pck"
                + " WHERE " + KardexTransaccion.FIELD_idprovcli + " in ('" + id_cli + "')";
        selectQuery += " ORDER BY pck." + KardexTransaccion.FIELD_nombre + ",pck." + KardexTransaccion.FIELD_idorigen + ",pck." + KardexTransaccion.FIELD_fechatrans + ",pck." + KardexTransaccion.FIELD_transid + ",pck." + KardexTransaccion.FIELD_nombre;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraConInteres(String id_cli, double dias_gracia, double tasa, boolean order_by_codforma) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck." + PCKardex.FIELD_idcartera + " as codigo,"
                + "pck." + PCKardex.FIELD_trans + " as transaccion,"
                + "pck." + PCKardex.FIELD_plazo + " as plazo,"
                + "strftime('%d/%m/%Y',pck." + PCKardex.FIELD_fechaemision + ") as emision,"
                + "(CASE WHEN (" + PCKardex.FIELD_fechaultimopago + ") is null THEN (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechavenci + ")) As Integer)-" + dias_gracia + ") ELSE (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechaultimopago + ")) As Integer)-" + dias_gracia + ") END) as dias_vencidos,"
                //+"(Cast ((JulianDay('now')-JulianDay("+PCKardex.FIELD_fechavenci+")) As Integer)-"+dias_gracia+") as dias_vencidos,"
                + "strftime('%d/%m/%Y',pck." + PCKardex.FIELD_fechavenci + ") as vencimiento,"
                + "round(cast(" + PCKardex.FIELD_valor + " as numeric),2) as valor,"
                + "round(cast(" + PCKardex.FIELD_pagado + " as numeric),2) as valor_cancelado,"
                + "round(cast(" + PCKardex.FIELD_valor + "-" + PCKardex.FIELD_pagado + " as numeric),2) as saldo,"
                + "round(cast(" + PCKardex.FIELD_saldoxvence + " as numeric),2) as saldo_vencer,"
                + "round(cast(" + PCKardex.FIELD_saldovencido + " as numeric),2) as saldo_vencido,"
                + "(CASE WHEN (" + PCKardex.FIELD_fechaultimopago + ") is null "
                + "THEN (CASE WHEN (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechavenci + ")) As Integer)-" + dias_gracia + ") > 0 "
                + "THEN (round((" + tasa + "/360),4)*(CASE WHEN (" + PCKardex.FIELD_fechaultimopago + ") is null THEN (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechavenci + ")) As Integer)-" + dias_gracia + ") ELSE (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechaultimopago + ")) As Integer)-" + dias_gracia + ") END))*(" + PCKardex.FIELD_valor + "-" + PCKardex.FIELD_pagado + ")"
                + " ELSE 0.0 END)"
                + "ELSE "
                + "(CASE WHEN (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechaultimopago + ")) As Integer)-" + dias_gracia + ") >0 "
                + "THEN (round((" + tasa + "/360),4)*(CASE WHEN (" + PCKardex.FIELD_fechaultimopago + ") is null THEN (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechavenci + ")) As Integer)-" + dias_gracia + ") ELSE (Cast ((JulianDay('now')-JulianDay(" + PCKardex.FIELD_fechaultimopago + ")) As Integer)-" + dias_gracia + ") END))*(" + PCKardex.FIELD_valor + "-" + PCKardex.FIELD_pagado + ")"
                + "ELSE 0.0 END)"
                + "END )  as interes,"
                + "pck." + PCKardex.FIELD_ordencuota + " as orden,"
                + "pck." + PCKardex.FIELD_doc + " as documento,"
                + "pck." + PCKardex.FIELD_codforma + " as forma,"
                + "pck." + PCKardex.FIELD_idvendedor + " as idvendedor,"
                + "pck." + PCKardex.FIELD_idcobrador + " as idcobrador,"
                + "strftime('%d/%m/%Y',pck." + PCKardex.FIELD_fechaultimopago + ") as ultimopago"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + "= cli." + Cliente.FIELD_idprovcli
                + " WHERE cli." + Cliente.FIELD_idprovcli + " in ('" + id_cli + "')"
                + " AND pck." + PCKardex.FIELD_band_generado + " = 0"
                + " AND pck." + PCKardex.FIELD_band_cobrado + "  = 0"
                + " GROUP BY codigo, transaccion, plazo, emision, dias_vencidos, vencimiento, valor, valor_cancelado, saldo, saldo_vencer, saldo_vencido, interes, orden, documento, ultimopago"
                + " ORDER BY ";
        if (order_by_codforma)
            selectQuery += " pck." + PCKardex.FIELD_codforma + " desc,";
        selectQuery += "pck." + PCKardex.FIELD_fechavenci + " asc";
        System.out.println("Cartera agrupada: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println(selectQuery);
        return cursor;
    }

    public Cursor consultarSaldoFactura(String id_cli) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck." + PCKardex.FIELD_trans + " as transaccion,"
                + "sum(round(cast(" + PCKardex.FIELD_valor + " as numeric),2)) as valor,"
                + "sum(round(cast(" + PCKardex.FIELD_pagado + " as numeric),2)) as valor_cancelado,"
                + "sum(round(cast(" + PCKardex.FIELD_valor + "-" + PCKardex.FIELD_pagado + " as numeric),2)) as saldo"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + " = cli." + Cliente.FIELD_idprovcli
                + " WHERE cli." + Cliente.FIELD_idprovcli + " in ('" + id_cli + "')"
                + " GROUP BY  transaccion";
        System.out.println("Saldo x factura: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println(selectQuery);
        return cursor;
    }

    public Cursor consultarCarteraClienteFactura(String factura) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + PCKardex.TABLE_NAME + " pck,"
                + Cliente.TABLE_NAME + " cli"
                + " WHERE " + Cliente.FIELD_idprovcli + "=" + PCKardex.FIELD_idprovcli
                + " AND " + PCKardex.FIELD_trans + " in ('" + factura + "')"
                + " AND " + PCKardex.FIELD_band_generado + " = 0"
                + " AND " + PCKardex.FIELD_band_cobrado + "  = 0"
                + " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPCKardexTransaccionCatalogo(String id_trans) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + PCKardex.TABLE_NAME + " pck,"
                + Transaccion.TABLE_NAME + " trans"
                + " WHERE trans." + Transaccion.FIELD_ID_Trans + "=pck." + PCKardex.FIELD_transid
                + " AND trans." + Transaccion.FIELD_identificador + " like '%" + id_trans + "%'"
                + " AND pck." + PCKardex.FIELD_band_generado + " = 1";
        System.out.println("QUery kardex por transaccion: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPCKardexTransaccion(String catalogo) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + PCKardex.TABLE_NAME + " pck,"
                + Transaccion.TABLE_NAME + " trans"
                + " WHERE trans." + Transaccion.FIELD_ID_Trans + "=pck." + PCKardex.FIELD_transid
                + " and trans." + Transaccion.FIELD_ID_Trans + " in ('" + catalogo + "')"
                + " and pck." + PCKardex.FIELD_band_generado + " = 1";
        System.out.println("QUery kardex por transaccion: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public Cursor consultarFormasCobroPago() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *"
                + " FROM " + TSFormaCobroPago.TABLE_NAME + " tsf"
                + " WHERE tsf." + TSFormaCobroPago.FIELD_bandvalida + " = 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarFormaCobro(String cod_forma) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + TSFormaCobroPago.TABLE_NAME + " tsf" +
                " Where " + TSFormaCobroPago.FIELD_codforma + " =" + cod_forma;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor buscarClientesCobro(String text, String[] accesos, int busqueda) throws SQLException {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_ID + ","
                + " cli." + Cliente.FIELD_idprovcli + ","
                + " cli." + Cliente.FIELD_nombre + ","
                + " cli." + Cliente.FIELD_nombrealterno + ","
                + " cli." + Cliente.FIELD_ruc + ","
                + " cli." + Cliente.FIELD_direccion1 + ","
                + " cli." + Cliente.FIELD_telefono1 + ","
                + " cli." + Cliente.FIELD_email + ","
                + " cli." + Cliente.FIELD_observacion + ","
                + " cli." + Cliente.FIELD_idvendedor + ","
                + " cli." + Cliente.FIELD_diasplazo
                + " FROM " + Cliente.TABLE_NAME + " cli";
        switch (busqueda) {
            case 0:
                selectQuery += " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idvendedor + "= vend." + Vendedor.FIELD_idvendedor;
                break;
            case 1:
                selectQuery += " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idcobrador + "= vend." + Vendedor.FIELD_idvendedor;
                break;
            case 2:
                break;
        }
        selectQuery += " WHERE ((cli." + Cliente.FIELD_nombre + " like '%" + text + "%') OR (cli." + Cliente.FIELD_nombrealterno + " like '%" + text + "%') OR (cli." + Cliente.FIELD_ruc + " like '%" + text + "%'))";
        switch (busqueda) {
            case 0:
            case 1:
                selectQuery += " AND (";
                for (int i = 0; i < accesos.length; i++) {
                    selectQuery += "(vend." + Vendedor.FIELD_idvendedor + " in ('" + accesos[i] + "'))";
                    if (accesos.length != 1 && i != accesos.length - 1) {
                        selectQuery += " OR ";
                    }
                }
                selectQuery += ")";
                break;
            case 2:
                break;
        }
        selectQuery += " AND cli." + Cliente.FIELD_estado + " = 1";
        System.out.println("Query FetchByItems: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor buscarClientes(String text, String[] accesos, int busqueda) throws SQLException {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_ID + ","
                + " cli." + Cliente.FIELD_idprovcli + ","
                + " cli." + Cliente.FIELD_nombre + ","
                + " cli." + Cliente.FIELD_nombrealterno + ","
                + " cli." + Cliente.FIELD_ruc + ","
                + " cli." + Cliente.FIELD_direccion1 + ","
                + " cli." + Cliente.FIELD_telefono1 + ","
                + " cli." + Cliente.FIELD_email + ","
                + " cli." + Cliente.FIELD_observacion + ","
                + " cli." + Cliente.FIELD_idvendedor + ","
                + " cli." + Cliente.FIELD_diasplazo
                + " FROM " + Cliente.TABLE_NAME + " cli";
        switch (busqueda) {
            case 0:
                selectQuery += " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idvendedor + "= vend." + Vendedor.FIELD_idvendedor;
                break;
            case 1:
                selectQuery += " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idcobrador + "= vend." + Vendedor.FIELD_idvendedor;
                break;
            case 2:
                break;
        }
        selectQuery += " WHERE ((cli." + Cliente.FIELD_nombre + " like '%" + text + "%') OR (cli." + Cliente.FIELD_nombrealterno + " like '%" + text + "%') OR (cli." + Cliente.FIELD_ruc + " like '%" + text + "%'))";
        switch (busqueda) {
            case 0:
            case 1:
                selectQuery += " AND (";
                for (int i = 0; i < accesos.length; i++) {
                    selectQuery += "(vend." + Vendedor.FIELD_idvendedor + " in ('" + accesos[i] + "'))";
                    if (accesos.length != 1 && i != accesos.length - 1) {
                        selectQuery += " OR ";
                    }
                }
                selectQuery += ")";
                break;
            case 2:
                break;
        }
        selectQuery += " AND cli." + Cliente.FIELD_estado + " = 1";
        System.out.println("Query FetchByItems: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor filtrarBancos(String text) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT "
                + "ban." + Banco.FIELD_ID + ","
                + "ban." + Banco.FIELD_descripcion + ","
                + "ban." + Banco.FIELD_codbanco
                + " FROM " + Banco.TABLE_NAME + " ban"
                + " WHERE ban." + Banco.FIELD_descripcion + " like '%" + text + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor filtrarClientesCartera(String text, String[] accesos, boolean conf_rutas) throws SQLException {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_ID + ","
                + " cli." + Cliente.FIELD_idprovcli + ","
                + " cli." + Cliente.FIELD_nombre + ","
                + " cli." + Cliente.FIELD_nombrealterno + ","
                + " cli." + Cliente.FIELD_ruc + ","
                + " cli." + Cliente.FIELD_direccion1 + ","
                + " cli." + Cliente.FIELD_telefono1 + ","
                + " cli." + Cliente.FIELD_email + ","
                + " cli." + Cliente.FIELD_idvendedor
                + " FROM " + Cliente.TABLE_NAME + " cli";
        if (!conf_rutas) {
            selectQuery += " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idvendedor + " = vend." + Vendedor.FIELD_idvendedor;
        }
        selectQuery += " INNER JOIN " + PCKardex.TABLE_NAME + " pck ON pck." + PCKardex.FIELD_idprovcli + "= cli." + Cliente.FIELD_idprovcli
                + " WHERE ((cli." + Cliente.FIELD_nombre + " like '%" + text + "%') OR (cli." + Cliente.FIELD_nombrealterno + " like '%" + text + "%'))";
        if (!conf_rutas) {
            selectQuery += " AND (";
            for (int i = 0; i < accesos.length; i++) {
                selectQuery += "(vend." + Vendedor.FIELD_idvendedor + " in ('" + accesos[i] + "'))";
                if (accesos.length != 1 && i != accesos.length - 1) {
                    selectQuery += " OR ";
                }
            }
            selectQuery += ") ";
        }
        selectQuery += " group by cli." + Cliente.FIELD_ID + ",cli." + Cliente.FIELD_idprovcli +
                ",cli." + Cliente.FIELD_nombre + ",cli." + Cliente.FIELD_nombrealterno
                + ",cli." + Cliente.FIELD_direccion1 + ",cli." + Cliente.FIELD_telefono1
                + ",cli." + Cliente.FIELD_email + ",cli." + Cliente.FIELD_idvendedor;
        System.out.println("Query FetchByItems: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor consultarPedidosPendientes(String[] accesos) throws SQLException {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ppe.*"
                + " FROM " + PedidoPendiente.TABLE_NAME + " ppe";
        selectQuery += " WHERE ppe." + PedidoPendiente.FIELD_vendedor_id + "  is not null";
        selectQuery += " AND (";
        for (int i = 0; i < accesos.length; i++) {
            selectQuery += "(ppe." + PedidoPendiente.FIELD_vendedor_id + " in ('" + accesos[i] + "'))";
            if (accesos.length != 1 && i != accesos.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ") ";
        System.out.println("Query FetchByItems: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor buscarItemsDescripcion(String text, String[] lineas, String[] bodegas) throws SQLException {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pro." + IVInventario.FIELD_ID + ","
                + "pro." + IVInventario.FIELD_identificador + ","
                + "pro." + IVInventario.FIELD_descripcion + ","
                + "pro." + IVInventario.FIELD_cod_item + ","
                + "pro." + IVInventario.FIELD_cod_alterno + ","
                + "pro." + IVInventario.FIELD_precio1 + ","
                + "pro." + IVInventario.FIELD_precio2 + ","
                + "pro." + IVInventario.FIELD_precio3 + ","
                + "pro." + IVInventario.FIELD_precio4 + ","
                + "pro." + IVInventario.FIELD_cod_moneda + ","
                + "pro." + IVInventario.FIELD_porc_iva + ","
                + "pro." + IVInventario.FIELD_fecha_grabado + ","
                + "ifnull((SELECT sum(" + Existencia.FIELD_existencia + ")"
                + " FROM " + Existencia.TABLE_NAME + "," + Bodega.TABLE_NAME
                + " WHERE " + Existencia.FIELD_bodega_id + "=" + Bodega.FIELD_idbodega
                + " AND " + Existencia.FIELD_inventario_id + " = pro." + IVInventario.FIELD_identificador;
        selectQuery += " AND (";
        for (int i = 0; i < bodegas.length; i++) {
            selectQuery += "(" + Bodega.FIELD_idbodega + " in ('" + bodegas[i] + "'))";
            if (bodegas.length != 1 && i != bodegas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")),'0') as existencia,"
                + "pro." + IVInventario.FIELD_band_iva + ","
                + "pro." + IVInventario.FIELD_presentacion
                + " FROM " + IVInventario.TABLE_NAME + " pro"
                + " INNER JOIN " + IVGrupo1.TABLE_NAME + " ivg1 ON pro." + IVInventario.FIELD_ivg1 + " = ivg1." + IVGrupo1.FIELD_idgrupo1
                + " WHERE ((pro." + IVInventario.FIELD_descripcion + " like '%" + text + "%') OR (pro." + IVInventario.FIELD_cod_item + " like '%" + text + "%'))";

        selectQuery += " AND (";
        for (int i = 0; i < lineas.length; i++) {
            selectQuery += "(ivg1." + IVGrupo1.FIELD_idgrupo1 + " in ('" + lineas[i] + "'))";
            if (lineas.length != 1 && i != lineas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += " AND  pro." + IVInventario.FIELD_estado + " = 1";
        selectQuery += " ORDER BY pro." + IVInventario.FIELD_descripcion;
        System.out.println("Query Items Descripcion: " + selectQuery);
        Cursor mCursor = db.rawQuery(selectQuery, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor consultarClientesxVendedor(String[] usuarios) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Cliente.TABLE_NAME + " cli,"
                + Vendedor.TABLE_NAME + " ven"
                + " WHERE cli." + Cliente.FIELD_idvendedor + "=ven." + Vendedor.FIELD_idvendedor;
        selectQuery += " AND (";
        for (int i = 0; i < usuarios.length; i++) {
            selectQuery += "(ven." + Vendedor.FIELD_idvendedor + " in ('" + usuarios[i] + "'))";
            if (usuarios.length != 1 && i != usuarios.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += " AND cli." + Cliente.FIELD_estado + " = 1";
        selectQuery += " ORDER BY cli." + Cliente.FIELD_nombre + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarClientesxCobrador(String[] usuarios) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Cliente.TABLE_NAME + " cli"
                + " INNER JOIN " + Vendedor.TABLE_NAME + " ven ON cli." + Cliente.FIELD_idcobrador + "=ven." + Vendedor.FIELD_idvendedor
                + " LEFT JOIN " + PCGrupo1.TABLE_NAME + " pcg1 ON cli." + Cliente.FIELD_idgrupo1 + " = pcg1." + PCGrupo1.FIELD_idgrupo1
                + " WHERE cli." + Cliente.FIELD_idcobrador + " = ven." + Vendedor.FIELD_idvendedor;
        selectQuery += " AND (";
        for (int i = 0; i < usuarios.length; i++) {
            selectQuery += "(ven." + Vendedor.FIELD_idvendedor + " in ('" + usuarios[i] + "'))";
            if (usuarios.length != 1 && i != usuarios.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += " AND cli." + Cliente.FIELD_estado + " = 1";
        selectQuery += " ORDER BY cli." + Cliente.FIELD_nombre + " ASC";
        System.out.println("Query aplicado: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarClientesTodo() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Cliente.TABLE_NAME + " cli"
                + " WHERE cli." + Cliente.FIELD_estado + " = 1";
        selectQuery += " ORDER BY cli." + Cliente.FIELD_nombre + " ASC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraxVendedor(String[] usuarios, boolean agrupada) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck.*, cli.*"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + " = cli." + Cliente.FIELD_idprovcli
                + " WHERE pck." + PCKardex.FIELD_band_generado + " = 0";
        selectQuery += " AND (";
        for (int i = 0; i < usuarios.length; i++) {
            selectQuery += "(pck." + PCKardex.FIELD_idvendedor + " = " + usuarios[i] + ")";
            if (usuarios.length != 1 && i != usuarios.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        if (agrupada) selectQuery += " GROUP BY pck." + PCKardex.FIELD_idprovcli;
        selectQuery += " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraxCobrador(String[] usuarios, boolean agrupada) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck.*, cli.*"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + " = cli." + Cliente.FIELD_idprovcli
                + " WHERE pck." + PCKardex.FIELD_band_generado + " = 0";
        selectQuery += " AND (";
        for (int i = 0; i < usuarios.length; i++) {
            selectQuery += "(pck." + PCKardex.FIELD_idcobrador + " = " + usuarios[i] + ")";
            if (usuarios.length != 1 && i != usuarios.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        if (agrupada) selectQuery += " GROUP BY pck." + PCKardex.FIELD_idprovcli;
        selectQuery += " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraxRutas(String[] rutas, boolean agrupada) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck.*, cli.*"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + " = cli." + Cliente.FIELD_idprovcli
                + " WHERE pck." + PCKardex.FIELD_band_generado + " = 0";
        selectQuery += " AND (";
        for (int i = 0; i < rutas.length; i++) {
            selectQuery += "(pck." + PCKardex.FIELD_idruta + " = " + rutas[i] + ")";
            if (rutas.length != 1 && i != rutas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        if (agrupada) selectQuery += " GROUP BY pck." + PCKardex.FIELD_idprovcli;
        selectQuery += " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarDescuentosxAgrupados() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli.*"
                + " FROM " + Descuento.TABLE_NAME + " desct"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON desct." + Descuento.FIELD_cliente_id + "=cli." + Cliente.FIELD_idprovcli
                + " WHERE desct." + Descuento.FIELD_estado + " = 1"
                + " GROUP BY cli." + Cliente.FIELD_idprovcli
                + " ORDER BY cli." + Cliente.FIELD_nombre + "  ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarDescuentosxCliente(String identificador) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_idprovcli + ","
                + "ivi." + IVInventario.FIELD_descripcion + ","
                + "ivi." + IVInventario.FIELD_cod_item + ","
                + "desct.*"
                + " FROM " + Descuento.TABLE_NAME + " desct"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON desct." + Descuento.FIELD_cliente_id + "=cli." + Cliente.FIELD_idprovcli
                + " INNER JOIN " + IVInventario.TABLE_NAME + " ivi ON desct." + Descuento.FIELD_inventario_id + "=ivi." + IVInventario.FIELD_identificador
                + " WHERE cli." + Cliente.FIELD_idprovcli + " = " + identificador
                + " AND desct." + Descuento.FIELD_estado + " = 1"
                + " ORDER BY ivi." + IVInventario.FIELD_descripcion + "  ASC"
                + " LIMIT 1000";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarCarteraPersonal(String cliente) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_idprovcli + ","
                + "cli." + Cliente.FIELD_nombre + ","
                + "cli." + Cliente.FIELD_nombrealterno + ","
                + "cli." + Cliente.FIELD_direccion1 + ","
                + "cli." + Cliente.FIELD_ruc + ","
                + "cli." + Cliente.FIELD_fechagrabado + ","
                + "cli." + Cliente.FIELD_telefono1 + ","
                + "cli." + Cliente.FIELD_email + ","
                + "cli." + Cliente.FIELD_bandproveedor + ","
                + "cli." + Cliente.FIELD_bandcliente + ","
                + "cli." + Cliente.FIELD_estado + ","
                + "cli." + Cliente.FIELD_posgooglemaps + ","
                + "cli." + Cliente.FIELD_idparroquia + ","
                + "cli." + Cliente.FIELD_idcanton + ","
                + "cli." + Cliente.FIELD_idgrupo1 + ","
                + "cli." + Cliente.FIELD_idgrupo2 + ","
                + "cli." + Cliente.FIELD_idgrupo3 + ","
                + "cli." + Cliente.FIELD_idgrupo4 + ","
                + "cli." + Cliente.FIELD_idvendedor + ","
                + "pck.*"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON pck." + PCKardex.FIELD_idprovcli + "=cli." + Cliente.FIELD_idprovcli
                + " WHERE cli." + Cliente.FIELD_idprovcli + "  IN ('" + cliente + "') and"
                + " pck." + PCKardex.FIELD_band_generado + "  = 0"
                + " ORDER BY pck." + PCKardex.FIELD_dvencidos + " DESC";
        System.out.println("Query consultar cartera: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarDescuentos(String cliente, String producto) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli." + Cliente.FIELD_idprovcli + ","
                + "cli." + Cliente.FIELD_nombre + ","
                + "cli." + Cliente.FIELD_nombrealterno + ","
                + "cli." + Cliente.FIELD_direccion1 + ","
                + "cli." + Cliente.FIELD_ruc + ","
                + "cli." + Cliente.FIELD_fechagrabado + ","
                + "cli." + Cliente.FIELD_telefono1 + ","
                + "cli." + Cliente.FIELD_email + ","
                + "cli." + Cliente.FIELD_bandproveedor + ","
                + "cli." + Cliente.FIELD_bandcliente + ","
                + "cli." + Cliente.FIELD_estado + ","
                + "cli." + Cliente.FIELD_posgooglemaps + ","
                + "cli." + Cliente.FIELD_idparroquia + ","
                + "cli." + Cliente.FIELD_idcanton + ","
                + "cli." + Cliente.FIELD_idgrupo1 + ","
                + "cli." + Cliente.FIELD_idgrupo2 + ","
                + "cli." + Cliente.FIELD_idgrupo3 + ","
                + "cli." + Cliente.FIELD_idgrupo4 + ","
                + "cli." + Cliente.FIELD_idvendedor + ","
                + "ivi." + IVInventario.FIELD_descripcion + ","
                + "ivi." + IVInventario.FIELD_cod_item + ","
                + "desct.*"
                + " FROM " + Descuento.TABLE_NAME + " desct"
                + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON desct." + Descuento.FIELD_cliente_id + "=cli." + Cliente.FIELD_idprovcli
                + " INNER JOIN " + IVInventario.TABLE_NAME + " ivi ON desct." + Descuento.FIELD_inventario_id + "=ivi." + IVInventario.FIELD_identificador;
        selectQuery += " AND desct." + Descuento.FIELD_estado + " = 1";
        selectQuery += " AND upper(cli." + Cliente.FIELD_nombre + ") like ('%" + cliente + "%')";
        selectQuery += " AND upper(ivi." + IVInventario.FIELD_descripcion + ") like ('%" + producto + "%')";
        selectQuery += " ORDER BY cli." + Cliente.FIELD_nombre + " ASC";

        System.out.println("Query descuentos cliente: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerCliente(String id_cliente) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli.*"
                + ",par." + Parroquia.FIELD_descripcion
                + ",can." + Canton.FIELD_descripcion
                + ",pro." + Provincia.FIELD_descripcion
                + ",vend." + Vendedor.FIELD_nombre
                + " FROM " + Cliente.TABLE_NAME + " cli"
                + " LEFT JOIN " + Parroquia.TABLE_NAME + " par ON cli." + Cliente.FIELD_idparroquia + " = par." + Parroquia.FIELD_idparroquia
                + " LEFT JOIN " + Canton.TABLE_NAME + " can ON cli." + Cliente.FIELD_idcanton + " = can." + Canton.FIELD_idcanton
                + " LEFT JOIN " + Provincia.TABLE_NAME + " pro ON cli." + Cliente.FIELD_idprovincia + " = pro." + Provincia.FIELD_idprovincia
                + " LEFT JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idvendedor + " = vend." + Vendedor.FIELD_idvendedor
                + " WHERE cli." + Cliente.FIELD_idprovcli + " in ('" + id_cliente + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerClientePrecio(String id_cliente, int numpcgrupo) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT cli.*"
                + ",par." + Parroquia.FIELD_descripcion
                + ",can." + Canton.FIELD_descripcion
                + ",pro." + Provincia.FIELD_descripcion
                + ",vend." + Vendedor.FIELD_nombre
                + ",pcg.*"
                + " FROM " + Cliente.TABLE_NAME + " cli"
                + " LEFT JOIN " + Parroquia.TABLE_NAME + " par ON cli." + Cliente.FIELD_idparroquia + " = par." + Parroquia.FIELD_idparroquia
                + " LEFT JOIN " + Canton.TABLE_NAME + " can ON cli." + Cliente.FIELD_idcanton + " = can." + Canton.FIELD_idcanton
                + " LEFT JOIN " + Provincia.TABLE_NAME + " pro ON cli." + Cliente.FIELD_idprovincia + " = pro." + Provincia.FIELD_idprovincia
                + " LEFT JOIN " + Vendedor.TABLE_NAME + " vend ON cli." + Cliente.FIELD_idvendedor + " = vend." + Vendedor.FIELD_idvendedor;
        switch (numpcgrupo) {
            case 1:
                selectQuery += " LEFT JOIN " + PCGrupo1.TABLE_NAME + " pcg ON cli." + Cliente.FIELD_idgrupo1 + " = pcg." + PCGrupo1.FIELD_idgrupo1;
                break;
            case 2:
                selectQuery += " LEFT JOIN " + PCGrupo2.TABLE_NAME + " pcg ON cli." + Cliente.FIELD_idgrupo2 + " = pcg." + PCGrupo2.FIELD_idgrupo2;
                break;
            case 3:
                selectQuery += " LEFT JOIN " + PCGrupo3.TABLE_NAME + " pcg ON cli." + Cliente.FIELD_idgrupo3 + " = pcg." + PCGrupo3.FIELD_idgrupo3;
                break;
            case 4:
                selectQuery += " LEFT JOIN " + PCGrupo4.TABLE_NAME + " pcg ON cli." + Cliente.FIELD_idgrupo4 + " = pcg." + PCGrupo4.FIELD_idgrupo4;
                break;
        }
        selectQuery += " WHERE cli." + Cliente.FIELD_idprovcli + " in ('" + id_cliente + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerVendedor(int id_vendedor) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Vendedor.TABLE_NAME + " vend"
                + " LEFT JOIN " + Usuario.TABLE_NAME + " usr ON vend." + Vendedor.FIELD_codusuario + " = usr." + Usuario.FIELD_codusuario
                + " LEFT JOIN " + Grupo.TABLE_NAME + " gru ON gru." + Grupo.FIELD_codgrupo + " = usr." + Usuario.FIELD_codgrupo
                + " WHERE vend." + Vendedor.FIELD_idvendedor + " = " + id_vendedor;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerTransaccion(String id_trans) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Transaccion.TABLE_NAME + " trans"
                + " LEFT JOIN " + Vendedor.TABLE_NAME + " vend ON trans." + Transaccion.FIELD_vendedor_id + " = vend." + Vendedor.FIELD_idvendedor
                + " LEFT JOIN " + Usuario.TABLE_NAME + " usr ON vend." + Vendedor.FIELD_codusuario + " = usr." + Usuario.FIELD_codusuario
                + " LEFT JOIN " + Cliente.TABLE_NAME + " cli ON trans." + Transaccion.FIELD_cliente_id + " = cli." + Cliente.FIELD_idprovcli
                + " WHERE trans." + Transaccion.FIELD_ID_Trans + " in ('" + id_trans + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerExistenciaItem(String id_item, String[] bodegas) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT exi." + Existencia.FIELD_existencia
                + ",ivi." + IVInventario.FIELD_descripcion
                + ",bod." + Bodega.FIELD_codbodega
                + ",bod." + Bodega.FIELD_idbodega
                + " FROM " + Existencia.TABLE_NAME + " exi"
                + " LEFT JOIN " + IVInventario.TABLE_NAME + " ivi ON exi." + Existencia.FIELD_inventario_id + " = ivi." + IVInventario.FIELD_identificador
                + " INNER JOIN " + Bodega.TABLE_NAME + " bod ON exi." + Existencia.FIELD_bodega_id + " = bod." + Bodega.FIELD_idbodega
                + " WHERE (" +
                "           (ivi." + IVInventario.FIELD_identificador + " in ('" + id_item + "')) or (ivi." + IVInventario.FIELD_cod_item + " in ('" + id_item + "'))"
                + ")";
        selectQuery += " AND (";
        for (int i = 0; i < bodegas.length; i++) {
            selectQuery += "(bod." + Bodega.FIELD_idbodega + " in ('" + bodegas[i] + "'))";
            if (bodegas.length != 1 && i != bodegas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += "ORDER BY bod." + Bodega.FIELD_codbodega;
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    /*public Cursor obtenerExistenciaItemInforme(String id_item){
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ifnull(exi."+Existencia.FIELD_existencia+",0)"
                +",ivi."+IVInventario.FIELD_descripcion
                +",bod."+Bodega.FIELD_descripcion
                +",bod."+Bodega.FIELD_identificador
                +",bod."+Bodega.FIELD_cod_bodega
                +" FROM " + Bodega.TABLE_NAME+" bod"
                +" LEFT JOIN " + Existencia.TABLE_NAME+" exi"
                +" INNER JOIN " + IVInventario.TABLE_NAME+" ivi ON exi."+Existencia.FIELD_inventario_id+" = ivi."+IVInventario.FIELD_identificador
                +" ON bod."+Bodega.FIELD_identificador+" = exi."+Existencia.FIELD_bodega_id
                +" WHERE ivi."+ IVInventario.FIELD_identificador+" in ('"+id_item+"')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }*/
    public Cursor obtenerExistenciaItemBodega(String id_item, String id_bodega) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT exi." + Existencia.FIELD_existencia
                + ",ivi." + IVInventario.FIELD_descripcion
                + ",bod." + Bodega.FIELD_codbodega
                + " FROM " + Existencia.TABLE_NAME + " exi"
                + " INNER JOIN " + IVInventario.TABLE_NAME + " ivi ON exi." + Existencia.FIELD_inventario_id + " = ivi." + IVInventario.FIELD_identificador
                + " INNER JOIN " + Bodega.TABLE_NAME + " bod ON exi." + Existencia.FIELD_bodega_id + " = bod." + Bodega.FIELD_idbodega
                + " WHERE ivi." + IVInventario.FIELD_identificador + " in ('" + id_item + "')"
                + " AND bod." + Bodega.FIELD_codbodega + " in ('" + id_bodega + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerItem(String id_item) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ivi.*,"
                + "ivg1." + IVGrupo1.FIELD_descripcion + ","
                + "ivg2." + IVGrupo2.FIELD_descripcion + ","
                + "ivg3." + IVGrupo3.FIELD_descripcion + ","
                + "ivg4." + IVGrupo4.FIELD_descripcion + ","
                + "ivg5." + IVGrupo5.FIELD_descripcion + ","
                + "ivg6." + IVGrupo6.FIELD_descripcion
                + " FROM " + IVInventario.TABLE_NAME + " ivi"
                + " LEFT JOIN " + IVGrupo1.TABLE_NAME + " ivg1 ON ivg1." + IVGrupo1.FIELD_idgrupo1 + "=ivi." + IVInventario.FIELD_ivg1
                + " LEFT JOIN " + IVGrupo2.TABLE_NAME + " ivg2 ON ivg2." + IVGrupo2.FIELD_idgrupo2 + "=ivi." + IVInventario.FIELD_ivg2
                + " LEFT JOIN " + IVGrupo3.TABLE_NAME + " ivg3 ON ivg3." + IVGrupo3.FIELD_idgrupo3 + "=ivi." + IVInventario.FIELD_ivg3
                + " LEFT JOIN " + IVGrupo4.TABLE_NAME + " ivg4 ON ivg4." + IVGrupo4.FIELD_idgrupo4 + "=ivi." + IVInventario.FIELD_ivg4
                + " LEFT JOIN " + IVGrupo5.TABLE_NAME + " ivg5 ON ivg5." + IVGrupo5.FIELD_idgrupo5 + "=ivi." + IVInventario.FIELD_ivg5
                + " LEFT JOIN " + IVGrupo6.TABLE_NAME + " ivg6 ON ivg6." + IVGrupo6.FIELD_idgrupo6 + "=ivi." + IVInventario.FIELD_ivg6
                + " WHERE ivi." + IVInventario.FIELD_identificador + " in ('" + id_item + "')";
        System.out.println("QUery ejecutado inventario: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor obtenerItemCodigo(String cod_item) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ivi.*,"
                + "ivg1." + IVGrupo1.FIELD_descripcion + ","
                + "ivg2." + IVGrupo2.FIELD_descripcion + ","
                + "ivg3." + IVGrupo3.FIELD_descripcion + ","
                + "ivg4." + IVGrupo4.FIELD_descripcion + ","
                + "ivg5." + IVGrupo5.FIELD_descripcion + ","
                + "ivg6." + IVGrupo6.FIELD_descripcion
                + " FROM " + IVInventario.TABLE_NAME + " ivi"
                + " LEFT JOIN " + IVGrupo1.TABLE_NAME + " ivg1 ON ivg1." + IVGrupo1.FIELD_idgrupo1 + "=ivi." + IVInventario.FIELD_ivg1
                + " LEFT JOIN " + IVGrupo2.TABLE_NAME + " ivg2 ON ivg2." + IVGrupo2.FIELD_idgrupo2 + "=ivi." + IVInventario.FIELD_ivg2
                + " LEFT JOIN " + IVGrupo3.TABLE_NAME + " ivg3 ON ivg3." + IVGrupo3.FIELD_idgrupo3 + "=ivi." + IVInventario.FIELD_ivg3
                + " LEFT JOIN " + IVGrupo4.TABLE_NAME + " ivg4 ON ivg4." + IVGrupo4.FIELD_idgrupo4 + "=ivi." + IVInventario.FIELD_ivg4
                + " LEFT JOIN " + IVGrupo5.TABLE_NAME + " ivg5 ON ivg5." + IVGrupo5.FIELD_idgrupo5 + "=ivi." + IVInventario.FIELD_ivg5
                + " LEFT JOIN " + IVGrupo6.TABLE_NAME + " ivg6 ON ivg6." + IVGrupo6.FIELD_idgrupo6 + "=ivi." + IVInventario.FIELD_ivg6
                + " WHERE ivi." + IVInventario.FIELD_cod_item + " in ('" + cod_item + "')";
        System.out.println("QUery ejecutado inventario: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarSectores() {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + PCGrupo1.TABLE_NAME + " par"
                + " WHERE par." + PCGrupo1.FIELD_bandvalida + " = 1";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarProductos(String[] lineas) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ivi.*,"
                + "ivg1." + IVGrupo1.FIELD_descripcion + ","
                + "ivg2." + IVGrupo2.FIELD_descripcion + ","
                + "ivg3." + IVGrupo3.FIELD_descripcion + ","
                + "ivg4." + IVGrupo4.FIELD_descripcion + ","
                + "ivg5." + IVGrupo5.FIELD_descripcion + ","
                + "ivg6." + IVGrupo6.FIELD_descripcion
                + " FROM " + IVInventario.TABLE_NAME + " ivi"
                + " LEFT JOIN " + IVGrupo1.TABLE_NAME + " ivg1 ON ivg1." + IVGrupo1.FIELD_idgrupo1 + "=ivi." + IVInventario.FIELD_ivg1
                + " LEFT JOIN " + IVGrupo2.TABLE_NAME + " ivg2 ON ivg2." + IVGrupo2.FIELD_idgrupo2 + "=ivi." + IVInventario.FIELD_ivg2
                + " LEFT JOIN " + IVGrupo3.TABLE_NAME + " ivg3 ON ivg3." + IVGrupo3.FIELD_idgrupo3 + "=ivi." + IVInventario.FIELD_ivg3
                + " LEFT JOIN " + IVGrupo4.TABLE_NAME + " ivg4 ON ivg4." + IVGrupo4.FIELD_idgrupo4 + "=ivi." + IVInventario.FIELD_ivg4
                + " LEFT JOIN " + IVGrupo5.TABLE_NAME + " ivg5 ON ivg5." + IVGrupo5.FIELD_idgrupo5 + "=ivi." + IVInventario.FIELD_ivg5
                + " LEFT JOIN " + IVGrupo6.TABLE_NAME + " ivg6 ON ivg6." + IVGrupo6.FIELD_idgrupo6 + "=ivi." + IVInventario.FIELD_ivg6
                + " WHERE ivi." + IVInventario.FIELD_estado + " = 1";
        selectQuery += " AND (";
        for (int i = 0; i < lineas.length; i++) {
            selectQuery += "(ivg1." + IVGrupo1.FIELD_idgrupo1 + " in ('" + lineas[i] + "'))";
            if (lineas.length != 1 && i != lineas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += " ORDER BY ivi." + IVInventario.FIELD_descripcion + "  ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarProductosShop(String[] lineas, String pcg1, String pcg2, String pcg3, String pcg4, String pcg5, String pcg6) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT ivi.*,"
                + "ivg1." + IVGrupo1.FIELD_descripcion + ","
                + "ivg2." + IVGrupo2.FIELD_descripcion + ","
                + "ivg3." + IVGrupo3.FIELD_descripcion + ","
                + "ivg4." + IVGrupo4.FIELD_descripcion + ","
                + "ivg5." + IVGrupo5.FIELD_descripcion + ","
                + "ivg6." + IVGrupo6.FIELD_descripcion
                + " FROM " + IVInventario.TABLE_NAME + " ivi"
                + " LEFT JOIN " + IVGrupo1.TABLE_NAME + " ivg1 ON ivg1." + IVGrupo1.FIELD_idgrupo1 + "=ivi." + IVInventario.FIELD_ivg1
                + " LEFT JOIN " + IVGrupo2.TABLE_NAME + " ivg2 ON ivg2." + IVGrupo2.FIELD_idgrupo2 + "=ivi." + IVInventario.FIELD_ivg2
                + " LEFT JOIN " + IVGrupo3.TABLE_NAME + " ivg3 ON ivg3." + IVGrupo3.FIELD_idgrupo3 + "=ivi." + IVInventario.FIELD_ivg3
                + " LEFT JOIN " + IVGrupo4.TABLE_NAME + " ivg4 ON ivg4." + IVGrupo4.FIELD_idgrupo4 + "=ivi." + IVInventario.FIELD_ivg4
                + " LEFT JOIN " + IVGrupo5.TABLE_NAME + " ivg5 ON ivg5." + IVGrupo5.FIELD_idgrupo5 + "=ivi." + IVInventario.FIELD_ivg5
                + " LEFT JOIN " + IVGrupo6.TABLE_NAME + " ivg6 ON ivg6." + IVGrupo6.FIELD_idgrupo6 + "=ivi." + IVInventario.FIELD_ivg6
                + " WHERE ivi." + IVInventario.FIELD_estado + " = 1";
        selectQuery += " AND (";
        for (int i = 0; i < lineas.length; i++) {
            selectQuery += "(ivg1." + IVGrupo1.FIELD_idgrupo1 + " in ('" + lineas[i] + "'))";
            if (lineas.length != 1 && i != lineas.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        selectQuery += " AND (ivg1." + IVGrupo1.FIELD_codgrupo1 + " in ('" + pcg1 + "') or ivg2." + IVGrupo2.FIELD_codgrupo2 + " in ('" + pcg2 + "')";
        selectQuery += " or ivg3." + IVGrupo3.FIELD_codgrupo3 + " in ('" + pcg3 + "') or ivg4." + IVGrupo4.FIELD_codgrupo4 + " in ('" + pcg4 + "')";
        selectQuery += " or ivg5." + IVGrupo5.FIELD_codgrupo5 + " in ('" + pcg5 + "') or ivg6." + IVGrupo6.FIELD_codgrupo6 + " in ('" + pcg6 + "')";
        selectQuery += ")";
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println("Query categorias: " + selectQuery);
        return cursor;
    }

    public Cursor consultarTransacciones(String[] accesos, int send, String nombre_trans, int orden, int comodin) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Transaccion.TABLE_NAME + " trans,"
                + Vendedor.TABLE_NAME + " vend,"
                + Cliente.TABLE_NAME + " cli"
                + " WHERE trans." + Transaccion.FIELD_vendedor_id + "=vend." + Vendedor.FIELD_idvendedor
                + " AND trans." + Transaccion.FIELD_cliente_id + "=cli." + Cliente.FIELD_idprovcli
                + " AND upper(trans." + Transaccion.FIELD_identificador + ") like '%" + nombre_trans + "%'";
        selectQuery += " AND (";
        for (int i = 0; i < accesos.length; i++) {
            selectQuery += "(vend." + Vendedor.FIELD_idvendedor + " in ('" + accesos[i] + "'))";
            if (accesos.length != 1 && i != accesos.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        switch (send) {
            case 0:
                break;
            case 1:
                selectQuery += " AND trans." + Transaccion.FIELD_band_enviado + " in (1)";
                break;
            case 2:
                selectQuery += " AND trans." + Transaccion.FIELD_band_enviado + " in (0)";
                break;
        }
        switch (orden) {
            case 0: //Numero de transaccion
                selectQuery += " ORDER BY trans." + Transaccion.FIELD_numTransaccion;
                break;
            case 1://Nombre del Cliente
                selectQuery += " ORDER BY cli." + Cliente.FIELD_nombre;
                break;
            case 2://Codigo de transaccion
                selectQuery += " ORDER BY trans." + Transaccion.FIELD_referencia;
                break;
            case 3://Fecha de envio
                selectQuery += " ORDER BY trans." + Transaccion.FIELD_fecha_envio;
                break;
            case 4://Fecha de modificacion
                selectQuery += " ORDER BY trans." + Transaccion.FIELD_fecha_grabado;
                break;
        }
        if (comodin != 0) selectQuery += " ASC";
        else selectQuery += " DESC";
        System.out.println("consulta transacciones: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarTransferencias(String[] accesos, int send, String nombre_trans) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Transaccion.TABLE_NAME + " trans,"
                + Vendedor.TABLE_NAME + " vend"
                + " WHERE trans." + Transaccion.FIELD_vendedor_id + "=vend." + Vendedor.FIELD_idvendedor
                + " AND upper(trans." + Transaccion.FIELD_identificador + ") like '%" + nombre_trans + "%'";
        selectQuery += " AND (";
        for (int i = 0; i < accesos.length; i++) {
            selectQuery += "(vend." + Vendedor.FIELD_idvendedor + " in ('" + accesos[i] + "'))";
            if (accesos.length != 1 && i != accesos.length - 1) {
                selectQuery += " OR ";
            }
        }
        selectQuery += ")";
        switch (send) {
            case 0:
                break;
            case 1:
                selectQuery += " AND trans." + Transaccion.FIELD_band_enviado + " in (1)";
                break;
            case 2:
                selectQuery += " AND trans." + Transaccion.FIELD_band_enviado + " in (0)";
                break;
        }
        selectQuery += " ORDER BY trans." + Transaccion.FIELD_numTransaccion + " DESC";

        //System.out.println("consulta transacciones: "+selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarIVKardex(String id_trans) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Transaccion.TABLE_NAME + " trans,"
                + IVKardex.TABLE_NAME + " ivk,"
                + IVInventario.TABLE_NAME + " pro"
                + " WHERE trans." + Transaccion.FIELD_ID_Trans + "=ivk." + IVKardex.FIELD_trans_id
                + " AND pro." + IVInventario.FIELD_identificador + " = ivk." + IVKardex.FIELD_inventario_id
                + " AND ivk." + IVKardex.FIELD_trans_id + " in ('" + id_trans + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarIVKardexCatalogo(String catalogo) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Transaccion.TABLE_NAME + " trans,"
                + IVKardex.TABLE_NAME + " ivk"
                + " WHERE trans." + Transaccion.FIELD_ID_Trans + "=ivk." + IVKardex.FIELD_trans_id
                + " AND trans." + Transaccion.FIELD_identificador + " like '%" + catalogo.toUpperCase() + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPCKardex(String id_trans) {
        db = this.getReadableDatabase();
        System.out.println("Consultando PCKardex Trans: " + id_trans);
        String selectQuery = "SELECT * "
                + "FROM " + PCKardex.TABLE_NAME + " pck"
                + " WHERE pck." + PCKardex.FIELD_transid + " in('" + id_trans + "')"
                + " AND pck." + PCKardex.FIELD_band_generado + " =1 ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPCKardexCapitalInteres(String id_int, String id_cap, double dias_gracia) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT pck." + PCKardex.FIELD_transid + " as codigo,"
                + "pck." + PCKardex.FIELD_trans + " as transaccion,"
                + "pck." + PCKardex.FIELD_plazo + " as plazo,"
                + "strftime('%d/%m/%Y',pck." + PCKardex.FIELD_fechaemision + ") as emision,"
                + "(pck." + PCKardex.FIELD_dvencidos + "-" + dias_gracia + ") as dias_vencidos,"
                + "strftime('%d/%m/%Y',pck." + PCKardex.FIELD_fechavenci + ") as vencimiento,"
                + " (select sum(round(cast(" + PCKardex.FIELD_valor + " as numeric),2)) from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_dvencidos + " = " + "pck." + PCKardex.FIELD_dvencidos + ") as valor,"
                + " (select sum(round(cast(" + PCKardex.FIELD_pagado + " as numeric),2)) from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_dvencidos + " = " + "pck." + PCKardex.FIELD_dvencidos + ") as valor_cancelado,"
                + " (select sum(round(cast(" + PCKardex.FIELD_valor + "-" + PCKardex.FIELD_pagado + " as numeric),2)) from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_dvencidos + " = " + "pck." + PCKardex.FIELD_dvencidos + ") as saldo,"
                + " (select sum(round(cast(" + PCKardex.FIELD_saldoxvence + " as numeric),2)) from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_dvencidos + " = " + "pck." + PCKardex.FIELD_dvencidos + ") as saldo_vencer,"
                + " (select sum(round(cast(" + PCKardex.FIELD_saldovencido + " as numeric),2)) from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_dvencidos + " = " + "pck." + PCKardex.FIELD_dvencidos + ") as saldo_vencido,"
                + " (select " + PCKardex.FIELD_idcartera + " from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_ordencuota + " = " + "pck." + PCKardex.FIELD_ordencuota + " and " + PCKardex.FIELD_codforma + "='CRI') as pck_int,"
                + " (select " + PCKardex.FIELD_idcartera + " from " + PCKardex.TABLE_NAME + " where " + PCKardex.FIELD_trans + " = " + "pck." + PCKardex.FIELD_trans + " and " + PCKardex.FIELD_ordencuota + " = " + "pck." + PCKardex.FIELD_ordencuota + " and " + PCKardex.FIELD_codforma + "='CR') as pck_cap,"
                + PCKardex.FIELD_ordencuota + " as orden"
                + " FROM " + PCKardex.TABLE_NAME + " pck"
                + " WHERE pck." + PCKardex.FIELD_band_generado + " = 0"
                + " AND (pck." + PCKardex.FIELD_idcartera + " = " + id_int + " OR pck." + PCKardex.FIELD_idcartera + "=" + id_cap + ")"
                + " GROUP BY codigo, transaccion, plazo, emision, dias_vencidos, vencimiento, valor, valor_cancelado, saldo, saldo_vencer, saldo_vencido,pck_int,pck_cap, orden";
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println("Query ejecutado pck: " + selectQuery);
        return cursor;
    }

    public Cursor consultarPCKardexNormal(String idInt, String idCap) {
        db = this.getReadableDatabase();
        String selectQuery = "select * from " + PCKardex.TABLE_NAME + " pck WHERE pck." + PCKardex.FIELD_band_generado + " = 0 and (pck." + PCKardex.FIELD_idcartera + "=" + idCap + " OR pck." + PCKardex.FIELD_idcartera + "=" + idInt + ")";
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println("Query ejecutado: " + selectQuery);
        return cursor;
    }


    public Cursor consultarItem(String coditem) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + IVInventario.TABLE_NAME + " ivi"
                + " WHERE (" +
                "(ivi." + IVInventario.FIELD_identificador + " in ('" + coditem + "')) or (ivi." + IVInventario.FIELD_cod_item + " in ('" + coditem + "'))" +
                ")";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarGNTrans(String trans) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + GNTrans.TABLE_NAME + " tra"
                + " WHERE tra." + GNTrans.FIELD_codtrans + " in ('" + trans + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarBodega(String id_bodega) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT bod.*" +
                " FROM " + Bodega.TABLE_NAME + " bod"
                + " WHERE (bod." + Bodega.FIELD_idbodega + " in ('" + id_bodega + "') OR bod." + Bodega.FIELD_codbodega + " in ('" + id_bodega + "'))";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarGNOpcion(String clave) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + GNOpcion.TABLE_NAME + " tra"
                + " WHERE tra." + GNOpcion.FIELD_codigo + " in ('" + clave + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPerfil(String emp_id, String vend_id) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT usr." + Usuario.FIELD_nombreusuario
                + ",usr." + Usuario.FIELD_codusuario
                + ",usr." + Usuario.FIELD_bandsupervisor
                + ",gru." + Grupo.FIELD_codgrupo
                + ",emp." + Empresa.FIELD_idempresa
                + ",emp." + Empresa.FIELD_nombreempresa
                + ",ven." + Vendedor.FIELD_idvendedor
                + ",ven." + Vendedor.FIELD_lineas
                + " FROM " + Vendedor.TABLE_NAME + " ven,"
                + Usuario.TABLE_NAME + " usr,"
                + Grupo.TABLE_NAME + " gru,"
                + Empresa.TABLE_NAME + " emp,"
                + Permiso.TABLE_NAME + " per"
                + " WHERE usr." + Usuario.FIELD_codusuario + " = ven." + Vendedor.FIELD_codusuario
                + " AND gru." + Grupo.FIELD_codgrupo + " = usr." + Usuario.FIELD_codgrupo
                + " AND gru." + Grupo.FIELD_codgrupo + " = per." + Permiso.FIELD_codgrupo
                + " AND emp." + Empresa.FIELD_idempresa + " = per." + Permiso.FIELD_codempresa
                + " AND emp." + Empresa.FIELD_idempresa + " in ('" + emp_id + "')"
                + " AND ven." + Vendedor.FIELD_idvendedor + " in ('" + vend_id + "')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarDescuentoItem(String cliente, String item) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Descuento.TABLE_NAME + " desc,"
                + Cliente.TABLE_NAME + " cli,"
                + IVInventario.TABLE_NAME + " ivi"
                + " WHERE cli." + Cliente.FIELD_idprovcli + "=desc." + Descuento.FIELD_cliente_id
                + " AND ivi." + IVInventario.FIELD_identificador + "=desc." + Descuento.FIELD_inventario_id
                + " AND cli." + Cliente.FIELD_idprovcli + " in ('" + cliente + "')"
                + " AND (ivi." + IVInventario.FIELD_identificador + " in ('" + item + "') or ivi." + IVInventario.FIELD_cod_item + " in ('" + item + "'))";
        System.out.println("Query aplicado para el descuento: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor consultarPromocionItem(String item, int cantidad) {
        db = this.getReadableDatabase();
        String selectQuery = "SELECT *" +
                " FROM " + Promocion.TABLE_NAME + " pro"
                + " INNER JOIN " + IVInventario.TABLE_NAME + " ivi ON ivi." + IVInventario.FIELD_identificador + "=pro." + Promocion.FIELD_inventario_id
                + " WHERE ivi." + IVInventario.FIELD_identificador + " IN ('" + item + "')"
                + " AND pro." + Promocion.FIELD_cantidad_min + " <= " + cantidad;
        System.out.println("QUery ejecutado promocion: " + selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public Cursor consultarPCGrupo(int grupo, String cliente_id) {
        db = this.getReadableDatabase();
        String query = "";
        switch (grupo) {
            case 1:
                query = "SELECT cli." + Cliente.FIELD_ID + "," +
                        "pcg." + PCGrupo1.FIELD_preciosdisponibles +
                        " FROM " + PCGrupo1.TABLE_NAME + " pcg"
                        + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON cli." + Cliente.FIELD_idgrupo1 + "=pcg." + PCGrupo1.FIELD_idgrupo1;
                break;
            case 2:
                query = "SELECT cli." + Cliente.FIELD_ID + "," +
                        "pcg." + PCGrupo2.FIELD_preciosdisponibles +
                        " FROM " + PCGrupo2.TABLE_NAME + " pcg"
                        + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON cli." + Cliente.FIELD_idgrupo2 + "=pcg." + PCGrupo2.FIELD_idgrupo2;
                break;
            case 3:
                query = "SELECT cli." + Cliente.FIELD_ID + "," +
                        "pcg." + PCGrupo3.FIELD_preciosdisponibles +
                        " FROM " + PCGrupo3.TABLE_NAME + " pcg"
                        + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON cli." + Cliente.FIELD_idgrupo3 + "=pcg." + PCGrupo3.FIELD_idgrupo3;
                break;
            case 4:
                query = "SELECT cli." + Cliente.FIELD_ID + "," +
                        "pcg." + PCGrupo4.FIELD_preciosdisponibles +
                        " FROM " + PCGrupo4.TABLE_NAME + " pcg"
                        + " INNER JOIN " + Cliente.TABLE_NAME + " cli ON cli." + Cliente.FIELD_idgrupo4 + "=pcg." + PCGrupo4.FIELD_idgrupo4;
                break;
            default:
                break;
        }
        query = query + " WHERE cli." + Cliente.FIELD_idprovcli + " = " + cliente_id;
        System.out.println("Query a ejecutar consultar PCGrupo: " + query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public List<SortItem> consultarCategoriaItem(String tabla, String descripcion) {
        db = this.getReadableDatabase();
        List<SortItem> sortItemList = new ArrayList<>();
        sortItemList.add(new SortItem("0", "Seleccionar  " + descripcion));
        String query = "SELECT * FROM " + tabla;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                sortItemList.add(new SortItem(cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        return sortItemList;
    }

    public Cursor consultarIVGrupo(String tabla) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + tabla + " order by 4 asc";
        System.out.println("consulta ivg: " + query);
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int numeroTransacciones(String id_trans, String id_vendedor) {
        db = this.getReadableDatabase();
        int count = 0;
        String sql = "SELECT trans." + Transaccion.FIELD_numTransaccion
                + " FROM " + Transaccion.TABLE_NAME + " trans"
                + " INNER JOIN " + Vendedor.TABLE_NAME + " vend ON trans." + Transaccion.FIELD_vendedor_id + " = vend." + Vendedor.FIELD_idvendedor
                + " WHERE trans." + Transaccion.FIELD_identificador + " in ('" + id_trans + "')"
                + " AND vend." + Vendedor.FIELD_idvendedor + " in ('" + id_vendedor + "')"
                + " ORDER BY trans." + Transaccion.FIELD_fecha_grabado + " DESC"
                + " LIMIT 1";
        System.out.println("Numero de transacciones: " + sql);
        Cursor result = db.rawQuery(sql, null);
        if (result.moveToFirst()) {
            count = result.getInt(result.getColumnIndex(Transaccion.FIELD_numTransaccion)) + 1;
        } else {
            count = 1;
        }
        result.close();
        return count;
    }

    public String getDirCliente(String id_client) {

        db = this.getReadableDatabase();

        String query = "SELECT cli." + Cliente.FIELD_direccion1
                + ", cli." + Cliente.FIELD_direccion2
                + " FROM " + Cliente.TABLE_NAME + " cli WHERE cli." + Cliente.FIELD_idprovcli + " in ('" + id_client + "')";
        Cursor mCursor = db.rawQuery(query, null);

        String dir = "";

        if (mCursor.moveToFirst()) {
            do {
                dir = mCursor.getString(mCursor.getColumnIndex(Cliente.FIELD_direccion1));
                if (dir == null) {
                    dir = mCursor.getString(mCursor.getColumnIndex(Cliente.FIELD_direccion2));
                    if (dir == null) {
                        dir = "SD";
                    }
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return dir;
    }

    public boolean actualizarEstadoTransaccion(String referencia, int estado) {
        db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Transaccion.FIELD_estado, estado);

        db.update(Transaccion.TABLE_NAME, content, Transaccion.FIELD_referencia + " in ('" + referencia + "')", null);
        return true;
    }

    public boolean buscarTransaccionPorReferencia(String referencia) {
        db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + Transaccion.TABLE_NAME + " als WHERE als." + Transaccion.FIELD_referencia + "=?", new String[]{referencia});
        if (mCursor.getCount() > 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }
}
