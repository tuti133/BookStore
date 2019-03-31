package ptit.htpt.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.BuyBook;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Buy buy;
    private List<BuyBook> listBuyBook;
}
