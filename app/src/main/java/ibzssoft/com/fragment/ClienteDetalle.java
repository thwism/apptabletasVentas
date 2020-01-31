package ibzssoft.com.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.adaptadores.Validaciones;
import ibzssoft.com.enviar.EnviarTransaccion;
import ibzssoft.com.enviar.EnviarUbicacion;
import ibzssoft.com.modelo.Canton;
import ibzssoft.com.modelo.PCGrupo4;
import ibzssoft.com.modelo.Parroquia;
import ibzssoft.com.modelo.Provincia;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.ishidamovile.R;
/**
 * A placeholder fragment containing a simple view.
 */
public class ClienteDetalle extends Fragment {

    private TextView txtNombres,txtNombreAlterno,txtRuc,txtDireccion,txtDireccion2,txtTelefono,
            txtTelefono2,txtCorreo,txtFax,txtEstado,txtObservacion,txtModificacion,txtProvincia,txtCanton,txtParroquia,
        txtPlazo,txtPrecios,txtPagos,txtAdministrador,txtCompras,txtGeoreferencia,txtVendedor;
    private int conf_pcg=0;
    private String geo, cliente_id;
    public ClienteDetalle() {
        this.geo="";
        this.cliente_id="";
    }
    public void extraerCongiguraciones(){
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
        conf_pcg = ext.configuracionPreciosGrupos();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        this.cliente_id=getActivity().getIntent().getStringExtra("cliente");
        extraerCongiguraciones();
        Cursor cursor=helper.obtenerClientePrecio(cliente_id, conf_pcg);
        System.out.println("Cliente seleccionado: "+cliente_id);
        View view = inflater.inflate(R.layout.fragment_cliente_detalle, container, false);
        txtNombres=(TextView)view.findViewById(R.id.formClienteTxt2);
        txtNombreAlterno=(TextView)view.findViewById(R.id.formClienteTxt4);
        txtRuc=(TextView)view.findViewById(R.id.formClienteTxt6);
        txtDireccion=(TextView)view.findViewById(R.id.formClienteTxt8);
        txtDireccion2=(TextView)view.findViewById(R.id.formClienteTxt10);
        txtTelefono=(TextView)view.findViewById(R.id.formClienteTxt12);
        txtTelefono2=(TextView)view.findViewById(R.id.formClienteTxt14);
        txtCorreo=(TextView)view.findViewById(R.id.formClienteTxt16);
        txtFax=(TextView)view.findViewById(R.id.formClienteTxt18);
        txtEstado=(TextView)view.findViewById(R.id.formClienteTxt20);
        txtObservacion=(TextView)view.findViewById(R.id.formClienteTxt22);
        txtProvincia=(TextView)view.findViewById(R.id.formClienteTxt29);
        txtCanton=(TextView)view.findViewById(R.id.formClienteTxt31);
        txtParroquia=(TextView)view.findViewById(R.id.formClienteTxt33);
        txtModificacion=(TextView)view.findViewById(R.id.formClienteTxt35);
        txtPrecios=(TextView)view.findViewById(R.id.formClienteTxt31b);
        txtPlazo=(TextView)view.findViewById(R.id.formClienteTxt31a);
        txtPagos=(TextView)view.findViewById(R.id.formClienteTxt83);
        txtAdministrador=(TextView)view.findViewById(R.id.formClienteTxt89);
        txtCompras=(TextView)view.findViewById(R.id.formClienteTxt81);
        txtGeoreferencia=(TextView)view.findViewById(R.id.formClienteTxt78);
        txtVendedor=(TextView)view.findViewById(R.id.formClienteTxt102);
        if(cursor.moveToFirst()){
            String nombre=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombre));
            String alterno=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_nombrealterno));
            String ruc=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_ruc));
            String direccion=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion1));
            String direccion2=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direccion2));
            String telefono=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono1));
            String telefono2=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_telefono2));
            String correo=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_email));
            String fax=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fax));
            int estado=cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_estado));
            String observacion=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_observacion));
            String modificacion=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_fechagrabado));
            String provincia=cursor.getString(cursor.getColumnIndex(Provincia.FIELD_descripcion));
            String canton=cursor.getString(cursor.getColumnIndex(Canton.FIELD_descripcion));
            String parroquia=cursor.getString(cursor.getColumnIndex(Parroquia.FIELD_descripcion));
            String pcg4=cursor.getString(cursor.getColumnIndex(PCGrupo4.FIELD_descripcion));
            int pl=cursor.getInt(cursor.getColumnIndex(Cliente.FIELD_diasplazo));
            String admi=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_banco));
            String comp=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_numcuenta));
            String pago=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_direcbanco));
            this.geo=cursor.getString(cursor.getColumnIndex(Cliente.FIELD_posgooglemaps));
            String vend=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_nombre));

            if(nombre.isEmpty()!=true){txtNombres.setText(nombre);}
            if(alterno.isEmpty()!=true){txtNombreAlterno.setText(alterno);}
            if(ruc.isEmpty()!=true){txtRuc.setText(ruc);}
            if(direccion.isEmpty()!=true){txtDireccion.setText(direccion);}
            if(direccion2.isEmpty()!=true){txtDireccion2.setText(direccion2);}
            if(telefono.isEmpty()!=true){txtTelefono.setText(telefono);}
            if(telefono2.isEmpty()!=true){txtTelefono2.setText(telefono2);}
            if(correo.isEmpty()!=true){txtCorreo.setText(correo);}
            if(fax.isEmpty()!=true){txtFax.setText(fax);}
            if(observacion!=null){txtObservacion.setText(observacion);}
            if(modificacion!=null){txtModificacion.setText(modificacion);}
            if(provincia!=null){txtProvincia.setText(provincia);}
            if(canton!=null){txtCanton.setText(canton);}
            if(parroquia!=null){txtParroquia.setText(parroquia);}
            if(estado!=0){txtEstado.setText("Habilitado");txtEstado.setTextColor(getActivity().getResources().getColor(R.color.successfull));}
            if(pcg4!=null){txtPrecios.setText(pcg4);}
            if(admi!=null){txtAdministrador.setText(admi);}
            if(comp!=null){txtCompras.setText(comp);}
            if(pago!=null){txtPagos.setText(pago);}
            if(geo!=null){txtGeoreferencia.setText(geo);}
            txtPlazo.setText(String.valueOf(pl));
            txtVendedor.setText(vend);
        }
        cursor.close();
        helper.close();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cliente_detalle, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enviar:
                if(new Validaciones().validar_coordenadas(this.geo)){
                    AlertDialog.Builder quitDialog
                            = new AlertDialog.Builder(getActivity());
                    quitDialog.setTitle("Seguro deseas enviar la ubicacion del cliente a Sii4A?");
                    quitDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            EnviarUbicacion enviarUbicacion = new EnviarUbicacion(cliente_id, geo,getActivity());
                            enviarUbicacion.ejecutarTarea();
                        }});
                    quitDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }});
                    quitDialog.show();
                }else{
                    Toast ts = Toast.makeText(getActivity(), "No existen coordenadas validas, en los puntos de georeferenciacion", Toast.LENGTH_LONG);
                    ts.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
