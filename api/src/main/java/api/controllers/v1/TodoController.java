package api.controllers.v1;

import api.dtos.requests.TodoCreationRequestDTO;
import api.dtos.responses.PaginatedResponseDTO;
import api.dtos.responses.Response;
import api.dtos.responses.TodoDetailResponseDTO;
import api.models.Todo;
import api.models.User;
import api.services.AuthenticationService;
import api.services.TodoListService;
import api.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/todo")
@RestController
public class TodoController {
    private final TodoListService todoListService;
    private final TodoService todoService;
    private final AuthenticationService authenticationService;

    @PostMapping(path = "/create")
    public ResponseEntity<Response<TodoDetailResponseDTO>> create(@RequestBody @Valid TodoCreationRequestDTO request) {
        User authenticatedUser = this.authenticationService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(request.getTodoListId(), authenticatedUser);
        Todo createdTodo = this.todoService.create(request.convert());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new Response<>(new TodoDetailResponseDTO(createdTodo), null));
    }

    @GetMapping(path = "/read/{id}")
    public ResponseEntity<Response<TodoDetailResponseDTO>> read(@PathVariable("id") Long id) {
        Todo todo = this.todoService.read(id);
        User authenticatedUser = this.authenticationService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(todo.getTodoList().getId(), authenticatedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new TodoDetailResponseDTO(todo), null));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        Todo todo = this.todoService.read(id);
        User authenticatedUser = this.authenticationService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(todo.getTodoList().getId(), authenticatedUser);
        this.todoService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>("Tarefa deletada com sucesso.", null));
    }

    @PutMapping(path = "/toggle/{id}")
    public ResponseEntity<Response<TodoDetailResponseDTO>> toggle(@PathVariable("id") Long id) {
        Todo todo = this.todoService.read(id);
        User authenticatedUser = this.authenticationService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(todo.getTodoList().getId(), authenticatedUser);
        todo.setFinished(!todo.getFinished());
        Todo updatedTodo = this.todoService.update(todo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new TodoDetailResponseDTO(updatedTodo), null));
    }

    @GetMapping(path = "/list/{id}")
    public ResponseEntity<Response<PaginatedResponseDTO<TodoDetailResponseDTO>>> list(@PathVariable("id") Long id, @RequestParam Integer pageNumber, @RequestParam Integer size) {
        User authenticatedUser = this.authenticationService.getAuthenticatedUser();
        this.todoListService.validateTodoListOwnership(id, authenticatedUser);
        Page<TodoDetailResponseDTO> data = this.todoService.findAllByTodoListId(pageNumber, size, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response<>(new PaginatedResponseDTO<>(data), null));
    }
}
