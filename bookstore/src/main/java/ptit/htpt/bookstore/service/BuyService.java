package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.dto.BuyBookDto;
import ptit.htpt.bookstore.dto.CreateBuyDto;
import ptit.htpt.bookstore.dto.OrderDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.*;
import ptit.htpt.bookstore.repository.BookQuantityRepository;
import ptit.htpt.bookstore.repository.BuyBookRepository;
import ptit.htpt.bookstore.repository.BuyRepository;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.util.SecurityUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BuyService {
    @Autowired
    private BuyRepository buyRepository;
    @Autowired
    private BuyBookRepository buyBookRepository;
    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public ResponseDto buy(CreateBuyDto dto) {
        Buy buy = new Buy();
        buy.setNote(dto.getNote());
        buy.setShipAddress(dto.getShipAddress());
        buy.setCreatedDate((new Date()).getTime());
        buy.setTotalMoney(dto.getTotalMoney());
        buy.setStatus(StatusBuyConstants.ORDERED);
        Customer customer = customerRepository.findByAccount(SecurityUtils.getCurrentUser());
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
        return new ResponseDto("0", "success", buy);
    }

    public ResponseDto getAll() {
        List<Buy> buys = buyRepository.findAll();
        return new ResponseDto("0", "Success", mapFromBuyList(buys));

    }

    public ResponseDto getByType(String type) {
        if (type.equals(StatusBuyConstants.GET_ALL)) return getAll();
        List<Buy> buys = buyRepository.findByStatus(type);
        return new ResponseDto("0", "Success", mapFromBuyList(buys));
    }

    private List<OrderDto> mapFromBuyList(List<Buy> buys){
        List<OrderDto> result = new ArrayList<>();
        for (Buy buy : buys) {
            OrderDto dto = new OrderDto();
            List<BuyBook> listBuyBook = buyBookRepository.findAllByBuy(buy);
            dto.setBuy(buy);
            dto.setListBuyBook(listBuyBook);
            result.add(dto);
        }
        return result;
    }

    public ResponseDto updateStatus(Long id, String status) {
        Buy buy = buyRepository.findById(id).orElse(null);
        if (buy == null) return new ResponseDto("1", "Error", null);
        if (buy.getStatus().equals(StatusBuyConstants.CANCEL)) return new ResponseDto("1", "Không thể hủy đơn hàng", null);
        buy.setStatus(status);
        buy = buyRepository.save(buy);
        if (buy.getStatus().equals(StatusBuyConstants.CANCEL)) return new ResponseDto("0", "Hủy đơn hàng thành công!", buy);
        return new ResponseDto("0", "Cập nhật thành công", buy);
    }

    public ResponseDto getByCustomer(Long customerId) {
        Customer customer = new Customer();
        customer.setId(customerId);
        List<Buy> buys = buyRepository.findAllByCustomer(customer);
        return new ResponseDto("0", "Success", mapFromBuyList(buys));
    }

}
