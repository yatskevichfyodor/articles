package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findById(Long id);

    Category findByName(String name);

    List<Category> findByArticlesIn(List<Article> articles);

    @Query(value = "SELECT *\n" +
            "FROM category c\n" +
            "WHERE (\n" +
            "\tSELECT COUNT(*)\n" +
            "\tFROM article a\n" +
            "\tWHERE a.category_id = c.id\n" +
            ") > 0;\n", nativeQuery=true)
    List<Category> findUsedCategories();
}
