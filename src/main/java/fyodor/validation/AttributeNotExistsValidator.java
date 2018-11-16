package fyodor.validation;

import fyodor.service.UserAttributeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AttributeNotExistsValidator implements ConstraintValidator<AttributeNotExists, String> {

    @Autowired
    private UserAttributeService userAttributeService;

    @Override
    public void initialize(AttributeNotExists contactNumber) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
        return !userAttributeService.attributeExists(name);
    }
}