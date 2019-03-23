package ptit.htpt.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ptit.htpt.bookstore.service.InitialService;

@Controller
public class MainController {
    @Autowired
    private InitialService initialService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/register")
    public String register() {
        return "customer/register";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/employee")
    public String createAccount() {
        return "admin/employee";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "common/403";
    }

    @GetMapping("/admin/books")
    public String getMethodName() {
        return "admin/bookList";
    }

    @GetMapping("/employee/sale")
    public String sale() {
        return "/employee/sale";
    }

    @GetMapping("profile")
    public String profile(){
        return "common/profile";
    }

    @GetMapping("history")
    public String history(){
        return "customer/history";
    }

    @GetMapping("order")
    public String order(){
        return "customer/order";
    }

    @GetMapping("/init-data")
    public String initData() {
        initialService.initData();
        return "index";
    }
}
