package fyodor.events;

import fyodor.model.Article;
import lombok.Data;

@Data
public class ArticleDeletedEvent {
    private Article article;

    public ArticleDeletedEvent(Article article) {
        this.article = article;
    }
}
