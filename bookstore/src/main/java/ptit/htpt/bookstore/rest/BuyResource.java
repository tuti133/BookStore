package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.CreateBuyDto;
import ptit.htpt.bookstore.dto.GetBuyBookDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Account;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.service.BuyService;
import ptit.htpt.bookstore.util.SecurityUtils;

import java.util.List;

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

    @GetMapping("buy/{id}")
    public List<GetBuyBookDto> getDetail(@PathVariable("id") Long id) {
        return buyService.getDetailBuy(id);
    }

    @GetMapping("buy/getAll")
    public ResponseDto getAll() {
        return buyService.getAll();
    }

    @PostMapping("buy/update/{id}")
    public ResponseDto updateStatus(@PathVariable("id") Long id, @RequestParam String status) {
        return buyService.updateStatus(id, status);
    }

    @GetMapping("buy/getByType")
    //type= 0 getall
    public ResponseDto getByType(@RequestParam("type") String type) {
        return buyService.getByType(type);
    }

    @GetMapping("buy/getByCustomer")
    public ResponseDto getByCustomer(@RequestParam String phone) {
        return buyService.getByCustomer(phone);
    }

    @GetMapping("buy/getByCurrentUser")
    public ResponseDto getByCurrentUser() {
        Account account = SecurityUtils.getCurrentUser();
        Customer customer = customerRepository.findByAccount(account);
        return getByCustomer(customer.getPhone());
    }
}
