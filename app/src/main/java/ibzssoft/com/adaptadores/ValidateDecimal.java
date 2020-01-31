package ibzssoft.com.adaptadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author eduardo
 */
public class ValidateDecimal {

    private Pattern pattern;
    private Matcher matcher;

    private static final String TRAMA_PATTERN =
            "[0-9]+(\\.[0-9][0-9]?)?";

    public ValidateDecimal() {
        pattern = Pattern.compile(TRAMA_PATTERN);
    }

    public boolean validate(String hex) {
        System.out.println("Valor a validar: "+hex);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
