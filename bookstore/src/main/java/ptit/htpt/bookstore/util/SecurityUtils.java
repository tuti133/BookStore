package ptit.htpt.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.repository.AccountRepository;

import java.util.Optional;

@Component
public final class SecurityUtils {

    private static AccountRepository accountRepository;

    private SecurityUtils(AccountRepository repo){
        accountRepository = repo;
    }

    public static Account getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username);
    }

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
                .orElse(false);
    }

}
