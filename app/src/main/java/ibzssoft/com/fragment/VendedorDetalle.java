package ibzssoft.com.fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * A placeholder fragment containing a simple view.
 */
public class VendedorDetalle extends Fragment {

    private TextView txtNombres,txtCodigo,txtCargo,txtCorreo,txtTelefono,txtVend,
            txtCob,txtLineas,txtOrdenBod,txtRutas,txtVendedores,txtEstado,txtModificacion,txtUsrSuper,txtGrupo;
    public VendedorDetalle() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DBSistemaGestion helper= new DBSistemaGestion(getActivity());
        Cursor cursor=helper.obtenerVendedor(getActivity().getIntent().getIntExtra("vendedor",0));
        View view = inflater.inflate(R.layout.fragment_vendedor_detalle, container, false);
        txtCargo=(TextView)view.findViewById(R.id.formVendedorTxt78);
        txtCorreo=(TextView)view.findViewById(R.id.formVendedorTxt6);
        txtTelefono=(TextView)view.findViewById(R.id.formVendedorTxt6a);
        txtVend=(TextView)view.findViewById(R.id.formVendedorTxt8);
        txtCob=(TextView)view.findViewById(R.id.formVendedorTxt10);
        txtLineas=(TextView)view.findViewById(R.id.formVendedorTxt12);
        txtOrdenBod=(TextView)view.findViewById(R.id.formVendedorTxt16);
        txtRutas=(TextView)view.findViewById(R.id.formVendedorTxt18);
        txtVendedores=(TextView)view.findViewById(R.id.formVendedorTxt18a);
        txtEstado=(TextView)view.findViewById(R.id.formVendedorTxt20);
        txtModificacion=(TextView)view.findViewById(R.id.formVendedorTxt20);
        txtCodigo=(TextView)view.findViewById(R.id.formVendedorTxt182);
        txtNombres=(TextView)view.findViewById(R.id.formVendedorTxt184);
        txtUsrSuper=(TextView)view.findViewById(R.id.formVendedorTxt186);
        txtGrupo=(TextView)view.findViewById(R.id.formVendedorTxt20A);
        if(cursor.moveToFirst()){
            String nombre=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_nombre));
            String codigo=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_codvendedor));
            int band_cob=cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_bandcobrador));
            String lines=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_lineas));
            String orden=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_ordenbodegas));
            String routes=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_rutastablet));
            String vendedors=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_vendedores));
            int estado=cursor.getInt(cursor.getColumnIndex(Vendedor.FIELD_bandvalida));
            String user_cod=cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario));
            String user_nom=cursor.getString(cursor.getColumnIndex(Usuario.FIELD_nombreusuario));
            int user_super=cursor.getInt(cursor.getColumnIndex(Usuario.FIELD_bandsupervisor));
            String modificacion=cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_fechagrabado));
            String group=cursor.getString(cursor.getColumnIndex(Grupo.FIELD_descripcion));

            if(nombre.isEmpty()!=true){txtNombres.setText(nombre);}
            if(codigo.isEmpty()!=true){txtCodigo.setText(codigo);}
            if(band_cob!=0){txtCob.setText("SI");}
            if(lines!=null){txtLineas.setText(lines);}
            if(orden!=null){txtOrdenBod.setText(orden);}
            if(routes!=null){txtRutas.setText(routes);}
            if(vendedors!=null){txtVendedores.setText(vendedors);}
            if(estado!=0){txtEstado.setText("Habilitado"); txtEstado.setTextColor(getActivity().getResources().getColor(R.color.colorInfo));}
            if(modificacion!=null){txtModificacion.setText(modificacion);}
            if(user_cod!=null){txtCodigo.setText(user_cod);}
            if(user_nom!=null){txtNombres.setText(user_nom);}
            if(group!=null){txtGrupo.setText(group);}
            if(user_super!=0){txtUsrSuper.setText("SI"); txtUsrSuper.setTextColor(getActivity().getResources().getColor(R.color.colorAccent));}
        }
        cursor.close();
        helper.close();
        return view;
    }
}
