package ptit.htpt.bookstore.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import ptit.htpt.bookstore.entity.Category;

@Data
public class CreateBookDto {
  private MultipartFile image;
  private String name;
  private Long price;
  private String description;
  private String author;
  private String publisher;
  private Long publishedYear;
  private Category category;
  private Long favorite;
}