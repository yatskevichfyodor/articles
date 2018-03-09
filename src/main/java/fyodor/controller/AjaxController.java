package fyodor.controller;

import fyodor.model.Article;
import fyodor.service.IArticleService;
import fyodor.service.ICategoryService;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class AjaxController {

    @Autowired
    IUserService userService;

    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;

    @PostMapping("/findArticlesByCategory")
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
    public Boolean addArticle(@RequestBody String json, Principal principal) {
        articleService.save(json, principal);

        return true;
    }

    @PostMapping("/checkIfUsernameNotExists")
    public Boolean checkIfUsernameNotExists(@RequestBody String username) {
        if (userService.findByUsernameIgnoreCase(username) == null)
            return true;
        return false;
    }

    @PostMapping("/checkIfEmailNotExists")
    public Boolean checkIfEmailNotExists(@RequestBody String email) {
        if (userService.findByEmailIgnoreCase(email) == null)
            return true;
        return false;
    }
}
