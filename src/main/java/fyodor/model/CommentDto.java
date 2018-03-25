package fyodor.model;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String author;
    private String timestamp;
}
