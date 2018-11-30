package fyodor.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmedUserValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmedUser {
    String message() default "This user was not confirmed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}