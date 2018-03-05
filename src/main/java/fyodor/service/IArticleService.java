package fyodor.service;

import fyodor.model.Article;
import fyodor.model.ArticleDto;
import fyodor.model.Category;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;

public interface IArticleService {
    void save(Article article, Principal author);

    void save(ArticleDto articleDto, Principal author);

    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    List<Article> findByAuthor(User Author);

    List<Article> findAll();
}
