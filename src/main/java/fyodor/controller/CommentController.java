package fyodor.controller;

import fyodor.dto.CommentDto;
import fyodor.exception.ForbiddenException;
import fyodor.model.Comment;
import fyodor.model.User;
import fyodor.repository.UserRepository;
import fyodor.service.CommentService;
import fyodor.service.CustomUserDetails;
import fyodor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Controller
public class CommentController {

    @Autowired private UserRepository userRepository;
    @Autowired private CommentService commentService;
    @Autowired private SimpMessagingTemplate simpMessagingTemplate;

    @ModelAttribute("currentUser")
    public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return null;
        return userDetails.getUser();
    }

    @PostMapping("comment/add")
    @ResponseBody
    public ResponseEntity<?> saveComment(@RequestBody CommentDto commentDto, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (errors.hasErrors())
            throw new RuntimeException(("Article validation error.\n" + errors.getAllErrors().toString()));
        Comment comment = commentService.save(commentDto, userRepository.findByUsernameIgnoreCase(userDetails.getUser().getUsername()));
        CommentDto savedCommentDto = new CommentDto();
        savedCommentDto.setId(comment.getId());
        savedCommentDto.setAuthor(comment.getAuthor().getUsername());
        savedCommentDto.setTimestamp(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getTimestamp())));
        savedCommentDto.setText(comment.getText());
        simpMessagingTemplate.convertAndSend("/comments/" + commentDto.getArticleId(), savedCommentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/comment/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@RequestBody Long commentId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new ForbiddenException();
        } else if (!commentService.findById(commentId).getAuthor().equals(userDetails.getUser()) &&
                !userDetails.getUser().isAdmin()) {
            throw new ForbiddenException();
        }

        commentService.delete(commentId);
    }

    @PostMapping("/comment/edit")
    @ResponseBody
    public ResponseEntity<?> updateComment(@RequestBody CommentDto commentDto, Errors errors,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (errors.hasErrors())
            throw new RuntimeException(("Article validation error.\n" + errors.getAllErrors().toString()));

        Comment editableComment = commentService.findById(commentDto.getId());
        editableComment.setText(commentDto.getText());
        Date newTimestamp = new Date();
        editableComment.setTimestamp(newTimestamp);
        commentService.save(editableComment);

        commentDto.setTimestamp(String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(newTimestamp)));
        return new ResponseEntity<>(commentDto, HttpStatus.OK);

    }
}