package fyodor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fyodor.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailIgnoreCase(String email);
    User findByUsernameIgnoreCase(String username);

    @Modifying
    @Query("update User u set u.enabled = 1 where u.username = ?1")
    void confirm(String username);
}