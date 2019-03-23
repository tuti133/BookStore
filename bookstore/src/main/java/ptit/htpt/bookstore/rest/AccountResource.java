package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.ChangePasswordDto;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.service.AccountService;

@RestController
@RequestMapping("/api/account/")
public class AccountResource {
    @Autowired
    private AccountService accountService;

    @GetMapping("current-customer")
    public ResponseDto getCurrentAccount() {
        CustomerAccountDto response = accountService.getCurrentCustomer();
        return new ResponseDto("0", "Success", response);
    }

    @GetMapping("customer")
    public ResponseDto getCustomer(@RequestParam Long id) {
        CustomerAccountDto response = accountService.getCustomer(id);
        return new ResponseDto("0", "Success", response);
    }

    @GetMapping("employee")
    public ResponseDto getEmployee(@RequestParam Long id) {
        EmployeeAccountDto response = accountService.getEmployee(id);
        return new ResponseDto("0", "Success", response);
    }

    @PostMapping("register")
    public ResponseDto createCustomer(@RequestBody CustomerAccountDto customerAccountDto) {
        return accountService.createCustomer(customerAccountDto);
    }

    @PostMapping("update")
    public ResponseDto updateCustomer(@RequestBody CustomerAccountDto customerAccountDto) {
        return accountService.updateCustomer(customerAccountDto);
    }

    @PostMapping("change-password")
    public ResponseDto changePassword(@RequestBody ChangePasswordDto dto) {
        return accountService.changePassword(dto);
    }
}
