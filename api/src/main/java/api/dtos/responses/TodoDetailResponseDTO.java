package api.dtos.responses;

import api.models.Todo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Getter
public class TodoDetailResponseDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final Boolean finished;

    public TodoDetailResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.dueDate = todo.getDueDate();
        this.finished = todo.getFinished();
    }

    @JsonSerialize
    public Boolean deadlineHasPassed() {
        if (Objects.isNull(dueDate)) {
            return Boolean.FALSE;
        }

        return LocalDate.now().isAfter(this.getDueDate());
    }
}
