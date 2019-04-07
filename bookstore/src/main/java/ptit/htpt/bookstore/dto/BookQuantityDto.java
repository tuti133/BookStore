package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class BookQuantityDto {
    private Long id;
    private Long bookPrice;
    private String bookName;
    private Long quantity;
}
