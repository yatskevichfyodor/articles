package fyodor.repository;

import fyodor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM\n" +
            "  category c\n" +
            "WHERE\n" +
            "  c.id IN\n" +
            "    (SELECT\n" +
            "      a.category_id\n" +
            "    FROM\n" +
            "      article a\n" +
            "    WHERE\n" +
            "      a.author_id = :id\n" +
            "    )", nativeQuery=true)
    List<Category> findUsedCategoriesByUserId(@Param("id") Long id);
}
