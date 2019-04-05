package ptit.htpt.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateBuyDto {
    private String shipAddress;
    private String note;
    private Long totalMoney;
    private String phone;
    private List<BuyBookDto> buyBookDtoList;

}
