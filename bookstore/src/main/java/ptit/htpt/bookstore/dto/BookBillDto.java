package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class BookBillDto {
  private Long quantity;
  private Long bookId;
}