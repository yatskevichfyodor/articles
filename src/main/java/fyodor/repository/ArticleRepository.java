package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    Article findByTitleIgnoreCase(String title);

    List<Article> findArticlesByCategoryIn(List<Category> categories);

}
