package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.Message;
import ptit.htpt.bookstore.dto.CreateEmployeeDto;
import ptit.htpt.bookstore.dto.LoginDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.RegisterDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Authority;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Employee;
import ptit.htpt.bookstore.repository.AccountRepository;
import ptit.htpt.bookstore.repository.AuthorityRepository;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.repository.EmployeeRepository;

import java.util.Date;
import java.util.HashSet;

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

    @Autowired
    private AuthorityRepository authorityRepository;

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
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(AuthoritiesConstants.CUSTOMER));
        account.setAuthorities(authorities);
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setCreatedDate(new Date());
        account.setActivated(true);

        accountRepository.save(account);
        Customer cus = new Customer();
        cus.setAccount(account);
        customerRepository.save(cus);
        return new ResponseDto("0", Message.ACCOUNT_CREATED, null);
    }

    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseDto createEmployee(CreateEmployeeDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", Message.ACCOUNT_EXIST, null);

        account = new Account();
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setUsername(dto.getUsername());
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(dto.getAuthority()));
        account.setAuthorities(authorities);
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

    public void initAccount() {
        Account saved = null;
        Authority adminAuth = authorityRepository.save(new Authority(AuthoritiesConstants.ADMIN));
        Authority employeeAuth = authorityRepository.save(new Authority(AuthoritiesConstants.EMPLOYEE));
        Authority customerAuth = authorityRepository.save(new Authority(AuthoritiesConstants.CUSTOMER));

        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        HashSet<Authority> auth1 = new HashSet<>();
        auth1.add(adminAuth);
        auth1.add(employeeAuth);
        auth1.add(customerAuth);
        admin.setAuthorities(auth1);
        admin.setActivated(true);
        accountRepository.save(admin);

        Account employee = new Account();
        employee.setUsername("employee");
        employee.setPassword(passwordEncoder.encode("employee"));
        employee.setActivated(true);
        HashSet<Authority> auth2 = new HashSet<>();
        auth2.add(employeeAuth);
        employee.setAuthorities(auth2);
        saved = accountRepository.save(employee);
        Employee emp = new Employee();
        emp.setAccount(saved);
        employeeRepository.save(emp);


        Account customer = new Account();
        customer.setUsername("customer");
        customer.setActivated(true);
        customer.setPassword(passwordEncoder.encode("customer"));
        HashSet<Authority> auth3 = new HashSet<>();
        auth3.add(customerAuth);
        customer.setAuthorities(auth3);
        saved = accountRepository.save(customer);
        Customer cus = new Customer();
        cus.setAccount(saved);
        customerRepository.save(cus);
    }
}
