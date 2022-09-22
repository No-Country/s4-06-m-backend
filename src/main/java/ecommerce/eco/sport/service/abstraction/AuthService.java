package ecommerce.eco.sport.service.abstraction;

import ecommerce.eco.sport.model.request.AuthRequest;
import ecommerce.eco.sport.model.request.UserRequest;
import ecommerce.eco.sport.model.response.AuthResponse;

public interface AuthService {
    AuthResponse register(UserRequest request);

    AuthResponse login(AuthRequest request);
}
