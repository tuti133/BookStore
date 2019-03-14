package ptit.htpt.bookstore.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ptit.htpt.bookstore.dto.CreateBookDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.service.BookService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

  @PostMapping(value = "books")
  public ResponseDto saveBook(@ModelAttribute CreateBookDto createBookDto) {
    return bookService.save(createBookDto);
  }

}