package ptit.htpt.bookstore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ptit.htpt.bookstore.dto.CreateBookDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.repository.BookRepository;

@Service
public class BookService {
  @Autowired
  private BookRepository bookRepository;

  public List<Book> getAll() {
    return bookRepository.findAll();
  }

  public Book findById(long id) {
    return bookRepository.findById(id).orElse(null);
  }

  public ResponseDto update(Book book) {
    bookRepository.save(book);
    return new ResponseDto("0", "success", null);
  }

  private String getExtensionFile(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.'));
  }

  public ResponseDto save(CreateBookDto createBookDto) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsszz");
    String fileName = sdf.format(new Date()) + getExtensionFile(createBookDto.getImage().getOriginalFilename());
    try {
      Files.createDirectories(Paths.get("/image"));
      Files.copy(createBookDto.getImage().getInputStream(), Paths.get("/image", fileName));
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseDto("1", e.getMessage(), null);
    }
    createBookDto.getImage();
    Book book = new Book();
    book.setImageUrl(Paths.get("/image", fileName).toString());
    book.setAuthor(createBookDto.getAuthor());
    book.setName(createBookDto.getName());
    book.setDescription(createBookDto.getDescription());
    book.setPrice(createBookDto.getPrice());
    book.setPublishedYear(createBookDto.getPublishedYear());
    book.setPublisher(createBookDto.getPublisher());
    bookRepository.save(book);
    return new ResponseDto("0", "success", null);
  }
}