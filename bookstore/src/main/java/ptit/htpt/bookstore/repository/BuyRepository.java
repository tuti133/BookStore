package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Buy;

public interface BuyRepository extends JpaRepository<Buy, Long> {
}
