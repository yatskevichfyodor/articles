package fyodor.Dto;


import lombok.Data;

@Data
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String imageData;
    private Long categoryId;
}
