package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AttributeNotExistsValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AttributeNotExists {
    String message() default "Article title already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}