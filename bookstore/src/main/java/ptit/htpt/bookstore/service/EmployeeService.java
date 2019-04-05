package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.StatusBuyConstants;
import ptit.htpt.bookstore.dto.BillDto;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.entity.Employee;
import ptit.htpt.bookstore.repository.BuyRepository;
import ptit.htpt.bookstore.util.DatetimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private BuyRepository buyRepository;

    public List<BillDto> getAllBill(Long from, Long to) {
        List<BillDto> result = new ArrayList<>();
        EmployeeAccountDto employee = accountService.getCurrentEmployee();
        Employee em = new Employee();
        em.setId(employee.getEmployeeId());
        List<Buy> buyList = buyRepository.findAllByEmployeeAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqualAndStatusOrderByCreatedDateDesc
                (em, from, to, StatusBuyConstants.OFFLINE);
        for (Buy buy : buyList) {
            BillDto billDto = new BillDto();
            billDto.setId(buy.getId());
            billDto.setTotal(buy.getTotalMoney());
            billDto.setPhone(buy.getCustomer().getPhone());
            billDto.setCustomerName(buy.getCustomer().getName());
            billDto.setCreateDate(DatetimeUtils.convert(new Date(buy.getCreatedDate())));
            result.add(billDto);
        }
        return result;
    }
}
