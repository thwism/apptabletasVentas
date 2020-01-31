package ibzssoft.com.adaptadores;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 20/04/16.
 */
public class Validaciones {

    private Pattern pattern;
    private Matcher matcher;

    private static final String TRAMA_3_DIGITS = "[0-9]{3}";
    private static final String TRAMA_9_DIGITS = "[0-9]{9}";
    private static final String TRAMA_10_DIGITS = "[0-9]{10}";
    private static final String TRAMA_37_DIGITS = "[0-9]{37}";
    private static final String TRAMA_49_DIGITS = "[0-9]{49}";
    private static final String TRAMA_FECHA_MASK="(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d";
    private static final String EXPRESION_NUMEROS_ENTEROS= "([1-9])\\d*";
    private static final String EXPRESION_NUMEROS_DECIMALES="(?:\\d*\\.)?\\d+";
    private static final String EXPRESION_COORDENADAS_GEOGRAFICAS="^([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?)$";

    public Validaciones() {

    }
    public boolean validar_coordenadas(String coordenadas) {
        this.pattern = pattern.compile(EXPRESION_COORDENADAS_GEOGRAFICAS);
        matcher = pattern.matcher(coordenadas);
        return matcher.matches();
    }

    public boolean validar_enteros(String hex) {
        this.pattern = pattern.compile(EXPRESION_NUMEROS_ENTEROS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean validar_decimales(String hex) {
        this.pattern = pattern.compile(EXPRESION_NUMEROS_DECIMALES);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public boolean validate_3_digits(String hex) {
        this.pattern = pattern.compile(TRAMA_3_DIGITS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public boolean validate_9_digits(String hex) {
        this.pattern = pattern.compile(TRAMA_9_DIGITS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public boolean validate_10_digits(String hex) {
        this.pattern = pattern.compile(TRAMA_10_DIGITS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public boolean validate_37_digits(String hex) {
        this.pattern = pattern.compile(TRAMA_37_DIGITS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean validate_49_digits(String hex) {
        this.pattern = pattern.compile(TRAMA_49_DIGITS);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean validate_formato_fecha(String hex) {
        this.pattern = pattern.compile(TRAMA_FECHA_MASK);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public boolean validarFechaChequePostfechado(String fecha){
        try{
            Date fecha_vencimiento  = new ParseDates().changeStringToDateSimpleFormat1(fecha);
            Date now = new Date();
            if(now.before(fecha_vencimiento)){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }
}
