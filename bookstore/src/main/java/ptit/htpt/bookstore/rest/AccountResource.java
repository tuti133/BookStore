package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.LoginDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.RegisterDto;
import ptit.htpt.bookstore.service.AccountService;

@RestController
@RequestMapping("/api/")
public class AccountResource {
    @Autowired
    private AccountService accountService;

    @PostMapping("login")
    public ResponseDto login(@RequestBody LoginDto loginDto){
        return accountService.login(loginDto);
    }

    @PostMapping("register")
    public ResponseDto register(@RequestBody RegisterDto registerDto){
        return accountService.register(registerDto);
    }
}
