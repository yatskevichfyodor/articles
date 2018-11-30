package fyodor.controller;

import fyodor.model.Role;
import fyodor.model.User;
import fyodor.repository.RoleRepository;
import fyodor.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityControllerTestSQLInjections {

    @Autowired private WebApplicationContext context;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @Before
    public void setup() throws Exception {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        initDB();
    }

    private void initDB() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        Role role = roleRepository.save(new Role(1L, "ROLE_USER"));
        userRepository.save(new User("user", "email1@mail.com", passwordEncoder.encode("password"),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").parse("2018-01-02 12:30:19.228"),
                new HashSet<>(Arrays.asList(role))));
    }

    @Test
    public void checkIfUsernameNotExists_userExists() throws Exception{
        this.mvc.perform(post("/checkIfUsernameNotExists")
                .param("username", "user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(content().string("false"));
    }

    @Test
    public void checkIfUsernameNotExists_sqlInjection() throws Exception{
        this.mvc.perform(post("/checkIfUsernameNotExists")
                .param("username", "user1' OR username='user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf()))
                .andDo(print())
                .andExpect(content().string("true"));
    }
}