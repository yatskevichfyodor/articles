package fyodor.controller;

import fyodor.model.Article;
import fyodor.model.ArticleDto;
import fyodor.model.CategoryObject;
import fyodor.service.IArticleService;
import fyodor.service.ICategoryService;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AjaxController {

    @Autowired
    IUserService userService;

    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;

    @PostMapping("/findArticlesByCategory")
    public ResponseEntity<?> findArticlesByCategory(@RequestBody CategoryObject category, Principal principal) {
        List<Article> articles = articleService.findByCategoryAndAuthor(categoryService.findByName(category.getCategory()),
                userService.findByUsernameIgnoreCase(principal.getName()));
        List<String> titles = new ArrayList<String>();
        for (Article article: articles) {
            titles.add(article.getTitle());
        }
        return ResponseEntity.ok(titles);
    }

    @PostMapping("/add-article")
    public ResponseEntity<?> addArticle(@RequestBody ArticleDto articleDto, Principal principal) {
        articleService.save(articleDto, principal);

        return ResponseEntity.ok(true);
    }
}
