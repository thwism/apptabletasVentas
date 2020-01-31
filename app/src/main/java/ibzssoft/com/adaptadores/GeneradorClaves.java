package ibzssoft.com.adaptadores;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by root on 09/11/15.
 */
public class GeneradorClaves {

    public String generarClave(){
        String result="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
        String time=sdf.format(new Date());
        String []datos=time.split(" ");
        String fecha=datos[0];
        String hora=datos[1];
        String []datos2=fecha.split("-");
        String []datos3=hora.split(":");
        int numAleatorio = (int)Math.floor(Math.random()*(90 -65)+65);
        Random r = new Random();
        int valor = r.nextInt(10)+1;
        result+=datos2[0]+datos2[1]+datos2[2]+datos3[0]+datos3[1]+datos3[2]+datos3[3]+valor+(char)numAleatorio;
        return result;
    }
}
