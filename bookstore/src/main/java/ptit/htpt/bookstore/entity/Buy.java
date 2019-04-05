package ptit.htpt.bookstore.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "buy")
public class Buy {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "created_date")
    private Long createdDate;

    @NotNull
    @Column(name = "ship_address", columnDefinition = "nvarchar(500)")
    private String shipAddress;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status;

    @Column(columnDefinition = "nvarchar(500)")
    private String note;

    @NotNull
    @Column(name = "total_money")
    private Long totalMoney;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
