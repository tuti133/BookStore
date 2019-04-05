package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class BillDto {
    private Long id;
    private int type;
    private String createDate;
    private String status;
    private String customerName;
    private String phone;
    private Long total;
}
