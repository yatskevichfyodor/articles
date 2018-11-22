package fyodor.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.Assert.*;

public class SecurityServiceTest {

    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private UserDetailsService customUserDetailsService;
    @MockBean private SessionRegistry sessionRegistry;

    @Test
    public void autologin() {

    }

    @Test
    public void getLoggedInUsers() {
    }

    @Test
    public void logout() {
    }
}