package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "customer")
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String phone;

    @Column(columnDefinition = "nvarchar(50)")
    private String name;

    private String gender;

    @Column(columnDefinition = "nvarchar(500)")
    private String address;

    @Column(name = "credit_number")
    private String creditNumber;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;


}
