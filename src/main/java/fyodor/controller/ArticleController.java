package fyodor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fyodor.model.*;
import fyodor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ArticleController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IRatingService ratingService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @ModelAttribute("currentUser")
    public User getPrincipal(Principal principal) {
        if (principal == null) return null;

        return userService.findByUsernameIgnoreCase(principal.getName());
    }

    @GetMapping("/add-article")
    public String addArticle( Model model) {
        model.addAttribute("listOfCategories", categoryService.findAll());

        return "add-article";
    }

    @PostMapping("/add-article")
    @ResponseBody
    public Boolean addArticle(@RequestBody String json, Principal principal) {
        articleService.save(json, principal);

        return true;
    }

    @GetMapping("/article/{articleId}")
    public String article(Model model, @PathVariable Long articleId, Principal principal) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findByArticleId(articleId));
        Rating currentUserRating;
        if (principal == null)
            currentUserRating = null;
        else
            currentUserRating = ratingService.findRatingByUsernameAndArticleId(principal.getName(), articleId);
        model.addAttribute("currentUserRating", currentUserRating);
        model.addAttribute("likesNumber", ratingService.getValuesNumberByArticleId(articleId, "LIKE"));
        model.addAttribute("dislikesNumber", ratingService.getValuesNumberByArticleId(articleId, "DISLIKE"));
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

    @MessageMapping("/comment/save")
    @SendTo("/comments")
    public String saveComment(@RequestBody String json, Principal principal) {
        Comment comment = commentService.save(json, principal);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", comment.getId());
        objectNode.put("author", comment.getAuthor().getUsername());
        objectNode.put("text", comment.getText());
        objectNode.put("timestamp", comment.getTimestamp().toString());
        return objectNode.toString();
    }

    @DeleteMapping("/comment")
    @ResponseBody
    public Boolean deleteComment(@RequestBody Long id, Principal principal) {
        if (!commentService.findById(id).getAuthor().getUsername().equals(principal.getName()))
            return false;

        commentService.deleteComment(id);

        return true;
    }

    @GetMapping("/article/{articleId}/changeRating")
    @ResponseBody
    public String changeRating(@PathVariable Long articleId, Principal principal, @RequestParam String ratingState) {
        ratingService.changeState(articleId, principal.getName(), ratingState);

        Article article = articleService.findById(articleId);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("value", ratingState);
//        objectNode.put("likesNumber", article.getLikesNumber());
//        objectNode.put("dislikesNumber", article.getDislikesNumber());
        objectNode.put("likesNumber", ratingService.getValuesNumberByArticleId(articleId, "LIKE"));
        objectNode.put("dislikesNumber", ratingService.getValuesNumberByArticleId(articleId, "DISLIKE"));
        String str = objectNode.toString();
        return objectNode.toString();
    }
}