package fyodor.service;

import java.util.List;

public interface ISecurityService {
    void autologin(String username, String password);

    List<Object> getLoggedInUsers();

    void logout(Object principal);
}
