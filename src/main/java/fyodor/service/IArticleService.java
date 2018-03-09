package fyodor.service;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;

public interface IArticleService {

    void save(String json, Principal author);

    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    List<Article> findByAuthor(User Author);

    List<Article> findAll();
}
