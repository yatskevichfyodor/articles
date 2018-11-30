package fyodor.dto;

import fyodor.validation.ConfirmedUser;
import fyodor.validation.ValidCredentials;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ValidCredentials
@ConfirmedUser
public class UserLoginDto {
    @NotNull
    @Size(min=4, max=32, message="Username should have from 4 to 32 characters")
    private String username;

    @NotNull
    @Size(min=4, max=32, message="Password should have from 4 to 32 characters")
    private String password;
}
