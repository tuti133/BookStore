package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "employee")
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String gender;

    @Column(name = "work_shift")
    private Long workShift;

    @ManyToOne
    @JoinColumn(name = "book_store_id")
    private BookStore bookStore;

    @NotNull
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
