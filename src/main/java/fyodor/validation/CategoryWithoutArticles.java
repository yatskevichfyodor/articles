package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryWithoutArticlesValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryWithoutArticles {
    String message() default "CategoryNotExists title already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}