package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.BillBook;

@Repository
public interface BillBookRepository extends JpaRepository<BillBook, Long> {
}
