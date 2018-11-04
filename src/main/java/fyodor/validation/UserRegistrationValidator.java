package fyodor.validation;

import fyodor.model.User;
import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserRegistrationValidator implements Validator {
    @Autowired
    private UserService userService;

    private String username_regex = "^[a-z0-9_-]{3,32}$";
    private String email_regex = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";


    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors err) {
        checkIfEmpty(err);
        if (err.hasErrors()) return;
        User user = (User)obj;
        checkCorrectness(user, err);
        if (err.hasErrors()) return;
        checkIfRegistered(user, err);
    }

    public void checkIfEmpty(Errors err) {
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "username", "form.error.username.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "email", "form.error.email.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(err, "password", "form.error.password.empty");
    }

    public void checkCorrectness(User user, Errors err) {
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) err.rejectValue("username", "form.error.username.size");
        if (user.getEmail().length() < 4 || user.getUsername().length() > 100) err.rejectValue("email", "form.error.email.size");
        if (user.getPassword().length() < 4 || user.getPassword().length() > 32) err.rejectValue("password", "form.error.password.size");

        if (!user.getUsername().matches(username_regex)) err.rejectValue("username", "form.error.username.incorrect");
        if (!user.getEmail().matches(email_regex)) err.rejectValue("email", "form.error.email.incorrect");
        if (!user.getPassword().equals(user.getConfirmPassword())) err.rejectValue("password", "form.error.password.notmatch");
    }

    public void checkIfRegistered(User user, Errors err) {
        if (userService.findByUsernameIgnoreCase(user.getUsername()) != null) err.rejectValue("username", "form.error.username.registered");
        if (userService.findByEmailIgnoreCase(user.getEmail()) != null) err.rejectValue("email", "form.error.email.registered");
    }
}
