package fyodor.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidCredentialsValidator.class)
public @interface ValidCredentials {
    String message() default "Invalid credentials";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}