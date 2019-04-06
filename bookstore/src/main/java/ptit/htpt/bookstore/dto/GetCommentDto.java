package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class GetCommentDto {
    private Long commentId;
    private String comment;
    private Long createDate;
    private String customerName;
}
