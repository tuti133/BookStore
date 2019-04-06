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

@Service
public class RateService {
    @Autowired
    private RateRepository rateRepository;

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
        rate.setCreateDate(new Date().getTime());
        rate.setRate(createRateDto.getRate());
        rate = rateRepository.save(rate);
        return new ResponseDto("0", "success", rate);
    }

    public Long getRate(Long bookId){
        Book book = new Book();
        book.setId(bookId);
        return rateRepository.getAvgRateByBook(book);
    }
}
