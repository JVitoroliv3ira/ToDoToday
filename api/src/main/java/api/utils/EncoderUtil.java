package api.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncoderUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public static String encode(String value) {
        return encoder.encode(value);
    }

    public static Boolean matches(String rawValue, String encodedValue) {
        return encoder.matches(rawValue, encodedValue);
    }
}
