package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ArticleTitleNotExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ArticleTitleNotExists {
    String message() default "Article title already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}