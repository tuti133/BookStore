package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.CreateRateDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.entity.Rate;
import ptit.htpt.bookstore.repository.RateRepository;

import java.util.Date;
import java.util.List;

@Service
public class RateService {
    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private AccountService accountService;

    public ResponseDto rating(CreateRateDto createRateDto) {
        Book book = new Book();
        book.setId(createRateDto.getBookId());
        Customer customer = new Customer();
        customer.setPhone(createRateDto.getPhone());
        Rate rate = rateRepository.findByCustomerAndBook(customer, book);
        if(rate == null){
            rate = new Rate();
            rate.setBook(book);
            rate.setCustomer(customer);
        }
        rate.setComment(createRateDto.getComment());
        rate.setCreateDate(new Date().getTime());
        rate.setRate(createRateDto.getRate());
        rate = rateRepository.save(rate);
        return new ResponseDto("0", "Đánh giá đã được đăng", rate);
    }

    public Long getRate(Long bookId){
        Book book = new Book();
        book.setId(bookId);
        return rateRepository.getAvgRateByBook(book);
    }

    public ResponseDto getBookRating(Long bookId) {
        Book b = new Book();
        b.setId(bookId);
        List<Rate> list = rateRepository.findAllByBook(b);
        return new ResponseDto("0", "Success", list);
    }

    public ResponseDto getUserRatings(){
        Customer c = new Customer();
        c.setPhone(accountService.getCurrentCustomer().getPhone());
        List<Rate> list = rateRepository.findAllByCustomer(c);
        return new ResponseDto("0", "Success", list);
    }

    public ResponseDto getUserRating(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        Customer customer = new Customer();
        customer.setPhone(accountService.getCurrentCustomer().getPhone());
        Rate rate = rateRepository.findByCustomerAndBook(customer, book);
        return new ResponseDto("0", "Success", rate);
    }
}
