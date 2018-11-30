package fyodor.validation;

import fyodor.repository.UserRepository;
import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotExistsValidator implements ConstraintValidator<EmailNotExists, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailNotExists contactNumber) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext cxt) {
        return userRepository.findByEmailIgnoreCase(username) == null;
    }
}