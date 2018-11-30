package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.model.UserAttribute;
import fyodor.model.UserParam;
import fyodor.registration.EmailConfirm;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserAttributeRepository;
import fyodor.repository.UserParamRepository;
import fyodor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class UserService {

    @Autowired private SecurityService securityService;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserAttributeRepository userAttributeRepository;

    @Value("${emailConfirmation}")
    private String emailConfirmation;

    public User findByUsernameOrEmailIgnoreCase(String usernameOrEmail) {
        User user = userRepository.findByUsernameIgnoreCase(usernameOrEmail);
        if (user != null) return user;
        user = userRepository.findByEmailIgnoreCase(usernameOrEmail);
        return user;
    }

    public void delete(User user) {
        userRepository.delete(userRepository.findById(user.getId()).get());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public void delete(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < idArray.length; i++) {
            logoutUser(idArray[i]);
            userRepository.deleteById(idArray[i]);
        }
    }

    @Transactional
    public void block(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < idArray.length; i++) {
            logoutUser(idArray[i]);
            userRepository.block(idArray[i]);
        }
    }

    @Transactional
    public void unblock(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < idArray.length; i++) {
            userRepository.unblock(idArray[i]);
        }
    }

    @Transactional
    public void addRole(String[] users, String role) {
        User user;
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < idArray.length; i++) {
            user = userRepository.findById(idArray[i]).get();
            Set<Role> roles = user.getRoles();
            roles.add(roleRepository.findByName(role).iterator().next());
//            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteRole(String[] users, String role) {
        User user;
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < idArray.length; i++) {
            user = userRepository.findById(idArray[i]).get();
            Set<Role> roles = user.getRoles();
            roles.remove(roleRepository.findByName(role).iterator().next());
//            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    public void logoutUser(long userId) {
        List<Object> loggedInUsers = securityService.getLoggedInUsers();
        for (Object principal : loggedInUsers)
            if (((CustomUserDetails) principal).getId() == userId) {
                securityService.logout(principal);
                return;
            }
    }

    public Map<UserAttribute, String> getUserParams(Long userId) {
        User user = userRepository.findById(userId).get();
        List<UserParam> params = user.getParams();
        List<UserAttribute> attributes = userAttributeRepository.findAll();
        Map<UserAttribute, String> paramsMap = new HashMap<>();
        for (UserAttribute attribute : attributes) {
            if (!attribute.isEnabled()) continue;
            boolean paramExists = false;
            for (UserParam param : params) {
                if (param.getId().getAttribute().equals(attribute)) {
                    paramExists = true;
                    paramsMap.put(attribute, param.getValue());
                    break;
                }
            }
            if (!paramExists)
                paramsMap.put(attribute, "");
        }

        return paramsMap;
    }
}
