package fyodor.controller;


import fyodor.model.Article;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

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

	@GetMapping(value = { "/", "home" })
	public String home(HttpServletRequest request, Model model) {
		int horizontalSize = 3;
		String messageValue = messageSource.getMessage("message.welcome", null, localeResolver.resolveLocale(request));
		model.addAttribute("message", messageValue);
		model.addAttribute("horizontalSize", horizontalSize);
		model.addAttribute("articlesMatrix", getArticlesMatrix(horizontalSize));

		return "index";
	}

	public List<List<Article>> getArticlesMatrix(int horizontalSize) {
		List<Article> simpleArticlesList = articleService.findAll();
		List<List<Article>> articlesMatrix = new LinkedList();
		Iterator<Article> iterator = simpleArticlesList.iterator();

		int i = 0;
		List<Article> innerList = new LinkedList<>();
		while (iterator.hasNext()) {
			if (i % horizontalSize == 0) {
				innerList = new LinkedList<>();
				articlesMatrix.add(innerList);
			}
			Article article = iterator.next();
			innerList.add(article);
			i++;
		}
		return articlesMatrix;
	}

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