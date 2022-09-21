package ecommerce.eco.sport.service;

import ecommerce.eco.sport.config.utils.JwtUtil;
import ecommerce.eco.sport.model.entity.User;
import ecommerce.eco.sport.model.mapper.UserMapper;
import ecommerce.eco.sport.model.request.AuthRequest;
import ecommerce.eco.sport.model.request.UserRequest;
import ecommerce.eco.sport.model.response.AuthResponse;
import ecommerce.eco.sport.repository.UserRepository;
import ecommerce.eco.sport.service.abstraction.AuthService;
import ecommerce.eco.sport.service.abstraction.RoleService;
import ecommerce.eco.sport.util.RolesEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtUtil jwtToken;
    public final AuthenticationManager authenticationManager;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  getUser(username);
    }
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
        User user = userMapper.entityToDto(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(roleService.findBy(RolesEnum.USER.getFullRoleName()));
        User userCreate = userRepository.save(user);
        AuthResponse response = userMapper.dtoToEntity(userCreate);
        response.setToken(jwtToken.generateToken(userCreate));
        return response;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = getUser(request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        AuthResponse a=userMapper.dtoToEntity(user);
        a.setToken(jwtToken.generateToken(user));
        return a;
    }
}
