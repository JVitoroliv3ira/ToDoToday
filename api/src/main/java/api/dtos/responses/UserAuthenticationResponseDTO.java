package api.dtos.responses;

import lombok.Data;

@Data
public class UserAuthenticationResponseDTO {
    private final String email;
    private final String token;
}
