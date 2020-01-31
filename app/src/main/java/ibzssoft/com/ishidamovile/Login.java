package ibzssoft.com.ishidamovile;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.listeners.OnTouchPasswordListener;
import ibzssoft.com.modelo.Grupo;
import ibzssoft.com.modelo.Usuario;
import ibzssoft.com.modelo.Vendedor;
import ibzssoft.com.storage.DBSistemaGestion;
import android.support.design.widget.TextInputLayout;

public class Login extends AppCompatActivity implements  View.OnClickListener{
    private Button btnLogin;
    private TextInputLayout loginUserWrapper;
    private TextInputLayout loginPasswordWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prepareComponents();
        btnLogin.setOnClickListener(this);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        Window window = getWindow();
        window.setLayout(width, height);
        window.setWindowAnimations(R.style.dialogFragmentAnimation);
    }

    public void prepareComponents(){
        loginUserWrapper = (TextInputLayout) findViewById(R.id.login_user_wrapper);
        loginPasswordWrapper= (TextInputLayout) findViewById(R.id.login_password_wrapper);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        ExtraerConfiguraciones ext = new ExtraerConfiguraciones(Login.this);
        String user=ext.get(getString(R.string.key_act_user),getString(R.string.pref_act_user_default));
        loginUserWrapper.getEditText().setText(user);
        loginUserWrapper.getEditText().setSelection(user.length());
        EditText registrationPassword = loginPasswordWrapper.getEditText();
        if (registrationPassword!= null) {
            registrationPassword.setOnTouchListener(new OnTouchPasswordListener(registrationPassword));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                if (isRequiredFields(loginUserWrapper, loginPasswordWrapper)) {
                    validar(loginUserWrapper.getEditText().getText().toString(),loginPasswordWrapper.getEditText().getText().toString());
                }
                break;
        }
    }

    private boolean validar(String user, String pass){
        DBSistemaGestion helper= new DBSistemaGestion(this);
        boolean result =helper.login(user, pass);
        if(result==true){
            Cursor cursor=helper.buscarUsuario(user);
            if(cursor.moveToFirst()){
                    Intent intent= new Intent(Login.this, SeleccionarEmpresa.class);
                    intent.putExtra("supervisor",cursor.getString(cursor.getColumnIndex(Usuario.FIELD_bandsupervisor)));
                    intent.putExtra("usuario",cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codusuario)));
                    intent.putExtra("vendedor",cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_idvendedor)));
                    intent.putExtra("nombres",cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_nombre)));
                    intent.putExtra("grupo",cursor.getString(cursor.getColumnIndex(Usuario.FIELD_codgrupo)));
                    intent.putExtra("lineas", cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_lineas)));
                    intent.putExtra("bodegas", cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_ordenbodegas)));
                    intent.putExtra("accesos", cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_vendedores)));
                    intent.putExtra("rutas", cursor.getString(cursor.getColumnIndex(Vendedor.FIELD_rutastablet)));
                    startActivity(intent);
                    finish();
            }else{
                Toast ts = Toast.makeText(getApplicationContext(), R.string.no_found_vendedor, Toast.LENGTH_SHORT);
                ts.show();
            }
            cursor.close();
            }else{
            Toast ts = Toast.makeText(getApplicationContext(), R.string.info_no_login, Toast.LENGTH_SHORT);
            ts.show();
        }
        helper.close();
        return true;
    }

    private boolean isRequiredFields(TextInputLayout emailWrapper, TextInputLayout passwordWrapper) {

        boolean isEmail = false;
        boolean isPassword = false;

        EditText email = emailWrapper.getEditText();
        EditText password = passwordWrapper.getEditText();

        if (email.getText().toString().equalsIgnoreCase("")) {
            emailWrapper.setErrorEnabled(true);
            emailWrapper.setError(getString(R.string.info_field_required));
        } else {
            emailWrapper.setErrorEnabled(false);
            isEmail = true;
        }

        if (password.getText().toString().equalsIgnoreCase("")) {
            passwordWrapper.setErrorEnabled(true);
            passwordWrapper.setError(getString(R.string.info_field_required));
        } else {
            passwordWrapper.setErrorEnabled(false);
            isPassword = true;
        }

        if (isEmail && isPassword) {
            return true;
        } else {
            return false;
        }
    }

}
