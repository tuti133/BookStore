package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.dto.BookBillDto;
import ptit.htpt.bookstore.dto.CreateBillDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.*;
import ptit.htpt.bookstore.repository.jdbc.BillJdbc;

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

    @Autowired
    private BillJdbc billJdbc;

    @Transactional
    public ResponseDto createBill(CreateBillDto dto) {
        for (BookBillDto bookBillDto : dto.getBookList()) {
            BookQuantity bookQuantity = bookQuantityRepository.findById(bookBillDto.getBookQuantityId()).orElse(null);
            if (bookQuantity.getQuantity() - bookBillDto.getQuantity() < 0)
                throw new IllegalArgumentException("exception");
        }
        Customer customer = new Customer();
        customer.setPhone(dto.getCustomerPhone());
        customer.setName(dto.getCustomerName());
        billJdbc.saveCustomer(customer.getName(), customer.getPhone());
        Buy buy = new Buy();
        buy.setShipAddress("");
        buy.setCreatedDate((new Date()).getTime());
        buy.setCustomer(customer);
        buy.setStatus(StatusBuyConstants.OFFLINE);
        buy.setTotalMoney(dto.getTotal());
        Employee emp = new Employee();
        emp.setId(dto.getEmployeeId());
        buy.setEmployee(emp);
        Long buyId = billJdbc.saveBill(buy);
        buy.setId(buyId);
        for (BookBillDto bookBillDto : dto.getBookList()) {
            BookQuantity bookQuantity = bookQuantityRepository.findById(bookBillDto.getBookQuantityId()).orElse(null);
            billJdbc.updateBookQuantity(bookQuantity.getId(), bookQuantity.getQuantity() - bookBillDto.getQuantity() );
            billJdbc.ínertBuyBook(buyId, bookBillDto.getQuantity(), bookQuantity.getId());

        }
        return new ResponseDto("0", "success", null);
    }
}