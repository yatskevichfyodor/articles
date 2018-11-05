package fyodor.repository;

import fyodor.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Set;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT r FROM Rating r where r.id.user.id = :user_id and r.id.article.id = :article_id")
    Rating findByUserIdAndArticleId(@Param("user_id") Long userId, @Param("article_id") Long articleId);

    void deleteById(Rating.RatingId ratingId);

    @Query(value = "SELECT COUNT(r) FROM Rating r where r.id.article.id = :article_id and r.value = :value")
    Long getValuesNumberByArticleId(@Param("article_id") Long id, @Param("value") Rating.RatingEnum value);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.id in ?1")
    @Transactional
    void deleteRatings(@Param("id") Set<Rating.RatingId> id);
}