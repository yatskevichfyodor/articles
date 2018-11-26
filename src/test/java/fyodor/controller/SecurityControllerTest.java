package fyodor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.repository.UserRepository;
import fyodor.service.SecurityService;
import fyodor.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityControllerTest {

    @Autowired private WebApplicationContext context;

    @MockBean private UserService userService;
    @MockBean private SecurityService securityService;
    @MockBean private UserRepository userRepository;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void loginGet() throws Exception {
        this.mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void loginPost() throws Exception {
        when(userRepository.findByUsernameIgnoreCase(any()))
                .thenReturn(new User("username", "email", "password", new Date(), false, true, new HashSet<Role>(Arrays.asList(new Role("ROLE_USER)")))));

        this.mvc.perform(post("/login")
                .param("username", "user")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(cookie().exists("JSESSIONID"));
    }

    @Test
    public void logoutGet() throws Exception {
        this.mvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void registrationGet() throws Exception {
        this.mvc.perform(get("/reg"))
                .andExpect(status().isOk())
                .andExpect(view().name("reg"));
    }

    @Test
    public void registrationPost() throws Exception {
        when(securityService.register(any(), any())).thenReturn(new User("username", "email", "password"));

        this.mvc.perform(post("/reg")
                .param("username", "user")
                .param("email", "email1@mail.com")
                .param("password", "password")
                .param("confirmPassword", "confirmPassword")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("message"));
    }

    @Test
    public void confirmRegistration() throws Exception {
        when(securityService.confirm(any(), any())).thenReturn(true);

        this.mvc.perform(get("/registrationConfirm")
                .param("username", "user")
                .param("hash", "WUGw498BIngHGEGN99NGOHG")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("message"));
    }

    @Test
    public void checkIfUsernameNotExists() throws Exception {
        when(userService.findByUsernameIgnoreCase(any())).thenReturn(new User("user", "email1@mail.com", "password"));

        this.mvc.perform(post("/checkIfUsernameNotExists")
                .param("username", "user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void checkIfEmailNotExists() throws Exception {
        when(userService.findByEmailIgnoreCase(any())).thenReturn(new User("user", "email1@mail.com", "password"));

        this.mvc.perform(post("/checkIfEmailNotExists")
                .param("username", "user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}