package fyodor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fyodor.model.Comment;
import fyodor.model.User;
import fyodor.service.ICommentService;
import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICommentService commentService;


    @ModelAttribute("currentUser")
    public User getPrincipal(Principal principal) {
        if (principal == null) return null;

        return userService.findByUsernameIgnoreCase(principal.getName());
    }

    @MessageMapping("/comment/save")
    @SendTo("/comments")
    public String saveComment(@RequestBody String json, Principal principal) {
        Comment comment = commentService.save(json, principal);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", comment.getId());
        objectNode.put("author", comment.getAuthor().getUsername());
        objectNode.put("text", comment.getText());
        objectNode.put("timestamp", String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(comment.getTimestamp())));
        return objectNode.toString();
    }

    @DeleteMapping("/comment")
//    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteComment(@RequestBody String requestBody) {
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = springParser.parseMap(requestBody);

        Long commentId = Long.valueOf((String)data.get("id"));
        commentService.delete(commentId);
        return String.valueOf(commentId);
    }
}