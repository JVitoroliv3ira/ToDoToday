package api.services;

import api.contracts.ICrudService;
import api.dtos.responses.TodoDetailResponseDTO;
import api.models.Todo;
import api.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoService implements ICrudService<Todo, Long, TodoRepository> {
    private final TodoRepository repository;

    @Override
    public TodoRepository getRepository() {
        return this.repository;
    }

    @Override
    public String getNotFoundMessage() {
        return "Tarefa não encontrada na base de dados.";
    }

    public Page<TodoDetailResponseDTO> findAllByTodoListId(Integer pageNumber, Integer size, Long todoListId) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return this.repository.findAllByTodoListId(pageable, todoListId).map(TodoDetailResponseDTO::new);
    }
}
