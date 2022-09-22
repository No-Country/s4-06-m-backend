package ecommerce.eco.sport.service;

import ecommerce.eco.sport.config.utils.JwtUtil;
import ecommerce.eco.sport.exception.InvalidCredentialsException;
import ecommerce.eco.sport.exception.UserAlreadyExistException;
import ecommerce.eco.sport.model.entity.User;
import ecommerce.eco.sport.model.mapper.UserMapper;
import ecommerce.eco.sport.model.request.AuthRequest;
import ecommerce.eco.sport.model.request.UserRequest;
import ecommerce.eco.sport.model.response.AuthResponse;
import ecommerce.eco.sport.repository.UserRepository;
import ecommerce.eco.sport.service.abstraction.AuthService;
import ecommerce.eco.sport.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtToken;

    private final AuthenticationManager authenticationManager;

    private User getUser(String username) {
        User user = userRepository.findByEmail(username);
        if (user == null || !user.isEnabled()) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        return user;
    }
    @Override
    public AuthResponse register(UserRequest request) {
        if(userRepository.findByEmail(request.getEmail()) != null){
            throw new UserAlreadyExistException("Email is already in use.");
        }
        User userCreate = userRepository.save(userMapper.entityToDto(request));
        AuthResponse response = userMapper.dtoToEntity(userCreate);
        response.setToken(jwtToken.generateToken(userCreate));
        return response;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticate(request);
        User user = getUser(request.getEmail());
        LOGGER.warn("Correo:" + user.getEmail());
        AuthResponse a=userMapper.dtoToEntity(user);
        a.setToken(jwtToken.generateToken(user));
        return a;
    }

    private void authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        }catch (Exception e){
            throw new InvalidCredentialsException("Invalid email or password.");
        }
    }
}
