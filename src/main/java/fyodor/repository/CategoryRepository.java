package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findById(Long id);

    Category findByName(String name);

    List<Category> findByArticlesIn(List<Article> articles);
}
