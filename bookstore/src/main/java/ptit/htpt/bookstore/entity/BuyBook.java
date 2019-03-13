package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "buy_book")
public class BuyBook implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "buy_id")
    private Buy buy;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
