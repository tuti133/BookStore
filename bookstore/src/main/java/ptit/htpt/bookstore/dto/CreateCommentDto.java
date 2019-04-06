package ptit.htpt.bookstore.dto;

import lombok.Data;

@Data
public class CreateCommentDto {
    private String phone;
    private Long bookId;
    private String comment;
}
