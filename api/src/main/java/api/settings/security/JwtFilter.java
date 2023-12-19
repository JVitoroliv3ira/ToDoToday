package api.settings.security;

import api.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    private final AuthenticationService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (Boolean.TRUE.equals(Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX))) {
            String token = authorizationHeader.replace(TOKEN_PREFIX, "");
            if (Boolean.TRUE.equals(!token.isEmpty())) {
                if (Boolean.TRUE.equals(this.authService.isTokenValid(token))) {
                    String email = this.authService.getTokenSubject(token);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            new ArrayList<>()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
