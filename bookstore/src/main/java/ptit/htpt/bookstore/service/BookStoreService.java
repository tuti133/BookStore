package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.repository.BookStoreRepository;

@Service
public class BookStoreService {
    @Autowired
    BookStoreRepository bookStoreRepository;

    public ResponseDto getAllStore() {
        return new ResponseDto("0", "success", bookStoreRepository.findAll());
    }
}
