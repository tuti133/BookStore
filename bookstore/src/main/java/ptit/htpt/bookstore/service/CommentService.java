package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.CreateCommentDto;
import ptit.htpt.bookstore.dto.GetCommentDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.Comment;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.repository.CommentRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public List<GetCommentDto> getAllCommentByBook(Long bookId) {
        List<GetCommentDto> result = new ArrayList<>();
        Book book = new Book();
        book.setId(bookId);
        List<Comment> comments = commentRepository.findAllByBookOrderByCreateDateDesc(book);
        for (Comment comment : comments) {
            GetCommentDto getCommentDto = new GetCommentDto();
            getCommentDto.setComment(comment.getComment());
            getCommentDto.setCommentId(comment.getId());
            getCommentDto.setCreateDate(comment.getCreateDate());
            getCommentDto.setCustomerName(comment.getCustomer().getName());
            result.add(getCommentDto);
        }
        return result;
    }

    public ResponseDto createComment(CreateCommentDto createCommentDto) {
        Book book = new Book();
        book.setId(createCommentDto.getBookId());
        Customer customer = new Customer();
        customer.setPhone(createCommentDto.getPhone());
        Comment comment = new Comment();
        comment.setCreateDate(new Date().getTime());
        comment.setComment(createCommentDto.getComment());
        comment.setBook(book);
        comment.setCustomer(customer);
        comment = commentRepository.save(comment);
        return new ResponseDto("0", "success", comment);
    }
}
