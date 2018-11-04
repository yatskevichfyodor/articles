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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    @Autowired
    private ArticleDao articleDao;

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

    public boolean checkIfUserAddedArticleRecently(User user) {
        Date lastTimeUserAddedArticle = null;
        try {
            lastTimeUserAddedArticle = articleDao.getLastTimeUserAddedArticle(user);
            if (lastTimeUserAddedArticle == null) return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Date currentTime = new Date();

        return (currentTime.getTime() - lastTimeUserAddedArticle.getTime()) < 1000 * 60 * 60 * 2;
    }

    public Article edit(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).get();
    }

    public List<Article> findByCategoryIdHierarchically(Long id) {
        List<Category> categories = categoryService.findCategoriesAndSubcategoriesById(id);
        return articleRepository.findArticlesByCategoryIn(categories);
    }

    public List<Article> findByCategoryIdAndAuthor(Long categoryId, User author) {
        List<Article> articles = findByCategoryIdHierarchically(categoryId);
        List<Article> filteredArticles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getAuthor().equals(author)) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    public List<Article> findByCategoryIdAndAuthorId(Long categoryId, Long authorId) {
        List<Article> articles = findByCategoryIdHierarchically(categoryId);
        List<Article> filteredArticles = new ArrayList<>();
        for (Article article : articles) {
            if (article.getAuthor().getId() == authorId) {
                filteredArticles.add(article);
            }
        }
        return filteredArticles;
    }

    public Article findByTitleIgnoreCase(String title) {
        return articleRepository.findByTitleIgnoreCase(title);
    }

    public List<Article> findByCategoryIdAndOrderId(Long categoryId, int orderId) {
        List<Category> categories = null;
        if (categoryId == 0) {
            categories = categoryService.findAll();
        } else {
            Category subhierarchy = usedCategoriesHierarchyBuilder.getSubhierarchy(categoryService.findById(categoryId));
            categories = new CategoryHierarchyToListConverter().convert(subhierarchy);
        }

        switch (orderId) {
            case 1:
                return articleDao.findAllByCategoriesSortedByPopularity(categories);
            case 2:
                return articleDao.findAllByCategoriesSortedByDateDesc(categories);
            case 3:
                return articleDao.findAllByCategoriesSortedByDateAsc(categories);
        }

        throw new RuntimeException("Unsupported order");
    }

    @Transactional
    public void delete(Long id) {
        Article article = articleRepository.findById(id).get();
        List<Comment> comments = article.getComments();
        if (comments.size() > 0)
            commentService.deleteComments(comments);
        List<Rating> ratings = article.getRatings();
        if (ratings.size() > 0)
            ratingService.deleteRatings(ratings);
        articleRepository.deleteById(id);
        imageService.delete(article.getImage());

        this.publisher.publishEvent(new ArticleDeletedEvent(article));
    }

    public Article edit(ArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId()).get();
        article.setCategory(categoryService.findById(articleDto.getCategoryId()));
        article.setContent(articleDto.getContent());

        Article editedArticle = articleRepository.save(article);

        this.publisher.publishEvent(new ArticleEditedEvent(article));

        return editedArticle;
    }
}
