package fyodor.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    private Long id;

    @NotNull
    private Long articleId;

    @NotNull
    @Size(min=1, max=1000, message="Name should have atleast 3 characters")
    private String text;

    @NotNull
    private String author;
    private String timestamp;
}
