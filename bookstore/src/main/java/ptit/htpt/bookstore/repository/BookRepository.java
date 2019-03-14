package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ptit.htpt.bookstore.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    
}