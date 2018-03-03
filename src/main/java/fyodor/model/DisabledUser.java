package fyodor.model;

import lombok.Data;

import javax.persistence.OneToOne;

@Data
public class DisabledUser {
    @OneToOne()
    private User user;
}
