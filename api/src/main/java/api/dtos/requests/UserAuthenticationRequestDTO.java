package api.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserAuthenticationRequestDTO {
    @NotNull(message = "O e-mail não pode ser nulo.")
    @Size(min = 5, max = 80, message = "O e-mail deve ter entre 5 e 80 caracteres.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;

    @NotNull(message = "A senha não pode ser nula.")
    @Size(min = 6, max = 120, message = "A senha deve ter entre 6 e 120 caracteres.")
    private String password;
}
