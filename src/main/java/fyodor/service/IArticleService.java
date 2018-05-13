package fyodor.service;

import fyodor.dto.ArticleDto;
import fyodor.model.Article;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;

public interface IArticleService {

    Article save(ArticleDto articleDto, User author);

    boolean checkIfUserAddedArticleRecently(User user);

    Article edit(Article article);

    Article findById(Long id);

    List<Article> findByCategoryIdHierarchically(Long id);

    List<Article> findByCategoryIdAndAuthor(Long categoryId, User author);

    List<Article> findByCategoryIdAndAuthorId(Long categoryId, Long authorId);

    Article findByTitleIgnoreCase(String title);

    List<Article> findByCategoryIdAndOrderId(Long categoryId, int orderId);

    void delete(Long id);

    Article edit(ArticleDto articleDto);
}
