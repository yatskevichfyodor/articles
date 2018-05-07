package fyodor.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rating")
@Data
//@EqualsAndHashCode(exclude={"id"})
//@ToString(exclude={"id"})
public class Rating {
    public enum RatingEnum {
        LIKE,
        DISLIKE
    }

    @Embeddable
    public static class RatingId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "article_id")
        private Article article;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        public RatingId() {
        }

        public Article getArticle() {
            return article;
        }

        public void setArticle(Article article) {
            this.article = article;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    @EmbeddedId
    private RatingId id;

    @Enumerated(EnumType.STRING)
    private RatingEnum value;

}
