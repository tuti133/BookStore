package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.CreateBuyDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.BuyService;

@RestController
public class BuyResource {

    @Autowired
    private BuyService buyService;

    @PostMapping(value = "/api/buy")
    public ResponseDto buy(@RequestBody CreateBuyDto dto) {
        return buyService.buy(dto);
    }
}
