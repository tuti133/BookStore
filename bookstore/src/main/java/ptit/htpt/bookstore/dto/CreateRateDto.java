package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class CreateRateDto {
    private String phone;
    private Long bookId;
    private Long rate;
}
