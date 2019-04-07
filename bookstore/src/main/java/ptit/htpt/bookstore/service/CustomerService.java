package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.repository.CustomerRepository;
import ptit.htpt.bookstore.repository.jdbc.BillJdbc;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private BillJdbc billJdbc;

    public List<CustomerAccountDto> getAllCustomer() {
       return  billJdbc.getAllCustomer();

    }
}
