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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import ibzssoft.com.enviar.EnviarTransaccion;
import ibzssoft.com.ishidamovile.GenerarPedidoPDF;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.oferta.screens.CodigosOferta;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleG;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleM;
import ibzssoft.com.ishidamovile.pedido.modificar.ModificaPedido;
import ibzssoft.com.ishidamovile.pedido.modificar.ModificaPedidoJM;
import ibzssoft.com.ishidamovile.pedido.visualizar.PedidoDetalle;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 03/12/15.
 */
public class TransPedidoAdapter extends RecyclerView.Adapter<TransPedidoAdapter.ViewHolderTransPedidoA> {

    private final List<Transaccion> mModels;
    private final LayoutInflater mInflater;
    private Context context;
    private int opcion;
    private String codemp;

    public TransPedidoAdapter(Context context, List<Transaccion> models,String codemp, int opcion) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.opcion=opcion;
        this.context=context;
        this.codemp = codemp;
    }


    @Override
    public ViewHolderTransPedidoA onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_transaccion_descripcion, parent, false);
        return new ViewHolderTransPedidoA(itemView,context);
    }


    @Override
    public void onBindViewHolder(ViewHolderTransPedidoA holder, int position) {
        Transaccion model = mModels.get(position);
        holder.bind(model);
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
    /*
    Adaptador para llenar campos de la fila
     */
    public class ViewHolderTransPedidoA extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        private int nro;
        private String idtrans,identificador;
        private String referencia;
        private int bandenviado;
        private TextView nro_text,cli_text,est_text,mod_text,fecha_envio,ref_text;
        private Context context;


        public ViewHolderTransPedidoA(View view, Context context) {
            super(view);
            nro_text= (TextView) itemView.findViewById(R.id.filaTransaccionNro);
            cli_text= (TextView) itemView.findViewById(R.id.filaTransaccionCliente);
            est_text= (TextView) itemView.findViewById(R.id.filaTransaccionEstado);
            mod_text= (TextView) itemView.findViewById(R.id.filaTransaccionFecha);
            ref_text= (TextView) itemView.findViewById(R.id.filaTransaccionReferencia);
            fecha_envio= (TextView) itemView.findViewById(R.id.filaTransaccionFechaEnvio);

            this.context=context;
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        public void bind(Transaccion model) {
            nro=model.getNumTransaccion();
            referencia=model.getReferencia();
            bandenviado=model.getBand_enviado();
            idtrans=model.getId_trans();
            identificador=model.getIdentificador();
            nro_text.setText(String.valueOf(model.getNumTransaccion()));
            cli_text.setText(model.getCliente_id());
            est_text.setText(String.valueOf(model.getBand_enviado()));
            mod_text.setText(new ParseDates().changeDateToStringCompact(new ParseDates().changeStringToDateSimpleFormat(model.getFecha_grabado())));
            fecha_envio.setText(new ParseDates().changeDateToStringCompact(new ParseDates().changeStringToDateSimpleFormat(model.getFecha_envio())));
            ref_text.setText(model.getReferencia());

            if(referencia!=null && referencia.isEmpty()!=true){
                ref_text.setText("REF: "+referencia);
            }else{
                ref_text.setText(R.string.info_reference_empty);
            }
            if(bandenviado==1){
                est_text.setText(R.string.info_send);
                est_text.setTextColor(context.getResources().getColor(R.color.successfull));
            }else{
                est_text.setText(R.string.info_no_send);
                est_text.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem menuItem1=menu.add(0, R.id.itemallEnviar, 1, "Enviar Transacción");
            MenuItem menuItem2=menu.add(0, R.id.itemallPDF, 2, "Generar Documento PDF");
            MenuItem menuItem3=menu.add(0, R.id.itemallEnviarPDF,3, "Enviar en PDF");
            MenuItem menuItem5=menu.add(0, R.id.itemallVer,5, "Ver Transacción");
            MenuItem menuItem6=menu.add(0, R.id.itemallModificar,6, "Modificar Transacción");
            MenuItem menuItem7=menu.add(0, R.id.itemallEliminar,6, "Eliminar Transacción");

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
            CodigosOferta gno = CodigosOferta.valueOf(codemp);
            switch (item.getItemId()){
                case R.id.itemallEnviar:
                    if(bandenviado!=1){
                        android.support.v7.app.AlertDialog.Builder quitDialog
                                = new android.support.v7.app.AlertDialog.Builder(context);
                        quitDialog.setTitle("Seguro desea enviar la transacción?");
                        quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                EnviarTransaccion sendTransacction = new EnviarTransaccion(idtrans, context,  opcion);
                                sendTransacction.ejecutarTarea();
                            }});

                        quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }});
                        quitDialog.show();
                    }else Toast.makeText(context,context.getString(R.string.title_transaction_send_already),Toast.LENGTH_SHORT).show();
                    break;
                case R.id.itemallPDF:
                    GenerarPedidoPDF generarPDF1 = new GenerarPedidoPDF(context,idtrans);
                    generarPDF1.ejecutarProceso();
                    break;
                case R.id.itemallEnviarPDF:
                    String file = "sdcard/" + context.getString(R.string.title_folder_root) + "/" + identificador+ "_" + nro + ".pdf";
                    File adjunto = new File(file);
                    if (adjunto.exists()) {
                        String[] to = {""};
                        Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
                        itSend.setType("plain/text");
                        itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
                        itSend.putExtra(android.content.Intent.EXTRA_EMAIL, to);
                        itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, "PEDIDO" + " Nro. " + nro);
                        itSend.putExtra(android.content.Intent.EXTRA_TEXT, "Transacción  - " + "PEDIDO"+ " (" + nro+ ")");
                        itSend.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
                        context.startActivity(itSend);
                    } else {
                        Toast.makeText(context, R.string.no_found_file_to_send, Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.itemallVer:
                    Intent intent1 = new Intent(activity, PedidoDetalle.class);
                    intent1.putExtra("transid", idtrans);
                    intent1.putExtra("opcion", opcion);
                    activity.startActivity(intent1);
                    activity.finish();
                    break;
                case R.id.itemallModificar:

                    Intent intent2;
                    switch (gno){
                        case JM2014:intent2 = new Intent(activity, ModificaPedidoJM.class);break;
                        case JM2015:intent2 = new Intent(activity, ModificaPedidoJM.class);break;
                        default:
                            intent2 = new Intent(activity, ModificaPedido.class);break;
                    }
                    intent2.putExtra("transid", idtrans);
                    intent2.putExtra("opcion", opcion);
                    activity.startActivity(intent2);
                    activity.finish();
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
                                    EliminarTransaccionTask taskDel= new EliminarTransaccionTask();
                                    taskDel.execute(idtrans,String.valueOf(opcion));
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
            Intent intent1 = new Intent(activity, PedidoDetalle.class);
            intent1.putExtra("transid", idtrans);
            intent1.putExtra("opcion", opcion);
            activity.startActivity(intent1);
            activity.finish();
        }
    }
    //Clase para eliminar transaccion y detalles
    /*Tarea Asincrona*/
    class EliminarTransaccionTask extends AsyncTask<String,Integer,Boolean> {
        private ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress=new ProgressDialog(context);
            progress.setCancelable(false);
            progress.setTitle("Eliminando Transacción");
            progress.setMessage("Espere...");
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    Activity activity=(Activity)context;
                    Intent intent= new Intent(activity,MainActivity.class);
                    intent.putExtra("opcion",opcion);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result;
            try{
                DBSistemaGestion helper = new DBSistemaGestion(context);
                //Eliminado ivkardex
                helper.eliminarIVKardexTrans(params[0]);
                //Eliminado pckcardex
                helper.eliminarPCKardexTrans(params[0]);
                //Eliminado transaccion
                helper.eliminarTransaccion(params[0]);
                helper.close();
                result= true;
            }catch (Exception e){
                e.printStackTrace();
                result = false;
            }
            return result;
        }
    }
}