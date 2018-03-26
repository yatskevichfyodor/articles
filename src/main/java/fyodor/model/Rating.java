package fyodor.model;

import lombok.Data;

import javax.persistence.*;

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

    @EmbeddedId
    private RatingId id;

    @Enumerated(EnumType.STRING)
    private RatingEnum value;

}
