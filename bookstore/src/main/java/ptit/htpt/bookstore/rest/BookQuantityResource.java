package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.BookQuantity;
import ptit.htpt.bookstore.service.BookQuantityService;

@RestController
@RequestMapping("/api/bookQuantity/")
public class BookQuantityResource {

    @Autowired
    private BookQuantityService bookQuantityService;

    @GetMapping("getByStore")
    public ResponseDto getByStore(@RequestParam Long bookStoreId){
        return bookQuantityService.getByStore(bookStoreId);
    }

    @GetMapping("getOneByStore")
    public ResponseDto getOneByStore(@RequestParam Long bookId, @RequestParam Long storeId){
        return bookQuantityService.getOneByStore(bookId, storeId);
    }

    @PostMapping("update")
    public ResponseDto update(@RequestBody BookQuantity bookQuantity){
        return bookQuantityService.update(bookQuantity);
    }



}
