package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByUsernameAndPassword(String username, String password);

    Account findByEmail(String email);

}
