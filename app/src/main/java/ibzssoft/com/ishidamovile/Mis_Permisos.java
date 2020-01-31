package ibzssoft.com.ishidamovile;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import ibzssoft.com.adaptadores.ListadoMisPermisosAdaptador;
import ibzssoft.com.storage.DBSistemaGestion;

public class Mis_Permisos extends Fragment {
    private String empresa,grupo;
    private ListView listadoPermisos;
    private ListadoMisPermisosAdaptador adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.content_mis__permisos,null);
        this.extraerParametros();
        listadoPermisos = (ListView) myInflatedView.findViewById(R.id.listadoMisPermisos);
        this.cargarListado();
        return myInflatedView;
    }

    public void extraerParametros(){
        this.empresa = getArguments().getString("empresa").toString();
        this.grupo= getArguments().getString("grupo").toString();
    }

    public void cargarListado() {
        DBSistemaGestion helper = new DBSistemaGestion(getActivity());
        Cursor cursor = helper.consultarPermisos(grupo, empresa, "", false, false,null);
        String[] from = new String[] {};
        int[] to = new int[] {};
        adapter=new ListadoMisPermisosAdaptador(getActivity(), R.layout.fila_mi_permiso, cursor, from, to);
        listadoPermisos.setAdapter(adapter);
        helper.close();
    }
}
