package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.EmployeeAccountDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.service.AccountService;

@RestController
@RequestMapping("/api/admin/")
public class AdminResource {
    @Autowired
    private AccountService accountService;

    @PostMapping("create-employee")
    public ResponseDto createEmployee(@RequestBody EmployeeAccountDto employeeAccountDto){
        return accountService.createEmployee(employeeAccountDto);
    }

    @PostMapping("update-employee")
    public ResponseDto updateEmployee(@RequestBody EmployeeAccountDto employeeAccountDto){
        return accountService.updateEmployee(employeeAccountDto);
    }
}
