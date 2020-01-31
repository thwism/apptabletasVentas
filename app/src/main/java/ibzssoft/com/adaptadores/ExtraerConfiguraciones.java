package ibzssoft.com.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;

import ibzssoft.com.ishidamovile.R;
import ibzssoft.com.modelo.PCGrupo1;
import ibzssoft.com.modelo.PCGrupo2;
import ibzssoft.com.modelo.PCGrupo3;
import ibzssoft.com.modelo.PCGrupo4;

/**
 * Created by Ricardo on 25/11/2016.
 */
public class ExtraerConfiguraciones {
    private Context context;

    public ExtraerConfiguraciones(Context context) {
        this.context = context;
    }
    public void update(String key, String value){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public void updateBool(String key, boolean value){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public String configuracionPrecios(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        int result = settings.getInt(context.getString(R.string.key_act_num_pcg),Integer.parseInt(context.getString(R.string.pref_act_num_pcg_default)));
        String grupo = "";
        switch (result){
            case 1:
                    grupo = PCGrupo1.TABLE_NAME;
                break;
            case 2:
                    grupo = PCGrupo2.TABLE_NAME;
                break;
            case 3:
                    grupo = PCGrupo3.TABLE_NAME;
                break;
            case 4:
                    grupo = PCGrupo4.TABLE_NAME;
                break;
        }
        return grupo;
    }

    public int  configuracionPreciosGrupos(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String result = settings.getString(context.getString(R.string.key_act_num_pcg),context.getString(R.string.pref_act_num_pcg_default));
        return Integer.parseInt(result);
    }

    public String get(String key,String value_default){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, value_default);
    }
    public String [] getArray(String key, String value_default){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> selections = preferences.getStringSet(key, null);
        if(selections!=null){
            return selections.toArray(new String[] {});
        }else {
            return context.getResources().getStringArray(R.array.list_precios_default);
        }
    }
    public boolean getBoolean(String key,boolean value_default){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key,value_default);
    }
}
