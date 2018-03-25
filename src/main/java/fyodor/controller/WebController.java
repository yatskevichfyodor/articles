package fyodor.controller;


import fyodor.model.*;
import fyodor.service.IArticleService;
import fyodor.service.ICategoryService;
import fyodor.service.ICommentService;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Controller
public class WebController {

	@Bean
	private User anonymousUser() {
		User user = new User();
		user.setUsername("anonymous");
		return user;
	}

	@Autowired
	private IUserService userService;

	@Autowired
    private IArticleService articleService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ICommentService commentService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@ModelAttribute("currentUser")
	public User getPrincipal(Principal principal) {
		if (principal == null) return anonymousUser();

		return userService.findByUsernameIgnoreCase(principal.getName());
	}

	@GetMapping(value = { "/", "home" })
	public String home(HttpServletRequest request, Model model) {
		String messageValue = messageSource.getMessage("message.welcome", null, localeResolver.resolveLocale(request));
		model.addAttribute("message", messageValue);
		model.addAttribute("articlesMatrix", getArticlesMatrix());

		return "index";
	}

	public List<List<Article>> getArticlesMatrix() {
		List<Article> simpleArticlesList = articleService.findAll();
		List<List<Article>> articlesMatrix = new LinkedList();
		Iterator<Article> iterator = simpleArticlesList.iterator();

		int i = 0;
		List<Article> innerList = new LinkedList<>();
		while (iterator.hasNext()) {
			if (i % 4 == 0) {
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

	@GetMapping("/add-article")
	public String addArticle( Model model) {
		model.addAttribute("listOfCategories", categoryService.findAll());

		return "add-article";
	}

    @GetMapping("/article/{articleId}")
    public String article(Model model, @PathVariable Long articleId) {
	    //Article article = articleService.findByTitle(articleName);
		Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findByArticleId(articleId));
        return "article";
    }

    @PostMapping("/findArticlesByCategory")
    @ResponseBody
    public Set<String> findArticlesByCategory(@RequestBody String id, Principal principal) {
        List<Article> articles = articleService.findByCategoryAndAuthor(categoryService.findById(Long.valueOf(id)),
                userService.findByUsernameIgnoreCase(principal.getName()));
        Set<String> titles = new HashSet<>();
        for (Article article: articles) {
            titles.add(article.getTitle());
        }
        return titles;
    }

    @PostMapping("/add-article")
    @ResponseBody
    public Boolean addArticle(@RequestBody String json, Principal principal) {
        articleService.save(json, principal);

        return true;
    }

	@MessageMapping("/comment/save")
	@SendTo("/comments")
	public CommentDto saveComment(@RequestBody String json, Principal principal) {
		Comment comment = commentService.save(json, principal);

		CommentDto commentDto = new CommentDto();
		commentDto.setId(comment.getId());
		commentDto.setAuthor(comment.getAuthor().getUsername());
		commentDto.setText(comment.getText());
		commentDto.setTimestamp(comment.getTimestamp().toString());
//		String jsonComment = "{\"id\":" + comment.getId()+
//				", \"text\":\"" + comment.getText() +
//				"\", \"author\":\"" + comment.getAuthor().getUsername() +
//				"\", \"timestamp\":\"" + comment.getTimestamp().toString() + "\"}";

		return commentDto;
	}

	@DeleteMapping("/comment")
	@ResponseBody
	public Boolean deleteComment(@RequestBody Long id, Principal principal) {
		if (!commentService.findById(id).getAuthor().getUsername().equals(principal.getName()))
			return false;

		commentService.deleteComment(id);

		return true;
	}
}