package ptit.htpt.bookstore.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ptit.htpt.bookstore.dto.BookQuantityDto;
import ptit.htpt.bookstore.dto.CustomerAccountDto;
import ptit.htpt.bookstore.entity.Buy;
import ptit.htpt.bookstore.service.AccountService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class BillJdbc {
    @Qualifier("template1")
    @Autowired
    private JdbcTemplate jdbcTemplateHanoi;
    @Qualifier("template2")
    @Autowired
    private JdbcTemplate jdbcTemplateHcm;
    @Autowired
    private AccountService accountService;

    public List<CustomerAccountDto> getAllCustomer() {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "SELECT phone, name FROM customer";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CustomerAccountDto dto = new CustomerAccountDto();

            dto.setPhone(rs.getString("phone"));
            dto.setName(rs.getNString("name"));

            return dto;
        });
    }


    public List<BookQuantityDto> getAllBookQuantityByStoreId(Long storeId) {
        JdbcTemplate jdbcTemplate = getConnectionByStore(storeId);
        String sql = "SELECT book_quantity.id,  book.name, book.price, quantity FROM book_quantity JOIN book ON book.id = book_quantity.book_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BookQuantityDto dto = new BookQuantityDto();
            dto.setId(rs.getLong(1));
            dto.setBookName(rs.getNString(2));
            dto.setBookPrice(rs.getLong(3));
            dto.setQuantity(rs.getLong(4));

            return dto;
        });
    }
    public BookQuantityDto getAllBookQuantityByStoreIdAndBookId(Long storeId, Long bookId) {
        JdbcTemplate jdbcTemplate = getConnectionByStore(storeId);
        String sql = "SELECT book_quantity.id,  book.name, book.price, quantity FROM book_quantity JOIN book ON book.id = book_quantity.book_id WHERE book.id = ?";
        List<BookQuantityDto> list =  jdbcTemplate.query(sql,new Object[]{storeId}, (rs, rowNum) -> {
            BookQuantityDto dto = new BookQuantityDto();
            dto.setId(rs.getLong(1));
            dto.setBookName(rs.getNString(2));
            dto.setBookPrice(rs.getLong(3));
            dto.setQuantity(rs.getLong(4));

            return dto;
        });
        if(list.isEmpty()) return new BookQuantityDto();
        return list.get(0);
    }

    public boolean updateBookQuantity(Long id, Long quantity) {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "UPDATE  book_quantity SET quantity=? WHERE id = ?";
        int count = jdbcTemplate.update(
                sql, new Object[]{quantity, id});
        if (count > 0) return true;
        return false;
    }

    public boolean ínertBuyBook(Long buyId, Long quantity, Long bookQuantityId) {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "INSERT INTO  buy_book(buy_id, quantity,book_quantity_id) VALUES(?,?,?)";
        int count = jdbcTemplate.update(
                sql, new Object[]{buyId, quantity, bookQuantityId});
        if (count > 0) return true;
        return false;
    }

    public Long saveBill(Buy buy) {
        JdbcTemplate jdbcTemplate = getConnection();
        String insertSql = "insert into buy(created_date,status,ship_address,total_money,customer_id,employee_id) values(?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertSql, new String[]{"id"});
                    ps.setLong(1, buy.getCreatedDate());
                    ps.setString(2, buy.getStatus());
                    ps.setString(3, "");
                    ps.setLong(4, buy.getTotalMoney());
                    ps.setString(5, buy.getCustomer().getPhone());
                    ps.setLong(6, buy.getEmployee().getId());
                    return ps;
                },
                keyHolder);
        return keyHolder.getKey().longValue();
    }

    public boolean isExistCustomer(String phone) {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "SELECT count(*) FROM customer WHERE phone = ?";
        boolean result = false;

        int count = jdbcTemplate.queryForObject(
                sql, new Object[]{phone}, Integer.class);

        if (count > 0) {
            result = true;
        }

        return result;
    }

    private boolean updateCustomer(String name, String phone) {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "UPDATE  customer SET name=? WHERE phone = ?";
        int count = jdbcTemplate.update(
                sql, new Object[]{name, phone});
        if (count > 0) return true;
        return false;
    }

    private boolean insertCustomer(String name, String phone) {
        JdbcTemplate jdbcTemplate = getConnection();
        String sql = "INSERT INTO  customer(name, phone) VALUES(?,?)";
        int count = jdbcTemplate.update(
                sql, new Object[]{name, phone});
        if (count > 0) return true;
        return false;
    }

    public String saveCustomer(String name, String phone) {
        if (isExistCustomer(phone)) {
            updateCustomer(name, phone);
        } else {
            insertCustomer(name, phone);
        }
        return phone;

    }

    private JdbcTemplate getConnectionByStore(Long storeId) {
        if (storeId == 1) {
            return jdbcTemplateHanoi;
        }
        return jdbcTemplateHcm;
    }

    private JdbcTemplate getConnection() {
        if (accountService.getCurrentEmployee().getBookStoreId() == 1) {
            return jdbcTemplateHanoi;
        }
        return jdbcTemplateHcm;

    }

}
