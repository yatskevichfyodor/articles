package fyodor.service;

import fyodor.dto.CommentDto;
import fyodor.model.Comment;
import fyodor.model.User;
import fyodor.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private UserService userService;
    @Autowired private ArticleService articleService;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment save(String json, Principal author) {
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = springParser.parseMap(json);

        Comment comment = new Comment();
        User user = userService.findByUsernameIgnoreCase(author.getName());
        comment.setAuthor(user);
        comment.setText((String)data.get("text"));
        comment.setArticle(articleService.findById(Long.valueOf((String)data.get("articleId"))));

        return commentRepository.save(comment);
    }

    public Comment save(CommentDto commentDto, User author) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setText(commentDto.getText());
        comment.setArticle(articleService.findById(commentDto.getArticleId()));

        return commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    public List<Comment> findByArticleId(Long id) {
        return commentRepository.findByArticleId(id);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public void deleteComments(List<Comment> comments) {
        Set<Long> commentIds = new HashSet<>();
        for (Comment comment: comments) {
            commentIds.add(comment.getId());
        }
        commentRepository.deleteComments(commentIds);
    }
}
