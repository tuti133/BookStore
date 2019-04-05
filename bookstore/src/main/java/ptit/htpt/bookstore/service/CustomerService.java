package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.Customer;
import ptit.htpt.bookstore.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<CustomerAccountDto> getAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerAccountDto> result = new ArrayList<>();
        for (Customer customer : customerList) {
            CustomerAccountDto customerAccountDto = new CustomerAccountDto();
            customerAccountDto.setName(customer.getName());
            customerAccountDto.setPhone(customer.getPhone());
            result.add(customerAccountDto);
        }
        return result;
    }
}
