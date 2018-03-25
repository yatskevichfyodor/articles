package fyodor.repository;

import fyodor.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findById(Long id);

    @Query(value = "SELECT * FROM comment c where c.article_id = :article_id", nativeQuery=true)
    List<Comment> findByArticleId(@Param("article_id") Long id);

    void deleteById(long id);
}