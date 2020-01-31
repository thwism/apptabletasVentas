package ibzssoft.com.adaptadores;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import ibzssoft.com.ishidamovile.Cliente_Detalle;
import ibzssoft.com.ishidamovile.reportes.DescuentosCliente;
import ibzssoft.com.ishidamovile.reportes.EstadoCuenta;
import ibzssoft.com.ishidamovile.reportes.Historial;
import ibzssoft.com.ishidamovile.Localizacion_Cliente;
import ibzssoft.com.ishidamovile.R;


import ibzssoft.com.ishidamovile.ReportePromedios;
import ibzssoft.com.modelo.Cliente;
import ibzssoft.com.modelo.Localizacion;
import ibzssoft.com.recibir.RecibirProvincias;
import ibzssoft.com.storage.DBSistemaGestion;
import ibzssoft.com.utils.MsgUtils;


/**
 * Created by root on 04/12/15.
 */
public class ViewHolderCliente extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{
    private String id_cliente;
    private String cli_identificador;
    private String cli_ubicacion;
    private String cli_nombres;
    private String cli_direccion;
    private String cli_comercial;
    private TextView nombres_text;
    private TextView alterno_text;
    private TextView ci_text;
    private TextView direccion_text;
    private TextView modificacion_text;
    private Context context;
    private String usuario;

    private  ItemClickListener clickListener;

    public ViewHolderCliente(View view,Context context,String usuario) {
        super(view);
        alterno_text= (TextView) itemView.findViewById(R.id.filaClienteNombreAlterno);
        nombres_text= (TextView) itemView.findViewById(R.id.filaClienteNombres);
        ci_text= (TextView) itemView.findViewById(R.id.filaClienteCedula);
        this.context=context;
        this.usuario=usuario;
        view.setOnClickListener(this);
        view.setOnCreateContextMenuListener(this);
    }

