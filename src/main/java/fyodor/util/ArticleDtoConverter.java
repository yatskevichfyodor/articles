package fyodor.util;

import fyodor.dto.ArticleDto;
import fyodor.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDtoConverter {
    public static List<ArticleDto> covert(List<Article> articles) {
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article: articles) {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(article.getId());
            articleDto.setTitle(article.getTitle());
            articleDto.setImageData(article.getImage().getData());
            articleDtoList.add(articleDto);
        }

        return articleDtoList;
    }
}
