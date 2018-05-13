package fyodor.service;

import fyodor.model.User;
import fyodor.model.UserAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IUserService {

    User register(User userDto, HttpServletRequest request);

    void delete(User user);

    User findById(Long id);

    List<User> findAll();

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

    boolean confirm(String user, String hash);

    void delete(String[] delArray);

    void block(String[] blockArray);

    void unblock(String[] unblockArray);

    void addRole(String[] roleArray, String role);

    void deleteRole(String[] roleArray, String role);

    Map<UserAttribute, String> getUserParams(Long userId);
}
