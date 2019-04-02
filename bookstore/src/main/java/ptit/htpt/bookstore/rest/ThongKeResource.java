package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ptit.htpt.bookstore.dto.ThongKeDto;
import ptit.htpt.bookstore.service.ThongKeService;

import java.util.Date;

@RestController
public class ThongKeResource {
    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/api/thongke")
    //type = 0 getall, type =1 get bill, type =2 get online
    public ThongKeDto thongke(@RequestParam("type") int type,
                              @RequestParam("from") Long from, @RequestParam("to") Long to, @RequestParam("buyStatus") String buyStatus) {
        return thongKeService.thongKeFromDateToDate(from, to, type, buyStatus);
    }
}
