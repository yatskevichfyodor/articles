package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityServiceTest {

//    @Autowired UserRepository userRepository;
    @Autowired SecurityService securityService;
    @Autowired private AuthenticationManager authenticationManager;

    @MockBean private UserRepository userRepository;
//    @MockBean private AuthenticationManager authenticationManager;

//    @Before
//    public void initDb() {
//
//    }

    @Test
    public void autologin() {
        User user = new User("user", "email", "password", new Date(), false, true, new HashSet<>(Arrays.asList(new Role("ROLE_USER)"))));
        UserDetails userDetails = new CustomUserDetails(user);
        when(userRepository.findByUsernameIgnoreCase(any()))
                .thenReturn(user);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        assertThat(authenticationManager.authenticate(usernamePasswordAuthenticationToken));

        doNothing().when(securityService).autologin(user.getUsername(), user.getPassword());
    }

    @Test
    public void getLoggedInUsers() {
    }

    @Test
    public void logout() {
    }
}