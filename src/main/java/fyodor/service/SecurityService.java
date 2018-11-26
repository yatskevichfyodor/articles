package fyodor.service;

import fyodor.model.User;
import fyodor.registration.EmailConfirm;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserParamRepository;
import fyodor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;

@Service
public class SecurityService {
	
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDetailsService customUserDetailsService;
    @Autowired private SessionRegistry sessionRegistry;
    @Autowired private LocaleResolver localeResolver;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private EmailConfirm emailConfirm;

    @Value("${emailConfirmation}")
    private String emailConfirmation;

    /**
     * Makes user authenticated
     *
     * @param username
     * @param password
     */
    public void autologin(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    public List<Object> getLoggedInUsers() {
        return sessionRegistry.getAllPrincipals();
    }

    public void logout(Object principal) {
        List<SessionInformation> sessionInformation = sessionRegistry.getAllSessions(principal, true);
        for (SessionInformation el : sessionInformation) {
            el.expireNow();
        }
    }

    @Transactional
    public User register(User userDto, HttpServletRequest request) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRoles(roleRepository.findByName("ROLE_USER"));
        User user = userRepository.save(userDto);

        if (emailConfirmation.equals("true")) {
            String appUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
            confirmRegistration(user, localeResolver.resolveLocale(request), appUrl);
        }

        return user;
    }

    private void confirmRegistration(User user, Locale locale, String appUrl) {
        String username = user.getUsername();
        String hash = passwordEncoder.encode(username);
        emailConfirm.sendMail(appUrl, locale, username, user.getEmail(), hash);
    }

    @Transactional
    public boolean confirm(String username, String hash) {
        if (passwordEncoder.matches(username, hash)) {
            userRepository.confirm(username);
            return true;
        }
        return false;
    }
}
