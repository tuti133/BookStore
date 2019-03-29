package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "book_store")
public class BookStore {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "nvarchar(100)")
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Column(unique = true)
    private String area;


}
