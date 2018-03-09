package fyodor.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(exclude={"users"})
@ToString(exclude={"users"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
