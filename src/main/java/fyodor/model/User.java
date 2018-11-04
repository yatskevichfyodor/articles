package fyodor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(of={"id", "username", "email", "password"})
@ToString(of={"id", "username", "email", "password"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @Transient
    private String confirmPassword;

    private boolean blocked = false;
    private boolean confirmed = false;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "author")
    List<Comment> comments;

    @OneToMany(mappedBy = "id.user")
    List<Rating> ratings;

    @OneToMany(mappedBy = "id.user")
    List<UserParam> params;

    public boolean isAdmin() {
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) return true;
        }
        return false;
    }
}
