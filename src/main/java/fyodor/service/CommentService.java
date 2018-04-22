package fyodor.service;

import fyodor.model.Comment;
import fyodor.model.User;
import fyodor.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
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

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findByArticleId(Long id) {
        return commentRepository.findByArticleId(id);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

}
