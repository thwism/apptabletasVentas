package ibzssoft.com.ishidamovile;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import java.util.List;
import ibzssoft.com.adaptadores.Alertas;
import ibzssoft.com.adaptadores.ExtraerConfiguraciones;
import ibzssoft.com.eliminar.EliminarProvincias;
import ibzssoft.com.storage.DBSistemaGestion;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class Configuraciones extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static final int SELECT_PICTURE = 1;
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || ConexionPreferenceFragment.class.getName().equals(fragmentName)
                || TransaccionesPreferenceFragment.class.getName().equals(fragmentName)
                || EtiquetasPreferenceFragment.class.getName().equals(fragmentName)
                || SesionPreferenceFragment.class.getName().equals(fragmentName)
                || DecimalesFragment.class.getName().equals(fragmentName)
                || WSPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        String imagePath;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);
            imagePath="";
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_codigo)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_base)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_nombre)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_direccion)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_telefono)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_logo)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_iva)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_decimales)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_empresa_nro_compilacion)));
            Preference pref_ini= (Preference)getPreferenceManager().findPreference("conf_logotipo");
            Preference pref_str= (Preference)getPreferenceManager().findPreference("conf_storage");
            ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
            pref_str.setSummary("Version Actual: "+getString(R.string.pref_nro_compilacion_actual));
            if(pref_ini!=null){
                pref_ini.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        System.out.println("Click en la preferencia");
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Seleccionar Logotipo"), SELECT_PICTURE);
                        return false;
                    }
                });
            }
            if(pref_str!=null){
                pref_str.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        try{
                            ExtraerConfiguraciones ext1 = new ExtraerConfiguraciones(getActivity());
                            DBSistemaGestion helper = new DBSistemaGestion(getActivity());
                            double act= Double.parseDouble(getString(R.string.pref_nro_compilacion_actual));
                            double ant= Double.parseDouble(ext1.get(getString(R.string.key_empresa_nro_compilacion),getString(R.string.pref_nro_compilacion_default)));
                            String comands = helper.upgradeDatabase(ant,act);
                            new Alertas(getActivity(),"Actualizacion Realizada Correctamente",comands).mostrarMensaje();
                            ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
                            ext.update(getString(R.string.key_empresa_nro_compilacion),getString(R.string.pref_nro_compilacion_actual));
                            helper.close();
                        }catch (Exception e){
                            new Alertas(getActivity(),"Error Actualizado BD",e.getMessage()).mostrarMensaje();
                        }
                        return false;
                    }
                });
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            System.out.println("result: "+resultCode);
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    Uri selectedImageUri = data.getData();
                    imagePath = getPath(selectedImageUri);
                    System.out.println("Cambiando directorio: "+imagePath);
                    ExtraerConfiguraciones ext = new ExtraerConfiguraciones(getActivity());
                    ext.update(getString(R.string.key_empresa_logo),imagePath);
                }
            }
        }
        public String getPath(Uri uri) {
            // just some safety built in
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class EtiquetasPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_etiquetas);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g1)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g2)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g3)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g4)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g5)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_g6)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pcg1)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pcg2)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pcg3)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_pcg4)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class WSPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_ws);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_test)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_provincias)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cantones)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_parroquias)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_bodegas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_bancos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_formas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_gnopcion)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_gntrans)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_empresas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_grupos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_permisos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_perxtran)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_usuarios)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_vendedores)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cartera_vendedor)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cartera_cobrador)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cartera_ruta)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cheques)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_pcg1)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_pcg2)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_pcg3)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_pcg4)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_clientes_vendedor)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_clientes_cobrador)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_clientes_all)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_descuentos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_existencias)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_facturas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_items_pendientes)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg1)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg2)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg3)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg4)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg5)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ivg6)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_productos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_promociones)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_transacciones)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_cierreventas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_balance)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_visitas)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_imagenes)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_upload_image)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_ws_ubicacion_gps)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DecimalesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_decimales);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_num_dec_punitario)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_num_dec_ptotal)));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ConexionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_conexion);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_ip)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_port)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_url)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sinc_clientes)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sinc_productos)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_sinc_inicial)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_ini)));
            Preference pref_ini= (Preference)getPreferenceManager().findPreference("import_ini");
            if(pref_ini!=null){
                pref_ini.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        System.out.println("Click en la preferencia");
                        Intent intent= new Intent(getActivity(),Bienvenida.class);
                        startActivity(intent);
                        return false;
                    }
                });
            }
            Preference pref_clear= (Preference)getPreferenceManager().findPreference("import_clear");
            if(pref_clear!=null){
                pref_clear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        android.support.v7.app.AlertDialog.Builder quitDialog
                                = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        quitDialog.setTitle("Estas seguro de eliminar todos los catálogos cargados en el sistema?");

                        quitDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                EliminarProvincias eliminarProvincias = new EliminarProvincias(getActivity());
                                eliminarProvincias.ejecutarTarea();
                            }});

                        quitDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }});

                        quitDialog.show();

                        return false;
                    }
                });
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_imprimir);
            setHasOptionsMenu(true);
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class TransaccionesPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_transacciones);
            setHasOptionsMenu(true);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_num_pcg)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_num_lin)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_nombre_impresora)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_descarga_cartera)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_descarga_clientes)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_tipo_envio_cobro)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_aplica_calculo_mensual)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_tasa_mora)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_dias_gracia_mora)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_conf_ishida_movil)));
            Preference pref_export= (Preference)getPreferenceManager().findPreference("conf_export_img");
            if(pref_export!=null){
                pref_export.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent= new Intent(getActivity(),Catalogo_Productos.class);
                        startActivity(intent);
                        return false;
                    }
                });
            }
            Preference pref_ini= (Preference)getPreferenceManager().findPreference("conf_transacciones");
            if(pref_ini!=null){
                pref_ini.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        Intent intent= new Intent(getActivity(),Permisos.class);
                        startActivity(intent);
                        return false;
                    }
                });
            }
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SesionPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sesion);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_user)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_lin)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_acc)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_rut)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_act_bod)));
            Preference pref_ini= (Preference)getPreferenceManager().findPreference("conf_sesiones");
            if(pref_ini!=null){
                pref_ini.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        System.out.println("Click en la preferencia");
                        Intent intent= new Intent(getActivity(),Catalogo_Vendedores.class);
                        startActivity(intent);
                        return false;
                    }
                });
            }
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_conexion);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), Configuraciones.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
