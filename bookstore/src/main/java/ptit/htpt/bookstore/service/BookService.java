package ptit.htpt.bookstore.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import ptit.htpt.bookstore.dto.BookQuantityDto;
import ptit.htpt.bookstore.dto.CreateBookDto;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Book;
import ptit.htpt.bookstore.entity.BookQuantity;
import ptit.htpt.bookstore.entity.BookStore;
import ptit.htpt.bookstore.repository.BookQuantityRepository;
import ptit.htpt.bookstore.repository.BookRepository;
import ptit.htpt.bookstore.repository.BookStoreRepository;
import ptit.htpt.bookstore.repository.jdbc.BillJdbc;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookQuantityRepository bookQuantityRepository;

    @Autowired
    private BookStoreRepository bookStoreRepository;

    @Autowired
    private RateService rateService;

    @Autowired
    private BillJdbc billJdbc;

    public List<Book> getAll() {
        List<Book> list = bookRepository.findAll();
        for (Book b: list) {
            b.setAvgRate(rateService.getRate(b.getId()));
        }
        return list;
    }

    public List<BookQuantityDto> getByBookStore(Long bookStoreId) {
        List<BookQuantityDto> res =  billJdbc.getAllBookQuantityByStoreId(bookStoreId);
        return res;
    }


    public Book findById(long id) {
        Book b = bookRepository.findById(id).orElse(null);
        b.setAvgRate(rateService.getRate(id));
        return b;
    }

    private String getExtensionFile(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public ResponseDto save(Book book, MultipartFile image) {
        Book entity = new Book();
        if (book.getId() != null) {
            entity = bookRepository.findById(book.getId()).get();
        }
        if (book.getImageUrl() != null) entity.setImageUrl(book.getImageUrl());
        if (image != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsszz");
            String fileName = sdf.format(new Date()) + getExtensionFile(image.getOriginalFilename());
            entity.setImageUrl(Paths.get("/image", fileName).toString());
            try {
                Files.createDirectories(Paths.get("image"));
                Files.copy(image.getInputStream(), Paths.get("image", fileName));
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseDto("1", e.getMessage(), null);
            }
        }

        entity.setAuthor(book.getAuthor());
        entity.setName(book.getName());
        entity.setDescription(book.getDescription());
        entity.setPrice(book.getPrice());
        entity.setPublishedYear(book.getPublishedYear());
        entity.setPublisher(book.getPublisher());
        entity.setCategory(book.getCategory());


        Book result = bookRepository.save(entity);

        // init book quantity for all store
        if (book.getId() == null) {
            List<BookStore> listBs = bookStoreRepository.findAll();
            for (BookStore bs : listBs) {
                BookQuantity bq = new BookQuantity();
                bq.setBook(result);
                bq.setBookStore(bs);
                bq.setQuantity(1000L);
                bookQuantityRepository.save(bq);
            }
        }

        return new ResponseDto("0", "Book saved successfully", result);
    }

    public ResponseDto likeBook(Long id) {
        Book b = bookRepository.findById(id).get();
        b.setFavorite(b.getFavorite() + 1);
        try {
            Book result = bookRepository.save(b);
            return new ResponseDto("0", "Success", result);
        } catch (Exception e){
            return new ResponseDto("1", "Error", null);
        }
    }
}