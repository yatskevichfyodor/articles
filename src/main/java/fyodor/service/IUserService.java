package fyodor.service;

import fyodor.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserService {

    User register(User userDto, HttpServletRequest request);

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
