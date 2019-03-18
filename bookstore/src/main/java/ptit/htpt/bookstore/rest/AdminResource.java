package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.AccountService;

@RestController
@RequestMapping("/admin")
public class AdminResource {
    @Autowired
    private AccountService accountService;

    @PostMapping("save-employee")
    public ResponseDto saveEmployee(@RequestBody EmployeeAccountDto employeeAccountDto){
        return accountService.saveEmployee(employeeAccountDto);
    }
}
