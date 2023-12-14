package api.controllers.v1;

import api.dtos.requests.UserRegistrationRequestDTO;
import api.dtos.responses.Response;
import api.models.User;
import api.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
@RestController
public class AuthController {
    private final UserService userService;

    @PostMapping(path = "/register")
    public Response<User> register(@RequestBody UserRegistrationRequestDTO request) {
        return null;
    }
}
