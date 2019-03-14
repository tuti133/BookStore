package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.LoginDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.dto.RegisterDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseDto login(LoginDto login) {
        Account account = accountRepository.findByUsernameAndPassword(login.getUsername(), passwordEncoder.encode(login.getPassword()));
        if (account == null) return new ResponseDto("1", "fail", null);
        return new ResponseDto("0", "success", null);
    }

    public ResponseDto register(RegisterDto registerDto) {
        return null;
    }
}
