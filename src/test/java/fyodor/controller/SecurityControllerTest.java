package fyodor.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import fyodor.config.SecurityConfig;
import fyodor.model.User;
import fyodor.service.SecurityService;
import fyodor.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;

@RunWith(SpringRunner.class)
@WebMvcTest(SecurityController.class)
@Import(value = { SecurityConfig.class, ViewResolver.class })
public class SecurityControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private MessageSource messageSource;
    @MockBean private LocaleResolver localeResolver;
    @MockBean private SecurityService securityService;
    @MockBean private BindingResult bindingResult;

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void loginPost() throws Exception {
        Mockito.doNothing().when(securityService).autologin(any(), any());
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/login")
                .param("username", "user")
                .param("password", "password")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

//    @Test
//    public void logout() throws Exception {
//    }
//
    @Test
    public void registration() throws Exception {
        this.mockMvc.perform(get("/reg"))
                .andDo(print()).andExpect(status().isOk());
    }
//
//    @Test
//    public void confirmRegistration() throws Exception {
//    }
//
//    @Test
//    public void checkIfUsernameNotExists() throws Exception {
//    }
//
    @Test
    public void checkIfEmailNotExists() throws Exception {
        Mockito.when(userService.findByUsernameIgnoreCase(any())).thenReturn(new User());

        this.mockMvc.perform(post("/checkIfUsernameNotExists")
                .param("username", "user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }



    @Test
    public void testtest() throws Exception {
        this.mockMvc.perform(post("/testtest")
                .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}