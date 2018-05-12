package fyodor.events;

import fyodor.model.Article;
import lombok.Data;

@Data
public class ArticleEditedEvent{
    private Article article;

    public ArticleEditedEvent(Article article) {
        this.article = article;
    }
}