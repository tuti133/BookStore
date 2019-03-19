package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AreaConstants;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.Message;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;
import ptit.htpt.bookstore.util.SecurityUtils;

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

    public ResponseDto createCustomer(CustomerAccountDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", Message.ACCOUNT_EXIST, null);

        account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setActivated(true);
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setCreatedDate(new Date());
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(AuthoritiesConstants.CUSTOMER));
        account.setAuthorities(authorities);

        Customer customer = new Customer();
        customer.setAccount(accountRepository.save(account));
        customerRepository.save(customer);
        return new ResponseDto("0", Message.ACCOUNT_CREATED, null);
    }

    public ResponseDto updateCustomer(CustomerAccountDto dto){
        Account account = accountRepository.findById(dto.getAccountId()).get();
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setPhone(dto.getPhone());
        account.setEmail(dto.getEmail());
        accountRepository.save(account);
        Customer customer = customerRepository.findByAccount(account);
        customer.setAddress(dto.getAddress());
        customer.setCreditNumber(dto.getCreditNumber());
        customerRepository.save(customer);
        return new ResponseDto("0", Message.ACCOUNT_UPDATED, null);
    }

    public ResponseDto createEmployee(EmployeeAccountDto dto) {
        return new ResponseDto("0", "Success", null);
    }

    public ResponseDto updateEmployee(EmployeeAccountDto dto){
        return new ResponseDto("0", "Success", null);
    }

    public ResponseDto changePassword(Account account) {
        return null;
    }

    public Account getCustomer(Long id) {
        return accountRepository.findById(id).get();
    }

    public CustomerAccountDto getCurrentAccount() {
        Account account = SecurityUtils.getCurrentUser();
        Customer customer = customerRepository.findByAccount(account);
        CustomerAccountDto result = new CustomerAccountDto();
        result.setAccountId(account.getId());
        result.setCustomerId(customer.getId());
        result.setFirstName(account.getFirstName());
        result.setLastName(account.getLastName());
        result.setEmail(account.getEmail());
        result.setPhone(account.getPhone());
        result.setAddress(customer.getAddress());
        result.setCreditNumber(customer.getCreditNumber());
        result.setActivated(account.getActivated());
        return result;
    }
}
