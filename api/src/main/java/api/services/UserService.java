package api.services;

import api.contracts.ICrudService;
import api.dtos.DetailsDTO;
import api.exceptions.BadRequestException;
import api.models.User;
import api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements ICrudService<User, Long, UserRepository>, UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserRepository getRepository() {
        return this.repository;
    }

    @Override
    public String getNotFoundMessage() {
        return "Usuário não encontrado na base de dados.";
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
                .orElseThrow(() -> new BadRequestException(this.getNotFoundMessage()));
        return new DetailsDTO(user);
    }
}
