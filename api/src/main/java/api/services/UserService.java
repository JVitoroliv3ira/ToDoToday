package api.services;

import api.dtos.DetailsDTO;
import api.exceptions.BadRequestException;
import api.exceptions.UnprocessableEntityException;
import api.models.User;
import api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    public User create(User entity) {
        entity.setId(null);
        return this.repository.save(entity);
    }

    public void validateEmailUniqueness(String email) {
        if (Boolean.TRUE.equals(this.repository.existsByEmail(email))) {
            throw new BadRequestException("Este e-mail já está sendo utilizado.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws BadRequestException {
        User user = this.repository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado na base de dados."));
        return new DetailsDTO(user);
    }
}
