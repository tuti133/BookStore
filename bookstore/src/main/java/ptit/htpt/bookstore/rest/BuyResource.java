package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.CreateBuyDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.service.BuyService;
import ptit.htpt.bookstore.util.SecurityUtils;

@RestController
@RequestMapping("/api/")
public class BuyResource {

    @Autowired
    private BuyService buyService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping(value = "buy")
    public ResponseDto buy(@RequestBody CreateBuyDto dto) {
        return buyService.buy(dto);
    }

    @GetMapping("buy/getAll")
    public ResponseDto getAll(){
        return buyService.getAll();
    }

    @GetMapping("buy/getByCustomer")
    public ResponseDto getByCustomer(@RequestParam Long customerId){
        return buyService.getByCustomer(customerId);
    }

    @GetMapping("buy/getByCurrentUser")
    public ResponseDto getByCurrentUser(){
        Account account = SecurityUtils.getCurrentUser();
        Customer customer = customerRepository.findByAccount(account);
        return getByCustomer(customer.getId());
    }
}
