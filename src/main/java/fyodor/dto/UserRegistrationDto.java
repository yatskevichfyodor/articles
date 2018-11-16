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
    @Size(min=4, max=32, message="Name should have atleast 2 characters")
    @Pattern(regexp="^[a-z0-9_-]{3,32}$", message = "invalid.amount")
    private String username;

    @NotNull
    @Size(min=4, max=100, message="Name should have atleast 2 characters")
    @Pattern(regexp="^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$", message = "invalid.amount")
    private String email;

    @NotNull
    @Size(min=4, max=32, message="Name should have atleast 2 characters")
    private String password;

    @NotNull
    @Size(min=4, max=32, message="Name should have atleast 2 characters")
    private String confirmPassword;
}
