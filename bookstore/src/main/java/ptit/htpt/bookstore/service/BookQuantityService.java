package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.BookQuantity;
import ptit.htpt.bookstore.entity.BookStore;
import ptit.htpt.bookstore.repository.BookQuantityRepository;
import ptit.htpt.bookstore.repository.BookRepository;
import ptit.htpt.bookstore.repository.BookStoreRepository;

import java.util.List;

@Service
public class BookQuantityService {
    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Autowired
    private BookStoreRepository bookStoreRepository;

    @Autowired
    private BookRepository bookRepository;

    public ResponseDto getOneByStore(Long bookId, Long storeId){
        BookStore bs = new BookStore();
        bs.setId(storeId);
        Book b = new Book();
        b.setId(bookId);
        BookQuantity bq = bookQuantityRepository.findByBookStoreAndBook(bs, b);
        return new ResponseDto("0", "Success", bq);
    }

    public ResponseDto getByStore(Long bookStoreId) {
        BookStore bs = new BookStore();
        bs.setId(bookStoreId);
        List<BookQuantity> list = bookQuantityRepository.findAllByBookStore(bs);
        return new ResponseDto("0", "Success", list);
    }

    public ResponseDto update(BookQuantity bookQuantity) {
        try {
            BookQuantity bq = bookQuantityRepository.save(bookQuantity);
            return new ResponseDto("0", "Cập nhật thành công", bq);
        } catch (Exception e) {
            return new ResponseDto("1", "Cập nhật không thành công", null);
        }
    }
}
