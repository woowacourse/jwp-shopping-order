package common;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import static common.Fixtures.*;

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

    public void setUp() {
        createTables();

        addProducts();
        addMembers();
        addCartItems();
    }

    public void createTables() {
        jdbcTemplate.update("CREATE TABLE if not exists products (\n" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT,\n" +
                "    name VARCHAR(255) NOT NULL,\n" +
                "    price INT NOT NULL,\n" +
                "    image_url VARCHAR(255) NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists members (\n" +
                "     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "     email VARCHAR(255) NOT NULL UNIQUE,\n" +
                "     password VARCHAR(255) NOT NULL\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists cart_items (\n" +
                "    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    member_id BIGINT NOT NULL,\n" +
                "    product_id BIGINT NOT NULL,\n" +
                "    quantity INT NOT NULL,\n" +
                "    FOREIGN KEY (member_id) REFERENCES members(id),\n" +
                "    FOREIGN KEY (product_id) REFERENCES products(id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists orders (\n" +
                "    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    member_id BIGINT NOT NULL,\n" +
                "    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "    FOREIGN KEY(member_id) REFERENCES members(id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE if not exists order_items (\n" +
                "    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
                "    order_id BIGINT NOT NULL,\n" +
                "    product_id BIGINT NOT NULL,\n" +
                "    quantity INT NOT NULL,\n" +
                "    FOREIGN KEY (order_id) REFERENCES orders(id),\n" +
                "    FOREIGN KEY (product_id) REFERENCES products(id)\n" +
                ");");
    }

    public void addProducts() {
        productDao.createProduct(치킨);
        productDao.createProduct(샐러드);
        productDao.createProduct(피자);
    }

    public void addMembers() {
        memberDao.addMember(멤버_A);
        memberDao.addMember(멤버_B);
    }

    public void addCartItems() {
        cartItemDao.save(A_치킨_1);
        cartItemDao.save(A_샐러드_1);
        cartItemDao.save(B_피자_1);
    }

    public void clearDatabase() {
        jdbcTemplate.update("drop table if exists order_items");
        jdbcTemplate.update("drop table if exists orders");
        jdbcTemplate.update("drop table if exists cart_items");
        jdbcTemplate.update("drop table if exists products");
        jdbcTemplate.update("drop table if exists members");
    }
}
