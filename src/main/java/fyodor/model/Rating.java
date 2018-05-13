package fyodor.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rating")
@Data
public class Rating {
    public enum RatingEnum {
        LIKE,
        DISLIKE
    }

    @Data
    @Embeddable
    public static class RatingId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "article_id")
        private Article article;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
    }

    @EmbeddedId
    private RatingId id;

    @Enumerated(EnumType.STRING)
    private RatingEnum value;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();
}
