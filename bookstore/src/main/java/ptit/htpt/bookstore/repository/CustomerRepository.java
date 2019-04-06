package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Customer findByAccount(Account account);

    Customer findByPhone(String phone);
}
