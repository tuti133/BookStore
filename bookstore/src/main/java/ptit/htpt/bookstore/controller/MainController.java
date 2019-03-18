package ptit.htpt.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ptit.htpt.bookstore.service.AccountService;


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

    @GetMapping("/403")
    public String accessDenied(){
        return "common/403";
    }

    @GetMapping("/init-data")
    public String initData(){
        accountService.initData();
        return "index";
    }
    @GetMapping("/admin/books")
    public String getMethodName() {
        return "admin/bookList";
    }
    
}
