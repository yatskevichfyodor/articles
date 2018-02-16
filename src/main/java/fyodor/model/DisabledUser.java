package fyodor.model;

import javax.persistence.OneToOne;

public class DisabledUser {
    @OneToOne()
    private User user;

    public DisabledUser() {
    }

    public DisabledUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
