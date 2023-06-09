package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.enviar.EnviarTransaccion;
import ibzssoft.com.ishidamovile.AprobacionOferta;
import ibzssoft.com.ishidamovile.GenerarOfertaPDF;
import ibzssoft.com.ishidamovile.GenerarOfertaPDFCGYP;
import ibzssoft.com.ishidamovile.GenerarOfertaPDFCGYPSII4;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.oferta.modificar.ModificaOfertaG;
import ibzssoft.com.ishidamovile.oferta.modificar.ModificaOfertaJM;
import ibzssoft.com.ishidamovile.oferta.screens.CodigosOferta;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleG;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.vista.DetalleOfertaSii4;
import ibzssoft.com.paginas.TabOfertaAll;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 03/12/15.
 */

/**
 * Adaptador para Recyclerview
 */
public class TransOfertaAdapter extends RecyclerView.Adapter<TransOfertaAdapter.ViewHolderTransOferta> {

    private final List<Transaccion> mModels;
    private final LayoutInflater mInflater;
    private Context context;
    private int opcion;
    private boolean import_of;
    private String codemp;
    private String ip, port, url, ws, ws_test;
    boolean estado = false;
    String idTransSelect, codigoTransAprobada, numeroTransAprobada;
    List<DetalleOfertaSii4> detalleTransaccion;

    public Context getContext() {
        return context;
    }

