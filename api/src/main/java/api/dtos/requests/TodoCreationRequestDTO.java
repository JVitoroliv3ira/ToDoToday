package api.dtos.requests;

import api.models.Todo;
import api.models.TodoList;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;

@Getter
public class TodoCreationRequestDTO {
    @NotNull(message = "Para adicionar uma nova tarefa, por favor, especifique-a na lista de tarefas correspondente.")
    private Long todoListId;

    @NotNull(message = "O título não pode ser nulo.")
    @Size(max = 70, message = "O título deve ter entre {min} e {max} caracteres.")
    private String title;

    @Size(max = 1000, message = "A descrição deve conter no máximo {max} caracteres.")
    private String description;

    private LocalDate dueDate;

    public Todo convert() {
        return Todo
                .builder()
                .title(this.getTitle())
                .description(this.getDescription())
                .finished(Boolean.FALSE)
                .dueDate(this.getDueDate())
                .todoList(TodoList.builder().id(this.getTodoListId()).build())
                .build();
    }
}
