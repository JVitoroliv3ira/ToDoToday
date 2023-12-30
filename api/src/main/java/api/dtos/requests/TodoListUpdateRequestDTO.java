package api.dtos.requests;

import api.models.TodoList;
import api.models.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TodoListUpdateRequestDTO {
    @NotNull(message = "Informe a lista que deseja atualizar.")
    private Long id;

    @NotNull(message = "O título não pode ser nulo.")
    @Size(min = 1, max = 60, message = "O título deve ter entre {min} e {max} caracteres.")
    private String title;

    @Size(min = 0, max = 500, message = "A descrição deve conter no máximo {max} caracteres.")
    private String description;

    public TodoList convert(User owner) {
        return TodoList
                .builder()
                .id(this.getId())
                .title(this.getTitle())
                .description(this.getDescription())
                .owner(owner)
                .build();
    }
}
