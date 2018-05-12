package fyodor.repository;

import fyodor.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "SELECT * FROM comment c where c.article_id = :article_id", nativeQuery=true)
    List<Comment> findByArticleId(@Param("article_id") Long id);

    @Transactional
    void deleteById(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id in ?1")
    @Transactional
    void deleteComments(@Param("id") Set<Long> id);
}