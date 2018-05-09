package fyodor.events;

import fyodor.model.Article;
import lombok.Data;

@Data
public class ArticleAddedEvent {
    private Article article;

    public ArticleAddedEvent(Article article) {
        this.article = article;
    }
}
