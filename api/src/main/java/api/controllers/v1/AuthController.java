package api.controllers.v1;

import api.dtos.requests.UserAuthenticationRequestDTO;
import api.dtos.requests.UserRegistrationRequestDTO;
import api.dtos.responses.Response;
import api.dtos.responses.UserAuthenticationResponseDTO;
import api.models.User;
import api.services.AuthenticationService;
import api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@RestController
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<Response<User>> register(@RequestBody @Valid UserRegistrationRequestDTO request) {
        this.userService.validateEmailUniqueness(request.getEmail());
        User user = this.userService.create(request.convert());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response<>(user, null));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Response<UserAuthenticationResponseDTO>> login(@RequestBody @Valid UserAuthenticationRequestDTO request) {
        UserDetails details = this.userService.loadUserByUsername(request.getEmail());
        this.authService.validateCredentials(request.getPassword(), details.getPassword());
        String token = this.authService.generateToken(request.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new UserAuthenticationResponseDTO(details.getUsername(), token), null));
    }
}
