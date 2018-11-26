package fyodor.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(of={"id", "username", "email", "password"})
@ToString(of={"id", "username", "email", "password"})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

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

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, Date timestamp, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.timestamp = timestamp;
        this.roles = roles;
    }

    public User(String username, String email, String password, Date timestamp, boolean blocked, boolean confirmed, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.timestamp = timestamp;
        this.blocked = blocked;
        this.confirmed = confirmed;
        this.roles = roles;
    }

    public boolean isAdmin() {
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) return true;
        }
        return false;
    }
}
