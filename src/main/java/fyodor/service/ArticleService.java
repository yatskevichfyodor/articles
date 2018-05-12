package fyodor.service;

import fyodor.dto.ArticleDto;
import fyodor.events.ArticleAddedEvent;
import fyodor.events.ArticleDeletedEvent;
import fyodor.events.ArticleEditedEvent;
import fyodor.model.*;
import fyodor.repository.ArticleDao;
import fyodor.repository.ArticleRepository;
import fyodor.util.CategoryHierarchyToListConverter;
import fyodor.util.UsedCategoriesHierarchyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService implements IArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IRatingService ratingService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    @Autowired
    private ArticleDao articleDao;

    @Override
    public Article save(ArticleDto articleDto, User author) {
        Article article = new Article();
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        article.setCategory(categoryService.findById(articleDto.getCategoryId()));
        article.setAuthor(author);
        String imageCode = articleDto.getImageData();
        Image image = imageService.save(imageCode);
        article.setImage(image);

        Article savedArticle = articleRepository.save(article);

        this.publisher.publishEvent(new ArticleAddedEvent(savedArticle));

        return savedArticle;
    }

    @Override
    public boolean checkIfUserAddedArticleRecently(User user) {
        Date lastTimeUserAddedArticle = null;
        try {
            lastTimeUserAddedArticle = articleDao.getLastTimeUserAddedArticle(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Date currentTime = new Date();

        return (currentTime.getTime() - lastTimeUserAddedArticle.getTime()) < 1000 * 60 * 60 * 2;
    }

    @Override
    public Article edit(Article article) {
        return articleRepository.save(article);
    }


    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id).get();
    }

    @Override
    public List<Article> findByCategoryIdHierarchically(Long id) {
        List<Category> categories = categoryService.findCategoriesAndSubcategoriesById(id);
        return articleRepository.findArticlesByCategoryIn(categories);
    }

    @Override
    public List<Article> findByCategoryIdAndAuthor(Long categoryId, User author) {
        List<Article> articles = findByCategoryIdHierarchically(categoryId);
        List<Article> filteredArticles = new ArrayList<>();
        for (Article article: articles) {
            if (article.getAuthor().equals(author)) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    @Override
    public Article findByTitleIgnoreCase(String title) {
        return articleRepository.findByTitleIgnoreCase(title);
    }

    @Override
    public List<Article> findByCategoryIdAndOrderId(Long categoryId, int orderId) {
        List<Category> categories = null;
        if (categoryId == 0) {
            categories = categoryService.findAll();
        } else {
            Category subhierarchy = usedCategoriesHierarchyBuilder.getSubhierarchy(categoryService.findById(categoryId));
            categories = new CategoryHierarchyToListConverter().convert(subhierarchy);
        }
        try {
            switch (orderId) {
                case 1:
                    return articleDao.getArticlesByCategoriesSortedByPopularity(categories);
                case 2:
                    return articleDao.getArticlesByCategoriesSortedByDateDesc(categories);
                case 3:
                    return articleDao.getArticlesByCategoriesSortedByDateAsc(categories);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unsupported order");
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Article article = articleRepository.findById(id).get();
        List<Comment> comments = article.getComments();
        if (comments.size() > 0)
            commentService.deleteComments(comments);
        List<Rating> ratings = article.getRatings();
        if (ratings.size() > 0)
            ratingService.deleteRatings(ratings);
        articleRepository.delete(id);
        imageService.delete(article.getImage());

        this.publisher.publishEvent(new ArticleDeletedEvent(article));
    }

    @Override
    public Article edit(ArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId()).get();
        article.setCategory(categoryService.findById(articleDto.getCategoryId()));
        article.setContent(articleDto.getContent());

        Article editedArticle = articleRepository.save(article);

        this.publisher.publishEvent(new ArticleEditedEvent(article));

        return editedArticle;
    }
}
