package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Buy;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Long> {
}
