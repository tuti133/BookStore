package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.constant.TypeOrderConstant;
import ptit.htpt.bookstore.dto.BillDto;
import ptit.htpt.bookstore.dto.ThongKeDto;
import ptit.htpt.bookstore.entity.Bill;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.repository.BillRepository;
import ptit.htpt.bookstore.repository.BuyRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BuyRepository buyRepository;

    public ThongKeDto thongKeFromDateToDate(Long from, Long to, int type, String buyStatus) {
        ThongKeDto response = new ThongKeDto();
        List<BillDto> billDtoList = new ArrayList<>();
        response.setBillDtoList(billDtoList);
        if (type == TypeOrderConstant.GET_ALL || type == TypeOrderConstant.BILL) {
            List<Bill> billList = billRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThan(from, to);
            for (Bill bill : billList) {
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
            List<Buy> buyList = buyRepository.getByCreatedDateGreaterThanEqualAndCreatedDateLessThanAndStatus(from, to, buyStatus);

            for (Buy buy : buyList) {
                BillDto billDto = new BillDto();
                billDto.setId(buy.getId());
                billDto.setTotal(buy.getTotalMoney());
                billDto.setType(2);
                billDto.setCustomerName(buy.getCustomer().getAccount().getEmail());
                billDto.setPhone(buy.getCustomer().getAccount().getPhone());
                response.setTotal(response.getTotal() + buy.getTotalMoney());
                billDtoList.add(billDto);
            }
        }
        return response;
    }
}
