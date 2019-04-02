package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import ptit.htpt.bookstore.dto.CreateBillDto;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.ResponseDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ptit.htpt.bookstore.service.AccountService;
import ptit.htpt.bookstore.service.BillService;


@RestController
@RequestMapping("/api/")
public class BillResource {
    @Autowired
    private BillService billService;
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "bill")
    public ResponseDto postMethodName(@RequestBody CreateBillDto dto) {
        EmployeeAccountDto employee = accountService.getCurrentEmployee();
        dto.setEmployeeId(employee.getEmployeeId());
        try{
            return billService.createBill(dto);
        }catch (IllegalArgumentException ex){
            return new ResponseDto("3", "het hang", null);
        }
    }

}