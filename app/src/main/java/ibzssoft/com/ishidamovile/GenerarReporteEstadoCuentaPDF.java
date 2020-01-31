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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.GNTrans;
import ibzssoft.com.modelo.IVInventario;
import ibzssoft.com.modelo.IVKardex;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by Eduardo
 **/
public class GenerarReporteEstadoCuentaPDF {
    private SimpleDateFormat sdf;
    private Cliente cliente;
    private String nombreempresa;
    private String logoempresa;
    private String direccionempresa;
    private String telefonoempresa;
    private String impuestos;
    private Context context;
    private String id_cliente,tipo_trans;
    private int obs=0;
    private int precio=0;
    private int filas=0;
    private int[]iva;
    private double [] totales;
    private static final int LIMITE=50;
    private double PORC_IVA=0.0;
    //totales
    private String subtotal_12,subtotal_0,subtotal,total_iva,total_trans;
    public GenerarReporteEstadoCuentaPDF(Context context, String id_cliente) {
        this.context=context;
        this.id_cliente=id_cliente;
        this.tipo_trans="ESTADO DE CUENTA";
        this.nombreempresa="";this.direccionempresa="";this.telefonoempresa="";logoempresa="";this.totales=new double[LIMITE];
        this.subtotal_12="";this.subtotal_0="";this.total_iva="";this.total_trans="";
        this.iva=new int[LIMITE];
        this.cargarPreferenciasEmpresa();
        this.PORC_IVA=Double.parseDouble(impuestos)/100;
        this.cliente=loadCliente();
        //this.cargarPrecio(this.cliente.getIdprovcli(), this.transaccion.getIdentificador().split("-")[0]);
    }
    public void ejecutarProceso(){
        GenerarPDFTask task = new GenerarPDFTask();
        task.execute();
    }
    public void cargarPreferenciasEmpresa(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        nombreempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_nombre),context.getString(R.string.pref_nombre_empresa));
        direccionempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_direccion),context.getString(R.string.pref_direccion_empresa));
        telefonoempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_telefono),context.getString(R.string.pref_telefono_empresa));
        logoempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_logo),context.getString(R.string.pref_logo_empresa));
        impuestos=extraerConfiguraciones.get(context.getString(R.string.key_empresa_iva),context.getString(R.string.pref_iva_empresa));
    }

    public Cliente loadCliente(){
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor=helper.obtenerCliente(this.id_cliente);
        Cliente cli= null;
        if(cursor.moveToFirst()){
        cli= new Cliente(
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

    public boolean cargarPrecio(String id_cliente,String id_gntrans){
        int numGrupo=0;
        try {
            DBSistemaGestion helper= new DBSistemaGestion(context);
            ExtraerConfiguraciones ext = new ExtraerConfiguraciones(context);
            numGrupo = ext.configuracionPreciosGrupos();
            Cursor cursor1= helper.consultarPCGrupo(numGrupo,id_cliente);
            if(cursor1.moveToFirst()){
                precio=cursor1.getInt(1);
            }
            cursor1.close();
            helper.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public PdfPTable crearEncabezado(){
        PdfPTable tablapedido=new PdfPTable(8);
        tablapedido.setWidthPercentage(100);
        PdfPCell celda = new PdfPCell();
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        celda.setPadding(10);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setColspan(4);
        try
        {
            Image foto = Image.getInstance(logoempresa);
            foto.scaleToFit(180, 180);
            foto.setAlignment(Chunk.ALIGN_LEFT);
            Chunk chunk = new Chunk(foto,0,-25);
            Phrase severityChunk = new Phrase(chunk);
            celda.addElement(severityChunk);
        }
        catch ( Exception e )
        {
            Toast toast = Toast.makeText(context, R.string.error_no_load_file, Toast.LENGTH_LONG);
            toast.show();
        }
        tablapedido.addCell(celda);

        //columna 2
        Font font = FontFactory.getFont("arial", 14, Font.BOLD,BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK);
        Font font3 = FontFactory.getFont("arial", 10, Font.NORMAL,BaseColor.BLACK);


        Chunk tit = new Chunk("REPORTE - "+tipo_trans.toUpperCase(),font);
        Chunk head_fecha = new Chunk("Fecha de Emision: ",font2);
        Chunk conten_fecha = new Chunk(new ParseDates().changeDateToStringSimpleNow(),font3);

        Paragraph p=new Paragraph(tit);
        Paragraph p1=new Paragraph();
        p1.add(head_fecha);
        p1.add(conten_fecha);
        celda = new PdfPCell();
        celda.setBorderColor(BaseColor.WHITE);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        celda.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
        celda.addElement(p);
        celda.addElement(p1);
        celda.addElement(new Paragraph(" "));
        celda.setColspan(4);
        tablapedido.addCell(celda);
        return tablapedido;
    }


        public PdfPTable crearInfoEmpresa(){
        PdfPTable tablapedido=new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        Font font = FontFactory.getFont("arial", 14, Font.BOLD,BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK);
        Font font21 = FontFactory.getFont("arial", 10, Font.NORMAL,BaseColor.BLACK);


        Chunk tit = new Chunk(nombreempresa.toUpperCase(),font);
        Chunk head_dir = new Chunk("Dir. ",font2);
        Chunk chunk_dir = new Chunk(direccionempresa.toUpperCase(),font21);

        Chunk head_tel = new Chunk("Tel. ",font2);
        Chunk chunk_tel = new Chunk(telefonoempresa.toUpperCase(),font21);

        Chunk head_nombre= new Chunk("Cliente. ",font2);
        Chunk chunk_nombre= new Chunk(cliente.getNombre().toUpperCase(),font21);

        Chunk head_ruc= new Chunk("RUC/CI. ",font2);
        Chunk chunk_ruc= new Chunk(cliente.getRuc(),font21);

        Chunk head_comercial= new Chunk("Nombre Comercial. ",font2);
        Chunk chunk_comercial= new Chunk(cliente.getNombrealterno().toUpperCase(),font21);

        Chunk head_direc= new Chunk("Direccion. ",font2);
        Chunk chunk_direc= new Chunk(cliente.getDireccion1(),font21);

        Chunk head_tele= new Chunk("Telefono. ",font2);
        Chunk chunk_tele= new Chunk(cliente.getTelefono1(),font21);

        Chunk head_mail= new Chunk("E-Mail. ",font2);
        Chunk chunk_mail= new Chunk(cliente.getEmail(),font21);

        //Parrafos Columna cliente
        Paragraph par2=new Paragraph();
        par2.add(head_ruc);
        par2.add(chunk_ruc);
        Paragraph par1=new Paragraph();
        par1.add(head_nombre);
        par1.add(chunk_nombre);
        Paragraph par3=new Paragraph();
        par3.add(head_comercial);
        par3.add(chunk_comercial);
        Paragraph par4=new Paragraph();
        par4.add(head_direc);
        par4.add(chunk_direc);
        Paragraph par5=new Paragraph();
        par5.add(head_tele);
        par5.add(chunk_tele);
        Paragraph par6=new Paragraph();
        par6.add(head_mail);
        par6.add(chunk_mail);
        //parrafo titulo empresa
        Paragraph p=new Paragraph(tit);
        //parrafo direccion empresa
        Paragraph p1=new Paragraph();
        p1.add(head_dir);
        p1.add(chunk_dir);
        //parrafo telefono empresa
        Paragraph p2=new Paragraph();
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
    public PdfPTable crearInfoOpcional(){
        //observacion,solitante,tiempo entrega,forma,validez,atencion,ruc, razon social, nombre comercial, direccion,telefono
        PdfPTable tablapedido=new PdfPTable(8);
        tablapedido.setWidthPercentage(100);

        Font font = FontFactory.getFont("arial", 14, Font.BOLD,BaseColor.BLACK);
        Font font2 = FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK);
        Font font21 = FontFactory.getFont("arial", 10, Font.NORMAL,BaseColor.BLACK);


        Chunk tit = new Chunk(nombreempresa.toUpperCase(),font);
        Chunk head_dir = new Chunk("Dir. ",font2);
        Chunk chunk_dir = new Chunk(direccionempresa.toUpperCase(),font21);

        Chunk head_tel = new Chunk("Tel. ",font2);
        Chunk chunk_tel = new Chunk(telefonoempresa.toUpperCase(),font21);

        Chunk head_nombre= new Chunk("Cliente. ",font2);
        Chunk chunk_nombre= new Chunk(this.cliente.getNombre(),font21);

        Chunk head_ruc= new Chunk("RUC/CI. ",font2);
        Chunk chunk_ruc= new Chunk(this.cliente.getRuc(),font21);

        Chunk head_comercial= new Chunk("Nombre Comercial. ",font2);
        Chunk chunk_comercial= new Chunk(this.cliente.getNombrealterno(),font21);

        Chunk head_direc= new Chunk("Direccion. ",font2);
        Chunk chunk_direc= new Chunk(this.cliente.getDireccion1(),font21);

        Chunk head_tele= new Chunk("Telefono. ",font2);
        Chunk chunk_tele= new Chunk(this.cliente.getTelefono1(),font21);

        Chunk head_mail= new Chunk("E-Mail. ",font2);
        Chunk chunk_mail= new Chunk(this.cliente.getEmail(),font21);

        //Parrafos Columna cliente
        Paragraph par2=new Paragraph();
        par2.add(head_ruc);
        par2.add(chunk_ruc);
        Paragraph par1=new Paragraph();
        par1.add(head_nombre);
        par1.add(chunk_nombre);
        Paragraph par3=new Paragraph();
        par3.add(head_comercial);
        par3.add(chunk_comercial);
        Paragraph par4=new Paragraph();
        par4.add(head_direc);
        par4.add(chunk_direc);
        Paragraph par5=new Paragraph();
        par5.add(head_tele);
        par5.add(chunk_tele);
        Paragraph par6=new Paragraph();
        par6.add(head_mail);
        par6.add(chunk_mail);
        //parrafo titulo empresa
        Paragraph p=new Paragraph(tit);
        //parrafo direccion empresa
        Paragraph p1=new Paragraph();
        p1.add(head_dir);
        p1.add(chunk_dir);
        //parrafo telefono empresa
        Paragraph p2=new Paragraph();
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
        this.sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String  directorio = "/sdcard/"+context.getResources().getString(R.string.title_folder_root)+"/";
        OutputStream file = new FileOutputStream(new File(directorio + "estado_cuenta_"+this.cliente.getRuc()+".pdf"));
        Document document = new Document();
        PdfWriter.getInstance(document, file);
        document.open();
        document.add(crearEncabezado());
        document.add(crearInfoEmpresa());
        document.add(new Paragraph(" "));
        document.add(agregarDetalles());
        document.add(agregarResultadosOtros());
        document.close();
        file.close();
    }

    public PdfPTable agregarDetalles(){
        PdfPTable tablapedido=new PdfPTable(9);
        tablapedido.setWidthPercentage(100);
        //fila 2
        PdfPCell celda = new PdfPCell(new Paragraph ("DOCUMENTO",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("F.EMI",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("PLAZO",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("F.VENCI",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPaddingLeft(10);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("V.FACT",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("V.CAN.",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("SxVENC",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("S.VENCI",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph ("D.V",FontFactory.getFont("arial", 10, Font.BOLD,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderColorLeft(BaseColor.WHITE);
        celda.setBorderColorRight(BaseColor.WHITE);
        tablapedido.addCell(celda);

        /*Cargar Detalles de cartera*/
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cursor=helper.consultarCarteraPersonal(this.id_cliente);
        this.filas=cursor.getCount();
        int pst=0;
        if(cursor.moveToFirst()){
            do{


                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimple(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_fechaemision)))),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_plazo)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (new ParseDates().changeDateToStringSimple1(new ParseDates().changeStringToDateSimple(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_fechavenci)))),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                //precio unitario

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_valor)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_pagado)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_saldoxvence)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_saldovencido)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                celda = new PdfPCell(new Paragraph (cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_dvencidos)),FontFactory.getFont("arial",9,Font.NORMAL,BaseColor.BLACK)));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPaddingLeft(10);
                celda.setBorderColorLeft(BaseColor.WHITE);
                celda.setBorderColorRight(BaseColor.WHITE);
                celda.setBorderColorBottom(BaseColor.WHITE);
                celda.setBorderColorTop(BaseColor.WHITE);
                tablapedido.addCell(celda);

                pst++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        helper.close();
        return tablapedido;
    }

    public PdfPTable agregarResultadosOtros(){
        DBSistemaGestion helper= new DBSistemaGestion(context);
        Cursor cursor=helper.consultarCarteraPersonal(this.id_cliente);
        this.filas=cursor.getCount();
        double vfact=0.0;
        double vcan=0.0;
        double sxvencer=0.0;
        double svenci=0.0;
        if(cursor.moveToFirst()) {
            do {
                vfact += cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor));
                vcan += cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_pagado));
                sxvencer += cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_saldoxvence));
                svenci += cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_saldovencido));
            }while (cursor.moveToNext());
        }
        PdfPTable tablapedido=new PdfPTable(9);
        tablapedido.setWidthPercentage(100);
        //FILA 1
        PdfPCell celda = new PdfPCell(new Paragraph(" ",FontFactory.getFont("arial", 8,Font.BOLD,BaseColor.BLACK)));
        celda.setColspan(4);
        celda.setBackgroundColor(BaseColor.WHITE);
        celda.setBorderColor(BaseColor.WHITE);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph (redondearNumero(vfact),FontFactory.getFont("arial", 10,Font.NORMAL,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph (redondearNumero(vcan),FontFactory.getFont("arial", 10,Font.NORMAL,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph (redondearNumero(sxvencer),FontFactory.getFont("arial", 10,Font.NORMAL,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        celda = new PdfPCell(new Paragraph (redondearNumero(svenci) ,FontFactory.getFont("arial", 10,Font.NORMAL,BaseColor.BLACK)));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setColspan(1);
        tablapedido.addCell(celda);

        celda= new PdfPCell(new Paragraph(" "));
        celda.setColspan(9);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablapedido.addCell(celda);

        celda= new PdfPCell(new Paragraph(" "));
        celda.setColspan(9);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablapedido.addCell(celda);


        celda= new PdfPCell(new Paragraph(" "));
        celda.setColspan(5);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablapedido.addCell(celda);

        celda= new PdfPCell(new Paragraph("Generated: "+this.sdf.format(new Date()),FontFactory.getFont("arial", 10,Font.NORMAL,BaseColor.BLACK)));
        celda.setColspan(4);
        celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celda.setBorder(PdfPCell.NO_BORDER);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tablapedido.addCell(celda);
        return tablapedido;
    }

    //tarea asincrona
    class GenerarPDFTask extends AsyncTask<Void,Integer,Boolean>{
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Generando PDF");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    Toast ts= Toast.makeText(context, R.string.write_pdf_success,Toast.LENGTH_SHORT);
                    ts.show();
                    abrirPDF("estado_cuenta_"+cliente.getRuc()+".pdf");
                }else{
                    Toast ts= Toast.makeText(context,R.string.write_pdf_fail,Toast.LENGTH_SHORT);
                    ts.show();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                crearPdf();
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }
    public void abrirPDF(String archivo){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File("sdcard/"+context.getResources().getString(R.string.title_folder_root)+"/"+archivo)), "application/pdf");
        context.startActivity(intent);
    }


    public int obtenerNumeroPrecio(String inf_price){
        int result=0;
        for(int i=0 ;i<inf_price.length(); i++){
            if(inf_price.substring(i,inf_price.length()).equals("1")){
                result = i;
                break;
            }
        }
        return result+1;
    }
    public String redondearNumero(double numero){
        DecimalFormat formateador = new DecimalFormat("0.00");
        return formateador.format(numero).replace(",",".");
    }
}
