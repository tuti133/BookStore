package ptit.htpt.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Employee;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    String role;
    Account account;
    Employee employee;
    Customer customer;
}
