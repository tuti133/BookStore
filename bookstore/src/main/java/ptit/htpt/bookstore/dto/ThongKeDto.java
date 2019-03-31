package ptit.htpt.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class ThongKeDto {
    private Long total = 0L;
    private Long totalBook = 0L;
    private List<BillDto> billDtoList;
}
