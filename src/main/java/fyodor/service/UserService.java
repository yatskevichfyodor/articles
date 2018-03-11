package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.registration.EmailConfirm;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private EmailConfirm emailConfirm;

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public User findByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    @Override
    public User register(User userDto, HttpServletRequest request) {
        User user = save(userDto);

        String appUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());

        confirmRegistration(user, localeResolver.resolveLocale(request), appUrl);

        return user;
    }
    public User save(User userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(userDto);
    }
    private void confirmRegistration(User user, Locale locale, String appUrl) {
        String username = user.getUsername();
        String hash = passwordEncoder.encode(username);
        emailConfirm.sendMail(appUrl, locale, username, user.getEmail(), hash);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void deleteIfNotConfirmed() {
//        System.out.println("Tasks check!");
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
