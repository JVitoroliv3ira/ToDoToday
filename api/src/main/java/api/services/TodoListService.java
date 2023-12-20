package api.services;

import api.contracts.ICrudService;
import api.models.TodoList;
import api.repositories.TodoListRepository;
import lombok.RequiredArgsConstructor;
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
}
