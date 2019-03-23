package ptit.htpt.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.BookStore;
import ptit.htpt.bookstore.entity.Employee;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeAccountDto {
    private Long accountId;
    private Long employeeId;
    private String username;
    private String password;
    private Boolean activated;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Long salary;
    private Long workShift;
    private Long bookStoreId;
}
