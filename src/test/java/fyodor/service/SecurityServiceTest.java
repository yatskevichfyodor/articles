package fyodor.service;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SecurityServiceTest {

    @MockBean private UserRepository userRepository;

    @Test
    public void autologin() {
        when(userRepository.findByUsernameIgnoreCase(any()))
                .thenReturn(new User("username", "email", "password", new Date(), false, true, new HashSet<Role>(Arrays.asList(new Role("ROLE_USER)")))));


    }

    @Test
    public void getLoggedInUsers() {
    }

    @Test
    public void logout() {
    }
}