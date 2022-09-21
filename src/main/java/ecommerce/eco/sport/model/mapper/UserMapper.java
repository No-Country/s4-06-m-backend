package ecommerce.eco.sport.model.mapper;

import ecommerce.eco.sport.model.entity.User;
import ecommerce.eco.sport.model.request.UserRequest;
import ecommerce.eco.sport.model.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ImageMapper imageMapper;
    public User entityToDto(UserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .fullName(request.getLastName())
                .softDeleted(Boolean.FALSE)
                .build();
    }
    public AuthResponse dtoToEntity(User user) {
      return AuthResponse.builder()
              .id(user.getId())
              .email(user.getEmail())
              .role(user.getRole().getName())
              .fullName(user.getFullName())
              .build();
    }
}
