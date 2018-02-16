package fyodor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fyodor.model.Role;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Set<Role> findByName(String name);
}
