package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.constant.MessageConstants;
import ptit.htpt.bookstore.dto.*;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;
import ptit.htpt.bookstore.util.SecurityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

    private String validateCreateAccount(Account account) {
        Account check;
        check = accountRepository.findByUsername(account.getUsername());
        if (check != null) return MessageConstants.ACCOUNT_EXIST;
        check = accountRepository.findByEmail(account.getEmail());
        if (check != null) return MessageConstants.EMAIL_EXIST;
        check = accountRepository.findByPhone(account.getPhone());
        if (check != null) return MessageConstants.PHONE_EXIST;
        return null;
    }

    private String validateUpdateAccount(Account account) {
        Account check;
        check = accountRepository.findByEmail(account.getEmail());
        if (check != null && check.getId() != account.getId()) return MessageConstants.EMAIL_EXIST;
        check = accountRepository.findByPhone(account.getPhone());
        if (check != null && check.getId() != account.getId()) return MessageConstants.PHONE_EXIST;
        return null;
    }

    private boolean isAccountInRole(String role, Account account) {
        for (Authority authority : account.getAuthorities()) {
            if (role.equals(authority.getName())) {
                return true;
            }
        }
        return false;
    }

    public ResponseDto getAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        AccountDto result = new AccountDto();
        Employee employee = employeeRepository.findByAccount(account);
        Customer customer = customerRepository.findByAccount(account);

        if (isAccountInRole(AuthoritiesConstants.ADMIN, account)) {
            result.setRole(AuthoritiesConstants.ADMIN);
        } else {
            if (isAccountInRole(AuthoritiesConstants.EMPLOYEE, account)) {
                result.setRole(AuthoritiesConstants.EMPLOYEE);
            } else {
                if (isAccountInRole(AuthoritiesConstants.CHECKER, account))
                    result.setRole(AuthoritiesConstants.CHECKER);
                else result.setRole(AuthoritiesConstants.CUSTOMER);
            }
        }
        result.setAccount(account);
        result.setEmployee(employee);
        result.setCustomer(customer);
        return new ResponseDto("0", "Success", result);
    }

    public ResponseDto deactiveAccount(Long id) {
        Account account = accountRepository.findById(id).get();
        account.setActivated(!account.getActivated());
        return new ResponseDto("0", "Success", accountRepository.save(account));
    }

    public ResponseDto createAccount(AccountDto dto) {
        String msg = validateCreateAccount(dto.getAccount());
        if (msg != null) return new ResponseDto("1", msg, null);

        AccountDto result = new AccountDto();
        Authority authAdmin = authorityRepository.findByName(AuthoritiesConstants.ADMIN);
        Authority authEmployee = authorityRepository.findByName(AuthoritiesConstants.EMPLOYEE);
        Authority authCustomer = authorityRepository.findByName(AuthoritiesConstants.CUSTOMER);
        Authority authChecker = authorityRepository.findByName(AuthoritiesConstants.CHECKER);

        Account account = dto.getAccount();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreatedDate((new Date()).getTime());

        HashSet<Authority> listAuthorities = new HashSet<>();
        Account savedAccount;
        Employee savedEmployee = new Employee();
        Customer savedCustomer = new Customer();

        Employee employee = new Employee();
        Customer customer = new Customer();

        switch (dto.getRole()) {
            case AuthoritiesConstants.ADMIN: {
                listAuthorities.add(authAdmin);
                listAuthorities.add(authEmployee);
                listAuthorities.add(authCustomer);
                listAuthorities.add(authChecker);
                account.setAuthorities(listAuthorities);
                savedAccount = accountRepository.save(account);
                break;
            }
            case AuthoritiesConstants.EMPLOYEE: {
                listAuthorities.add(authEmployee);
                listAuthorities.add(authCustomer);
                account.setAuthorities(listAuthorities);
                savedAccount = accountRepository.save(account);
                employee.setAccount(savedAccount);
                employee.setBookStore(dto.getEmployee().getBookStore());
                savedEmployee = employeeRepository.save(employee);
                break;
            }
            case AuthoritiesConstants.CUSTOMER: {
                listAuthorities.add(authCustomer);
                account.setAuthorities(listAuthorities);
                savedAccount = accountRepository.save(account);
                customer.setAccount(savedAccount);
                savedCustomer = customerRepository.save(customer);
                break;
            }
            case AuthoritiesConstants.CHECKER: {
                listAuthorities.add(authChecker);
                listAuthorities.add(authCustomer);
                account.setAuthorities(listAuthorities);
                savedAccount = accountRepository.save(account);
                break;
            }
            default: {
                return new ResponseDto("1", MessageConstants.ERROR, null);
            }
        }
        result.setRole(dto.getRole());
        result.setAccount(savedAccount);
        result.setEmployee(savedEmployee);
        result.setCustomer(savedCustomer);
        return new ResponseDto("0", MessageConstants.ACCOUNT_CREATED, result);
    }

    public ResponseDto updateAccount(AccountDto dto) {
        String msg = validateUpdateAccount(dto.getAccount());
        if (msg != null) return new ResponseDto("1", msg, null);

        Account old = accountRepository.findById(dto.getAccount().getId()).get();

        Account update = dto.getAccount();
        update.setPassword(old.getPassword());

        Account savedAccount = accountRepository.save(update);
        Employee savedEmployee = new Employee();
        Customer savedCustomer = new Customer();


        switch (dto.getRole()) {
            case AuthoritiesConstants.EMPLOYEE: {
                savedEmployee = employeeRepository.save(dto.getEmployee());
                break;
            }
            case AuthoritiesConstants.CUSTOMER: {
                savedCustomer = customerRepository.save(dto.getCustomer());
                break;
            }
            default: {
                break;
            }
        }
        AccountDto result = new AccountDto();
        result.setRole(dto.getRole());
        result.setAccount(savedAccount);
        result.setEmployee(savedEmployee);
        result.setCustomer(savedCustomer);
        return new ResponseDto("0", MessageConstants.ACCOUNT_UPDATED, result);
    }

    public ResponseDto changePassword(ChangePasswordDto dto) {
        Account account = SecurityUtils.getCurrentUser();

        if (passwordEncoder.matches(dto.getOldPassword(), account.getPassword())) {
            account.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            accountRepository.save(account);
            return new ResponseDto("0", MessageConstants.PASSWORD_CHANGED, null);
        }
        return new ResponseDto("1", MessageConstants.INCORRECT_OLD_PASS, null);
    }

    public ResponseDto getAllAccount() {
        List<Account> list = accountRepository.findAll();
        List<AccountDto> results = new ArrayList<>();
        for (Account account : list) {
            AccountDto dto = new AccountDto();
            dto.setAccount(account);
            if (isAccountInRole(AuthoritiesConstants.ADMIN, account)) {
                dto.setRole(AuthoritiesConstants.ADMIN);
            } else {
                if (isAccountInRole(AuthoritiesConstants.EMPLOYEE, account)) {
                    dto.setRole(AuthoritiesConstants.EMPLOYEE);
                    dto.setEmployee(employeeRepository.findByAccount(account));
                } else {
                    if (isAccountInRole(AuthoritiesConstants.CHECKER, account)) {
                        dto.setRole(AuthoritiesConstants.CHECKER);
                    } else {
                        dto.setRole(AuthoritiesConstants.CUSTOMER);
                        dto.setCustomer(customerRepository.findByAccount(account));
                    }
                }
            }
            results.add(dto);
        }
        return new ResponseDto("0", "Success", results);
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

    public EmployeeAccountDto getCurrentEmployee() {
        Account account = SecurityUtils.getCurrentUser();
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
