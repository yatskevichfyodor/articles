package fyodor.repository;

import fyodor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM category c\n" +
            "WHERE\n" +
            "\t(\n" +
            "\t\tSELECT COUNT(*)\n" +
            "\t\tFROM article a\n" +
            "\t\tWHERE a.category_id = c.id\n" +
            "\t) > 0", nativeQuery=true)
    List<Category> findUsedCategories();
}
