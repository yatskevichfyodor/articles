package fyodor.service;

import fyodor.model.User;
import fyodor.model.UserRegistrationDto;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class UserService implements IUserService {
	
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
        user.setEnabled(false);
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
}
