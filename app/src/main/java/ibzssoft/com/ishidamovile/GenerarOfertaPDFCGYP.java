package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;


public class GenerarOfertaPDFCGYP {
    private SimpleDateFormat sdf;
    private Transaccion transaccion;
    private Cliente cliente;
    private String nombreempresa;
    private String logoempresa;
    private String direccionempresa;
    private String telefonoempresa;
    private String impuestos;
    private Context context;
    private String id_trans, tipo_trans;
    private int obs = 0;
    private int precio = 0;
    private int filas = 0;
    private int[] iva;
    private double[] totales;
    private double[] descuentos;
    private static final int LIMITE = 50;
    private double PORC_IVA = 0.0;
    private String numdec_ptotal, numdec_punitario;

    //totales
    private String subtotal_12, subtotal_0, descuento_trans, subtotal, total_iva, total_trans;
    private String forma_pago, atencion, observacion;
    private int validez, tiempo_entrega;
    private String nombres_vendedor;
    private String fecha_trans, fecha_validez;


    public GenerarOfertaPDFCGYP(Context context, String id_trans) {
        this.context = context;
        this.id_trans = id_trans;
        this.tipo_trans = "PROFORMA";
        this.nombreempresa = "";
        this.direccionempresa = "";
        this.telefonoempresa = "";
        logoempresa = "";
        this.totales = new double[LIMITE];
        this.descuentos = new double[LIMITE];
        this.iva = new int[LIMITE];

        this.subtotal_12 = "";
        this.subtotal_0 = "";
        this.total_iva = "";
        this.total_trans = "";

        this.cargarPreferenciasEmpresa();
        this.cargarPreferencias();
        this.PORC_IVA = Double.parseDouble(impuestos) / 100;
        this.transaccion = loadTransaccion();
        this.cliente = loadCliente();
        this.cargarPrecio(this.cliente.getIdprovcli(), this.transaccion.getIdentificador().split("-")[0]);
    }

    public void ejecutarProceso() {
        GenerarPDFTask task = new GenerarPDFTask();
        task.execute();
    }

    public void cargarPreferencias() {
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(context);
        this.numdec_punitario = ext.get(context.getString(R.string.key_act_num_dec_punitario), "0.00");
        this.numdec_ptotal = ext.get(context.getString(R.string.key_act_num_dec_ptotal), "0.00");
    }

    public void cargarPreferenciasEmpresa() {
        ExtraerConfiguraciones extraerConfiguraciones = new ExtraerConfiguraciones(context);
        nombreempresa = extraerConfiguraciones.get(context.getString(R.string.key_empresa_nombre), context.getString(R.string.pref_nombre_empresa));
        direccionempresa = extraerConfiguraciones.get(context.getString(R.string.key_empresa_direccion), context.getString(R.string.pref_direccion_empresa));
        telefonoempresa = extraerConfiguraciones.get(context.getString(R.string.key_empresa_telefono), context.getString(R.string.pref_telefono_empresa));
        logoempresa = extraerConfiguraciones.get(context.getString(R.string.key_empresa_logo), context.getString(R.string.pref_logo_empresa));
        impuestos = extraerConfiguraciones.get(context.getString(R.string.key_empresa_iva), context.getString(R.string.pref_iva_empresa));
    }

