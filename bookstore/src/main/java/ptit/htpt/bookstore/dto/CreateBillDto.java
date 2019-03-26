package ptit.htpt.bookstore.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreateBillDto {
  private  String customerName;
  private String customerPhone;
  private Long total;
  private Long employeeId;
  private List<BookBillDto> bookList;
}