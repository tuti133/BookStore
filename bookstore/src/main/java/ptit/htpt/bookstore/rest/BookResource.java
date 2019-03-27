package ptit.htpt.bookstore.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.service.BookService;

@RestController
@RequestMapping("/api/")
public class BookResource {
    @Autowired
    private BookService bookService;

    @GetMapping("books")
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping(value = "books/{id}")
    public Book getBookById(@PathVariable("id") long id) {
        return bookService.findById(id);
    }

    // create and update
    @PostMapping(value = "books")
    public ResponseDto saveBook(@RequestPart("book") Book book, @RequestPart(required=false, name = "image") MultipartFile image) {
        return bookService.save(book, image);
    }

}