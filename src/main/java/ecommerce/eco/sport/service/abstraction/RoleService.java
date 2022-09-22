package ecommerce.eco.sport.service.abstraction;

import ecommerce.eco.sport.model.entity.Role;

public interface RoleService {
    Role findBy(String fullRoleName);
}
