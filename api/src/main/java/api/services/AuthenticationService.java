package api.services;

import api.exceptions.BadRequestException;
import api.utils.EncoderUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AuthenticationService {

    private String SECRET_KEY;
    private Long EXPIRES_IN;
    private Algorithm ALGORITHM;

    public AuthenticationService(@Value("${jwt.secret_key}") String SECRET_KEY, @Value("${jwt.expires_in}") Long EXPIRES_IN) {
        this.SECRET_KEY = SECRET_KEY;
        this.EXPIRES_IN = EXPIRES_IN;
        this.ALGORITHM = Algorithm.HMAC256(this.SECRET_KEY);
    }

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + this.EXPIRES_IN))
                .sign(this.ALGORITHM);
    }

    public Boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(this.ALGORITHM).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    public String getTokenSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public void validateCredentials(String rawPassword, String encodedPassword) {
        if (Boolean.FALSE.equals(EncoderUtil.matches(rawPassword, encodedPassword))) {
            throw new BadRequestException("Credenciais incorretas. Por favor, verifique-as e tente novamente.");
        }
    }

    public String getAuthenticatedUserEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
