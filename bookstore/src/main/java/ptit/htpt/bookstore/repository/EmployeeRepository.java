package ptit.htpt.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ptit.htpt.bookstore.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
