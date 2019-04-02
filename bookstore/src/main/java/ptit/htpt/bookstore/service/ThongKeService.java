package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.constant.TypeOrderConstant;
import ptit.htpt.bookstore.dto.BillDto;
import ptit.htpt.bookstore.dto.ThongKeDto;
import ptit.htpt.bookstore.entity.Bill;
import ptit.htpt.bookstore.entity.BillBook;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.BuyBook;
import ptit.htpt.bookstore.repository.BillBookRepository;
import ptit.htpt.bookstore.repository.BillRepository;
import ptit.htpt.bookstore.repository.BuyBookRepository;
import ptit.htpt.bookstore.repository.BuyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillBookRepository billBookRepository;

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyBookRepository buyBookRepository;

    public ThongKeDto thongKeFromDateToDate(Long from, Long to, int type, String buyStatus) {
        ThongKeDto response = new ThongKeDto();
        List<BillDto> billDtoList = new ArrayList<>();
        Long totalBookOnline = 0L;
        Long totalBookOffline = 0L;
        response.setBillDtoList(billDtoList);
        if (type == TypeOrderConstant.GET_ALL || type == TypeOrderConstant.BILL) {
            List<Bill> billList = billRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThan(from, to);
            for (Bill bill : billList) {
                List<BillBook> list = billBookRepository.findAllByBill(bill);
                for (BillBook b: list) {
                    totalBookOffline += b.getQuantity();
                }
                BillDto billDto = new BillDto();
                billDto.setId(bill.getId());
                billDto.setTotal(bill.getTotalMoney());
                billDto.setType(1);
                billDto.setCustomerName(bill.getCustomerName());
                billDto.setPhone(bill.getCustomerPhone());
                response.setTotal(response.getTotal() + bill.getTotalMoney());
                billDtoList.add(billDto);
            }
        }
        if (type == TypeOrderConstant.GET_ALL || type == TypeOrderConstant.ONLINE) {
            List<Buy> buyList;
            if (buyStatus.equals(StatusBuyConstants.GET_ALL)) {
                buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThan(from, to);
            } else {
                buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatus(from, to, buyStatus);
            }
            for (Buy buy : buyList) {
                List<BuyBook> list = buyBookRepository.findAllByBuy(buy);
                for (BuyBook b: list) {
                    totalBookOnline += b.getQuantity();
                }
                BillDto billDto = new BillDto();
                billDto.setId(buy.getId());
                billDto.setTotal(buy.getTotalMoney());
                billDto.setType(2);
                billDto.setStatus(buy.getStatus());
                billDto.setCustomerName(buy.getCustomer().getAccount().getEmail());
                billDto.setPhone(buy.getCustomer().getAccount().getPhone());
                response.setTotal(response.getTotal() + buy.getTotalMoney());
                billDtoList.add(billDto);
            }
        }
        response.setTotalBookOffline(totalBookOffline);
        response.setTotalBookOnline(totalBookOnline);
        return response;
    }
}
