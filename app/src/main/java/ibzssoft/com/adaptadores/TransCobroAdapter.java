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
import ibzssoft.com.ishidamovile.AprobacionOferta;
import ibzssoft.com.ishidamovile.GenerarOfertaPDF;
import ibzssoft.com.ishidamovile.MainActivity;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.ReimprimirCobro;
import ibzssoft.com.ishidamovile.Transaccion_IT_Detalle;
import ibzssoft.com.ishidamovile.cobro.visualizar.CobroDetalle;
import ibzssoft.com.ishidamovile.oferta.modificar.ModificaOfertaG;
import ibzssoft.com.ishidamovile.oferta.modificar.ModificaOfertaM;
import ibzssoft.com.ishidamovile.oferta.screens.CodigosOferta;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleG;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleM;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * Created by root on 03/12/15.
 */

/**
 * Adaptador para Recyclerview
 */
public class TransCobroAdapter extends RecyclerView.Adapter<TransCobroAdapter.ViewHolderTransCobro> {

    private final List<Transaccion> mModels;
    private final LayoutInflater mInflater;
    private Context context;
    private int opcion;
    private String codemp;
    public TransCobroAdapter(Context context, List<Transaccion> models, String codemp, int opcion) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.codemp=codemp;
        this.opcion = opcion;
        this.context = context;
    }


    @Override
    public ViewHolderTransCobro onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.fila_transaccion_descripcion, parent, false);
        return new ViewHolderTransCobro(itemView, context);
    }


    @Override
    public void onBindViewHolder(ViewHolderTransCobro holder, int position) {
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
     */
    public class ViewHolderTransCobro extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
        private int nro;
        private String idtrans, identificador;
        private String referencia;
        private int bandenviado;
        private int opcion;
        private TextView nro_text,cli_text,est_text,mod_text,fecha_envio,ref_text;
        private Context context;

        public ViewHolderTransCobro(View view, Context context) {
            super(view);
            nro_text= (TextView) itemView.findViewById(R.id.filaTransaccionNro);
            cli_text= (TextView) itemView.findViewById(R.id.filaTransaccionCliente);
            est_text= (TextView) itemView.findViewById(R.id.filaTransaccionEstado);
            mod_text= (TextView) itemView.findViewById(R.id.filaTransaccionFecha);
            fecha_envio= (TextView) itemView.findViewById(R.id.filaTransaccionFechaEnvio);
            ref_text= (TextView) itemView.findViewById(R.id.filaTransaccionReferencia);
            this.context=context;
            view.setOnClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        public void bind(Transaccion model,int opcion) {
            this.opcion=opcion;
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
            MenuItem menuItem4=menu.add(0, R.id.itemallReimprimir,4, "Reimprimir Comprobante");
            MenuItem menuItem5=menu.add(0, R.id.itemallVer,5, "Ver Transacción");
            MenuItem menuItem7=menu.add(0, R.id.itemallEliminar,6, "Eliminar Transacción");

            menuItem1.setOnMenuItemClickListener(this);
            menuItem4.setOnMenuItemClickListener(this);
            menuItem5.setOnMenuItemClickListener(this);
            menuItem7.setOnMenuItemClickListener(this);
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Activity activity = (Activity) context;
            CodigosOferta gno = CodigosOferta.valueOf(codemp.toUpperCase());
            switch (item.getItemId()){
                case R.id.itemallEnviar:
                    if(bandenviado!=1){
                        AlertDialog.Builder quitDialog
                                = new AlertDialog.Builder(context);
                        quitDialog.setTitle("Seguro deseas enviar la transacción?");
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
                case R.id.itemallVer:
                    Intent intent;
                    switch (gno){
                        default:
                            intent = new Intent(activity, Transaccion_IT_Detalle.class);break;
                    }
                    intent.putExtra("transid", idtrans);
                    intent.putExtra("opcion", opcion);
                    activity.startActivity(intent);
                    activity.finish();
                    break;
                case R.id.itemallReimprimir:
                    ReimprimirCobro reimprimirCobro= new ReimprimirCobro(context, idtrans);
                    reimprimirCobro.ejecutarProceso();
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
                                    if(bandenviado!=0){
                                        EliminarTransaccionTask taskDel= new EliminarTransaccionTask();
                                        taskDel.execute(idtrans,String.valueOf(opcion));
                                    }else{
                                        Toast.makeText(context,"Operación no permitida",Toast.LENGTH_SHORT).show();
                                    }
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
            Intent intent = new Intent(activity, Transaccion_IT_Detalle.class);
            intent.putExtra("transid", idtrans);
            intent.putExtra("opcion", opcion);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * Clase para eliminar individualmente
     */

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