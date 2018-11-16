package fyodor.validation;

import fyodor.dto.UserRegistrationDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements
        ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public void initialize(PasswordsMatch arg0) {
    }

    @Override
    public boolean isValid(Object candidate, ConstraintValidatorContext arg1) {
        UserRegistrationDto user = (UserRegistrationDto) candidate;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}