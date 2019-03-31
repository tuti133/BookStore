package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.BuyBook;

import java.util.List;

@Repository
public interface BuyBookRepository extends JpaRepository<BuyBook, Long> {
    List<BuyBook> findAllByBuy(Buy buy);
}
