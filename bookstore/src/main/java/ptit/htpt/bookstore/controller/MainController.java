package ptit.htpt.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return  "customer/login";
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

}
