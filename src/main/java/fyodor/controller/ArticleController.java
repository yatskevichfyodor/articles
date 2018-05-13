package fyodor.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fyodor.dto.ArticleDto;
import fyodor.model.*;
import fyodor.service.*;
import fyodor.util.ArticleDtoConverter;
import fyodor.util.HierarchicalCategoryHierarchyToListConverter;
import fyodor.util.ListToMatrixConverter;
import fyodor.util.UsedCategoriesHierarchyBuilder;
import fyodor.validation.ArticleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ArticleController {

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

    @Autowired
    private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    @Autowired
    private LocaleResolver localeResolver;

    @Autowired
    private MessageSource messageSource;

    @Value("${methodOfStoringPictures}")
    private String methodOfStoringPictures;

    @Value("${horizontalSize}")
    private int horizontalSize;

    @ModelAttribute("currentUser")
    public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return null;
        return userDetails.getUser();
    }

    @GetMapping(value = { "/", "home" })
    public String home(Model model) {
        List<Article> articleList = articleService.findByCategoryIdAndOrderId(0L, 1);

        model.addAttribute("horizontalSize", horizontalSize);
        model.addAttribute("articlesMatrix", ListToMatrixConverter.convert(horizontalSize, articleList));
        Category categoryHierarchy = usedCategoriesHierarchyBuilder.getHierarchy();
        List<Category> categoryList = new HierarchicalCategoryHierarchyToListConverter().convert(categoryHierarchy);
        model.addAttribute("categories", categoryList);

        return "index";
    }

    @GetMapping("/getArticleMatrixByCategoryIdAndOrderId")
    @ResponseBody
    public List<List<ArticleDto>> getArticleMatrixByCategoryIdAndOrderId(@RequestParam("categoryId") Long categoryId,
                                                                      @RequestParam("orderId") int orderId) {
        List<Article> articleList = articleService.findByCategoryIdAndOrderId(categoryId, orderId);
        List<ArticleDto> articleDtoList = ArticleDtoConverter.covert(articleList);

        return ListToMatrixConverter.convert(horizontalSize, articleDtoList);
    }

    @GetMapping("/article/add")
    public String addArticle(HttpServletRequest request, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (articleService.checkIfUserAddedArticleRecently(userDetails.getUser())) {
            Locale locale = localeResolver.resolveLocale(request);
            String message = messageSource.getMessage("article.add.tooEarly", null, locale);
            model.addAttribute("message", message);
            return "message";
        }

        model.addAttribute("listOfCategories", categoryService.findAll());
        model.addAttribute("methodOfStoringPictures", methodOfStoringPictures);

        return "add-article";
    }

    @PostMapping("/article/add")
    @ResponseBody
    public Long addArticle(@RequestBody ArticleDto articleDto, Errors errors,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        articleValidator.validate(articleDto, errors);
        if (errors.hasErrors())
            throw new RuntimeException(("Article validation error.\n" + errors.getAllErrors().toString()));
        Article article = articleService.save(articleDto, userDetails.getUser());
        return article.getId();
    }

    @GetMapping("/article/{id}")
    public String article(Model model, @PathVariable("id") Long articleId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Article article = articleService.findById(articleId);

        if (article == null) {
            model.addAttribute("message", "Can't find article with id " + articleId);
            return "message";
        }

        article.setPopularity(article.getPopularity() + 1);
        Article updatedArticle = articleService.edit(article);

        model.addAttribute("article", updatedArticle);
        model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(article.getTimestamp()));

        List<Comment> comments = commentService.findByArticleId(articleId);
        Map<Long, String> timestamps = new HashMap<>();
        for (Comment c: comments) {
            timestamps.put(c.getId(), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(c.getTimestamp()));
        }
        model.addAttribute("comments", commentService.findByArticleId(articleId));
        model.addAttribute("timestamps", timestamps);
        Rating currentUserRating;
        if (userDetails == null) {
            currentUserRating = null;
        } else {
            currentUserRating = ratingService.findRatingByUserAndArticleId(userDetails.getUser(), articleId);
        }
        model.addAttribute("currentUserRating", currentUserRating);

        model.addAttribute("likesNumber", ratingService.getValuesNumberByArticleId(articleId, "LIKE"));
        model.addAttribute("dislikesNumber", ratingService.getValuesNumberByArticleId(articleId, "DISLIKE"));

        return "article";
    }

    @GetMapping("/findArticlesByCategoryIdAndAuthorId")
    @ResponseBody
    public Set<ArticleDto> findArticlesByCategoryIdAndAuthorId(@RequestParam("categoryId") Long categoryId, @RequestParam("authorId") Long authorId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Article> articles = articleService.findByCategoryIdAndAuthorId(categoryId, authorId);
        Set<ArticleDto> articleDtos = new HashSet<>();
        for (Article article: articles) {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(article.getId());
            articleDto.setTitle(article.getTitle());
            articleDtos.add(articleDto);
        }
        return articleDtos;
    }

    @GetMapping("/article/changeRating")
    @ResponseBody
    public String changeRating(@RequestParam Long articleId, @RequestParam String ratingState, Principal principal) {
        ratingService.changeState(articleId, principal.getName(), ratingState);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("value", ratingState);
        objectNode.put("likesNumber", ratingService.getValuesNumberByArticleId(articleId, "LIKE"));
        objectNode.put("dislikesNumber", ratingService.getValuesNumberByArticleId(articleId, "DISLIKE"));
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

    @DeleteMapping("/article/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArticle(@RequestBody Long articleId) {
        articleService.delete(articleId);
    }

    @GetMapping("article/edit/{articleId}")
    public String editArticle(@PathVariable("articleId") Long articleId, Model model,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Article article = articleService.findById(articleId);
        if (!article.getAuthor().equals(userDetails.getUser())) {
            model.addAttribute("message", "Access denied!");
            return "message";
        }
        model.addAttribute("listOfCategories", categoryService.findAll());
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PostMapping("article/edit")
    @ResponseStatus(HttpStatus.OK)
    public void editArticle(@RequestBody ArticleDto articleDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Article article = articleService.findById(articleDto.getId());
        if (!article.getAuthor().equals(userDetails.getUser()))
            return;

        articleService.edit(articleDto);
    }
}