    public Transaccion loadTransaccion() {
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cur = helper.obtenerTransaccion(id_trans);
        Transaccion trans = null;
        if (cur.moveToFirst()) {
            trans = new Transaccion(
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_descripcion)),
                    cur.getInt(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_trans)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_hora_trans)),
                    cur.getInt(cur.getColumnIndex(Transaccion.FIELD_band_enviado)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_vendedor_id)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_cliente_id)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_forma_cobro_id)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_referencia)),
                    cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_grabado)));

            //Se obtiene la fecha de la transacción
            String fecha_transaccion = cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_trans));
            fecha_trans = convertirDateToString(convertirStringToDate(fecha_transaccion, "yyyy-MM-dd"), "dd/MM/yyyy");

            //Se obtiene el nombre del vendedor
            nombres_vendedor = cur.getString(cur.getColumnIndex(Vendedor.FIELD_nombre));

            //Se obtienen campos de las observaciones
            //observacion,solitante,tiempo entrega,forma,validez,atencion,ruc, razon social, nombre comercial, direccion,telefono
            String[] adjuntos = cur.getString(cur.getColumnIndex(Transaccion.FIELD_descripcion)).split(";");
            observacion = adjuntos[0].toUpperCase();
            forma_pago = adjuntos[3].toUpperCase();
            atencion = adjuntos[1].toUpperCase();
            validez = Integer.parseInt(adjuntos[4]);
            tiempo_entrega = Integer.parseInt(adjuntos[2]);

            Date nueva_fecha = sumarRestarDiasFecha(convertirStringToDate(fecha_trans, "dd/MM/yyyy"), validez);
            fecha_validez = convertirDateToString(nueva_fecha, "dd/MM/yyyy");

            //loading opciones de precios y observaciones
            Cursor cursor1 = helper.consultarGNTrans(cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador)).split("-")[0]);
            if (cursor1.moveToFirst()) {
                this.obs = cursor1.getInt(cursor1.getColumnIndex(GNTrans.FIELD_bandobservacion));
            }
            cursor1.close();
        }
        cur.close();
        helper.close();
        return trans;
    }

    public String convertirDateToString(Date fecha, String formato) {
        DateFormat dateFormat = new SimpleDateFormat(formato);

        return dateFormat.format(fecha);
    }

    public Date convertirStringToDate(String fecha, String formato_fecha) {
        SimpleDateFormat formato = new SimpleDateFormat(formato_fecha);
        return formato.parse(fecha, new ParsePosition(0));
    }

    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

    }

    public Cliente loadCliente() {
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor = helper.obtenerCliente(this.transaccion.getCliente_id());
        Cliente cli = null;
        if (cursor.moveToFirst()) {
            cli = new Cliente(
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovcli)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion1)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion2)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono1)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono2)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fax)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_email)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_banco)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numcuenta)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direcbanco)),
                    cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_bandproveedor)),
                    cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_bandcliente)),
                    cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_estado)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idparroquia)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo1)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo2)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo3)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idgrupo4)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idvendedor)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idcobrador)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idprovincia)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_idcanton)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_diasplazo)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_observacion)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numprecio)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numserie)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono3)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_posgooglemaps)),
                    cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fechagrabado)));
        }
        helper.close();
        return cli;
    }

    public boolean cargarPrecio(String id_cliente, String id_gntrans) {
        int numGrupo = 0;
        try {
            DBSistemaGestion helper = new DBSistemaGestion(context);
            ExtraerConfiguraciones ext = new ExtraerConfiguraciones(context);
            numGrupo = ext.configuracionPreciosGrupos();
            Cursor cursor1 = helper.consultarPCGrupo(numGrupo, id_cliente);
            if (cursor1.moveToFirst()) {
                precio = cursor1.getInt(1);
            }
            cursor1.close();
            helper.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public PdfPTable crearEncabezadoP1() {

        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        PdfPCell celda1 = new PdfPCell();
        celda1.setBackgroundColor(BaseColor.WHITE);
        celda1.setBorderColor(BaseColor.WHITE);
        celda1.setPadding(10);
//        celda1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        celda1.setVerticalAlignment(Element.ALIGN_TOP);
        celda1.setColspan(5);
        tablapedido.addCell(celda1);

        PdfPCell celda2 = new PdfPCell();
        celda2.setBackgroundColor(BaseColor.WHITE);
        celda2.setBorderColor(BaseColor.WHITE);
        celda1.setPadding(10);
//        celda1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        celda1.setVerticalAlignment(Element.ALIGN_TOP);
        celda2.setColspan(3);

        try {
            Image foto = Image.getInstance(logoempresa);
            foto.scaleToFit(180, 180);
            foto.setAlignment(Chunk.ALIGN_RIGHT);

            Chunk chunk = new Chunk(foto, 0, -25);
            Phrase severityChunk = new Phrase(chunk);
            celda2.addElement(severityChunk);
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, R.string.error_no_load_file, Toast.LENGTH_LONG);
            toast.show();
        }
        tablapedido.addCell(celda2);

        return tablapedido;
    }

    public PdfPTable crearEncabezadoP2() {

        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);


        //Fila 2
        Font font = FontFactory.getFont("arial", 16, Font.BOLD, BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK);
        Font font3 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);


        Chunk nombre_transaccion = new Chunk(tipo_trans.toUpperCase(), font);
        Chunk texto_contrib_especial = new Chunk("CONTRIBUYENTES ESPECIALES", font2);
        Chunk texto_resolucion = new Chunk("Resolución No.870 del 03/Sep/2007", font3);

        Paragraph p1 = new Paragraph(nombre_transaccion);
