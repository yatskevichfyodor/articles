package fyodor.service;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;
import fyodor.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ArticleService implements IArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    IUserService userService;

    @Override
    public void save(Article article, Principal author) {
        User user = userService.findByUsernameIgnoreCase(author.getName());
        article.setAuthor(user);
        article.setDate(new Date());

        articleRepository.save(article);
    }

    @Override
    public Article findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public List<Article> findByCategoryAndAuthor(Category category, User author) {
        return articleRepository.findByCategoryAndAuthor(category, author);
    }

    @Override
    public List<Article> findByAuthor(User author) {
        return articleRepository.findByAuthor(author);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
