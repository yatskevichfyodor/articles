package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>{
    Article findById(Long id);

    Article findByTitle(String title);

    List<Article> findByCategoryAndAuthor(Category category, User author);

    @Query(value = "SELECT * FROM article a where a.category_id = :category_id", nativeQuery=true)
    List<Article> findByCategoryId(@Param("category_id") Long id);

    List<Article> findByAuthor(User author);

    @Query(value = "SELECT *\n" +
            "FROM article a\n" +
            "ORDER BY popularity DESC;", nativeQuery=true)
    List<Article> findAllMostPopular();

    @Query(value = "SELECT *\n" +
            "FROM article a\n" +
            "ORDER BY a.timestamp DESC;", nativeQuery=true)
    List<Article> findAllLastAdded();

    @Query(value = "SELECT *\n" +
            "FROM article a\n" +
            "ORDER BY a.timestamp ASC;", nativeQuery=true)
    List<Article> findAllFirstAdded();

    Article findByTitleIgnoreCase(String title);

    List<Article> findArticlesByCategoryIn(List<Category> categories);

//    List<Article> findSortByPopularityDescByCategoryIn(List<Category> categories);
//
//    List<Article> findSortByTimestampAscByCategoryIn(List<Category> categories);
//
//    List<Article> findSortByTimestampDescByCategoryIn(List<Category> categories);
}
