package ibzssoft.com.ishidamovile.oferta.crear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ibzssoft.com.adaptadores.CatalogoClientesAdapter;
import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.Cliente;

public class OfertaSimple extends AppCompatActivity implements View.OnClickListener{
    private TextView nombre,nombre_comercial,ruc,direccion,telefono,observacion;
    private TextView lbl1,lbl2,lbl3,lbl4,lbl5,lbl6;
    private LinearLayout container_cartera, container_cliente;
    private Button show_more_cliente, show_more_cartera;
    private Animation slideUpAnimation, slideDownAnimation;

    /*RecyclerView*/
    private RecyclerView mRecyclerView;
    private CatalogoClientesAdapter mAdapter;
    private ArrayList<Cliente> clientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta_simple);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*Cargando Clientes*/
        this.prepararComponentesCliente();
        this.prepararEtiquetasCliente();
    }
    public void prepararComponentesCliente(){
        this.nombre = (TextView)findViewById(R.id.nombres);
        this.nombre_comercial = (TextView)findViewById(R.id.comercial);
        this.ruc = (TextView)findViewById(R.id.cedula);
        this.direccion = (TextView)findViewById(R.id.direccion);
        this.telefono = (TextView)findViewById(R.id.telefono);
        this.observacion = (TextView)findViewById(R.id.observacion);
        this.show_more_cartera = (Button) findViewById(R.id.show_more_cartera);
        this.show_more_cliente = (Button)findViewById(R.id.show_more_cliente);
        this.slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_away_appear);
        this.slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_slowed);
        this.container_cliente = (LinearLayout) findViewById(R.id.container_cliente);
        this.container_cartera= (LinearLayout) findViewById(R.id.container_cartera);
        this.show_more_cartera.setOnClickListener(this);
        this.show_more_cliente.setOnClickListener(this);
    }

    public void prepararEtiquetasCliente(){
        this.lbl1= (TextView)findViewById(R.id.lbl1);
        this.lbl2= (TextView)findViewById(R.id.lbl2);
        this.lbl3= (TextView)findViewById(R.id.lbl3);
        this.lbl4= (TextView)findViewById(R.id.lbl4);
        this.lbl5= (TextView)findViewById(R.id.lbl5);
        this.lbl6= (TextView)findViewById(R.id.lbl6);
    }

    public void mostrarComponentesClientes(int visible){
        this.nombre_comercial.setVisibility(visible);lbl2.setVisibility(visible);
        this.ruc.setVisibility(visible);lbl3.setVisibility(visible);
        this.direccion.setVisibility(visible);lbl4.setVisibility(visible);lbl5.setVisibility(visible);
        this.telefono.setVisibility(visible);lbl6.setVisibility(visible);
        this.observacion.setVisibility(visible);
    }

    public void startSlideUpAnimation(View view) {
        view.startAnimation(slideUpAnimation);
    }

    public void startSlideDownAnimation(View view) {
        view.startAnimation(slideDownAnimation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_oferta_simple, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.show_more_cliente:
                int visible = 0;
                if(this.ruc.getVisibility()==View.VISIBLE){
                    visible = View.GONE;
                    this.show_more_cliente.setText(getString(R.string.ver_mas));
                    this.startSlideDownAnimation(this.container_cliente);
                } else{
                    this.show_more_cliente.setText(getString(R.string.ocultar));
                    this.startSlideUpAnimation(this.container_cliente);
                    visible = View.VISIBLE;
                }
                mostrarComponentesClientes(visible);
                break;
        }
    }
}
