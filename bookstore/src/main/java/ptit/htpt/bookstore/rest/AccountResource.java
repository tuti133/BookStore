package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.util.SecurityUtils;

@RestController
@RequestMapping("/api/account/")
public class AccountResource {
    @Autowired
    private AccountService accountService;

    @GetMapping("current")
    public ResponseDto getCurrentAccount() {
        CustomerAccountDto response = accountService.getCurrentAccount();
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
    public ResponseDto changePassword(@RequestBody Account account) {
        return accountService.changePassword(account);
    }
}
