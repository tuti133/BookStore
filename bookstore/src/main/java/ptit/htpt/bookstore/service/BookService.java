package ptit.htpt.bookstore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
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

  public ResponseDto save(Book book, MultipartFile image) {
    Book result = new Book();
    if (book.getId() != null){
      result = bookRepository.findById(book.getId()).get();
    }

    if (image != null){
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsszz");
      String fileName = sdf.format(new Date()) + getExtensionFile(image.getOriginalFilename());
      result.setImageUrl(Paths.get("/image", fileName).toString());
      try {
        Files.createDirectories(Paths.get("image"));
        Files.copy(image.getInputStream(), Paths.get("image", fileName));
      } catch (IOException e) {
        e.printStackTrace();
        return new ResponseDto("1", e.getMessage(), null);
      }
    }

    result.setAuthor(book.getAuthor());
    result.setName(book.getName());
    result.setDescription(book.getDescription());
    result.setPrice(book.getPrice());
    result.setPublishedYear(book.getPublishedYear());
    result.setPublisher(book.getPublisher());
    result.setCategory(book.getCategory());
    return new ResponseDto("0", "Book saved successfully", bookRepository.save(result));
  }
}