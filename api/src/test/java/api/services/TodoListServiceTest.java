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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private static final String TODO_LIST_OWNERSHIP_FAILURE_MESSAGE = "Você não tem permissão para realizar esta ação.";
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

    @Test
    void validate_todo_list_ownership_should_call_exists_by_id_and_owner_method_of_todo_list_repository() {
        Long payload = 21L;
        when(this.todoListRepository.existsByIdAndOwner(payload, ownerPayload)).thenReturn(Boolean.TRUE);
        this.todoListService.validateTodoListOwnership(payload, ownerPayload);
        verify(this.todoListRepository, times(1)).existsByIdAndOwner(payload, ownerPayload);
    }

    @Test
    void validate_todo_list_ownership_should_throw_an_exception_when_requested_todo_list_does_not_exists() {
        Long payload = 22L;
        when(this.todoListRepository.existsByIdAndOwner(payload, ownerPayload)).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> this.todoListService.validateTodoListOwnership(payload, ownerPayload))
                .hasMessage(TODO_LIST_OWNERSHIP_FAILURE_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void find_all_by_owner_should_call_find_all_by_owner_method_of_todo_list_repository() {
        int pageNumberPayload = 0;
        int pageSizePayload = 5;
        Pageable pageablePayload = PageRequest.of(pageNumberPayload, pageSizePayload);
        when(this.todoListRepository.findAllByOwner(pageablePayload, ownerPayload)).thenReturn(Page.empty());
        this.todoListService.findAllByOwner(pageNumberPayload, pageSizePayload, ownerPayload);
        verify(this.todoListRepository, times(1)).findAllByOwner(
                pageablePayload,
                ownerPayload
        );
    }
}