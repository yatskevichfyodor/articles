package fyodor.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Lob
    @Column(columnDefinition = "TEXT")
    private String data;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "image_id")
    Article article;
}