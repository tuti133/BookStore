package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.BookStore;

public interface BookStoreRepository extends JpaRepository<BookStore, Long> {
    BookStore findByArea(String area);
}
