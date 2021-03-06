package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Employee;

import java.util.List;

@Repository
public interface BuyRepository extends JpaRepository<Buy, Long> {
    List<Buy> findAllByCustomer(Customer customer);

    List<Buy> getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatus(Long from, Long to, String status);

    List<Buy> findByStatus(String status);

    List<Buy> getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatusIn(Long from, Long to, List<String> statusList);

    List<Buy> findAllByEmployeeAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndStatusOrderByCreatedDateDesc(Employee employee, Long from, Long to, String status);
}
