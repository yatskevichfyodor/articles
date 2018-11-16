package fyodor.validation;

import fyodor.dto.UserLoginDto;
import fyodor.model.User;
import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmedUserValidator implements ConstraintValidator<ConfirmedUser, UserLoginDto> {

    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void initialize(ConfirmedUser arg0) {
    }

    @Override
    public boolean isValid(UserLoginDto userLoginDto, ConstraintValidatorContext arg1) {
        User user = userService.findByUsernameOrEmailIgnoreCase(userLoginDto.getUsername());
        if (user == null) return false;

        return user.isConfirmed();
    }
}