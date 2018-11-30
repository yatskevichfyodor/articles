package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailNotExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotExists {
    String message() default "Specified email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}