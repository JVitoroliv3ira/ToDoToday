package api.services;

import api.dtos.DetailsDTO;
import api.exceptions.BadRequestException;
import api.models.User;
import api.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles(profiles = {"h2"})
@SpringBootTest
class UserServiceTest {
    @Mock
    private final UserRepository userRepository;
    private UserService userService;

    private static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado na base de dados.";
    private static final String EMAIL_ALREADY_IN_USE_MESSAGE = "Este e-mail já está sendo utilizado.";

    @Autowired
    public UserServiceTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        this.userService = new UserService(this.userRepository);
    }

    private User buildUserPayload(Long id, String name, String email, String password) {
        return User
                .builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    void create_should_call_save_method_of_user_repository() {
        User payload = this.buildUserPayload(null, "payload", "payload@payload.com", "payload_123");
        this.userService.create(payload);
        verify(this.userRepository, times(1)).save(payload);
    }

    @Test
    void create_should_set_payload_id_to_null_before_call_save_method_of_user_repository() {
        User payload = this.buildUserPayload(7L, "payload payload", "p@payload.com", "payload123");
        this.userService.create(payload);
        verify(this.userRepository, times(1))
                .save(
                        this.buildUserPayload(null, "payload payload", "p@payload.com", "payload123")
                );
    }

    @Test
    void create_should_return_registered_user() {
        User payload = this.buildUserPayload(null, "payload", "payload@payload.com", "payload_123");
        User expected = this.buildUserPayload(1L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.save(payload)).thenReturn(expected);
        User result = this.userService.create(payload);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void read_should_call_find_by_id_method_of_user_repository() {
        User payload = this.buildUserPayload(1L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.findById(payload.getId())).thenReturn(Optional.of(payload));
        this.userService.read(payload.getId());
        verify(this.userRepository, times(1)).findById(payload.getId());
    }

    @Test
    void read_should_throw_an_exception_when_requested_user_does_not_exists() {
        Long payload = 34L;
        when(this.userRepository.findById(payload)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> this.userService.read(payload))
                .hasMessage(USER_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_should_call_exists_by_id_method_of_user_repository() {
        User payload = this.buildUserPayload(1L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.existsById(payload.getId())).thenReturn(Boolean.TRUE);
        this.userService.update(payload);
        verify(this.userRepository, times(1)).existsById(payload.getId());
    }

    @Test
    void update_should_throw_an_exception_when_requested_user_does_not_exists() {
        User payload = this.buildUserPayload(1L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.existsById(payload.getId())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> this.userService.update(payload))
                .hasMessage(USER_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void update_should_call_save_method_of_user_repository() {
        User payload = this.buildUserPayload(1L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.existsById(payload.getId())).thenReturn(Boolean.TRUE);
        this.userService.update(payload);
        verify(this.userRepository, times(1)).save(payload);
    }

    @Test
    void delete_should_call_exists_by_id_method_of_user_repository() {
        Long payload = 36L;
        when(this.userRepository.existsById(payload)).thenReturn(Boolean.TRUE);
        this.userService.delete(payload);
        verify(this.userRepository, times(1)).existsById(payload);
    }

    @Test
    void delete_should_throw_an_exception_when_requested_user_does_not_exists() {
        Long payload = 36L;
        when(this.userRepository.existsById(payload)).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> this.userService.delete(payload))
                .hasMessage(USER_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void delete_should_call_delete_by_id_method() {
        Long payload = 36L;
        when(this.userRepository.existsById(payload)).thenReturn(Boolean.TRUE);
        this.userService.delete(payload);
        verify(this.userRepository, times(1)).deleteById(payload);
    }

    @Test
    void validate_email_uniqueness_should_call_exists_by_email_method_of_user_repository() {
        String payload = "payload@payload.com";
        when(this.userRepository.existsByEmail(payload)).thenReturn(Boolean.FALSE);
        this.userService.validateEmailUniqueness(payload);
        verify(this.userRepository, times(1)).existsByEmail(payload);
    }

    @Test
    void validate_email_uniqueness_should_throw_an_exception_when_requested_email_is_already_in_use() {
        String payload = "payload@payload.com";
        when(this.userRepository.existsByEmail(payload)).thenReturn(Boolean.TRUE);
        assertThatThrownBy(() -> this.userService.validateEmailUniqueness(payload))
                .hasMessage(EMAIL_ALREADY_IN_USE_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void load_user_by_username_should_call_find_by_email_method() {
        User payload = this.buildUserPayload(2L, "payload", "payload@payload.com", "payload_123");
        when(this.userRepository.findByEmail(payload.getEmail())).thenReturn(Optional.of(payload));
        this.userService.loadUserByUsername(payload.getEmail());
        verify(this.userRepository, times(1)).findByEmail(payload.getEmail());
    }

    @Test
    void load_user_by_username_should_throw_an_exception_when_requested_user_does_not_exists() {
        String payload = "payload@payload.com";
        when(this.userRepository.findByEmail(payload)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> this.userService.loadUserByUsername(payload))
                .hasMessage(USER_NOT_FOUND_MESSAGE)
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void load_user_by_username_should_return_details_of_requested_user() {
        User payload = this.buildUserPayload(2L, "payload", "payload@payload.com", "payload_123");
        UserDetails expected = new DetailsDTO(payload);
        when(this.userRepository.findByEmail(payload.getEmail())).thenReturn(Optional.of(payload));
        UserDetails result = this.userService.loadUserByUsername(payload.getEmail());
        Assertions.assertEquals(expected, result);
    }
}
