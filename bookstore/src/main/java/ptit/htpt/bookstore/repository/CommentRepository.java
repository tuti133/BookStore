package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBookOrderByCreateDateDesc(Book book);
}
