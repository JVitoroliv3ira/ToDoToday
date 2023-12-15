package api.services;

import api.exceptions.UnprocessableEntityException;
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

    public void validateEmailUniqueness(String email) {
        if (Boolean.TRUE.equals(this.repository.existsByEmail(email))) {
            throw new UnprocessableEntityException("Este e-mail já está sendo utilizado.");
        }
    }
}
