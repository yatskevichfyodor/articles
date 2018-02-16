package fyodor.service;

import fyodor.model.User;
import fyodor.model.UserRegistrationDto;

public interface IUserService {

    User save(UserRegistrationDto user);

    User findByUsernameIgnoreCase(String username);

    User findByEmailIgnoreCase(String email);

    boolean confirm(String user, String hash);
}
