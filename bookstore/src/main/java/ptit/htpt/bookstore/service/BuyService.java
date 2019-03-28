package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.BuyBookDto;
import ptit.htpt.bookstore.dto.CreateBuyDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.BookQuantityRepository;
import ptit.htpt.bookstore.repository.BuyBookRepository;
import ptit.htpt.bookstore.repository.BuyRepository;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BuyService {
    @Autowired
    private BuyRepository buyRepository;
    @Autowired
    private BuyBookRepository buyBookRepository;
    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Transactional
    public ResponseDto buy(CreateBuyDto dto) {
        Buy buy = new Buy();
        buy.setNote(dto.getNote());
        buy.setShipAddress(dto.getShipAddress());
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        buy.setCreatedDate(Long.parseLong(df.format(new Date())));
        buy.setStatus("1");
        buy.setTotalMoney(dto.getTotalMoney());
        buy.setStatus("1");
        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        buy.setCustomer(customer);
        buy = buyRepository.save(buy);
        for (BuyBookDto buyBookDto : dto.getBuyBookDtoList()) {
            BuyBook buyBook = new BuyBook();
            buyBook.setBuy(buy);
            buyBook.setQuantity(buyBookDto.getQuantity());
            BookStore bookStore = new BookStore();
            bookStore.setId(buyBookDto.getStoreId());
            Book book = new Book();
            book.setId(buyBookDto.getBookId());
            BookQuantity bookQuantity = bookQuantityRepository.findByBookStoreAndBook(bookStore, book);
            BookQuantity bq = new BookQuantity();
            bq.setId(bookQuantity.getId());
            buyBook.setBookQuantity(bq);
            buyBookRepository.save(buyBook);
            bookQuantity.setQuantity(bookQuantity.getQuantity() - buyBookDto.getQuantity());
            bookQuantityRepository.save(bookQuantity);



        }
        return new ResponseDto("0", "success", null);
    }
}
