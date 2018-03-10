package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.model.UserRegistrationDto;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Service
public class UserService implements IUserService {

    @Autowired
    private ISecurityService securityService;
	
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public User findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User save(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean confirm(String username, String hash) {
        if (passwordEncoder.matches(username, hash)) {
            userRepository.confirm(username);
            return true;
        }
        return false;
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user.getId());
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void delete(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i=0; i<idArray.length; i++) {
            logoutUser(idArray[i]);
            userRepository.deleteById(idArray[i]);
        }
    }

    @Override
    @Transactional
    public void block(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i=0; i<idArray.length; i++) {
            logoutUser(idArray[i]);
            userRepository.block(idArray[i]);
        }
    }

    @Override
    @Transactional
    public void unblock(String[] users) {
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i=0; i<idArray.length; i++) {
            userRepository.unblock(idArray[i]);
        }
    }

    @Override
    @Transactional
    public void addRole(String[] users, String role) {
        User user;
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i=0; i<idArray.length; i++) {
            user = userRepository.findById(idArray[i]);
            Set<Role> roles = user.getRoles();
            roles.add(roleRepository.findByName(role).iterator().next());
//            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteRole(String[] users, String role) {
        User user;
        long[] idArray = Arrays.asList(users).stream().mapToLong(Long::parseLong).toArray();
        for (int i=0; i<idArray.length; i++) {
            user = userRepository.findById(idArray[i]);
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
}
