package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class BuyBookDto {
    private Long bookId;
    private Long storeId;
    private Long quantity;
}
