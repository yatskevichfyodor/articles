package fyodor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.deploy.net.HttpResponse;
import fyodor.dto.CommentDto;
import fyodor.model.Comment;
import fyodor.model.User;
import fyodor.model.UserParam;
import fyodor.service.CustomUserDetails;
import fyodor.service.ICommentService;
import fyodor.service.IUserService;
import fyodor.validation.CommentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Controller
public class CommentController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private CommentValidator commentValidator;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @ModelAttribute("currentUser")
    public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return null;
        return userDetails.getUser();
    }

    @PostMapping("comment/save")
    @ResponseBody
    public ResponseEntity<?> saveComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Set<Integer> errorsSet = commentValidator.validate(commentDto.getText());
        if (errorsSet.size() != 0) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Comment comment = commentService.save(commentDto, userService.findByUsernameIgnoreCase(userDetails.getUser().getUsername()));
        CommentDto savedCommentDto = new CommentDto();
        savedCommentDto.setId(comment.getId());
        savedCommentDto.setAuthor(comment.getAuthor().getUsername());
        savedCommentDto.setTimestamp(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getTimestamp())));
        savedCommentDto.setText(comment.getText());
        simpMessagingTemplate.convertAndSend("/comments/" + commentDto.getArticleId() , savedCommentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comment")
    @ResponseBody
    public String deleteComment(@RequestBody Long commentId) {
        commentService.delete(commentId);
        return String.valueOf(commentId);
    }

    @PostMapping("/updateComment")
    @ResponseBody
    public ResponseEntity<?> updateComment(@RequestBody CommentDto commentDto) {
        Set<Integer> errorsSet = commentValidator.validate(commentDto.getText());
        if (errorsSet.size() == 0) {
            Comment editableComment = commentService.findById(commentDto.getId());
            editableComment.setText(commentDto.getText());
            Date newTimestamp = new Date();
            editableComment.setTimestamp(newTimestamp);
            commentService.save(editableComment);

            commentDto.setTimestamp(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(newTimestamp)));
            return new ResponseEntity<>(commentDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
    }
}