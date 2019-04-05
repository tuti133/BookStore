package ptit.htpt.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccountDto {
    private Long accountId;
    private String username;
    private String password;
    private Boolean activated;
    private String name;
    private String email;
    private String phone;
    private String creditNumber;
    private String address;
}
