package fyodor.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class UserParam {
    @Embeddable
    public static class UserParamId implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "attribute_id")
        private UserAttribute attribute;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public UserAttribute getAttribute() {
            return attribute;
        }

        public void setAttribute(UserAttribute attribute) {
            this.attribute = attribute;
        }
    }

    @EmbeddedId
    private UserParamId id;

    private String value;
}
