package fyodor.registration;

import fyodor.model.User;
import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegistrationConfirmListener {

    @Autowired
    UserService userService;

    @Autowired
    private EmailConfirm emailConfirm;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void handle(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
//        deleteIfNotConfirmed();
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String username = user.getUsername();
        String hash = passwordEncoder.encode(username);
        emailConfirm.sendMail(event.getAppUrl(), event.getLocale(), username, user.getEmail(), hash);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    private void deleteIfNotConfirmed() {
//        System.out.println("Tasks check!");
    }
}