package api.repositories;

import api.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = {"h2"})
@DataJpaTest
class UserRepositoryTest {
    private final UserRepository repository;

    @Autowired
    public UserRepositoryTest(UserRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
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
    void save_should_insert_user_in_database() {
        User payload = this.buildUserPayload(null, "payload", "payload@payload.com", "payload_123");
        User createdUser = this.repository.save(payload);
        boolean result = this.repository.existsById(createdUser.getId());
        assertTrue(result);
    }

    @Test
    void exists_by_email_should_return_false_when_requested_user_does_not_exists() {
        String payload = "payload@payload.com";
        Boolean result = this.repository.existsByEmail(payload);
        assertFalse(result);
    }

    @Test
    void exists_by_email_should_return_true_when_requested_user_exists() {
        User payload = this.buildUserPayload(null, "payload", "payload@payload.com", "payload_123");
        this.repository.save(payload);
        Boolean result = this.repository.existsByEmail(payload.getEmail());
        assertTrue(result);
    }

    @Test
    void find_by_email_should_return_empty_optional_when_requested_user_does_not_exists() {
        String payload = "payload@payload.com";
        Optional<User> result = this.repository.findByEmail(payload);
        assertTrue(result.isEmpty());
    }

    @Test
    void find_by_email_should_return_non_empty_optional_when_requested_user_exists() {
        User payload = this.buildUserPayload(null, "payload", "payload@payload.com", "payload_123");
        this.repository.save(payload);
        Optional<User> result = this.repository.findByEmail(payload.getEmail());
        assertTrue(result.isPresent());
    }
}