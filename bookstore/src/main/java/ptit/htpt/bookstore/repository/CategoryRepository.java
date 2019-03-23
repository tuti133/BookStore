package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
