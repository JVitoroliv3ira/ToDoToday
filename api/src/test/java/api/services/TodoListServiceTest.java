package api.services;

import api.exceptions.BadRequestException;
import api.models.TodoList;
import api.models.User;
import api.repositories.TodoListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles(profiles = {"h2"})
@SpringBootTest
class TodoListServiceTest {
    @Mock
    private final TodoListRepository todoListRepository;
    private TodoListService todoListService;

    private static final String TODO_LIST_NOT_FOUND_MESSAGE = "Lista de tarefas não encontrada na base de dados.";
    private final User ownerPayload = new User(1L, "payload", "payload@payload.com", "payload_123");

    @Autowired
    public TodoListServiceTest(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    @BeforeEach
    void setUp() {
        this.todoListService = new TodoListService(this.todoListRepository);
    }

    private TodoList buildTodoListPayload(Long id, String title, String description) {
        return TodoList
                .builder()
                .id(id)
                .title(title)
                .description(description)
                .owner(ownerPayload)
                .build();
    }

    @Test
    void create_should_call_save_method_of_todo_list_repository() {
        TodoList payload = this.buildTodoListPayload(null, "ToDo List 01", "Todo List 01");
        this.todoListService.create(payload);
        verify(this.todoListRepository, times(1)).save(payload);
    }

    @Test
    void create_should_set_id_to_null_before_call_save_method_of_todo_list_repository() {
        TodoList payload = this.buildTodoListPayload(7L, "ToDo List 02", "Todo List 02");
        this.todoListService.create(payload);
        verify(this.todoListRepository, times(1))
                .save(this.buildTodoListPayload(null, "ToDo List 02", "Todo List 02"));
    }

    @Test
    void read_should_call_find_by_id_method_of_todo_list_repository() {
        TodoList payload = this.buildTodoListPayload(5L, "ToDo List 03", "Todo List 03");
        when(this.todoListRepository.findById(payload.getId())).thenReturn(Optional.of(payload));
        this.todoListService.read(payload.getId());
        verify(this.todoListRepository, times(1)).findById(payload.getId());
    }

    @Test
    void read_should_throw_an_exception_when_requested_todo_list_does_not_exists() {
        Long payload = 8L;
        when(this.todoListRepository.findById(payload)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> this.todoListService.read(payload))
                .hasMessage(TODO_LIST_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_should_call_exists_by_id_method_of_todo_list_repository() {
        TodoList payload = this.buildTodoListPayload(5L, "ToDo List 03", "Todo List 03");
        when(this.todoListRepository.existsById(payload.getId())).thenReturn(Boolean.TRUE);
        this.todoListService.update(payload);
        verify(this.todoListRepository, times(1)).existsById(payload.getId());
    }

    @Test
    void update_should_throw_an_exception_when_requested_todo_list_does_not_exist() {
        TodoList payload = this.buildTodoListPayload(5L, "ToDo List 03", "Todo List 03");
        when(this.todoListRepository.existsById(payload.getId())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> this.todoListService.update(payload))
                .hasMessage(TODO_LIST_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_should_call_save_method_of_todo_list_repository() {
        TodoList payload = this.buildTodoListPayload(5L, "ToDo List 03", "Todo List 03");
        when(this.todoListRepository.existsById(payload.getId())).thenReturn(Boolean.TRUE);
        this.todoListService.update(payload);
        verify(this.todoListRepository, times(1)).save(payload);
    }

    @Test
    void delete_should_call_exists_by_id_method_of_todo_list_repository() {
        Long payload = 9L;
        when(this.todoListRepository.existsById(payload)).thenReturn(Boolean.TRUE);
        this.todoListService.delete(payload);
        verify(this.todoListRepository, times(1)).existsById(payload);
    }

    @Test
    void delete_should_throw_an_exception_when_requested_todo_list_does_not_exists() {
        Long payload = 19L;
        when(this.todoListRepository.existsById(payload)).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> this.todoListService.delete(payload))
                .hasMessage(TODO_LIST_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void delete_should_call_delete_by_id_method_of_todo_list_repository() {
        Long payload = 20L;
        when(this.todoListRepository.existsById(payload)).thenReturn(Boolean.TRUE);
        this.todoListService.delete(payload);
        verify(this.todoListRepository, times(1)).deleteById(payload);
    }
}