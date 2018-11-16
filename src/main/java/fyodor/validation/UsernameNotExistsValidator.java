package fyodor.validation;

import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameNotExistsValidator implements ConstraintValidator<UsernameNotExists, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UsernameNotExists contactNumber) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext cxt) {
        return userService.findByUsernameIgnoreCase(username) == null;
    }
}