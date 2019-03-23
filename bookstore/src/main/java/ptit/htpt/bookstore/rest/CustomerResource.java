package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.repository.AccountRepository;
import ptit.htpt.bookstore.repository.BuyBookRepository;
import ptit.htpt.bookstore.repository.BuyRepository;
import ptit.htpt.bookstore.repository.CustomerRepository;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer/")
public class CustomerResource {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyBookRepository buyBookRepository;


    @PostMapping("buy")
    public ResponseDto order(@RequestBody List<Buy> buys){
        for (Buy buy: buys){
            buy.setCreatedDate((new Date()).getTime());
        }
        return new ResponseDto("0", "Success", null);
    }
}
