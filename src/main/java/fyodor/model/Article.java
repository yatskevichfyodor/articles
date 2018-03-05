package fyodor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "article")
@Data
@EqualsAndHashCode(exclude={"content", "category", "author"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;

    @Type(type="date")
    private Date date = new Date();

    @ManyToOne
    @JoinColumn(name = "categoryid", referencedColumnName = "id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "authorid", referencedColumnName = "id")
    private User author;

    @OneToOne
    private Image image;
}
