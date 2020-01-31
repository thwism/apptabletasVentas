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
import android.widget.Filterable;
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
import ibzssoft.com.ishidamovile.cobro.visualizar.CobroDetalle;
import ibzssoft.com.modelo.Transaccion;
import ibzssoft.com.modelo.ValoresTransacciones;
import ibzssoft.com.storage.DBSistemaGestion;

public class PedidosEnviados extends Fragment {
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private int mParam4;
    private int mParam5;
    private String  mParam6;
    private String mParam7;
    private String mParam8;
    private String mParam9;
    private String mParam10;
    private int mParam11;
    private OnFragmentInteractionListener mListener;
    private ListView listadoPedidos;
    private ListadoTransaccionesEnviadasAdaptador adaptador;

    public PedidosEnviados() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 --> nombre de usuario
     * @param param2 --> tipo de transaccion
     * @param param3 --> empresa
     * @param param4 --> posicion del menu
     * @return A new instance of fragment PedidosEnviados.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidosEnviados newInstance(String param1, String param2,String param3,int param4,int param5,String param6,String param7,String param8,String param9,String param10,int param11) {
        PedidosEnviados fragment = new PedidosEnviados();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putInt(ARG_PARAM4, param4);
        args.putInt(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        args.putString(ARG_PARAM7, param7);
        args.putString(ARG_PARAM8, param8);
        args.putString(ARG_PARAM9, param9);
        args.putString(ARG_PARAM10, param10);
        args.putInt(ARG_PARAM11, param11);
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
            mParam4 = getArguments().getInt(ARG_PARAM4);
            mParam5 = getArguments().getInt(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
            mParam7 = getArguments().getString(ARG_PARAM7);
            mParam8 = getArguments().getString(ARG_PARAM8);
            mParam9 = getArguments().getString(ARG_PARAM9);
            mParam10 = getArguments().getString(ARG_PARAM10);
            mParam11 = getArguments().getInt(ARG_PARAM11);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBSistemaGestion helper=new DBSistemaGestion(getActivity());
        Cursor cursor= helper.consultarTransacciones(mParam6.split(","),1,mParam2,3,0);
        View v =inflater.inflate(R.layout.fragment_pedidos_enviados, container, false);
        listadoPedidos=(ListView)v.findViewById(R.id.lstPedidosSend);
        adaptador= new ListadoTransaccionesEnviadasAdaptador(getActivity(),cursor,0);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**
     * Adaptador
     */
    public class ListadoTransaccionesEnviadasAdaptador extends CursorAdapter implements Filterable {
        private Context context;
        public ListadoTransaccionesEnviadasAdaptador(Context context, Cursor c, int flags) {
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
            String modificacion= cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_fecha_grabado));
            String referencia= cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_referencia));

            TextView nro_text = (TextView) view.findViewById(R.id.filaTransaccionNro);
            TextView cli_text = (TextView) view.findViewById(R.id.filaTransaccionCliente);
            TextView est_text = (TextView) view.findViewById(R.id.filaTransaccionEstado);
            TextView mod_text = (TextView) view.findViewById(R.id.filaTransaccionFecha);
            TextView ref_text = (TextView) view.findViewById(R.id.filaTransaccionReferencia);
            String estado=cursor.getString(cursor.getColumnIndex(Transaccion.FIELD_band_enviado));
            if (nro!=null) {
                nro_text.setText(nro);cli_text.setText(cliente);
                mod_text.setText(modificacion);

                if(referencia!=null && referencia.isEmpty()!=true){
                    ref_text.setText("REF: "+referencia);
                }else{
                    ref_text.setText(R.string.info_reference_empty);
                }
                if(estado.equals("1")){
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
                public void onClick(View view) {
                    Cursor cur = getCursor();
                    if (cur.moveToPosition(position)) {
                        Intent intent = null;
                        ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                        switch (gno) {
                            case PEDIDO:
                                intent = new Intent(getActivity(), PedidoDetalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam8);
                                intent.putExtra("decimales", mParam11);
                                startActivity(intent);
                                break;
                            case OFERTA:
                                intent = new Intent(getActivity(), OfertaDetalleG.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam8);
                                intent.putExtra("decimales", mParam11);
                                startActivity(intent);
                                break;
                            case VISITA:
                                intent = new Intent(getActivity(), VisitaDetalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam8);
                                intent.putExtra("decimales", mParam11);
                                startActivity(intent);
                                break;
                            case COBRO:
                                intent = new Intent(getActivity(), Transaccion_IT_Detalle.class);
                                intent.putExtra("usuario", mParam1);
                                intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                intent.putExtra("empresa", mParam5);
                                intent.putExtra("posicion", mParam6);
                                intent.putExtra("impuestos", mParam8);
                                intent.putExtra("decimales", mParam11);
                                startActivity(intent);
                                break;
                        }
                    }
                }
            });
            view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem menuItem1 = menu.add(0, R.id.itemallPDF, 1, "Generar Documento PDF");
                    MenuItem menuItem2 = menu.add(0, R.id.itemallEnviarPDF, 2, "Enviar en PDF");
                    MenuItem menuItem4 = menu.add(0, R.id.itemallVer, 4, "Ver Transaccion");
                    MenuItem menuItem5 = menu.add(0, R.id.itemallModificar, 5, "Modificar Transaccion");
                    MenuItem menuItem6 = menu.add(0, R.id.itemallEliminar, 6, "Eliminar Transaccion");
                    //Second Action
                    menuItem1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno) {
                                    case PEDIDO:
                                        //GenerarPedidoPDF generarPDF1 = new GenerarPedidoPDF(getActivity(), cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)), mParam3, mParam2, mParam8);
                                        //generarPDF1.ejecutarProceso();
                                        break;
                                    case OFERTA:
                                        //GenerarOfertaPDF generarPDF2 = new GenerarOfertaPDF(getActivity(), cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)), mParam3, mParam2, mParam8);
                                        //generarPDF2.ejecutarProceso();
                                        break;
                                    case COBRO:case VISITA:
                                        Toast.makeText(context, "Operacion no disponible", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return true;
                        }
                    });
                    //three action
                    menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                               // System.out.println("ID de la transaccion: " + cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
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
                    //action 3
                    /*menuItem3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
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
                                    case OFERTA:case PEDIDO:
                                        Toast.makeText(context, "Operacion no disponible", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return true;
                        }
                    });

*/
                    menuItem4.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                Intent intent = null;
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno){
                                    case OFERTA:
                                        intent = new Intent(getActivity(), OfertaDetalleG.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam3);
                                        intent.putExtra("posicion", mParam4);
                                        intent.putExtra("impuestos", mParam8);
                                        intent.putExtra("decimales", mParam11);
                                        startActivity(intent);
                                        break;
                                    case PEDIDO:
                                        intent = new Intent(getActivity(), PedidoDetalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam3);
                                        intent.putExtra("posicion", mParam4);
                                        intent.putExtra("impuestos", mParam8);
                                        intent.putExtra("decimales", mParam11);
                                        startActivity(intent);
                                        break;
                                    case VISITA:
                                        intent = new Intent(getActivity(), VisitaDetalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam3);
                                        intent.putExtra("posicion", mParam4);
                                        intent.putExtra("impuestos", mParam8);
                                        intent.putExtra("decimales", mParam11);
                                        startActivity(intent);
                                        break;
                                    case COBRO:
                                        intent = new Intent(getActivity(), CobroDetalle.class);
                                        intent.putExtra("usuario", mParam1);
                                        intent.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent.putExtra("empresa", mParam3);
                                        intent.putExtra("posicion", mParam4);
                                        intent.putExtra("impuestos", mParam8);
                                        intent.putExtra("decimales", mParam11);
                                        startActivity(intent);
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    //action 4
                    menuItem5.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                Intent intent2 = null;
                                ValoresTransacciones gno = ValoresTransacciones.valueOf(mParam2);
                                switch (gno) {
                                    case PEDIDO:
                                        intent2 = new Intent(getActivity(), ModificaPedido.class);
                                        intent2.putExtra("usuario", mParam1);
                                        intent2.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent2.putExtra("empresa", mParam5);
                                        intent2.putExtra("posicion", mParam4);
                                        intent2.putExtra("accesos", mParam6);
                                        intent2.putExtra("lineas", mParam7);
                                        intent2.putExtra("impuestos", mParam8);
                                        intent2.putExtra("vendedor", mParam9);
                                        intent2.putExtra("bodegas", mParam10);
                                        intent2.putExtra("decimales", mParam11);
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
                                        intent2.putExtra("trans_id", cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans)));
                                        intent2.putExtra("empresa", mParam5);
                                        intent2.putExtra("posicion", mParam4);
                                        intent2.putExtra("accesos", mParam6);
                                        intent2.putExtra("lineas", mParam7);
                                        intent2.putExtra("impuestos", mParam8);
                                        intent2.putExtra("vendedor", mParam9);
                                        intent2.putExtra("bodegas", mParam10);
                                        intent2.putExtra("decimales", mParam11);
                                        startActivity(intent2);
                                        getActivity().finish();
                                        break;
                                    case COBRO:case VISITA:
                                        Toast.makeText(context, "Operacion no permitida", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                            return false;
                        }
                    });
                    //action 5 eliminar
                    menuItem6.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Cursor cur = getCursor();
                            if (cur.moveToPosition(position)) {
                                final String id_selected = cur.getString(cur.getColumnIndex(Transaccion.FIELD_ID_Trans));
                                final String num_selected = String.valueOf(cur.getInt(cur.getColumnIndex(Transaccion.FIELD_numTransaccion)));
                                final String cod_selected = cur.getString(cur.getColumnIndex(Transaccion.FIELD_identificador));
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);
                                alertDialogBuilder.setTitle("Advertencia");
                                alertDialogBuilder
                                        .setMessage("Esta seguro de eliminar la transaccion?")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //EliminarIVKardex delTransaccion = new EliminarIVKardex(context, id_selected, num_selected, cod_selected,mParam4);
                                                //delTransaccion.ejecutarTarea();
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
