package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AreaConstants;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.Message;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.LoginDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.RegisterDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;

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

    @Autowired
    private BookStoreRepository bookStoreRepository;

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
    public ResponseDto saveEmployee(EmployeeAccountDto dto) {
        Account account = new Account();
        Employee emp = new Employee();

        if (dto.getAccountId() != null){
            account.setId(dto.getAccountId());
            emp.setId(dto.getEmployeeId());
        } else {
            if (accountRepository.findByUsername(dto.getUsername()) != null){
                return new ResponseDto("1", Message.ACCOUNT_EXIST, null);
            }
        }

        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setUsername(dto.getUsername());
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(dto.getAuthority()));
        account.setAuthorities(authorities);
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());

        emp.setAccount(accountRepository.save(account));
        emp.setSalary(dto.getSalary());
        emp.setBookStore(dto.getBookStore());
        emp.setWorkShift(dto.getWorkShift());
        employeeRepository.save(emp);

        return new ResponseDto("0", Message.ACCOUNT_CREATED, null);
    }

    public void initData() {
        BookStore bookStore = new BookStore();
        bookStore.setArea(AreaConstants.HANOI);
        bookStore.setAddress("K10 Nguyen Trai, Ha Dong, Ha Noi");
        bookStore.setName("Ha Noi");
        bookStoreRepository.save(bookStore);

        bookStore = new BookStore();
        bookStore.setArea(AreaConstants.HCM);
        bookStore.setAddress("11 Nguyen Dinh Chieu, Quan 1, TP Ho Chi Minh");
        bookStore.setName("TP. HCM");
        bookStoreRepository.save(bookStore);

        bookStore = new BookStore();
        bookStore.setArea(AreaConstants.DN);
        bookStore.setAddress("566 Nui Thanh, Hoa Cuong Nam, Hai Chau, Da Nang");
        bookStore.setName("Da Nang");
        bookStoreRepository.save(bookStore);

        Authority adminAuth;
        Authority employeeAuth;
        Authority customerAuth;
        if (authorityRepository.findByName(AuthoritiesConstants.ADMIN ) != null){
            adminAuth = authorityRepository.findByName(AuthoritiesConstants.ADMIN );
            employeeAuth = authorityRepository.findByName(AuthoritiesConstants.EMPLOYEE );
            customerAuth = authorityRepository.findByName(AuthoritiesConstants.CUSTOMER );
        } else {
            adminAuth = authorityRepository.save(new Authority(AuthoritiesConstants.ADMIN));
            employeeAuth = authorityRepository.save(new Authority(AuthoritiesConstants.EMPLOYEE));
            customerAuth = authorityRepository.save(new Authority(AuthoritiesConstants.CUSTOMER));
        }


        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
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

        Employee em = new Employee();
        em.setAccount(accountRepository.save(employee));
        em.setWorkShift(1L);
        em.setBookStore(bookStoreRepository.findByArea(AreaConstants.HANOI));
        em.setSalary(10000000L);
        employeeRepository.save(em);

        Account customer = new Account();
        customer.setUsername("customer");
        customer.setActivated(true);
        customer.setPassword(passwordEncoder.encode("customer"));
        HashSet<Authority> auth3 = new HashSet<>();
        auth3.add(customerAuth);
        customer.setAuthorities(auth3);

        Customer cus = new Customer();
        cus.setAccount(accountRepository.save(customer));
        customerRepository.save(cus);

    }

}
