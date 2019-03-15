package ptit.htpt.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.util.SecurityUtils;

@Controller
public class MainController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return  "login";
    }

    @GetMapping("/register")
    public String register(){
        return  "customer/register";
    }

    @GetMapping("/admin")
    public String admin(){
        return  "admin/admin";
    }

    @GetMapping("/create-employee")
    public String createAccount(){
        return  "admin/create-employee";
    }

    @GetMapping("/init-account")
    public String initAccount(){
        accountService.initAccount();
        return "index";
    }

}
