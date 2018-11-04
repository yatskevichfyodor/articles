package fyodor.repository;

import fyodor.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    String articlesTablesName = "articles";
    String categoriesTablesName = "categories";

    Category findByName(String name);

//    @Query(value = "\n" +
//            "SELECT *\n" +
//            "FROM \"categories\" c\n" +
//            "WHERE\n" +
//            "\t(\n" +
//            "\t\tSELECT COUNT(*)\n" +
//            "\t\tFROM \"" + articlesTablesName + "\" a\n" +
//            "\t\tWHERE a.\"category_id\" = c.\"id\"\n" +
//            "\t) > 0", nativeQuery=true)
    @Query("SELECT c FROM Category c WHERE c IN (SELECT a.category FROM Article a)")
    List<Category> findUsedCategories();

    @Query(value = "\n" +
            "SELECT *\n" +
            "FROM\n" +
            "  " + categoriesTablesName + " c\n" +
            "WHERE\n" +
            "  c.id IN\n" +
            "    (SELECT\n" +
            "      a.category_id\n" +
            "    FROM\n" +
            "      " + articlesTablesName + " a\n" +
            "    WHERE\n" +
            "      a.author_id = :id\n" +
            "    )", nativeQuery=true)
//    @Query("SELECT c FROM Category c WHERE (SELECT COUNT(*) FROM Article a WHERE a.category.id = c.id) > 0")
    List<Category> findUsedCategoriesByUserId(@Param("id") Long id);
}
