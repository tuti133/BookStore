package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.dto.BookBillDto;
import ptit.htpt.bookstore.dto.CreateBillDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class BillService {
    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyBookRepository buyBookRepository;

    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public ResponseDto createBill(CreateBillDto dto) {
        Customer customer = new Customer();
        customer.setPhone(dto.getCustomerPhone());
        customer.setName(dto.getCustomerName());
        customer = customerRepository.save(customer);
        Buy buy = new Buy();
        buy.setShipAddress("");
        buy.setCreatedDate((new Date()).getTime());
        buy.setCustomer(customer);
        buy.setStatus(StatusBuyConstants.OFFLINE);
        buy.setTotalMoney(dto.getTotal());
        Employee emp = new Employee();
        emp.setId(dto.getEmployeeId());
        buy.setEmployee(emp);
        buy = buyRepository.save(buy);
        for (BookBillDto bookBillDto : dto.getBookList()) {
            BuyBook buyBook = new BuyBook();
            buyBook.setQuantity(bookBillDto.getQuantity());
            buyBook.setBuy(buy);
            BookQuantity bookQuantity = bookQuantityRepository.findById(bookBillDto.getBookQuantityId()).orElse(null);
            bookQuantity.setQuantity(bookQuantity.getQuantity() - bookBillDto.getQuantity());
            buyBook.setBookQuantity(bookQuantity);

            if (bookQuantity.getQuantity() - bookBillDto.getQuantity() < 0)
                throw new IllegalArgumentException("exception");
            bookQuantityRepository.save(bookQuantity);
            buyBookRepository.save(buyBook);
        }
        return new ResponseDto("0", "success", null);
    }
}