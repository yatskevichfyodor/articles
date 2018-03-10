package fyodor.validation;

import fyodor.model.User;
import fyodor.model.UserLoginDto;
import fyodor.model.UserRegistrationDto;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserLoginValidator implements Validator {
    @Autowired
    private IUserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    //    private String email_regex = "^.+@.+$";
    private String username_regex = "^[a-z0-9_-]{3,15}$";
    private String email_regex = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";


    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors err) {
        checkIfEmpty(err);
        if (err.hasErrors()) return;

        UserLoginDto userLoginDto = (UserLoginDto)obj;
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();

        User user = checkIfRegistered(username, err);
        if (err.hasErrors()) return;
        checkIfConfirmed(user, err);
        if (err.hasErrors()) return;
        checkPassword(password, user, err);
    }

    public void checkIfEmpty(Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "username", "form.error.username.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "password", "form.error.password.empty");
    }

    public User checkIfRegistered(String usernameOrEmail, Errors err) {
        User user;
        user = userService.findByUsernameIgnoreCase(usernameOrEmail);
        if (user != null)
            return user;
        user = userService.findByEmailIgnoreCase(usernameOrEmail);
        if (user != null)
            return user;
        err.rejectValue("username", "login.error.badCredentials");
        return null;
    }

    public void checkIfConfirmed(User user, Errors err) {
        if (!user.isConfirmed())
            err.rejectValue("username", "login.error.account.disabled");
    }

    public void checkPassword(String password, User user, Errors err) {
        if (!passwordEncoder.matches(password, user.getPassword()))
            err.rejectValue("username", "login.error.badCredentials");
    }
}
