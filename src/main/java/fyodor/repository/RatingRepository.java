package fyodor.repository;

import fyodor.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Set;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT * FROM rating r where r.user_id = :user_id and r.article_id = :article_id", nativeQuery=true)
    Rating findByUserIdAndArticleId(@Param("user_id") Long userId, @Param("article_id") Long articleId);

//    @Query(value = "DELETE FROM rating where user_id = :user_id and article_id = :article_id", nativeQuery=true)
//    void deleteByUserIdAndArticleId(@Param("user_id") Long userId, @Param("article_id") Long articleId);

    void deleteById(Rating.RatingId ratingId);

    @Query(value = "SELECT COUNT(*) FROM rating where article_id = :article_id and value = :value", nativeQuery=true)
    Long getValuesNumberByArticleId(@Param("article_id") String id, @Param("value") String value);

    @Modifying
    @Query("DELETE FROM Rating c WHERE c.id in ?1")
    @Transactional
    void deleteRatings(@Param("id") Set<Rating.RatingId> id);
}