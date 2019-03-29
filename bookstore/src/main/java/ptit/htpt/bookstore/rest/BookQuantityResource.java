package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.BookQuantity;
import ptit.htpt.bookstore.entity.BookStore;
import ptit.htpt.bookstore.service.BookQuantityService;

@RestController
@RequestMapping("/api/bookQuantity/")
public class BookQuantityResource {

    @Autowired
    private BookQuantityService bookQuantityService;

    @GetMapping("getByStore")
    public ResponseDto getByStore(@RequestBody BookStore bookStore){
        return bookQuantityService.getByStore(bookStore);
    }

    @PostMapping("update")
    public ResponseDto update(@RequestBody BookQuantity bookQuantity){
        return bookQuantityService.update(bookQuantity);
    }



}
