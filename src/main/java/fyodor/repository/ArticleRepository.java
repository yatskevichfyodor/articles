package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    List<Article> findByAuthor(User author);
}
