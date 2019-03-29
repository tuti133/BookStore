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
    @Column(columnDefinition = "nvarchar(100)")
    private String name;

    @NotNull
    @Column(columnDefinition = "nvarchar(100)")
    private String author;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(columnDefinition = "nvarchar(999)")
    private String description;

    @NotNull
    private Long price;

    @NotNull
    @Column(columnDefinition = "nvarchar(100)")
    private String publisher;

    @NotNull
    @Column(name = "published_year")
    private Long publishedYear;

    private Long favorite = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
