package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.BookBillDto;
import ptit.htpt.bookstore.dto.CreateBillDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.BillBookRepository;
import ptit.htpt.bookstore.repository.BillRepository;
import ptit.htpt.bookstore.repository.BookQuantityRepository;
import ptit.htpt.bookstore.repository.EmployeeRepository;
import ptit.htpt.bookstore.util.SecurityUtils;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillBookRepository billBookRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Transactional
    public ResponseDto createBill(CreateBillDto dto) {
        Bill bill = new Bill();
        bill.setCreatedDate((new Date()).getTime());
        bill.setCustomerName(dto.getCustomerName());
        bill.setCustomerPhone(dto.getCustomerPhone());
        bill.setEmployeeId(dto.getEmployeeId());
        bill.setTotalMoney(dto.getTotal());
        bill = billRepository.save(bill);
        BookStore bookStore = employeeRepository.findByAccount(SecurityUtils.getCurrentUser()).getBookStore();
        for (BookBillDto bookBillDto : dto.getBookList()) {
            BillBook billBook = new BillBook();
            billBook.setQuantity(bookBillDto.getQuantity());
            billBook.setBillId(bill.getId());
            billBook.setBookId(bookBillDto.getBookId());
            Book book = new Book();
            book.setId(bookBillDto.getBookId());
           BookQuantity bookQuantity = bookQuantityRepository.findByBookStoreAndBook(bookStore, book);
            bookQuantity.setQuantity(bookQuantity.getQuantity() - bookBillDto.getQuantity());
            billBookRepository.save(billBook);
        }
        return new ResponseDto("0", "success", null);
    }
}