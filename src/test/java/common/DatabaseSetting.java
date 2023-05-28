package common;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static common.DatabaseSetting.Fixture.*;

@Service
@Import(DatabaseConfiguration.class)
public class DatabaseSetting {

    private final ProductDao productDao;
    private final MemberDao memberDao;
    private final CartItemDao cartItemDao;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseSetting(ProductDao productDao, MemberDao memberDao, CartItemDao cartItemDao, JdbcTemplate jdbcTemplate) {
        this.productDao = productDao;
        this.memberDao = memberDao;
        this.cartItemDao = cartItemDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTables() {
        jdbcTemplate.update("CREATE TABLE if not exists product (" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(255) NOT NULL," +
                "    price INT NOT NULL," +
                "    image_url VARCHAR(255) NOT NULL" +
                ")");
        jdbcTemplate.update("CREATE TABLE if not exists member (\n" +
                "     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "     email VARCHAR(255) NOT NULL UNIQUE,\n" +
                "     password VARCHAR(255) NOT NULL\n" +
                ")");
        jdbcTemplate.update("CREATE TABLE if not exists cart_item (\n" +
                "    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    member_id BIGINT NOT NULL,\n" +
                "    product_id BIGINT NOT NULL,\n" +
                "    quantity INT NOT NULL,\n" +
                "    FOREIGN KEY (member_id) REFERENCES member(id),\n" +
                "    FOREIGN KEY (product_id) REFERENCES product(id)\n" +
                ")");
    }

    public void setUp() {
        createTables();

        productDao.createProduct(치킨);
        productDao.createProduct(샐러드);
        productDao.createProduct(피자);

        memberDao.addMember(멤버_A);
        memberDao.addMember(멤버_B);

        cartItemDao.save(A_치킨);
        cartItemDao.save(A_샐러드);
        cartItemDao.save(B_피자);
    }

    public void clearDatabase() {
        jdbcTemplate.update("drop table if exists cart_item");
        jdbcTemplate.update("drop table if exists product");
        jdbcTemplate.update("drop table if exists member");
    }

    public static class Fixture {
        public static final Product 치킨 = new Product(1L, "치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
        public static final Product 샐러드 = new Product(2L, "샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80");
        public static final Product 피자 = new Product(3L, "피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80");

        public static final Member 멤버_A = new Member(1L, "a@a.com", "1234");
        public static final Member 멤버_B = new Member(2L, "b@b.com", "1234");

        public static final CartItem A_치킨 = new CartItem(멤버_A, 치킨);
        public static final CartItem A_샐러드 = new CartItem(멤버_A, 샐러드);
        public static final CartItem B_피자 = new CartItem(멤버_B, 피자);
    }
}
