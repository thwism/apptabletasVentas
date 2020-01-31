package ibzssoft.com.ishidamovile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ibzssoft.com.modelo.Empresa;
import ibzssoft.com.storage.DBSistemaGestion;

public class Info_Company extends Fragment{
    private String empresa,ip,port,url;
    private TextView txt_empresa, txt_dir,txt_tel,txt_ip,txt_port,txt_url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.content_info__company,null);
        this.extraerParametros();
        txt_empresa = (TextView) myInflatedView.findViewById(R.id.infoEmpNombre);txt_empresa.setText(empresa);
        txt_dir = (TextView) myInflatedView.findViewById(R.id.infoEmpDir);
        txt_tel = (TextView) myInflatedView.findViewById(R.id.infoEmpTel);
        txt_ip = (TextView) myInflatedView.findViewById(R.id.infoEmpIP);txt_ip.setText(ip);
        txt_port = (TextView) myInflatedView.findViewById(R.id.infoEmpPort);txt_port.setText(port);
        txt_url = (TextView) myInflatedView.findViewById(R.id.infoEmpURL);txt_url.setText(url);
        return myInflatedView;
    }

    public void extraerParametros(){
        this.empresa = getArguments().getString("empresa").toString();
        this.ip = getArguments().getString("ip").toString();
        this.port= getArguments().getString("port").toString();
        this.url= getArguments().getString("url").toString();
    }
}
