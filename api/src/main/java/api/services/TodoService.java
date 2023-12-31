package api.services;

import api.contracts.ICrudService;
import api.models.Todo;
import api.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
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
}
