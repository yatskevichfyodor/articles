package fyodor.dto;


import fyodor.validation.ArticleTitleNotExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private Long id;

    @NotNull
    @Size(min=4, max=32, message="Name should have atleast 2 characters")
    @ArticleTitleNotExists
    private String title;

    @NotNull
    @Size(min=1000, max=100000, message="Name should have atleast 2 characters")
    private String content;

    @NotNull
    private String imageData;

    @NotNull
    private Long categoryId;
}
