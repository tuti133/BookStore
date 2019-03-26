package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
}
