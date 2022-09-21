package ecommerce.eco.sport.service;

import ecommerce.eco.sport.model.entity.Role;
import ecommerce.eco.sport.repository.RoleRepository;
import ecommerce.eco.sport.service.abstraction.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role findBy(String fullRoleName) {
        return roleRepository.findByName(fullRoleName);
    }
}
