package api.models;

import api.contracts.IEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(schema = "todo_today", name = "tb_todos")
@Entity
public class Todo implements IEntity<Long> {
    @Id
    @SequenceGenerator(name = "sq_todos", schema = "todo_today", sequenceName = "sq_todos", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_todos")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "finished")
    private Boolean finished;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "todo_list_id", nullable = false)
    private TodoList todoList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) && Objects.equals(title, todo.title) && Objects.equals(description, todo.description) && Objects.equals(finished, todo.finished) && Objects.equals(dueDate, todo.dueDate) && Objects.equals(todoList, todo.todoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, finished, dueDate, todoList);
    }
}
