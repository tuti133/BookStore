package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.BuyBook;

public interface BuyBookRepository extends JpaRepository<BuyBook, Long> {
}
