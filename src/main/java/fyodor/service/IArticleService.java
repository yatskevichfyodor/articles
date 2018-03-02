package fyodor.service;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface IArticleService {
    void save(Article article, Principal author);

    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    List<Article> findByAuthor(User Author);

    List<Article> findAll();
}
