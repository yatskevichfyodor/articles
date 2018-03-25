package fyodor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="comment")
@Data
@EqualsAndHashCode(exclude={"author", "article"})
@ToString(exclude={"author", "article"})
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;
}
