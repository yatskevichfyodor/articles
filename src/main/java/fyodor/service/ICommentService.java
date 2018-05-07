package fyodor.service;

import fyodor.dto.CommentDto;
import fyodor.model.Comment;
import fyodor.model.User;

import java.security.Principal;
import java.util.List;

public interface ICommentService {

    void save(Comment comment);

    Comment save(String json, Principal principal);

    Comment save(CommentDto commentDto, User author);

    Comment findById(Long id);

    List<Comment> findByArticleId(Long id);

    void delete(Long id);
}
