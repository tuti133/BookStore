package ptit.htpt.bookstore.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.ResponseDto;

@RestController
@RequestMapping("/api/customer/")
public class CustomerResource {
    @PostMapping("order")
    public ResponseDto order(){
        return new ResponseDto("0", "Success", null);
    }
}
