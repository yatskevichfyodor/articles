package fyodor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(exclude={"roles"})
@ToString(exclude={"roles"})
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

    @Column(nullable = false, columnDefinition = "boolean default 0")
    private boolean blocked = false;

    @Column(nullable = false, columnDefinition = "boolean default 0")
    private boolean confirmed = false;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "author")
    List<Comment> comments;

    public boolean isAdmin() {
        for (Role role : roles) {
            if (role.getName().equals("ROLE_ADMIN")) return true;
        }
        return false;
    }

    public boolean isAnonymous() {
        if (username.equals("anonymousUser"))
            return true;
        return false;
    }
}
