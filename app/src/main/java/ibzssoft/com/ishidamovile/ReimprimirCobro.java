package ibzssoft.com.ishidamovile;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.ParseDates;
import ibzssoft.com.ishidamovile.imprimir.PrinterCommands;
import ibzssoft.com.ishidamovile.imprimir.Utils;
import ibzssoft.com.ishidamovile.oferta.screens.CodigosOferta;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.PCKardex;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by Eduardo
 **/
public class ReimprimirCobro {
    /*
    Variables para imprimir
     */
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    //totales
    private String nombre_impresora,nombreempresa,direccionempresa,telefonoempresa,id_trans, nombrecliente, saldo_inicial, saldo_restante, total_cobro, codusuario;
    private Context context;
    private Transaccion transaccion;
    private String codemp;
    private String fecha, hora;
    public ReimprimirCobro(Context context, String id_trans) {
        this.context=context;
        this.id_trans=id_trans;
        this.nombreempresa="";this.direccionempresa="";this.telefonoempresa="";this.codusuario="";
        this.saldo_inicial="";this.saldo_restante="";this.total_cobro="";
        this.cargarPreferenciasEmpresa();
        this.transaccion= this.loadTransaccion();
        this.cargarSaldos();
    }
    public void ejecutarProceso(){
        ConectarImpresoraTask conectarImpresoraTask = new ConectarImpresoraTask();
        conectarImpresoraTask.execute();
    }
    public Transaccion loadTransaccion(){
        DBSistemaGestion helper = new DBSistemaGestion(context);
        Cursor cur=helper.obtenerTransaccion(id_trans);
        Transaccion trans= null;
        if(cur.moveToFirst()){
            trans= new Transaccion(
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
            this.nombrecliente = cur.getString(cur.getColumnIndex(Cliente.FIELD_nombre));
            this.codusuario= cur.getString(cur.getColumnIndex(Usuario.FIELD_codusuario));
            Date grabado = new ParseDates().changeStringToDateSimple(cur.getString(cur.getColumnIndex(Transaccion.FIELD_fecha_trans)));
            this.fecha = new ParseDates().changeDateToStringSimple1(grabado);
            this.hora = cur.getString(cur.getColumnIndex(Transaccion.FIELD_hora_trans));
        }
        cur.close();
        helper.close();
        return trans;
    }
    public void cargarSaldos(){
        try{
            String [] datos = this.transaccion.getDescripcion().split(";");
            this.saldo_inicial = datos[1];
            this.saldo_restante = datos[2];
            this.total_cobro = datos[3];
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void cargarPreferenciasEmpresa(){
        ExtraerConfiguraciones extraerConfiguraciones= new ExtraerConfiguraciones(context);
        nombreempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_nombre),context.getString(R.string.pref_nombre_empresa));
        direccionempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_direccion),context.getString(R.string.pref_direccion_empresa));
        telefonoempresa=extraerConfiguraciones.get(context.getString(R.string.key_empresa_telefono),context.getString(R.string.pref_telefono_empresa));
        nombre_impresora=extraerConfiguraciones.get(context.getString(R.string.key_nombre_impresora),"MTP-II");
        codemp=extraerConfiguraciones.get(context.getString(R.string.key_empresa_codigo),context.getString(R.string.pref_codigo_empresa_default));
    }
    /*Cargar metodos para la impresion*/
    void findBT() {
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter == null) {
                //impTrans.setText("Sin Conexion");
                System.out.println("Bluetooth desactivado");
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(context, "El dispositivo no tiene habilitada la Conexion Bluetooth", Toast.LENGTH_LONG).show();
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals(nombre_impresora)) {
                        mmDevice = device;
                        break;
                    }
                }
            }

            System.out.println("Bluetooth activado");
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void
    openBT() throws IOException {
        try {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();

            mmOutputStream = mmSocket.getOutputStream();
            byte[] format = { 27, 33, 0 };
            format[2] = ((byte)(0x5 | 0));
            mmOutputStream.write(format);
            printUnicode();
            //print normal text
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
            Toast.makeText(context, "Conexion Establecida", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void printPhoto() {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }*/
    public void printUnicode(){
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            workerThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                        try {
                            int bytesAvailable = mmInputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            public void run() {
                                                System.out.println("Listener: "+data);
                                            }
                                        });
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            stopWorker = true;
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendDataY() throws IOException {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
            String generado = sdf1.format(new Date());
            generarDetallesCobrados();
            String encabezado1 = "EMPRESA: "+this.nombreempresa+"\n";
            String encabezado2 = "DIR: "+this.direccionempresa + "\n"
                    + "CLIENTE: "+this.nombrecliente.toUpperCase() + "\n"
                    + "FECHA: "+this.fecha+" HORA: " +this.hora+ "\n";
                    printNewLine();
            String detalle1 = "------------------------------------" + "\n"
                            + "           REIMPRESION          " + "\n"
                            + "------------------------------------" + "\n"
                            + "TRANSACCION    CUOTA    PAGO    SALDO " + "\n"
                            + "------------------------------------" + "\n";
            printText(encabezado1);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(encabezado2);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(detalle1);
            String detalle2 = generarDetallesCobrados();
            String span1 = "TOTAL COBRADO: "; String detalle3 = total_cobro+ "\n";
            String span2 = "COBRADOR: ";String detalle4 = codusuario+ "\n";
            String span3 = "Generado: ";String detalle5 = generado+ "\n";

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(detalle2);
            printNewLine();

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            printText(span1);
            printText(detalle3);

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            printText(span2);
            printText(detalle4);

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(span3);
            printText(detalle5);

            printUnicode();
            printNewLine();
            printNewLine();
            this.closeBT();
        } catch (Exception e) {
            Toast.makeText(context, "No se puede completar la impresion del comprobante, revise que la impresora este encendida y que el dispositivo tenga activada la conexion bluetooth", Toast.LENGTH_LONG).show();
            reintentarImpresion();
        }
    }
    void sendDataB() throws IOException {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String generado = sdf1.format(new Date());
            generarDetallesCobrados();
            //imprimir cliente, fecha, nombre de la transaccion,transaccion,valor , saldo , valor cobrado
            String encabezado1 = "EMPRESA: "+this.nombreempresa+"\n";
            String encabezado2 = "DIR: "+this.direccionempresa + "\n"
                    + "CLIENTE: "+this.nombrecliente.toUpperCase() + "\n"
                    + "FECHA: "+this.fecha+" HORA: " +this.hora+ "\n";
            printNewLine();
            String detalle1 = "------------------------------------" + "\n"
                            + "           REIMPRESION          " + "\n"
                            + "------------------------------------" + "\n"
                            + "TRANSACCION    CUOTA    PAGO    SALDO " + "\n"
                            + "------------------------------------" + "\n";
            printText(encabezado1);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(encabezado2);
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(detalle1);
            String detalle2 = generarDetallesCobrados();
            String span1 = "SALDO INICIAL: "; String detalle3 = saldo_inicial+ "\n";
            String span2 = "SALDO RESTANTE: "; String detalle4 = saldo_restante+ "\n";
            String span3 = "TOTAL COBRADO: "; String detalle5 = total_cobro+ "\n";
            String span4 = "COBRADOR: ";String detalle6 = codusuario+ "\n";
            String span5 = "Generado: ";String detalle7 = generado+ "\n";

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            printText(detalle2);
            printNewLine();

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
            printText(span1);
            printText(detalle3);

            printText(span2);
            printText(detalle4);

            printText(span3);
            printText(detalle5);

            printText(span4);
            printText(detalle6);

            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(span5);
            printText(detalle7);

            printUnicode();
            printNewLine();
            printNewLine();
            this.closeBT();
        } catch (Exception e) {
            Toast.makeText(context, "No se puede completar la impresion del comprobante, revise que la impresora este encendida y que el dispositivo tenga activada la conexion bluetooth", Toast.LENGTH_LONG).show();
            reintentarImpresion();
        }
    }

    public void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.flush();
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String  generarDetallesCobrados(){
        String result="";
        DBSistemaGestion helper= new DBSistemaGestion(context);
        Cursor cursor=helper.consultarPCKardexTransaccion(id_trans);
            if(cursor.moveToFirst()){
                do{
                    result+=new Utils().completeChars(cursor.getString(cursor.getColumnIndex(PCKardex.FIELD_trans)),15)+"   "+
                            new Utils().completeChars(this.redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor))),5) +"   "+
                            new Utils().completeChars(this.redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_pagado))),5) +"   "+
                            new Utils().completeChars(this.redondearNumero(cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_valor))-cursor.getDouble(cursor.getColumnIndex(PCKardex.FIELD_pagado))),5) +"\n";
                }while (cursor.moveToNext());
            }
        cursor.close();
        return result;
    }

    private void reintentarImpresion(){
        android.support.v7.app.AlertDialog.Builder quitDialog
                = new android.support.v7.app.AlertDialog.Builder(context);
        quitDialog.setTitle("No se pudo imprimir, intetar nuevamente?");

        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ConectarImpresoraTask conectarImpresoraTask = new ConectarImpresoraTask();
                conectarImpresoraTask.execute();
            }});
        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }});
        quitDialog.show();
    }

    //tarea asincrona
    /**
     * Metodo para conectar la impresora
     */

    class ConectarImpresoraTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setTitle("Conectando Impresora");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if(progress.isShowing()){
                progress.dismiss();
                if(s){
                    try{
                        openBT();
                        CodigosOferta gno = CodigosOferta.valueOf(codemp);
                        switch (gno){
                            case BELLA2015:sendDataB(); break;
                            case BELLA2016:sendDataB(); break;
                            case BELLALUZ2015:sendDataB(); break;
                            case BELLALUZ2016:sendDataB(); break;
                            case BELLALUZN:sendDataB(); break;
                            case BELLALUZ:sendDataB(); break;
                            default:sendDataY();break;
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            try {
                findBT();
                Thread.sleep(1000);
                result=true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                result=false;
            }
            return result;
        }
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
