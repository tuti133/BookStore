package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.BookQuantity;
import ptit.htpt.bookstore.entity.BookStore;

import java.util.List;

@Repository
public interface BookQuantityRepository extends JpaRepository<BookQuantity, Long> {
    BookQuantity findByBookStoreAndBook(BookStore bookStore, Book book);

    List<BookQuantity> findAllByBookStore(BookStore bookStore);
}
