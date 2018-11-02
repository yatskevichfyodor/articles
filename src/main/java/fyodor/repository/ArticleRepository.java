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

//    @Query(value = "SELECT * FROM article a where a.category_id = :category_id", nativeQuery=true)
    @Query(value = "select a from Article as a where a.category.id = :category_id")
    List<Article> findByCategoryId(@Param("category_id") Long id);

    List<Article> findByAuthor(User author);

//    @Query(value = "SELECT *\n" +
//            "FROM article a\n" +
//            "ORDER BY popularity DESC;", nativeQuery=true)
    @Query(value = "SELECT a FROM Article as a ORDER BY a.popularity DESC")
    List<Article> findAllMostPopular();

//    @Query(value = "SELECT *\n" +
//            "FROM article a\n" +
//            "ORDER BY a.timestamp DESC;", nativeQuery=true)
    @Query(value = "SELECT a FROM Article as a ORDER BY a.timestamp DESC")
    List<Article> findAllLastAdded();

//    @Query(value = "SELECT *\n" +
//            "FROM article a\n" +
//            "ORDER BY a.timestamp ASC;", nativeQuery=true)
    @Query(value = "SELECT a FROM Article as a ORDER BY a.timestamp ASC")
    List<Article> findAllFirstAdded();

    Article findByTitleIgnoreCase(String title);

    List<Article> findArticlesByCategoryIn(List<Category> categories);

//    @Modifying
//    @Transactional
////    @Query(value="DELETE FROM article WHERE id=:id", nativeQuery = true)
//    @Query(value="delete Article WHERE id=:id")
//    void delete(@Param("id") Long id);
}
