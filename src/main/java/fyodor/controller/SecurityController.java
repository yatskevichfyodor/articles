package fyodor.controller;

import fyodor.model.User;
import fyodor.service.IUserService;
import fyodor.service.SecurityService;
import fyodor.validation.UserLoginValidator;
import fyodor.validation.UserRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class SecurityController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    @Autowired
    private UserLoginValidator userLoginValidator;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private SecurityService securityService;

    @Value("${emailConfirmation}")
    private String emailConfirmation;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userLoginDto", new User());
        model.addAttribute("registration", false);
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("userLoginDto") User userDto, Errors errors, HttpServletRequest request, Model model) {
        userLoginValidator.validate(userDto, errors);
        if (errors.hasErrors())
            return "login";
        securityService.autologin(userDto.getUsername(), userDto.getPassword());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("user") User userDto,
                               Errors errors, HttpServletRequest request, Model model) {
        userRegistrationValidator.validate(userDto, errors);
        if (errors.hasErrors())
            return "reg";
        User user = userService.register(userDto, request);

        Locale locale = localeResolver.resolveLocale(request);
        String message;
        if (emailConfirmation.equals("true")) {
            message = messageSource.getMessage("registration.message.confirm", null, locale);
            model.addAttribute("message", message);
            return "message";
        }

        message = messageSource.getMessage("auth.message.successful", null, locale);
        model.addAttribute("message", message);
        return "message";
    }

    @GetMapping("/registrationconfirm")
    public String confirmRegistration(@RequestParam("username") String user, @RequestParam("hash") String hash, HttpServletRequest request, Model model) {
        String message;
        boolean confirmed = userService.confirm(user, hash);
        Locale locale = localeResolver.resolveLocale(request);
        if (confirmed)
            message = messageSource.getMessage("auth.message.successful", null, locale);
        else
            message = messageSource.getMessage("auth.message.fail", null, locale);
        model.addAttribute("message", message);
        return "message";
    }

    @PostMapping("/checkIfUsernameNotExists")
    @ResponseBody
    public Boolean checkIfUsernameNotExists(@RequestBody String username) {
        if (userService.findByUsernameIgnoreCase(username) == null)
            return true;
        return false;
    }

    @PostMapping("/checkIfEmailNotExists")
    @ResponseBody
    public Boolean checkIfEmailNotExists(@RequestBody String email) {
        if (userService.findByEmailIgnoreCase(email) == null)
            return true;
        return false;
    }
}