    public void bind(Cliente model) {
        cli_identificador=model.getIdprovcli();
        cli_ubicacion=model.getPosgooglemaps();
        cli_direccion=model.getDireccion1();
        cli_nombres=model.getNombre();
        cli_comercial=model.getNombrealterno();
        id_cliente=model.getRuc();
        nombres_text.setText(model.getNombre());
        alterno_text.setText(model.getNombrealterno());
        ci_text.setText(model.getRuc());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem1=menu.add(0, R.id.menu_cliente_ver, 1, "Ver Cliente");
        MenuItem menuItem2=menu.add(0, R.id.menu_cliente_ver_cartera, 2, "Ver Estado de Cuenta");
        MenuItem menuItem3=menu.add(0, R.id.menu_cliente_ver_descuentos,3, "Ver Descuentos");
        MenuItem menuItem4=menu.add(0, R.id.menu_cliente_ver_promedio,4, "Ver Promedio de Cobros");
        MenuItem menuItem5=menu.add(0, R.id.menu_cliente_ver_historial,5, "Ver Historial de Compras");
        MenuItem menuItem6=menu.add(0, R.id.menu_cliente_set_ubicacion,6, "Cambiar coordenadas geograficas");
        MenuItem menuItem7=menu.add(0, R.id.menu_cliente_ver_ubicacion,6, "Ver Ubicacion en Google Maps");

        menuItem1.setOnMenuItemClickListener(this);
        menuItem2.setOnMenuItemClickListener(this);
        menuItem3.setOnMenuItemClickListener(this);
        menuItem4.setOnMenuItemClickListener(this);
        menuItem5.setOnMenuItemClickListener(this);
        menuItem6.setOnMenuItemClickListener(this);
        menuItem7.setOnMenuItemClickListener(this);
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cliente_ver:
                Intent intent= new Intent(context, Cliente_Detalle.class);
                Activity activity=(Activity)context;
                    intent.putExtra("vendedor",usuario);
                    intent.putExtra("cliente",cli_identificador);
                    activity.startActivity(intent);
                break;
            case R.id.menu_cliente_ver_descuentos:
                Activity activity3=(Activity)context;
                Intent intent19= new Intent(context,DescuentosCliente.class);
                intent19.putExtra("cliente",cli_identificador);
                intent19.putExtra("descripcion",cli_nombres);
                intent19.putExtra("comercial",cli_comercial);
                activity3.startActivity(intent19);
                break;
            case R.id.menu_cliente_ver_promedio:
                Activity activity21=(Activity)context;
                Intent intent21= new Intent(context,ReportePromedios.class);
                intent21.putExtra("cliente",cli_identificador);
                activity21.startActivity(intent21);
                break;
            case R.id.menu_cliente_ver_cartera:
                Activity activity9=(Activity)context;
                Intent intent9= new Intent(context,EstadoCuenta.class);
                intent9.putExtra("cliente",cli_identificador);
                intent9.putExtra("descripcion",cli_nombres);
                intent9.putExtra("comercial",cli_comercial);
                activity9.startActivity(intent9);
                break;
            case R.id.menu_cliente_ver_historial:
                System.out.println("Ver Historial");
                Activity activity2=(Activity)context;
                Intent intent10 = new Intent(context, Historial.class);
                intent10.putExtra("cliente",cli_identificador);
                activity2.startActivity(intent10);
                break;
            case R.id.menu_cliente_ver_ubicacion:
                try{
                    System.out.println("Ubicacion cliente: "+cli_ubicacion);
                    String [] puntos = cli_ubicacion.split(",");
                    double latitud = Double.parseDouble(puntos[0]);
                    double longitud = Double.parseDouble(puntos[1]);
                    Activity activity22=(Activity)context;
                    Intent intent22 = new Intent(activity22, Localizacion_Cliente.class);
                    intent22.putExtra("cliente",cli_nombres);
                    intent22.putExtra("latitud",latitud);
                    intent22.putExtra("longitud",longitud);
                    intent22.putExtra("direccion",cli_direccion);
                    activity22.startActivity(intent22);
                }catch (Exception e){
                    //e.printStackTrace();
                    Toast ts = Toast.makeText(context, "No existen coordenadas validas, en los puntos de georeferenciacion", Toast.LENGTH_LONG);
                    ts.show();
                }
                break;
            case R.id.menu_cliente_set_ubicacion:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setTitle("Actualizacion de coordenadas geograficas");
                alertDialogBuilder
                        .setMessage("A continuacion se obtendran las coordenadas geograficas, de la ubicacion actual del dispositivo, ¿está seguro de continuar?")
                        .setCancelable(false)
                        .setPositiveButton("Proceder",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                ObtenerCoordenadasGeograficas obtenerCoordenadas = new ObtenerCoordenadasGeograficas();
                                obtenerCoordenadas.execute(cli_identificador,cli_nombres,cli_direccion);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
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

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        clickListener.onClick(view, getPosition(), false);
    }

    class ObtenerCoordenadasGeograficas extends AsyncTask<String, Integer, Boolean>{
        private ProgressDialog progress;
        private CoordenadasGeograficas coor;
        private Localizacion localizacion;
        private String nombre,direccion;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            coor = new CoordenadasGeograficas(context);
            coor.getLocation();
            localizacion=new Localizacion(0.0,0.0);
            progress=new ProgressDialog(context);
            progress.setTitle("Obteniendo coordenadas geograficas");
            progress.setMessage("Espere...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(progress.isShowing()){
                progress.dismiss();
                if(aBoolean){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setTitle("Coordenadas Obtenidas");
                    alertDialogBuilder
                            .setMessage("Latitud: "+localizacion.getLatitud()+"    Longitud:"+localizacion.getLongitud())
                            .setCancelable(false)
                            .setPositiveButton("Ver en GoogleMaps",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    Activity activity22=(Activity)context;
                                    Intent intent22 = new Intent(activity22, Localizacion_Cliente.class);
                                    intent22.putExtra("cliente",nombre);
                                    intent22.putExtra("latitud",localizacion.getLatitud());
                                    intent22.putExtra("longitud",localizacion.getLongitud());
                                    intent22.putExtra("direccion",direccion);
                                    activity22.startActivity(intent22);
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else Toast.makeText(context,"No es posible obtener las coordenadas geograficas, verifique que el dispositivo tenga una conexion a internet",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try{
                nombre = strings[1];
                direccion = strings[2];
                localizacion = coor.getLocalizacion();
                Thread.sleep(3000);
                if(new Validaciones().validar_coordenadas(localizacion.getLatitud()+","+localizacion.getLongitud())){
                    DBSistemaGestion  helper = new DBSistemaGestion(context);
                    helper.modificarUbicacionGeografica(strings[0],localizacion.getLatitud()+","+localizacion.getLongitud());
                    helper.close();
                    return true;
                } else return false;
            }catch (Exception e){
                e.printStackTrace();
            }
            return false;
        }
    }
}
