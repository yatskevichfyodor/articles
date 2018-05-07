package fyodor.repository;

import fyodor.model.UserAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAttributeRepository extends JpaRepository<UserAttribute, Long> {
    UserAttribute findByNameIgnoreCase(String username);

    UserAttribute findById(Long id);
}