//        p1.setAlignment(PdfPCell.ALIGN_CENTER);
        Paragraph p2 = new Paragraph(texto_contrib_especial);
//        p1.setAlignment(PdfPCell.ALIGN_RIGHT);
        Paragraph p3 = new Paragraph(texto_resolucion);
//        p1.setAlignment(PdfPCell.ALIGN_RIGHT);

        PdfPCell celda1 = new PdfPCell();
        celda1.setBorderColor(BaseColor.WHITE);
        celda1.setBorder(PdfPCell.NO_BORDER);
        celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        celda1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        celda1.addElement(p1);
        celda1.setColspan(5);
        tablapedido.addCell(celda1);

        PdfPCell celda2 = new PdfPCell();
        celda2.setBorderColor(BaseColor.WHITE);
        celda2.setBorder(PdfPCell.NO_BORDER);
//        celda2.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//        celda2.setVerticalAlignment(PdfPCell.ALIGN_TOP);
        celda2.addElement(p2);
        celda2.addElement(p3);
        celda2.setColspan(3);
        tablapedido.addCell(celda2);


        return tablapedido;
    }

    public PdfPTable agregarTextoInformativoP1() {

        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        Font font2 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);
        Font font21 = FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK);

        Chunk texto_1 = new Chunk("De mi consideración:", font2);
        Chunk texto_2 = new Chunk("A continuación me es grato presentar la oferta de productos y/o servicios, solicitados por usted:", font21);

        Paragraph par1 = new Paragraph();
        par1.add(texto_1);

        Paragraph par2 = new Paragraph();
        par2.add(texto_2);

        /** Fila 1 **/
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(8);
        celda.addElement(par1);


        tablapedido.addCell(celda);

        /** Fila 2 **/
        celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(8);
        celda.addElement(par2);

        tablapedido.addCell(celda);

        return tablapedido;
    }


    public PdfPTable crearInfoEmpresa() {
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);


        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK);
        Font font21 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);


        Chunk head_texto_trans = new Chunk(tipo_trans.toUpperCase() + " No. ", font2);
        Chunk chunk_texto_trans = new Chunk(String.valueOf(transaccion.getNumTransaccion()), font21);

        Chunk head_fecha = new Chunk("FECHA            ", font2);
        Chunk chunk_fecha = new Chunk(fecha_trans, font21);

        Chunk head_nombre_cliente = new Chunk("CLIENTE         ", font2);
        Chunk chunk_nombre_cliente = new Chunk(cliente.getNombre().toUpperCase(), font21);

        Chunk head_direc = new Chunk("DIRECCION    ", font2);
        Chunk chunk_direc = new Chunk(cliente.getDireccion1(), font21);

        Chunk head_comercial = new Chunk("ATENCION    ", font2);
        Chunk chunk_comercial = new Chunk(atencion, font21); //CAMBIAR POR OBS

        Chunk head_ruc = new Chunk("RUC               ", font2);
        Chunk chunk_ruc = new Chunk(cliente.getRuc(), font21);

        Chunk head_telefono_cli = new Chunk("TELEFONO   ", font2);
        Chunk chunk_telefono_cli = new Chunk(cliente.getTelefono1(), font21);

        Chunk head_vendedor = new Chunk("VENDEDOR   ", font2);
        Chunk chunk_vendedor = new Chunk(nombres_vendedor, font21);

        //Parrafos columna 1
        Paragraph par1 = new Paragraph();
        par1.add(head_texto_trans);
        par1.add(chunk_texto_trans);

        Paragraph par2 = new Paragraph();
        par2.add(head_fecha);
        par2.add(chunk_fecha);

        Paragraph par3 = new Paragraph();
        par3.add(head_nombre_cliente);
        par3.add(chunk_nombre_cliente);

        Paragraph par4 = new Paragraph();
        par4.add(head_direc);
        par4.add(chunk_direc);

        //Parrafos columna 2
        Paragraph par5 = new Paragraph();
        par5.add(head_comercial);
        par5.add(chunk_comercial);

        Paragraph par6 = new Paragraph();
        par6.add(head_ruc);
        par6.add(chunk_ruc);

        Paragraph par7 = new Paragraph();
        par7.add(head_telefono_cli);
        par7.add(chunk_telefono_cli);

        Paragraph par8 = new Paragraph();
        par8.add(head_vendedor);
        par8.add(chunk_vendedor);

        /*Columnna 1*/
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(4);
        celda.addElement(par1);
        celda.addElement(par2);
        celda.addElement(par3);
        celda.addElement(par4);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);

        tablapedido.addCell(celda);

        /*Columnna 2*/
        celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(4);
        celda.addElement(par5);
        celda.addElement(par6);
        celda.addElement(par7);
        celda.addElement(par8);

        tablapedido.addCell(celda);

        return tablapedido;
    }

    public PdfPTable crearInfoOpcional() {
        //observacion,solitante,tiempo entrega,forma,validez,atencion,ruc, razon social, nombre comercial, direccion,telefono
        String[] adjuntos = transaccion.getDescripcion().split(";");
        for (int i = 0; i < adjuntos.length; i++) {
            System.out.println("Adjuntos: " + adjuntos[i]);
        }
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        Font font = FontFactory.getFont("arial", 14, Font.BOLD, BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK);
        Font font21 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);


        Chunk tit = new Chunk(nombreempresa.toUpperCase(), font);
        Chunk head_dir = new Chunk("Dir. ", font2);
        Chunk chunk_dir = new Chunk(direccionempresa.toUpperCase(), font21);

        Chunk head_tel = new Chunk("Tel. ", font2);
        Chunk chunk_tel = new Chunk(telefonoempresa.toUpperCase(), font21);

        Chunk head_nombre = new Chunk("Cliente. ", font2);
        Chunk chunk_nombre = new Chunk(adjuntos[6].toUpperCase(), font21);

        Chunk head_ruc = new Chunk("RUC/CI. ", font2);
        Chunk chunk_ruc = new Chunk(adjuntos[5], font21);

        Chunk head_comercial = new Chunk("Nombre Comercial. ", font2);
        Chunk chunk_comercial = new Chunk(adjuntos[7].toUpperCase(), font21);

        Chunk head_direc = new Chunk("Direccion. ", font2);
        Chunk chunk_direc = new Chunk(adjuntos[8].toUpperCase(), font21);

        Chunk head_tele = new Chunk("Telefono. ", font2);
        Chunk chunk_tele = new Chunk(adjuntos[9].toUpperCase(), font21);

        Chunk head_mail = new Chunk("E-Mail. ", font2);
        Chunk chunk_mail = new Chunk(cliente.getEmail(), font21);

        //Parrafos Columna cliente
        Paragraph par2 = new Paragraph();
        par2.add(head_ruc);
        par2.add(chunk_ruc);
        Paragraph par1 = new Paragraph();
        par1.add(head_nombre);
        par1.add(chunk_nombre);
        Paragraph par3 = new Paragraph();
        par3.add(head_comercial);
        par3.add(chunk_comercial);
        Paragraph par4 = new Paragraph();
        par4.add(head_direc);
        par4.add(chunk_direc);
        Paragraph par5 = new Paragraph();
        par5.add(head_tele);
        par5.add(chunk_tele);
        Paragraph par6 = new Paragraph();
        par6.add(head_mail);
        par6.add(chunk_mail);
        //parrafo titulo empresa
        Paragraph p = new Paragraph(tit);
        //parrafo direccion empresa
        Paragraph p1 = new Paragraph();
        p1.add(head_dir);
        p1.add(chunk_dir);
        //parrafo telefono empresa
        Paragraph p2 = new Paragraph();
        p2.add(head_tel);
        p2.add(chunk_tel);

        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(4);
        celda.addElement(new Paragraph(" "));
        celda.addElement(p);
        celda.addElement(p1);
        celda.addElement(p2);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablapedido.addCell(celda);

        /*Columnna 2*/
        celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.addElement(par2);
        celda.addElement(par1);
        celda.addElement(par3);
        celda.addElement(par4);
        celda.addElement(par5);
        celda.addElement(par6);
        celda.setColspan(4);
        celda.setPaddingLeft(15);
        celda.setPaddingBottom(10);
        celda.setBorderColor(BaseColor.LIGHT_GRAY);
        tablapedido.addCell(celda);
        return tablapedido;
    }

    public void crearPdf() throws DocumentException, IOException {
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String directorio = "/sdcard/" + context.getResources().getString(R.string.title_folder_root) + "/";
        OutputStream file = new FileOutputStream(new File(directorio + transaccion.getIdentificador() + "_" + transaccion.getNumTransaccion() + ".pdf"));
        Document document = new Document();
        PdfWriter.getInstance(document, file);
        document.open();

        document.add(agregarCabecera());
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        if (this.cliente.getRuc().equals("9999999999999")) {
            document.add(crearInfoOpcional());
        } else {
            document.add(agregarInformacionClienteTrans());
        }

        document.add(agregarTextoInformativoP1());

        document.add(new Paragraph(" "));
        document.add(agregarFilaDetalle());
        document.add(new Paragraph(" "));
        document.add(agregarDetalles());
        document.add(agregarResultadosOferta());
        document.close();
        file.close();
    }

    public PdfPTable agregarInformacionClienteTrans() {
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        Font font1 = FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK);

        //Fila 1
        PdfPCell celda = new PdfPCell(new Paragraph("PROF. No.", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(String.valueOf(transaccion.getNumTransaccion()), font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("ATENCION", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(atencion, font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        //Fila 2
        celda = new PdfPCell(new Paragraph("FECHA", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(fecha_trans, font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("RUC", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(cliente.getRuc(), font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);


        //Fila 3
        celda = new PdfPCell(new Paragraph("CLIENTE", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(cliente.getNombre().toUpperCase(), font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("TELEFONO", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(cliente.getTelefono1(), font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);


        //Fila 4
        celda = new PdfPCell(new Paragraph("DIRECCION", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(cliente.getDireccion1(), font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("VENDEDOR", font1));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(1);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(nombres_vendedor, font2));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setColspan(3);
        tablapedido.addCell(celda);


        return tablapedido;
    }

    public PdfPTable agregarFilaDetalle() {
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);
        //fila 2
        PdfPCell celda = new PdfPCell(new Paragraph("Detalle", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        celda.setColspan(8);

        tablapedido.addCell(celda);
        return tablapedido;
    }

    public PdfPTable agregarCabecera() {
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);
        //fila 1
        PdfPCell celda = new PdfPCell(new Paragraph("PROFORMA", FontFactory.getFont("arial", 14, Font.BOLD, BaseColor.BLACK)));
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setColspan(5);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tablapedido.addCell(celda);

        PdfPCell celda2 = new PdfPCell();
        celda2.setBorder(PdfPCell.NO_BORDER);
        celda2.setColspan(3);


        try {
            Image foto = Image.getInstance(logoempresa);
            foto.scaleToFit(200, 200);
            foto.setAlignment(Chunk.ALIGN_RIGHT);

            Chunk chunk = new Chunk(foto, 0, -25);
            Phrase severityChunk = new Phrase(chunk);
            celda2.addElement(severityChunk);
        } catch (Exception e) {
            Toast toast = Toast.makeText(context, R.string.error_no_load_file, Toast.LENGTH_LONG);
            toast.show();
        }
        tablapedido.addCell(celda2);

        return tablapedido;
    }

    public PdfPTable agregarDetalles() {
        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);
        //fila 2
        PdfPCell celda = new PdfPCell(new Paragraph("Código", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("Descripción", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        celda.setColspan(3);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("Cant.", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("P.U.", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("% Desc.", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);


        celda = new PdfPCell(new Paragraph("P.T.", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor = helper.consultarIVKardex(transaccion.getId_trans());
        this.filas = cursor.getCount();
        int pst = 0;
        if (cursor.moveToFirst()) {
            do {
                boolean bandpro = false;
                String idpadres = cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_padre_id));
                int numprecio = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_num_precio));


                double preciounitario = 0.0;
                if (idpadres != null) bandpro = true;
                if (bandpro) {
                    preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_pre_real_total)), this.numdec_punitario));
                } else {
                    switch (numprecio) {
                        case 1:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio1)), this.numdec_punitario));
                            break;
                        case 2:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio2)), this.numdec_punitario));
                            break;
                        case 3:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio3)), this.numdec_punitario));
                            break;
                        case 4:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio4)), this.numdec_punitario));
                            break;
                        case 5:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio5)), this.numdec_punitario));
                            break;
                        case 6:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio6)), this.numdec_punitario));
                            break;
                        case 7:
                            preciounitario = Double.parseDouble(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVInventario.FIELD_precio7)), this.numdec_punitario));
                            break;
                    }
                }


                celda = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_cod_item)), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(IVInventario.FIELD_descripcion)), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setColspan(3);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph(cursor.getString(cursor.getColumnIndex(IVKardex.FIELD_cantidad)), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);


                //precio unitario

                celda = new PdfPCell(new Paragraph(redondearNumero(preciounitario, this.numdec_punitario), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                //descuento solicitado
                celda = new PdfPCell(new Paragraph(redondearNumero(cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)), "0.00"), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                double cnt = cursor.getInt(cursor.getColumnIndex(IVKardex.FIELD_cantidad));
                double precio = preciounitario;
                double porcentaje = cursor.getDouble(cursor.getColumnIndex(IVKardex.FIELD_desc_sol)) / 100;

                double precio_total = cnt * precio;
                double desc = precio_total * porcentaje;
                double precioreal = (precio_total - desc) / cnt;
                double subtotal = cnt * precioreal;

                System.out.println("Subtotal: " + subtotal);
                //porcentajes
                this.descuentos[pst] = desc;
                //totales
                this.totales[pst] = subtotal;
                celda = new PdfPCell(new Paragraph(redondearNumero(subtotal, this.numdec_ptotal), FontFactory.getFont("arial", 9, Font.NORMAL, BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);
                //cargar iva
                this.iva[pst] = cursor.getInt(cursor.getColumnIndex(IVInventario.FIELD_band_iva));

                pst++;
            } while (cursor.moveToNext());
        }

//        celda = new PdfPCell(new Paragraph(" "));
//        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        celda.setBorderColor(BaseColor.WHITE);
//        celda.setColspan(8);
//        tablapedido.addCell(celda);
//
//        celda = new PdfPCell(new Paragraph(" "));
//        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
//        celda.setBorderColor(BaseColor.WHITE);
//        celda.setColspan(8);
//        tablapedido.addCell(celda);

        return tablapedido;
    }


    public PdfPTable agregarResultadosOferta() {
        cargarTotales();

        PdfPTable tablapedido = new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        //FILA 1

        PdfPCell celda = new PdfPCell(new Paragraph("Nuestras cuentas corrientes son: Bco.Pichincha #3168391004, Bco.Austro #0300181384, Bco.Bolivariano #4005022427", FontFactory.getFont("arial", 7, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(4);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(1);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("SUBTOTAL T0", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.subtotal_0, FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        // FILA 2

        celda = new PdfPCell(new Paragraph("FECHA VALIDEZ", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);


        celda = new PdfPCell(new Paragraph(fecha_validez, FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(1);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("SUBTOTAL T12", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.subtotal_12, FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        //FILA 3

        celda = new PdfPCell(new Paragraph("TIEMPO DE ENTREGA", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(String.valueOf(tiempo_entrega) + " DIA(S)", FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(1);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("DESCUENTO", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.descuento_trans, FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        //FILA 4

        celda = new PdfPCell(new Paragraph("FORMA DE PAGO", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(forma_pago, FontFactory.getFont("arial", 8, Font.NORMAL, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(1);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("SUMAN", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.subtotal, FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        //FILA 5

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(5);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("IVA " + this.impuestos + "%", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.total_iva, FontFactory.getFont("arial", 10, Font.NORMAL, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        //FILA 6
        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(5);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph("TOTAL", FontFactory.getFont("arial", 8, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(2);
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph(this.total_trans, FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        // FILA 7

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(8);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablapedido.addCell(celda);

        // FILA 8

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(8);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablapedido.addCell(celda);

        // FILA 9

        celda = new PdfPCell(new Paragraph("Muy Atentamente,", FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK)));
        celda.setColspan(8);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablapedido.addCell(celda);

        // FILA 10

        celda = new PdfPCell(new Paragraph(" "));
        celda.setColspan(8);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablapedido.addCell(celda);

        // FILA 11
        Font font = FontFactory.getFont("arial", 10, Font.BOLD, BaseColor.BLACK);

        //Columna 1
        Paragraph p1 = new Paragraph("------------------------------------------------------", font);
        Paragraph p2 = new Paragraph(nombres_vendedor.toUpperCase(), font);
        Paragraph p3 = new Paragraph("VENDEDOR", font);

        //Columna 2
        Paragraph p4 = new Paragraph("------------------------------------------------------", font);
        Paragraph p5 = new Paragraph("CONFIRMA-COMPRA", font);
        Paragraph p6 = new Paragraph(this.cliente.getNombre().toUpperCase(), font);

        PdfPCell celda1 = new PdfPCell();
        celda1.setBorderColor(BaseColor.WHITE);
        celda1.setBorder(PdfPCell.NO_BORDER);
        celda1.addElement(p1);
        celda1.addElement(p2);
        celda1.addElement(p3);
        celda1.setColspan(4);
        tablapedido.addCell(celda1);


        PdfPCell celda2 = new PdfPCell();
        celda2.setBorderColor(BaseColor.WHITE);
        celda2.setBorder(PdfPCell.NO_BORDER);
        celda2.addElement(p4);
        celda2.addElement(p5);
        celda2.addElement(p6);
        celda2.setColspan(4);
        tablapedido.addCell(celda2);


        return tablapedido;
    }

    public void cargarTotales() {
        double subtotal12 = 0;
        double subtotal0 = 0;
        double impuestos = 0;
        double total = 0;
        double descuento_total = 0;

        for (int i = 0; i < filas; i++) {
            if (iva[i] != 0) {
                subtotal12 += totales[i];
            } else {
                subtotal0 += totales[i];
            }
            descuento_total += descuentos[i];
        }
        subtotal_12 = redondearNumero(subtotal12, "0.00");
        subtotal_0 = redondearNumero(subtotal0, "0.00");
        subtotal = redondearNumero((subtotal0 + subtotal12), "0.00");
        impuestos = (subtotal12 * PORC_IVA);
        total = (subtotal0 + subtotal12 + impuestos);
        descuento_trans = redondearNumero(descuento_total, "0.00");
        total_iva = redondearNumero(impuestos, "0.00");
        total_trans = redondearNumero(total, "0.00");
    }

    //tarea asincrona
    class GenerarPDFTask extends AsyncTask<Void, Integer, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Generando PDF");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress.isShowing()) {
                progress.dismiss();
                if (aBoolean) {
                    Toast ts = Toast.makeText(context, R.string.write_pdf_success, Toast.LENGTH_SHORT);
                    ts.show();
                    abrirPDF(transaccion.getIdentificador() + "_" + transaccion.getNumTransaccion() + ".pdf");
                } else {
                    Toast ts = Toast.makeText(context, R.string.write_pdf_fail, Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                crearPdf();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void abrirPDF(String archivo) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File("sdcard/" + context.getResources().getString(R.string.title_folder_root) + "/" + archivo)), "application/pdf");
        context.startActivity(intent);
    }


    public int obtenerNumeroPrecio(String inf_price) {
        int result = 0;
        for (int i = 0; i < inf_price.length(); i++) {
            if (inf_price.substring(i, inf_price.length()).equals("1")) {
                result = i;
                break;
            }
        }
        return result + 1;
    }

    public String redondearNumero(double numero, String clave) {
        DecimalFormat formateador = new DecimalFormat(clave);
        return formateador.format(numero).replace(",", ".");
    }
}
