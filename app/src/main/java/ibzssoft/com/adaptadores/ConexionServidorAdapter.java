package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ibzssoft.com.ishidamovile.Cliente_Detalle;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.InfoItemList;

/**
 * Created by root on 03/12/15.
 */
public class ConexionServidorAdapter extends RecyclerView.Adapter<ViewHolderConexionServidor> {

    private final List<InfoItemList> mModels;
    private final LayoutInflater mInflater;
    private Context context;

    public ConexionServidorAdapter(Context context, List<InfoItemList> models) {
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        this.context=context;
    }


    @Override
    public ViewHolderConexionServidor onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.list_row, parent, false);
        return new ViewHolderConexionServidor(itemView,context);
    }


    @Override
    public void onBindViewHolder(ViewHolderConexionServidor holder, int position) {
        InfoItemList model = mModels.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }
}