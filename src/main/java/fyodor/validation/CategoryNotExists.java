package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryNotExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryNotExists {
    String message() default "CategoryNotExists title already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}