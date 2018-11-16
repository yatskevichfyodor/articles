package fyodor.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import fyodor.service.SecurityService;
import fyodor.service.UserService;
import fyodor.validation.UserLoginValidator;
import fyodor.validation.UserRegistrationValidator;
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
    @MockBean private UserRegistrationValidator userRegistrationValidator;
    @MockBean private UserLoginValidator userLoginValidator;
    @MockBean private MessageSource messageSource;
    @MockBean private LocaleResolver localeResolver;
    @MockBean private SecurityService securityService;

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("login")));
    }

    @Test
    public void loginPost() {
    }

    @Test
    public void logout() {
    }

    @Test
    public void registration() {
    }

    @Test
    public void registration1() {
    }

    @Test
    public void confirmRegistration() {
    }

    @Test
    public void checkIfUsernameNotExists() {
    }

    @Test
    public void checkIfEmailNotExists() {
    }
}