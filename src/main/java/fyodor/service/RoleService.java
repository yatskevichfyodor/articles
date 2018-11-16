package fyodor.service;

import fyodor.model.Role;
import fyodor.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {

    @Autowired private RoleRepository roleRepository;

    public Set<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
