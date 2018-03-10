package fyodor.service;

import fyodor.model.User;
import fyodor.model.UserRegistrationDto;

import java.util.List;

public interface IUserService {

    User save(UserRegistrationDto user);

    void delete(User user);

    List<User> findAll();

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

    boolean confirm(String user, String hash);

    void delete(String[] delArray);

    void block(String[] blockArray);

    void unblock(String[] unblockArray);

    void addRole(String[] roleArray, String role);

    void deleteRole(String[] roleArray, String role);
}
