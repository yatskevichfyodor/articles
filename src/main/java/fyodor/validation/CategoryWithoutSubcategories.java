package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryWithoutSubcategoriesValidator.class)
@Target( { ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoryWithoutSubcategories {
    String message() default "CategoryNotExists title already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}