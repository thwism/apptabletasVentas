package ibzssoft.com.fragment;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import ibzssoft.com.adaptadores.ValidateReferencia;
import ibzssoft.com.ishidamovile.AprobacionOferta;
import ibzssoft.com.ishidamovile.oferta.modificar.ModificaOfertaG;
import ibzssoft.com.ishidamovile.pedido.modificar.ModificaPedido;
import ibzssoft.com.ishidamovile.oferta.visualizar.OfertaDetalleG;
import ibzssoft.com.ishidamovile.pedido.visualizar.PedidoDetalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.ishidamovile.Transaccion_IT_Detalle;
import ibzssoft.com.ishidamovile.VisitaDetalle;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.ValoresTransacciones;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PedidosTodos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PedidosTodos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidosTodos extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";
    private static final String ARG_PARAM10 = "param10";
    private static final String ARG_PARAM11 = "param11";
    private static final String ARG_PARAM12 = "param12";
    private static final String ARG_PARAM13 = "param13";
    private static final String ARG_PARAM14 = "param14";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private int mParam6;
    private int mParam7;
    private String mParam8;
    private String mParam9;
    private String mParam10;
    private String mParam11;
    private String mParam12;
    private String mParam13;
    private int mParam14;
    private OnFragmentInteractionListener mListener;
    private ListView listadoPedidos;
    private ListadoTransaccionesAdaptador adaptador;
    /*private ListView listadoPedidos;
    private ListadoTransaccionesAdaptador adaptador;*/
    public PedidosTodos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidosNoEnviados.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosTodos newInstance(String param1, String param2,String param3,String param4,String param5,int param6,int param7,String param8,String param9,String param10,String param11, String param12,String param13, int param14) {
        PedidosTodos fragment = new PedidosTodos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putInt(ARG_PARAM6, param6);
        args.putInt(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        args.putString(ARG_PARAM10, param10);
        args.putString(ARG_PARAM11, param11);
        args.putString(ARG_PARAM12, param12);
        args.putString(ARG_PARAM13, param13);
        args.putInt(ARG_PARAM14, param14);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getInt(ARG_PARAM6);
            mParam7 = getArguments().getInt(ARG_PARAM7);
            mParam8 = getArguments().getString(ARG_PARAM8);
            mParam9 = getArguments().getString(ARG_PARAM9);
            mParam10 = getArguments().getString(ARG_PARAM10);
            mParam11 = getArguments().getString(ARG_PARAM11);
            mParam12 = getArguments().getString(ARG_PARAM12);
            mParam13 = getArguments().getString(ARG_PARAM13);
            mParam14 = getArguments().getInt(ARG_PARAM14);
            System.out.println("Argumentos fragment todos: " + mParam1 + ";" + mParam2+";"+mParam3+";"+mParam4+";"+mParam5+";"+mParam6+";"+mParam7+";"+mParam8+";"+mParam9+";"+mParam10+";"+mParam11+";"+mParam12+","+mParam13+","+mParam14);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarTransacciones(mParam8.split(","),0,mParam2,3,0);
        System.out.println("tab 1. query ejecutado");
        View v =inflater.inflate(R.layout.fragment_pedidos_todos, container, false);
        listadoPedidos=(ListView)v.findViewById(R.id.lstPedidosAll);
        adaptador= new ListadoTransaccionesAdaptador(getActivity(),cursor,0);
        listadoPedidos.setAdapter(adaptador);
        helper.close();
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ListadoTransaccionesAdaptador extends CursorAdapter{
        private Context context;

        public ListadoTransaccionesAdaptador(Context context, Cursor c, int flags) {
            super(context, c, 0);
            this.context=context;
        }

        @Override
        public String convertToString(Cursor cursor) {
            return cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre));
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String nro= cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_numTransaccion));
            String cliente= cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre));
            String modTrans= cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado));
            String referencia= cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_referencia));
            TextView nro_text = (TextView) view.findViewById(R.id.filaTransaccionNro);
            TextView cli_text = (TextView) view.findViewById(R.id.filaTransaccionCliente);
            TextView est_text = (TextView) view.findViewById(R.id.filaTransaccionEstado);
            TextView mod_text = (TextView) view.findViewById(R.id.filaTransaccionFecha);
            TextView ref_text = (TextView) view.findViewById(R.id.filaTransaccionReferencia);
            int estado=cursor.getInt(cursor.getColumnIndex(Transaccion.FIELD_band_enviado));

            if (nro!=null) {
                cli_text.setText(cliente);
                mod_text.setText(modTrans);
                nro_text.setText(nro);
                if(referencia!=null && referencia.isEmpty()!=true){
                    ref_text.setText("REF: "+referencia);
                }else{
                    ref_text.setText(R.string.info_reference_empty);
                }
                if(estado==1){
                    est_text.setText(R.string.info_send);
                    est_text.setTextColor(getActivity().getResources().getColor(R.color.successfull));
                }else{
                    est_text.setText(R.string.info_no_send);
                    est_text.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                }
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.fila_transaccion_descripcion, parent, false);
            return v;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View view = super.getView(position, convertView, parent);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cur = getCursor();
                    if (cur.moveToPosition(position)) {
                        Intent intent = null;
                        ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                        switch (gno) {
                            case OFERTA:
                                intent = new Intent(getActivity(), OfertaDetalleG.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam11);
                                intent.putExtra("decimales", mParam14);
                                startActivity(intent);
                                getActivity().finish();
                                break;
                            case PEDIDO:
                                intent = new Intent(getActivity(), PedidoDetalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam11);
                                intent.putExtra("decimales", mParam14);
                                startActivity(intent);
                                getActivity().finish();
                                break;
                            case COBRO:
                                intent = new Intent(getActivity(), Transaccion_IT_Detalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam11);
                                intent.putExtra("decimales", mParam14);
                                startActivity(intent);
                                getActivity().finish();
                                break;
                            case VISITA:
                                intent = new Intent(getActivity(), VisitaDetalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam11);
                                startActivity(intent);
                                getActivity().finish();
                                break;

                        }
                    }
                }
            });
            view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem menuItem1 = menu.add(0, R.id.itemallEnviar, 1, "Enviar Transacción");
                    MenuItem menuItem2 = menu.add(0, R.id.itemallPDF, 2, "Generar Documento PDF");
                    MenuItem menuItem3 = menu.add(0, R.id.itemallEnviarPDF, 3, "Enviar en PDF");
                    //MenuItem menuItem4 = menu.add(0, R.id.itemallReimprimir, 4, "Reimprimir Comprobante");
                    MenuItem menuItem5 = menu.add(0, R.id.itemallVer, 5, "Ver Transacción");
                    MenuItem menuItem6 = menu.add(0, R.id.itemallModificar, 6, "Modificar Transacción");
                    MenuItem menuItem7 = menu.add(0, R.id.itemallEliminar, 7, "Eliminar Transacción");
                    //First Action
                    menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            android.support.v7.app.AlertDialog.Builder quitDialog
                                    = new android.support.v7.app.AlertDialog.Builder(context);
                            quitDialog.setTitle("Seguro deseas enviar la transacción?");

                            quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Cursor cur = getCursor();
                                    Transaccion selec = null;
                                    if (cur.moveToPosition(position)) {
                                        selec = new Transaccion(
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
                                    }//extrayendo transaccion
                                    //EnviarTransaccion sendTransacction = new EnviarTransaccion(selec, context,  mParam6);
                                    //sendTransacction.ejecutarTarea();
                                }});

                            quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    dialog.cancel();
                                }});
                            quitDialog.show();
                            return true;
                        }
                    });
                    //Second Action
                    menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno) {
                                    case PEDIDO:
                                       // GenerarPedidoPDF generarPDF1 = new GenerarPedidoPDF(getActivity(), cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)), mParam5, mParam2, mParam11);
                                        //generarPDF1.ejecutarProceso();
                                        break;
                                    case OFERTA:
                                        //GenerarOfertaPDF generarPDF2 = new GenerarOfertaPDF(getActivity(), cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)), mParam5, mParam2, mParam11);
                                        //generarPDF2.ejecutarProceso();
                                        break;
                                    case COBRO:case VISITA:
                                        Toast.makeText(context, "Operación no disponible", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return true;
                        }
                    });
                    //three action
                    menuItem3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                System.out.println("ID de la transaccion: " + cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                String file = "sdcard/" + getActivity().getResources().getString(R.string.title_folder_root) + "/" + cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador)) + "_" + cur.getString(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)) + ".pdf";
                                File adjunto = new File(file);
                                if (adjunto.exists()) {
                                    String[] to = {""};
                                    Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
                                    itSend.setType("plain/text");
                                    itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
                                    itSend.putExtra(android.content.Intent.EXTRA_EMAIL, to);
                                    itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, mParam2 + " Nro. " + cur.getString(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                                    itSend.putExtra(android.content.Intent.EXTRA_TEXT, "Transaccion  - " + mParam2 + " (" + cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador)) + ")");
                                    itSend.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(file)));
                                    startActivity(itSend);
                                } else {
                                    Toast ts = Toast.makeText(getActivity(), R.string.no_found_file_to_send, Toast.LENGTH_SHORT);
                                    ts.show();
                                }
                            }
                            return false;
                        }
                    });
                    //action four reimprimir combro,bante
                    /*menuItem4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno) {
                                    case COBRO:
                                        ReimprimirCobro reimprimirCobro= new ReimprimirCobro(getActivity(), cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        reimprimirCobro.ejecutarProceso();
                                        break;
                                    case OFERTA:case PEDIDO:case VISITA:
                                        Toast.makeText(context, "Operacion no disponible", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return true;
                        }
                    });
*/
                    menuItem5.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                Intent intent = null;
                                switch (gno) {
                                    case PEDIDO:
                                        intent = new Intent(getActivity(), PedidoDetalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam5);
                                        intent.putExtra("posicion", mParam6);
                                        intent.putExtra("impuestos", mParam11);
                                        intent.putExtra("decimales", mParam14);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;
                                    case OFERTA:
                                        intent = new Intent(getActivity(), OfertaDetalleG.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam5);
                                        intent.putExtra("posicion", mParam6);
                                        intent.putExtra("impuestos", mParam11);
                                        intent.putExtra("decimales", mParam14);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;
                                    case COBRO:
                                        intent = new Intent(getActivity(), Transaccion_IT_Detalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam5);
                                        intent.putExtra("posicion", mParam6);
                                        intent.putExtra("impuestos", mParam11);
                                        intent.putExtra("decimales", mParam14);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;
                                    case VISITA:
                                        intent = new Intent(getActivity(), VisitaDetalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam5);
                                        intent.putExtra("posicion", mParam6);
                                        intent.putExtra("impuestos", mParam11);
                                        intent.putExtra("decimales", mParam14);
                                        startActivity(intent);
                                        getActivity().finish();
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    //action 5
                    menuItem6.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                Intent intent2 = null;
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno){
                                    case PEDIDO:
                                        intent2 = new Intent(getActivity(), ModificaPedido.class);
                                        intent2.putExtra("usuario", mParam1);
                                        intent2.putExtra("vendedor", mParam12);
                                        intent2.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent2.putExtra("empresa", mParam5);
                                        intent2.putExtra("posicion", mParam6);
                                        intent2.putExtra("accesos", mParam8);
                                        intent2.putExtra("lineas", mParam9);
                                        intent2.putExtra("impuestos", mParam11);
                                        intent2.putExtra("bodegas", mParam13);
                                        intent2.putExtra("decimales", mParam14);
                                        startActivity(intent2);
                                        getActivity().finish();
                                        break;
                                    case OFERTA:
                                        if(new ValidateReferencia().validate(cur.getString(cur.getColumnIndex(Transaccion.FIELD_referencia)))&&cur.getInt(cur.getColumnIndex(Transaccion.FIELD_band_enviado))==0){
                                            intent2 = new Intent(getActivity(), AprobacionOferta.class);
                                        }else{
                                            intent2 = new Intent(getActivity(), ModificaOfertaG.class);
                                        }
                                        intent2.putExtra("usuario", mParam1);
                                        intent2.putExtra("vendedor", mParam12);
                                        intent2.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent2.putExtra("empresa", mParam5);
                                        intent2.putExtra("posicion", mParam6);
                                        intent2.putExtra("accesos", mParam8);
                                        intent2.putExtra("lineas", mParam9);
                                        intent2.putExtra("impuestos", mParam11);
                                        intent2.putExtra("bodegas", mParam13);
                                        intent2.putExtra("decimales", mParam14);
                                        startActivity(intent2);
                                        getActivity().finish();
                                        break;
                                    case COBRO: case VISITA:
                                        Toast.makeText(context, "Operacion no permitida", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    //action 6 eliminar
                    menuItem7.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                final String id_selected = cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans));
                                final String num_selected = String.valueOf(cur.getInt(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                                final String cod_selected = cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador));
                                final int est_selected = cur.getInt(cur.getColumnIndex(Transaccion.FIELD_band_enviado));
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder.setTitle("Advertencia");
                                alertDialogBuilder
                                        .setMessage("Esta seguro de eliminar la transaccion?")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                                switch (gno) {
                                                    case PEDIDO: case OFERTA: case VISITA:
                                                        //EliminarIVKardex del1 = new EliminarIVKardex(context, id_selected, num_selected, cod_selected,mParam6);
                                                        //del1.ejecutarTarea();
                                                        break;
                                                    case COBRO:
                                                        if(est_selected!=0){
                                                          //  EliminarIVKardex del2= new EliminarIVKardex(context, id_selected, num_selected, cod_selected,mParam6);
                                                            //del2.ejecutarTarea();
                                                        }else{
                                                            Toast.makeText(getActivity(), "No se puede eliminar la transaccion", Toast.LENGTH_SHORT).show();
                                                        }

                                                        break;
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
                            }
                            return false;
                        }
                    });

                }


            });
            return view;
        }

    }
}