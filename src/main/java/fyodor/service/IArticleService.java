package fyodor.service;

import fyodor.dto.ArticleDto;
import fyodor.model.Article;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;

public interface IArticleService {

    Article save(String json, Principal author);

    Article save(ArticleDto articleDto, Principal author);

    Article save(Article article);

    Article findById(Long id);

    Article findByTitle(String title);

    List<Article> findByCategoryId(Long id);

    List<Article> findByCategoryIdHierarchically(Long id);

    List<Article> findByCategoryIdAndAuthor(Long categoryId, User author);

    List<Article> findByAuthor(User Author);

    List<Article> findAll();

    List<Article> findAllWithOrder(Long orderId);

    Article findByTitleIgnoreCase(String title);

    List<Article> findByCategoryIdAndOrderId(Long categoryId, int orderId);
}
