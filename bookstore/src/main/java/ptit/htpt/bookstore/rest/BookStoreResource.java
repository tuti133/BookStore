package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.BookStoreService;

@RestController
@RequestMapping("/api/bookStore/")
public class BookStoreResource {

    @Autowired
    BookStoreService bookStoreService;

    @GetMapping("getAll")
    public ResponseDto getAllStore(){
        return bookStoreService.getAllStore();
    }
}
