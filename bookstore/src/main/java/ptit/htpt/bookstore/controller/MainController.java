package ptit.htpt.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ptit.htpt.bookstore.constant.AuthoritiesConstants;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.entity.BookStore;
import ptit.htpt.bookstore.repository.BuyRepository;
import ptit.htpt.bookstore.service.*;
import ptit.htpt.bookstore.util.SecurityUtils;

@Controller
public class MainController {

    @Autowired
    private InitialService initialService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BuyRepository buyRepository;

    @GetMapping("/")
    public String index() {
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return "redirect:/admin/dashboard";
        } else {
            if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.EMPLOYEE)) {
                return "redirect:/employee/sale";
            } else {
                if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.CHECKER)) {
                    return "redirect:/checker";
                } else
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

    @GetMapping("/admin/book-quantity")
    public String manageBookQuantity() {
        return "/admin/book-quantity";
    }

    @Autowired
    private CustomerService customerService;

    @GetMapping("/admin/bill")
    public String manageBill() {
        return "/admin/bill";
    }

    //------------------------EMPLOYEE-----------------------------
    @GetMapping("/employee/sale")
    public String sale(Model model) {
        EmployeeAccountDto dto = accountService.getCurrentEmployee();
        model.addAttribute("user", dto.getName());
        model.addAttribute("sachList", bookService.getByBookStore(dto.getBookStoreId()));
        model.addAttribute("customerList", customerService.getAllCustomer());
        model.addAttribute("store", dto.getStore());
        return "/employee/sale";
    }

    @GetMapping("/employee/history")
    public String historyEmployee(Model model) {
        EmployeeAccountDto dto = accountService.getCurrentEmployee();
        model.addAttribute("user", dto.getName());
        model.addAttribute("store", dto.getStore());

        return "/employee/history";
    }

    @GetMapping("/history")
    public String history() {
        return "customer/history";
    }

    @GetMapping("/checker")
    public String checker() {
        return "/checker/checker";
    }

    @GetMapping("/order/success/{id}")
    public String orderSuccess(@PathVariable Long id) {
        if (!buyRepository.findById(id).isPresent()) {
            return "/common/404";
        }
        return "common/order-success";
    }

    @GetMapping("/order")
    public String order() {
        return "customer/order";
    }

    @GetMapping("/register")
    public String register() {
        return "customer/register";
    }

    @GetMapping("/book/{id}")
    public String bookDetail(@PathVariable Long id) {
        return "/book/book-detail";
    }

}
