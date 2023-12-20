package api.repositories;

import api.models.TodoList;
import api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    Boolean existsByIdAndOwner(Long id, User owner);
}
