package fyodor.service;

import fyodor.model.Comment;

import java.security.Principal;
import java.util.List;

public interface ICommentService {

    void save(Comment comment);

    Comment save(String json, Principal principal);

    Comment findById(Long id);

    List<Comment> findByArticleId(Long id);

    void delete(Long id);
}
