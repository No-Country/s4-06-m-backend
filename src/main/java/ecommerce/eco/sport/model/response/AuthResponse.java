package ecommerce.eco.sport.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;
    private String email;
    private String role;
    private String fullName;
}
