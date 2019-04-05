package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.BillDto;
import ptit.htpt.bookstore.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employee/")
public class EmployeeResource {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("history")
    public List<BillDto> getHistory( @RequestParam(name = "from", defaultValue = "0", required = false) Long from,
                                     @RequestParam(name = "to", defaultValue = "999999999999999", required = false) Long to) {
        return employeeService.getAllBill(from,to);
    }
}
