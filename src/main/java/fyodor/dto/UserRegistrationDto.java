package fyodor.dto;

import fyodor.validation.PasswordsMatch;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@PasswordsMatch
public class UserRegistrationDto {
    private Long id;

    @NotNull
    @Size(min=4, max=32, message="Username should have from 4 to 32 characters")
    @Pattern(regexp="^[a-z0-9_-]{3,32}$", message = "Specified username is incorrect")
    private String username;

    @NotNull
    @Size(min=4, max=100, message="Email should have from 4 to 32 characters")
    @Pattern(regexp="^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "Email should have from 4 to 32 characters")
    private String email;

    @NotNull
    @Size(min=4, max=32, message="Password should have from 4 to 32 characters")
    private String password;

    @NotNull
    @Size(min=4, max=32, message="Password should have from 4 to 32 characters")
    private String confirmPassword;
}
