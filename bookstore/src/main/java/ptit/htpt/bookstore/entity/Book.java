package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "book")
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String author;

    @Column(name = "image_url")
    private String imageUrl;

    private String description;

    @NotNull
    private Long price;

    @NotNull
    private String publisher;

    @NotNull
    @Column(name = "published_year")
    private Long publishedYear;
}
