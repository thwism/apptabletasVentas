package ibzssoft.com.adaptadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author eduardo
 */
public class ValidateReferencia {

    private Pattern pattern;
    private Matcher matcher;

    private static final String REF_PATTERN =
            "([A-Z]{2,5}\\-[0-9]{1,10})";

    public ValidateReferencia() {
        pattern = Pattern.compile(REF_PATTERN);
    }

    public boolean validate(String hex) {
        System.out.println("Validando referencia: "+hex);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
