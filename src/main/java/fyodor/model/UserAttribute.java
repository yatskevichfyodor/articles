package fyodor.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_attributes")
public class UserAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean enabled = true;
}
