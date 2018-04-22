package fyodor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fyodor.Dto.ArticleDto;
import fyodor.model.*;
import fyodor.service.*;
import fyodor.util.ListToMatrixConverter;
import fyodor.validation.ArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;
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
    private IImageService imageService;

    @Autowired
    private ArticleValidator articleValidator;

    @Value("${methodOfStoringPictures}")
    private String methodOfStoringPictures;

    @Value("${horizontalSize}")
    private int horizontalSize;

    @ModelAttribute("currentUser")
    public User getPrincipal(Principal principal) {
        if (principal == null) return null;

        return userService.findByUsernameIgnoreCase(principal.getName());
    }

    @GetMapping(value = { "/", "home" })
    public String home(Model model) {
        List<Article> articlesList = articleService.findAll();

        model.addAttribute("horizontalSize", horizontalSize);
        model.addAttribute("articlesMatrix", ListToMatrixConverter.convert(horizontalSize, articlesList));
        model.addAttribute("categories", categoryService.findUsedCategories());

        return "index";
    }

    @GetMapping("/getArticleMatrixByCategoryId")
    @ResponseBody
    public List<List<ArticleDto>> getArticleMatrixByCategoryId(@RequestParam("id") String id) {
        Long categoryId = Long.valueOf(id);
        List<Article> articles;
        if (categoryId == 0)
            articles = articleService.findAll();
        else
            articles = articleService.findByCategoryId(categoryId);

        List<ArticleDto> articleDtoList = new LinkedList<>();

        for (Article article: articles) {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(article.getId());
            articleDto.setTitle(article.getTitle());
            articleDto.setImageData(article.getImage().getData());
            articleDtoList.add(articleDto);
        }

        return ListToMatrixConverter.convert(horizontalSize, articleDtoList);
    }

    @GetMapping("/getArticleMatrixByOrderId")
    @ResponseBody
    public List<List<Article>> getArticleMatrixByOrderId(@RequestParam("id") String id) {
        Long orderId = Long.valueOf(id);
        if (orderId == 0)
            return ListToMatrixConverter.convert(horizontalSize, articleService.findAllWithOrder(orderId));
        List<Article> list = articleService.findByCategoryId(orderId);

        return ListToMatrixConverter.convert(horizontalSize, list);
    }

    @GetMapping("/add-article")
    public String addArticle( Model model) {
        model.addAttribute("listOfCategories", categoryService.findAll());
        model.addAttribute("methodOfStoringPictures", methodOfStoringPictures);

        return "add-article";
    }

    @PutMapping("/add-article")
//    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Long addArticle(@RequestBody ArticleDto articleDto, Errors errors, Principal principal) {
        articleValidator.validate(articleDto, errors);
        if (errors.hasErrors())
            throw new RuntimeException(("Article validation error.\n" + errors.getAllErrors().toString()));
        Article article = articleService.save(articleDto, principal);
        return article.getId();
    }

    @GetMapping("/article/{id}")
    public String article(Model model, @PathVariable String id, Principal principal) {
        Long articleId = new Long(id);
        Article article = articleService.findById(articleId);

        article.setPopularity(article.getPopularity() + 1);
        Article updatedArticle = articleService.save(article);

        model.addAttribute("article", updatedArticle);
        model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(article.getTimestamp()));
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

    @GetMapping("/findArticlesByCategory")
    @ResponseBody
    public Set<String> findArticlesByCategory(@RequestParam("id") String id, Principal principal) {
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


    @PostMapping("/checkIfTitleNotExists")
    @ResponseBody
    public Boolean checkIfTitleNotExists(@RequestBody String title) {
        if (articleService.findByTitleIgnoreCase(title) == null)
            return true;
        return false;
    }


    @PostMapping("/image-upload")
    @ResponseBody
    public MultipartFile upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Image image = imageService.saveOnServer(file, request);

        return file;
    }

}