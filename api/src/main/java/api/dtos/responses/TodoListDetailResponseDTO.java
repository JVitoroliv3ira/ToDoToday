package api.dtos.responses;

import api.models.TodoList;
import lombok.Getter;

@Getter
public class TodoListDetailResponseDTO {
    private final Long id;
    private final String title;
    private final String description;

    public TodoListDetailResponseDTO(TodoList todoList) {
        this.id = todoList.getId();
        this.title = todoList.getTitle();
        this.description = todoList.getDescription();
    }
}
