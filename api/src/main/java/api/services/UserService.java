package api.services;

import api.models.User;
import api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public User create(User entity) {
        entity.setId(null);
        return this.repository.save(entity);
    }
}
