package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.constant.TypeOrderConstant;
import ptit.htpt.bookstore.dto.BillDto;
import ptit.htpt.bookstore.dto.ThongKeDto;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.BuyBook;
import ptit.htpt.bookstore.repository.BuyBookRepository;
import ptit.htpt.bookstore.repository.BuyRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ThongKeService {

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyBookRepository buyBookRepository;

    private BillDto mapFromBuy(Buy buy) {
        BillDto billDto = new BillDto();
        billDto.setId(buy.getId());
        billDto.setTotal(buy.getTotalMoney());
        billDto.setStatus(buy.getStatus());
        billDto.setCustomerName(buy.getCustomer().getName());
        billDto.setPhone(buy.getCustomer().getPhone());
        return billDto;
    }

    public ThongKeDto thongKeFromDateToDate(Long from, Long to, int type, String buyStatus) {
        ThongKeDto response = new ThongKeDto();
        List<BillDto> billDtoList = new ArrayList<>();
        Long totalBookOnline = 0L;
        Long totalBookOffline = 0L;
        Long totalOnline = 0L;
        Long totalOffline = 0L;
        Long totalBookHanoi = 0L;
        Long totalBookHcm = 0L;
        Long totalHanoi = 0L;
        Long totalHcm = 0L;
        response.setBillDtoList(billDtoList);
        if (type == TypeOrderConstant.GET_ALL || type == TypeOrderConstant.BILL) {
            List<Buy> buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatus(from, to, StatusBuyConstants.OFFLINE);
            for (Buy buy : buyList) {
                List<BuyBook> list = buyBookRepository.findAllByBuy(buy);
                for (BuyBook b : list) {
                    totalBookOffline += b.getQuantity();
                    if (b.getBookQuantity().getBookStore().getId() == 1) {
                        totalBookHanoi += b.getQuantity();
                        totalHanoi += b.getQuantity() * b.getBookQuantity().getBook().getPrice();
                    } else {
                        totalBookHcm += b.getQuantity();
                        totalHcm += b.getQuantity() * b.getBookQuantity().getBook().getPrice();
                    }
                }
                BillDto billDto = mapFromBuy(buy);
                billDto.setType(1);
                response.setTotal(response.getTotal() + buy.getTotalMoney());
                totalOffline += buy.getTotalMoney();
                billDtoList.add(billDto);

            }
        }
        if (type == TypeOrderConstant.GET_ALL || type == TypeOrderConstant.ONLINE) {
            List<Buy> buyList;
            if (buyStatus.equals(StatusBuyConstants.GET_ALL)) {
                buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatusIn(from, to, Arrays.asList(StatusBuyConstants.ORDERED, StatusBuyConstants.SHIPPING, StatusBuyConstants.CANCEL, StatusBuyConstants.SUCCESS));
            } else {
                buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatus(from, to, buyStatus);
            }
            for (Buy buy : buyList) {

                BillDto billDto = mapFromBuy(buy);
                billDto.setType(2);
                if (StatusBuyConstants.SUCCESS.equals(buy.getStatus())) {
                    List<BuyBook> list = buyBookRepository.findAllByBuy(buy);
                    for (BuyBook b : list) {
                        totalBookOnline += b.getQuantity();
                        if (b.getBookQuantity().getBookStore().getId() == 1) {
                            totalBookHanoi += b.getQuantity();
                            totalHanoi += b.getQuantity() * b.getBookQuantity().getBook().getPrice();
                        } else {
                            totalBookHcm += b.getQuantity();
                            totalHcm += b.getQuantity() * b.getBookQuantity().getBook().getPrice();
                        }
                    }
                    totalOnline += buy.getTotalMoney();
                    response.setTotal(response.getTotal() + buy.getTotalMoney());
                }
                billDtoList.add(billDto);
            }
        }
        response.setTotalBookOffline(totalBookOffline);
        response.setTotalBookOnline(totalBookOnline);
        response.setTotalOnline(totalOnline);
        response.setTotalOffline(totalOffline);
        response.setTotalBookHanoi(totalBookHanoi);
        response.setTotalBookHcm(totalBookHcm);
        response.setTotalHanoi(totalHanoi);
        response.setTotalHcm(totalHcm);
        return response;
    }

}
