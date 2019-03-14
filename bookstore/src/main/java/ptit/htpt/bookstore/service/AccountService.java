package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.Authority;
import ptit.htpt.bookstore.constant.Message;
import ptit.htpt.bookstore.dto.CreateEmployeeDto;
import ptit.htpt.bookstore.dto.LoginDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.RegisterDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Employee;
import ptit.htpt.bookstore.repository.AccountRepository;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.repository.EmployeeRepository;

import java.util.Date;

@Service
public class AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseDto login(LoginDto login) {
        Account account = accountRepository.findByUsernameAndPassword(login.getUsername(), passwordEncoder.encode(login.getPassword()));
        if (account == null) return new ResponseDto("1", Message.LOGIN_FAIL, null);
        return new ResponseDto("0", Message.LOGIN_SUCCESS, null);
    }

    public ResponseDto register(RegisterDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", Message.ACCOUNT_EXIST, null);

        account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setAuthority(Authority.CUSTOMER);
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setCreatedDate(new Date());

        Account customer = accountRepository.save(account);
        Customer cus = new Customer();
        cus.setAccount(customer);
        customerRepository.save(cus);

        return new ResponseDto("0", Message.ACCOUNT_CREATED, null);
    }

    public ResponseDto createEmployee(CreateEmployeeDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", Message.ACCOUNT_EXIST, null);

        account = new Account();
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setUsername(dto.getUsername());
        account.setAuthority(dto.getAuthority());
        account.setCreatedDate(new Date());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());

        Account employee = accountRepository.save(account);
        Employee emp = new Employee();
        emp.setAccount(employee);
        emp.setSalary(dto.getSalary());
        emp.setBookStore(dto.getBookStore());
        emp.setWorkShift(dto.getWorkShift());
        employeeRepository.save(emp);

        return new ResponseDto("0", Message.ACCOUNT_CREATED, null);
    }
}
