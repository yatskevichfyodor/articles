package fyodor.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import fyodor.service.SecurityService;
import fyodor.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.LocaleResolver;

@RunWith(SpringRunner.class)
@WebMvcTest(SecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private MessageSource messageSource;
    @MockBean private LocaleResolver localeResolver;
    @MockBean private SecurityService securityService;

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("login")));
    }

    @Test
    public void loginPost() throws Exception {
        this.mockMvc.perform(post("/login").param("userLoginDto", "{\"username\":\"user\", \"password\":\"password\"}")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("redirect:/home")));
    }

    @Test
    public void logout() throws Exception {
    }

    @Test
    public void registration() throws Exception {
    }

    @Test
    public void confirmRegistration() throws Exception {
    }

    @Test
    public void checkIfUsernameNotExists() throws Exception {
    }

    @Test
    public void checkIfEmailNotExists() throws Exception {
    }
}