package ptit.htpt.bookstore.dto;

import lombok.Data;

import java.util.List;

@Data
public class ThongKeDto {
    private Long total = 0L;
    private Long totalOnline = 0L;
    private Long totalOffline = 0L;
    private Long totalBookOnline = 0L;
    private Long totalBookOffline = 0L;
    private Long totalBookHanoi = 0L;
    private Long totalBookHcm = 0L;
    private Long totalHanoi = 0L;
    private Long totalHcm = 0L;
    private List<BillDto> billDtoList;
}
