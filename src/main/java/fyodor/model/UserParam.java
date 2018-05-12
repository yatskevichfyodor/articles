package fyodor.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_param")
public class UserParam {

    @Data
    @Embeddable
    public static class UserParamId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "attribute_id")
        private UserAttribute attribute;
    }

    @EmbeddedId
    private UserParamId id;

    private String value;
}
