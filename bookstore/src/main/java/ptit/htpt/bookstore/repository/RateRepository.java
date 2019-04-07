package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Rate;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("SELECT SUM(r.rate)/COUNT(r.id) FROM Rate r WHERE r.book = :book")
    Long getAvgRateByBook(@Param("book") Book book);

    Rate findByCustomerAndBook(Customer customer, Book book);

    List<Rate> findAllByBook(Book b);

    List<Rate> findAllByCustomer(Customer c);
}
