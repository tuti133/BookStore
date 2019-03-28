package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.BuyBook;

@Repository
public interface BuyBookRepository extends JpaRepository<BuyBook, Long> {
}
