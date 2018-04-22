package fyodor.controller;


import fyodor.model.Category;
import fyodor.service.IArticleService;
import fyodor.service.ICategoryService;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class WebController {

	@Autowired
	private IUserService userService;

	@Autowired
    private IArticleService articleService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@GetMapping("/profile")
	public String userProfile(Model model, Principal principal) {
		String username = principal.getName();
		List<Category> listOfCategories = categoryService.findByArticlesIn(articleService.
				findByAuthor(userService.findByUsernameIgnoreCase((username))));
		Set<Category> setOfCategories = new HashSet<Category>(listOfCategories);
		model.addAttribute("user", userService.findByUsernameIgnoreCase(principal.getName()));
		model.addAttribute("setOfCategories", setOfCategories);

		return "profile";
	}

}