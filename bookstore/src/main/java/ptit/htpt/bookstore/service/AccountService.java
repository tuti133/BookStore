package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.MessageConstants;
import ptit.htpt.bookstore.dto.ChangePasswordDto;
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

    public ResponseDto createCustomer(CustomerAccountDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", MessageConstants.ACCOUNT_EXIST, null);

        account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setActivated(true);
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setCreatedDate((new Date()).getTime());
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(AuthoritiesConstants.CUSTOMER));
        account.setAuthorities(authorities);

        Customer customer = new Customer();
        customer.setAccount(accountRepository.save(account));
        customerRepository.save(customer);
        return new ResponseDto("0", MessageConstants.ACCOUNT_CREATED, null);
    }

    public ResponseDto updateCustomer(CustomerAccountDto dto) {
        Account current = accountRepository.findById(dto.getAccountId()).get();
        Account acc = accountRepository.findByEmail(dto.getEmail());
        if (acc != null && !acc.getEmail().equals(current.getEmail()))
            return new ResponseDto("1", MessageConstants.EMAIL_EXIST, null);
        acc = accountRepository.findByPhone(dto.getPhone());
        if (acc != null && !acc.getPhone().equals(current.getPhone()))
            return new ResponseDto("1", MessageConstants.PHONE_EXIST, null);

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
        return new ResponseDto("0", MessageConstants.ACCOUNT_UPDATED, null);
    }

    public ResponseDto createEmployee(EmployeeAccountDto dto) {
        Account account = accountRepository.findByUsername(dto.getUsername());
        if (account != null) return new ResponseDto("1", MessageConstants.ACCOUNT_EXIST, null);

        account = new Account();
        account.setActivated(true);

        account.setUsername(dto.getUsername());
        account.setCreatedDate((new Date()).getTime());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setLastName(dto.getLastName());
        account.setFirstName(dto.getFirstName());
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByName(AuthoritiesConstants.EMPLOYEE));
        account.setAuthorities(authorities);

        Employee emp = new Employee();
        emp.setAccount(accountRepository.save(account));
        emp.setSalary(dto.getSalary());
        emp.setBookStore(bookStoreRepository.findById(dto.getBookStoreId()).get());
        emp.setWorkShift(dto.getWorkShift());
        employeeRepository.save(emp);
        return new ResponseDto("0", MessageConstants.ACCOUNT_CREATED, null);
    }

    public ResponseDto updateEmployee(EmployeeAccountDto dto) {
        Account current = accountRepository.findById(dto.getAccountId()).get();
        Account acc = accountRepository.findByEmail(dto.getEmail());
        if (acc != null && !acc.getEmail().equals(current.getEmail()))
            return new ResponseDto("1", MessageConstants.EMAIL_EXIST, null);
        acc = accountRepository.findByPhone(dto.getPhone());
        if (acc != null && !acc.getPhone().equals(current.getPhone()))
            return new ResponseDto("1", MessageConstants.PHONE_EXIST, null);

        Account account = accountRepository.findById(dto.getAccountId()).get();
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setPhone(dto.getPhone());
        account.setEmail(dto.getEmail());
        accountRepository.save(account);
        return new ResponseDto("0", "Success", null);
    }

    public ResponseDto changePassword(ChangePasswordDto dto) {
        Account account = SecurityUtils.getCurrentUser();
        if (account.getPassword().equals(passwordEncoder.encode(dto.getOldPassword()))){
            account.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            accountRepository.save(account);
            return new ResponseDto("0", MessageConstants.PASSWORD_CHANGED, null);
        }
        return new ResponseDto("1", MessageConstants.INCORRECT_OLD_PASS, null);
    }

    public CustomerAccountDto getCustomer(Long id) {
        Account account = accountRepository.findById(id).get();
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

    public EmployeeAccountDto getEmployee(Long id) {
        Account account = accountRepository.findById(id).get();
        Employee employee = employeeRepository.findByAccount(account);
        EmployeeAccountDto result = new EmployeeAccountDto();
        result.setAccountId(account.getId());
        result.setEmployeeId(employee.getId());
        result.setUsername(account.getUsername());
        result.setActivated(account.getActivated());
        result.setFirstName(account.getFirstName());
        result.setLastName(account.getLastName());
        result.setEmail(account.getEmail());
        result.setPhone(account.getPhone());
        result.setSalary(employee.getSalary());
        result.setWorkShift(employee.getWorkShift());
        result.setBookStoreId(employee.getBookStore().getId());
        return result;
    }

    public CustomerAccountDto getCurrentCustomer() {
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
