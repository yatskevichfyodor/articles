package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityServiceTest {

    @Autowired private SecurityService securityService;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserDetailsService customUserDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SessionRegistry sessionRegistry;

    @Before
    public void initDB() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        Role role = roleRepository.save(new Role(1L, "ROLE_USER"));
        userRepository.save(new User("user", "email1@mail.com", passwordEncoder.encode("password"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse("2018-01-02 12:30:19.228"),
                new HashSet<>(Arrays.asList(role))));
    }

    @Test
    public void autologin() {
        securityService.autologin("user", "password");

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        assertThat(usernamePasswordAuthenticationToken.isAuthenticated()).isEqualTo(true);
    }

    @Test
    public void getLoggedInUsers() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user");
        sessionRegistry.registerNewSession("NWEfw3GW349jg49jgJOGJ", userDetails);
        List<Object> principals = securityService.getLoggedInUsers();
        assertThat( ((CustomUserDetails)principals.get(0)).getUsername() ).isEqualTo("user");
    }

    @Test
    public void logout() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user");
        sessionRegistry.registerNewSession("NWEfw3GW349jg49jgJOGJ", userDetails);
        securityService.logout(userDetails);

        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);
        assertThat( sessions.size()).isEqualTo(0);
    }
}