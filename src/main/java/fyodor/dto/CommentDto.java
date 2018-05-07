package fyodor.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long articleId;
    private String text;
    private String author;
    private String timestamp;
}
