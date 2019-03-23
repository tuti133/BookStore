package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AreaConstants;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.CategoryConstants;
import ptit.htpt.bookstore.constant.GenderConstants;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;

import java.util.HashSet;

@Service
public class InitialService {

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

    @Autowired
    private CategoryRepository categoryRepository;

    public void initData() {
        initBookStore();
        initCategory();
        initAccount();
    }

    void initBookStore() {
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
    }

    void initCategory() {
        Category c1 = new Category(CategoryConstants.COMIC);
        Category c2 = new Category(CategoryConstants.EDUCATION);
        Category c3 = new Category(CategoryConstants.LONG_STORY);
        Category c4 = new Category(CategoryConstants.LOVE_STORY);
        Category c5 = new Category(CategoryConstants.SHORT_STORY);
        Category c6 = new Category(CategoryConstants.SKILL);
        Category c7 = new Category(CategoryConstants.TECHNOLOGY);

        categoryRepository.save(c1);
        categoryRepository.save(c2);
        categoryRepository.save(c3);
        categoryRepository.save(c4);
        categoryRepository.save(c5);
        categoryRepository.save(c6);
        categoryRepository.save(c7);
    }

    void initAccount() {
        Authority adminAuth = authorityRepository.save(new Authority(AuthoritiesConstants.ADMIN));
        Authority employeeAuth = authorityRepository.save(new Authority(AuthoritiesConstants.EMPLOYEE));
        Authority customerAuth = authorityRepository.save(new Authority(AuthoritiesConstants.CUSTOMER));

        Account admin = new Account();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setGender(GenderConstants.MALE);
        HashSet<Authority> auth1 = new HashSet<>();
        auth1.add(adminAuth);
        auth1.add(employeeAuth);
        auth1.add(customerAuth);
        admin.setAuthorities(auth1);
        admin.setEmail("admin@localhost");
        admin.setPhone("123");
        admin.setActivated(true);
        accountRepository.save(admin);

        Account employee = new Account();
        employee.setUsername("employee");
        employee.setPassword(passwordEncoder.encode("employee"));
        employee.setGender(GenderConstants.FEMALE);
        employee.setActivated(true);
        HashSet<Authority> auth2 = new HashSet<>();
        auth2.add(employeeAuth);
        employee.setAuthorities(auth2);
        employee.setEmail("employee@localhost");
        employee.setPhone("456");

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
        customer.setGender(GenderConstants.OTHER);
        HashSet<Authority> auth3 = new HashSet<>();
        auth3.add(customerAuth);
        customer.setAuthorities(auth3);
        customer.setEmail("customer@localhost");
        customer.setPhone("789");

        Customer cus = new Customer();
        cus.setAccount(accountRepository.save(customer));
        customerRepository.save(cus);
    }

}
