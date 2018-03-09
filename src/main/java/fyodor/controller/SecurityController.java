package fyodor.controller;

import fyodor.model.User;
import fyodor.model.UserLoginDto;
import fyodor.model.UserRegistrationDto;
import fyodor.registration.OnRegistrationCompleteEvent;
import fyodor.service.IUserService;
import fyodor.service.SecurityService;
import fyodor.validation.UserLoginValidator;
import fyodor.validation.UserRegistrationValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    private LocaleResolver localeRsolver;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("userLoginDto") UserLoginDto userLoginDto, Errors errors, HttpServletRequest request, Model model) {
        userLoginValidator.validate((Object) userLoginDto, errors);
        if (errors.hasErrors())
            return "login";
        securityService.autologin(userLoginDto.getUsername(), userLoginDto.getPassword());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }

    @GetMapping("/reg")
    public String registration(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "reg";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("userRegistrationDto") UserRegistrationDto userRegistrationDto, Errors errors, HttpServletRequest request, Model model) {
        userRegistrationValidator.validate((Object) userRegistrationDto, errors);
        if (errors.hasErrors())
            return new String("reg");
        User user;
        String appUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
        user = userService.save(userRegistrationDto);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, localeRsolver.resolveLocale(request), appUrl));

        String message = messageSource.getMessage("registration.message.confirm", null, localeRsolver.resolveLocale(request));
        model.addAttribute("message", message);
        return "index";
    }

    @GetMapping("/registrationconfirm")
    public String confirmRegistration(@RequestParam("username") String user, @RequestParam("hash") String hash, HttpServletRequest request, Model model) {
        String message;
        boolean confirmed = userService.confirm(user, hash);
        Locale locale = localeRsolver.resolveLocale(request);
        if (confirmed) {
            message = messageSource.getMessage("auth.message.successful", null, locale);
            model.addAttribute("message", message);
//            model.addAttribute("message", "auth.successful");
            return "index";
        }

        message = messageSource.getMessage("auth.message.fail", null, locale);
        model.addAttribute("message", message);
//        model.addAttribute("message", "auth.expired");
        return "index";
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
