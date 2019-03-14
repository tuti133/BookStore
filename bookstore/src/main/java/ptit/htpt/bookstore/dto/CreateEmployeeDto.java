package ptit.htpt.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.htpt.bookstore.entity.BookStore;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String authority;
    private Long salary;
    private Long workShift;
    private BookStore bookStore;
}
