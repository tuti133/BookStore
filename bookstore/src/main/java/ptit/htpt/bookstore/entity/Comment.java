package ptit.htpt.bookstore.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "comment")
@Data
public class Comment implements Serializable {
    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "phone")
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name="book_id")
    private Book book;

    @NotNull
    private Long createDate;

    @NotNull
    private String comment;
}
