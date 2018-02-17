package fyodor.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fyodor.service.IUserService;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class WebController {

	@Autowired
	private IUserService userService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	LocaleResolver localeResolver;

	@RequestMapping(value = { "/", "home" })
	public String home(HttpServletRequest request, Model model) {
		String messageValue = messageSource.getMessage("message.welcome", null, localeResolver.resolveLocale(request));
		model.addAttribute("message", messageValue);
		return "index";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String userProfile(HttpServletRequest request) {
		return "profile";
	}

	@RequestMapping(value = "/ajax-test", method = RequestMethod.GET)
	public String ajaxTest(HttpServletRequest request) {
		return "ajax";
	}


	@RequestMapping(value = { "add-article" })
	public String addArticle(HttpServletRequest request, Model model) {
		return "add-article";
	}
}