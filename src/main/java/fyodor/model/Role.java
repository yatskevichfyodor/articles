package fyodor.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@EqualsAndHashCode(of={"id", "name"})
@ToString(of={"id", "name"})
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(String name) {
        this.name = name;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
