package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.CreateRateDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.service.RateService;

@RestController
public class RateResource {
    @Autowired
    private RateService rateService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/api/book/rate")
    public ResponseDto rating(@RequestBody CreateRateDto createRateDto) {
        createRateDto.setPhone(accountService.getCurrentCustomer().getPhone());
        return rateService.rating(createRateDto);
    }

    @GetMapping("/api/book/get-book-rating")
    public ResponseDto getBookRating(@RequestParam Long bookId){
        return rateService.getBookRating(bookId);
    }

    @GetMapping("/api/book/get-user-ratings")
    public ResponseDto getUserRatings(){
        return rateService.getUserRatings();
    }


    @GetMapping("/api/book/get-user-rating")
    public ResponseDto getUserRating(@RequestParam Long bookId){
        return rateService.getUserRating(bookId);
    }
}
