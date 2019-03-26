package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.BookBillDto;
import ptit.htpt.bookstore.dto.CreateBillDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Bill;
import ptit.htpt.bookstore.entity.BillBook;
import ptit.htpt.bookstore.repository.BillBookRepository;
import ptit.htpt.bookstore.repository.BillRepository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BillService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillBookRepository billBookRepository;

    @Transactional
    public ResponseDto createBill(CreateBillDto dto) {
        Bill bill = new Bill();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        bill.setCreatedDate(Long.parseLong(sdf.format(new Date())));
        bill.setCustomerName(dto.getCustomerName());
        bill.setCustomerPhone(dto.getCustomerPhone());
        bill.setEmployeeId(dto.getEmployeeId());
        bill.setTotalMoney(dto.getTotal());
        bill = billRepository.save(bill);
        for (BookBillDto bookBillDto : dto.getBookList()) {
            BillBook billBook = new BillBook();
            billBook.setQuantity(bookBillDto.getQuantity());
            billBook.setBillId(bill.getId());
            billBook.setBookId(bookBillDto.getBookId());
            billBookRepository.save(billBook);
        }
        return new ResponseDto("0", "success", null);
    }
}