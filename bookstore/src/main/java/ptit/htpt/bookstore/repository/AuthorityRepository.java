package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
}
