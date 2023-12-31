package api.controllers.v1;

import api.services.AuthenticationService;
import api.services.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/todo")
@RestController
public class TodoController {
    private final TodoService todoService;
    private final AuthenticationService authenticationService;
}
