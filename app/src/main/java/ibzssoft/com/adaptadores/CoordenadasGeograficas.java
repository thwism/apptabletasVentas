package ibzssoft.com.adaptadores;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.*;

import ibzssoft.com.modelo.Localizacion;

import android.provider.Settings;
/**
 * Created by Usuario-pc on 09/01/2017.
 */
public class CoordenadasGeograficas implements LocationListener{
    private Context context;
    LocationManager locationManager;
    String proveedor;
    private boolean networkOn;
    private Localizacion localizacion;
    public CoordenadasGeograficas(Context context) {
        this.context = context;
        localizacion = new Localizacion(0.0,0.0);
        this.locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        proveedor = LocationManager.NETWORK_PROVIDER;
        networkOn = locationManager.isProviderEnabled(proveedor);
        locationManager.requestLocationUpdates(proveedor,100,1,this);
    }

    public Localizacion getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Localizacion localizacion) {
        this.localizacion = localizacion;
    }

    public void getLocation(){
        if(networkOn){
            Location lc= locationManager.getLastKnownLocation(proveedor);
            if(lc!=null){
                StringBuilder builder = new StringBuilder();
                localizacion = new Localizacion(lc.getLatitude(),lc.getLongitude());
                builder.append("Latitud: ").append(lc.getLatitude()).append(" Longitud: ").append(lc.getLongitude());
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        showSettingsAlert();
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("¿Utilizar la Ubicacion?");

        // Setting Dialog Message
        alertDialog.setMessage("Esta aplicacion quiere que cambies los ajustes del dispositivo, habilitar GPS para determinar la ubicacion");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Cambiar", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Ahora no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
