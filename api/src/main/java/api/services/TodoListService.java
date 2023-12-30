package api.services;

import api.contracts.ICrudService;
import api.dtos.responses.TodoListDetailResponseDTO;
import api.exceptions.BadRequestException;
import api.models.TodoList;
import api.models.User;
import api.repositories.TodoListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TodoListService implements ICrudService<TodoList, Long, TodoListRepository> {
    private final TodoListRepository repository;

    @Override
    public TodoListRepository getRepository() {
        return this.repository;
    }

    @Override
    public String getNotFoundMessage() {
        return "Lista de tarefas não encontrada na base de dados.";
    }

    public void validateTodoListOwnership(Long id, User user) {
        if (Boolean.FALSE.equals(this.repository.existsByIdAndOwner(id, user))) {
            throw new BadRequestException("Você não tem permissão para realizar esta ação.");
        }
    }

    public Page<TodoListDetailResponseDTO> findAllByOwner(Integer pageNumber, Integer size, User owner) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        return this.repository.findAllByOwner(pageable, owner).map(TodoListDetailResponseDTO::new);
    }
}
