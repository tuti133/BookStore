package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.CreateCommentDto;
import ptit.htpt.bookstore.dto.GetCommentDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.service.CommentService;

import java.util.List;

@RestController
public class CommentResource {
    @Autowired
    private CommentService commentService;
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/book/comment")
    public List<GetCommentDto> getAllCommentByBook(@RequestParam("bookId") Long bookId) {
        return commentService.getAllCommentByBook(bookId);
    }

    @PostMapping("/api/book/comment")
    public ResponseDto createComment(@RequestBody CreateCommentDto createCommentDto) {
        createCommentDto.setPhone(accountService.getCurrentCustomer().getPhone());
        return commentService.createComment(createCommentDto);
    }
}
