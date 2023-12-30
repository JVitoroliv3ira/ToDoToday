package api.controllers.v1;

import api.dtos.requests.TodoListCreationRequestDTO;
import api.dtos.responses.PaginatedResponseDTO;
import api.dtos.responses.Response;
import api.dtos.responses.TodoListDetailResponseDTO;
import api.models.TodoList;
import api.models.User;
import api.services.AuthenticationService;
import api.services.TodoListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/todo-list")
@RestController
public class TodoListController {
    private final TodoListService todoListService;
    private final AuthenticationService authService;

    @PostMapping(path = "/create")
    public ResponseEntity<Response<TodoListDetailResponseDTO>> create(@RequestBody @Valid TodoListCreationRequestDTO request) {
        User owner = this.authService.getAuthenticatedUser();
        TodoList todoList = this.todoListService.create(request.convert(owner));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response<>(new TodoListDetailResponseDTO(todoList), null));
    }

    @GetMapping(path = "/read/{id}")
    public ResponseEntity<Response<TodoListDetailResponseDTO>> read(@PathVariable("id") Long id) {
        User authenticatedUser = this.authService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(id, authenticatedUser);
        TodoList todoList = this.todoListService.read(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new TodoListDetailResponseDTO(todoList), null));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        User authenticatedUser = this.authService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(id, authenticatedUser);
        this.todoListService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>("Lista de tarefas deletada com sucesso.", null));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<Response<PaginatedResponseDTO<TodoListDetailResponseDTO>>> list(@RequestParam Integer pageNumber, @RequestParam Integer size) {
        User owner = this.authService.getAuthenticatedUser();
        Page<TodoListDetailResponseDTO> data = this.todoListService.findAllByOwner(pageNumber, size, owner);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new PaginatedResponseDTO<>(data), null));
    }
}
