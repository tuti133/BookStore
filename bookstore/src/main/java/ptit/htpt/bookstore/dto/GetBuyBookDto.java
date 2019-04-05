package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class GetBuyBookDto {
    private String bookName;
    private String bookImage;
    private Long quantity;
    private String storeName;
    private Long price;
}
