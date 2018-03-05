package fyodor.model;

import lombok.Data;

@Data
public class ArticleDto {
    private Long category;
    private String title;
    private String content;
    private String picture;
}
