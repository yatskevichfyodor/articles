package fyodor.service;

import fyodor.model.Role;

import java.util.Set;

public interface IRoleService {
    Set<Role> findByName(String name);
}
