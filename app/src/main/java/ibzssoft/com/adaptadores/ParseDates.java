package ibzssoft.com.adaptadores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by root on 13/04/16.
 */
public class ParseDates {

    public Date changeStringToDateSimple(String fecha){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  convertedDate;
    }
    public Date changeStringToDateSimpleFormat1(String fecha){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  convertedDate;
    }
    public Date changeStringToDateSimpleFormat2(String fecha){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  convertedDate;
    }
    public Date changeStringToDateSimpleFormat(String fecha){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  convertedDate;
    }
    public String changeDateToStringSimple(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String time=sdf.format(fecha);
        return time;
    }
    public String changeDateToStringCompact(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        String time=sdf.format(fecha);
        return time;
    }
    public String changeDateToStringSimpleYear(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.US);
        String time=sdf.format(fecha);
        return time;
    }
    public String changeDateToStringSimple1(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String time=sdf.format(fecha);
        return time;
    }
    public String changeDateToStringSimpleNow(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String time=sdf.format(new Date());
        return time;
    }
    public String changeDateToStringSimpleTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
        String time=sdf.format(new Date());
        return time;
    }
    public String getNowDateString(){
        return changeDateToStringSimpleNow();
    }

    public Date sumarRestarDiasYear(Date fecha, int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.YEAR, -1);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }
}