    public TransOfertaAdapter(Context context, List<Transaccion> models, String codemp, int opcion, boolean import_of) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.codemp = codemp;
        this.opcion = opcion;
        this.context = context;
        this.import_of = import_of;
        codigoTransAprobada = "";
        numeroTransAprobada = "";
    }


    @Override
    public ViewHolderTransOferta onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_transaccion_descripcion, parent, false);
        return new ViewHolderTransOferta(itemView, context);
    }


    @Override
    public void onBindViewHolder(ViewHolderTransOferta holder, int position) {
        Transaccion model = mModels.get(position);
        holder.bind(model, opcion);
    }


    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void animateTo(List<Transaccion> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Transaccion> newModels) {
        for (int i = mModels.size() - 1; i >= 0; i--) {
            final Transaccion model = mModels.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Transaccion> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Transaccion model = newModels.get(i);
            if (!mModels.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Transaccion> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Transaccion model = newModels.get(toPosition);
            final int fromPosition = mModels.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Transaccion removeItem(int position) {
        final Transaccion model = mModels.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Transaccion model) {
        mModels.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Transaccion model = mModels.remove(fromPosition);
        mModels.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setFilter(List<Transaccion> transaccions) {
        mModels.clear();
        mModels.addAll(transaccions);
        notifyDataSetChanged();
    }

    /**
     * Clase de viewholder para fila oferta
     **/
    public class ViewHolderTransOferta extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        private int nro;
        private String idtrans, identificador;
        private String referencia;
        private int bandenviado;
        private int opcion;
        private int estadoTrans;
        private TextView nro_text, cli_text, est_text, mod_text, ref_text, fecha_envio, estadoTrans_text;
        private Context context;

        public ViewHolderTransOferta(View view, Context context) {
            super(view);
            nro_text = (TextView) itemView.findViewById(R.id.filaTransaccionNro);
            cli_text = (TextView) itemView.findViewById(R.id.filaTransaccionCliente);
            est_text = (TextView) itemView.findViewById(R.id.filaTransaccionEstado);
            mod_text = (TextView) itemView.findViewById(R.id.filaTransaccionFecha);
            fecha_envio = (TextView) itemView.findViewById(R.id.filaTransaccionFechaEnvio);
            ref_text = (TextView) itemView.findViewById(R.id.filaTransaccionReferencia);
            estadoTrans_text = (TextView) itemView.findViewById(R.id.filaTransaccionEstadoT);
            this.context = context;
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        public void bind(Transaccion model, int opcion) {
            this.opcion = opcion;
            nro = model.getNumTransaccion();
            referencia = model.getReferencia();
            bandenviado = model.getBand_enviado();
            idtrans = model.getId_trans();
            identificador = model.getIdentificador();
            nro_text.setText(String.valueOf(model.getNumTransaccion()));
            cli_text.setText(model.getCliente_id());
            est_text.setText(String.valueOf(model.getBand_enviado()));
            mod_text.setText(new ParseDates().changeDateToStringCompact(new ParseDates().changeStringToDateSimpleFormat(model.getFecha_grabado())));
            fecha_envio.setText(new ParseDates().changeDateToStringCompact(new ParseDates().changeStringToDateSimpleFormat(model.getFecha_envio())));
            ref_text.setText(model.getReferencia());
            estadoTrans = model.getEstado();
            if (referencia != null && referencia.isEmpty() != true) {
                ref_text.setText("REF: " + referencia);
            } else {
                ref_text.setText(R.string.info_reference_empty);
            }
            if (bandenviado == 1) {
                est_text.setText(R.string.info_send);
                est_text.setTextColor(context.getResources().getColor(R.color.successfull));
            } else {
                est_text.setText(R.string.info_no_send);
                est_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }

            //Para verificar el estado de las ofertas
            if (import_of) {
                estadoTrans_text.setText("");
                fecha_envio.setText("");
            } else {
                switch (estadoTrans) {
                    case 0:
                        estadoTrans_text.setText("EST: DESAPROBADO");
                        break;
                    case 1:
                        estadoTrans_text.setText("EST: APROBADO");
                        break;
                    case 2:
                        estadoTrans_text.setText("EST: DESPACHADO");
                        break;
                    case 3:
                        estadoTrans_text.setText("EST: ANULADO");
                        break;
                    case 4:
                        estadoTrans_text.setText("EST: SEMIDESPACHADO");
                        break;
                    default:
                        estadoTrans_text.setText("EST: ");
                }
            }
            //Para guardar información general de las transacciones aprobadas
            if (referencia.contains("-")) {
                codigoTransAprobada = referencia.substring(0, referencia.indexOf("-"));
                numeroTransAprobada = referencia.substring(referencia.indexOf("-") + 1);
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem menuItem1 = menu.add(0, R.id.itemallEnviar, 1, "Enviar Transacción");
            MenuItem menuItem2 = menu.add(0, R.id.itemallPDF, 2, "Generar Documento PDF");
            MenuItem menuItem3 = menu.add(0, R.id.itemallEnviarPDF, 3, "Enviar en PDF");
            MenuItem menuItem5 = menu.add(0, R.id.itemallVer, 5, "Ver Transacción");
            MenuItem menuItem6 = menu.add(0, R.id.itemallModificar, 6, "Modificar Transacción");
            MenuItem menuItem7 = menu.add(0, R.id.itemallEliminar, 6, "Eliminar Transacción");

            menuItem1.setOnMenuItemClickListener(this);
            menuItem2.setOnMenuItemClickListener(this);
            menuItem3.setOnMenuItemClickListener(this);
            menuItem5.setOnMenuItemClickListener(this);
            menuItem6.setOnMenuItemClickListener(this);
            menuItem7.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Activity activity = (Activity) context;
            CodigosOferta gno = CodigosOferta.valueOf(codemp.toUpperCase());
            switch (item.getItemId()) {
                case R.id.itemallEnviar:
                    if (bandenviado != 1) {
                        android.support.v7.app.AlertDialog.Builder quitDialog
                                = new android.support.v7.app.AlertDialog.Builder(context);
                        quitDialog.setTitle("Seguro desea enviar la transacción?");
                        quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                EnviarTransaccion sendTransacction = new EnviarTransaccion(idtrans, context, opcion);
                                sendTransacction.ejecutarTarea();
                            }
                        });

                        quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        });
                        quitDialog.show();
                    } else
                        Toast.makeText(context, context.getString(R.string.title_transaction_send_already), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.itemallPDF:
                    if (estadoTrans != 0 && estadoTrans != 3) {
                        detalleTransaccion = new ArrayList<>();
                        extraerConfigConexionServidor();
                        idTransSelect = idtrans;
                        TareaProbarConexion taskTestConection = new TareaProbarConexion();
                        taskTestConection.execute();

                    } else {
                        Toast.makeText(context, "No se puede generar el PDF, la transacción no se encuentra en un estado válido.", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.itemallEnviarPDF:
                    String numeroOferta = numeroTransAprobada.isEmpty() ? String.valueOf(nro) : numeroTransAprobada;

                    String file = "sdcard/" + context.getString(R.string.title_folder_root) + "/" + identificador + "_" + numeroOferta + ".pdf";
                    File adjunto = new File(file);
                    if (adjunto.exists()) {
                        String[] to = {""};
                        Intent itSend = new Intent(Intent.ACTION_SEND);
                        itSend.setType("plain/text");
                        itSend.putExtra(Intent.EXTRA_EMAIL, new String[]{});
                        itSend.putExtra(Intent.EXTRA_EMAIL, to);
                        itSend.putExtra(Intent.EXTRA_SUBJECT, "OFERTA" + " Nro. " + numeroOferta);
                        itSend.putExtra(Intent.EXTRA_TEXT, "Transacción  - " + "OFERTA" + " (" + numeroOferta + ")");
                        itSend.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
                        context.startActivity(itSend);
                    } else {
                        Toast.makeText(context, R.string.no_found_file_to_send, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.itemallVer:
                    Intent intent;
                    switch (gno) {
                        case CONSGYP2015:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                        case CONSGYP2016:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                        case CONSGYP2018:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                        case CONSGYP2019:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                        case CONSGYP2020:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                        default:
                            intent = new Intent(activity, OfertaDetalleG.class);
                            break;
                    }
                    intent.putExtra("transid", idtrans);
                    intent.putExtra("opcion", opcion);
                    activity.startActivity(intent);
                    activity.finish();
                    break;
                case R.id.itemallModificar:
                    if (bandenviado != 1) {
                        Intent intent2;
                        switch (gno) {
                            case JM2014:
                                intent2 = new Intent(activity, ModificaOfertaJM.class);
                                break;
                            case JM2015:
                                intent2 = new Intent(activity, ModificaOfertaJM.class);
                                break;
                            case CONSGYP2016:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            case CONSGYP2017:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            case CONSGYP2018:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            case CONSGYP2019:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            case CONSGYP2020:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            case CONSGYP2021:
                                if (import_of)
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                else intent2 = new Intent(activity, ModificaOfertaG.class);
                                break;
                            default:
                                if (import_of) {
                                    intent2 = new Intent(activity, AprobacionOferta.class);
                                } else {
                                    intent2 = new Intent(activity, ModificaOfertaG.class);
                                }
                                break;
                        }
                        intent2.putExtra("transid", idtrans);
                        intent2.putExtra("opcion", opcion);
                        activity.startActivity(intent2);
                        activity.finish();
                    } else {
                        Toast.makeText(context, context.getString(R.string.title_transaction_send_already), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.itemallEliminar:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setTitle("Advertencia");
                    alertDialogBuilder
                            .setMessage("Esta seguro de eliminar la transacción?")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    EliminarTransaccionTask taskDel = new EliminarTransaccionTask();
                                    taskDel.execute(idtrans, String.valueOf(opcion));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
                default:
                    break;
            }

            return true;
        }

        @Override
        public void onClick(View view) {
            Activity activity = (Activity) context;
            CodigosOferta gno = CodigosOferta.valueOf(codemp.toUpperCase());
            Intent intent;
            switch (gno) {
                case CONSGYP2015:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
                case CONSGYP2016:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
                case CONSGYP2018:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
                case CONSGYP2019:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
                case CONSGYP2020:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
                default:
                    intent = new Intent(activity, OfertaDetalleG.class);
                    break;
            }
            intent.putExtra("transid", idtrans);
            intent.putExtra("opcion", opcion);
            activity.startActivity(intent);
            activity.finish();
        }

        public void extraerConfigConexionServidor() {
            ExtraerConfiguraciones e = new ExtraerConfiguraciones(context);

            ip = e.get(context.getString(R.string.key_conf_ip), context.getString(R.string.pref_ip_default));
            port = e.get(context.getString(R.string.key_conf_port), context.getString(R.string.pref_port_default));
            url = e.get(context.getString(R.string.key_conf_url), context.getString(R.string.pref_url_default));
            ws_test = e.get(context.getString(R.string.key_ws_test), context.getString(R.string.pref_ws_test));
            ws = e.get(context.getString(R.string.key_ws_get_detalle_trans_aprobada), context.getString(R.string.pref_ws_get_detalle_trans_aprobada));

        }
    }

    /**
     * Clase para eliminar individualmente
     */

    class EliminarTransaccionTask extends AsyncTask<String, Integer, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Transacción");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progress.isShowing()) {
                progress.dismiss();
                if (aBoolean) {
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("opcion", opcion);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result;
            try {
                DBSistemaGestion helper = new DBSistemaGestion(context);
                //Eliminado ivkardex
                helper.eliminarIVKardexTrans(params[0]);
                //Eliminado pckcardex
                helper.eliminarPCKardexTrans(params[0]);
                //Eliminado transaccion
                helper.eliminarTransaccion(params[0]);
                helper.close();
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        }
    }

    class TareaProbarConexion extends AsyncTask<String, Float, Boolean> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle("Validación conexión al servidor");
            progress.setMessage("Realizando validaciones de conexión, por favor espere...");
            progress.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {
            Boolean result = false;
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 5000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 5000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            HttpGet get = new HttpGet("http://" + ip + ":" + port + url + ws_test);
            System.out.println("http://" + ip + ":" + port + url + ws_test);
            get.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(get);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONObject obj = new JSONObject(respStr);
                System.out.println("Response: " + respStr);
                int response = obj.getInt("estado");

                if (response == 1) {
                    result = true;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            estado = s;
            if (progress.isShowing()) {
                progress.dismiss();
            }
            if (estado) {
                RecibirDetalleTransaccionesTask recibirDetalleTrans = new RecibirDetalleTransaccionesTask();
                recibirDetalleTrans.execute();

            } else {
                Toast.makeText(context, "Error de conexión, no fue posible descargar el detalle.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    class RecibirDetalleTransaccionesTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setTitle("Descargando detalle transacción");
            progress.setMessage("Descargando datos, por favor espere...");
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;

            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);

            HttpGet del = new HttpGet("http://" + ip + ":" + port + url + ws + "/" + codigoTransAprobada + "/" + numeroTransAprobada);
            del.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());
                JSONArray respJSON = new JSONArray(respStr);
                Gson gson = new Gson();
                progress.setMax(respJSON.length());
                int count = 0;
                detalleTransaccion = new ArrayList<>(respJSON.length());
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    DetalleOfertaSii4 detalle = gson.fromJson(obj.toString(), DetalleOfertaSii4.class);
                    detalleTransaccion.add(detalle);
                    count++;
                    publishProgress(count);

                }

                Thread.sleep(50);
                result = true;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            if (progress.isShowing()) {
                progress.dismiss();
            }

            if (s) {
                GenerarOfertaPDFCGYPSII4 generarPDF1 = new GenerarOfertaPDFCGYPSII4(getContext(), idTransSelect, detalleTransaccion);
                generarPDF1.ejecutarProceso();
                Toast.makeText(context, "Detalle descargado correctamente.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error, no es posible generar el PDF. No fue posible descargar el detalle de la transacción.", Toast.LENGTH_SHORT).show();
            }


        }
    }

}