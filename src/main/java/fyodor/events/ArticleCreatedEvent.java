package fyodor.events;

import fyodor.model.Article;
import lombok.Data;

@Data
public class ArticleCreatedEvent {
    private Article article;

    public  ArticleCreatedEvent(Article article) {
        this.article = article;
    }
}
