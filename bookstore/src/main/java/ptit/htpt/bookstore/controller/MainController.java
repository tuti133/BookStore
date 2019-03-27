package ptit.htpt.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.service.BookService;
import ptit.htpt.bookstore.service.InitialService;
import ptit.htpt.bookstore.util.SecurityUtils;

@Controller
public class MainController {
    @Autowired
    private InitialService initialService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String index() {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return "redirect:/admin/dashboard";
        } else {
            if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.EMPLOYEE)) {
                return "redirect:/employee/sale";
            } else {
                return "index";
            }
        }
    }

    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/password")
    public String password() {
        return "common/password";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "common/403";
    }

    @GetMapping("profile")
    public String profile() {
        return "common/profile";
    }

    @GetMapping("/init-data")
    public String initData() {
        initialService.initData();
        return "index";
    }

    //--------------------------ADMIN------------------------------
    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/account")
    public String manageAccount() {
        return "admin/account";
    }

    @GetMapping("/admin/category")
    public String manageCategory() {
        return "admin/category";
    }

    @GetMapping("/admin/book")
    public String manageBook() {
        return "/admin/book";
    }

    @GetMapping("/admin/bill")
    public String manageBill() {
        return "/admin/bill";
    }

    //------------------------EMPLOYEE-----------------------------
    @GetMapping("/employee/sale")
    public String sale(Model model) {
        EmployeeAccountDto dto = accountService.getCurrentEmployee();
        model.addAttribute("user", dto.getFirstName() + " " + dto.getLastName());
        model.addAttribute("sachList", bookService.getAll());
        return "/employee/sale";
    }


    @GetMapping("history")
    public String history() {
        return "customer/history";
    }

    @GetMapping("/order")
    public String order() {
        return "customer/order";
    }

    @GetMapping("/register")
    public String register() {
        return "customer/register";
    }

}
