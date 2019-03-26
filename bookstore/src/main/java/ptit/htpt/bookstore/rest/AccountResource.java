package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.*;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.util.SecurityUtils;

@RestController
@RequestMapping("/api/account/")
public class AccountResource {
    @Autowired
    private AccountService accountService;

    @GetMapping("current")
    public ResponseDto getCurrentAccount() {
        if (SecurityUtils.getCurrentUser() == null) return new ResponseDto("1", "403", null);
        return accountService.getAccount(SecurityUtils.getCurrentUser().getId());
    }

    @GetMapping("get")
    public ResponseDto getAccount(@RequestParam Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping("getAll")
    public ResponseDto getAllAccount() {
        return accountService.getAllAccount();
    }

    @PostMapping("create")
    public ResponseDto create(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @PostMapping("update")
    public ResponseDto update(@RequestBody AccountDto accountDto) {
        return accountService.updateAccount(accountDto);
    }

    @PostMapping("change-password")
    public ResponseDto changePassword(@RequestBody ChangePasswordDto dto) {
        return accountService.changePassword(dto);
    }
}
