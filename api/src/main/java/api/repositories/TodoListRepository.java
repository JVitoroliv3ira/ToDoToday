package api.repositories;

import api.models.TodoList;
import api.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    Boolean existsByIdAndOwner(Long id, User owner);

    Page<TodoList> findAllByOwner(Pageable pageable, User owner);
}
