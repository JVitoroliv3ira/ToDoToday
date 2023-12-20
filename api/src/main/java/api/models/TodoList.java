package api.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(schema = "todo_today", name = "tb_todo_lists")
@Entity
public class TodoList {
    @Id
    @SequenceGenerator(name = "sq_todo_lists", schema = "todo_today", sequenceName = "sq_todo_lists", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_todo_lists")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoList todoList = (TodoList) o;
        return Objects.equals(id, todoList.id) && Objects.equals(title, todoList.title) && Objects.equals(description, todoList.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }
}
