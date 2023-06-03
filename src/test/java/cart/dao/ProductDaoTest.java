package cart.dao;

import cart.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql({"classpath:test_init.sql"})
@ActiveProfiles("test")
class ProductDaoTest {

    private ProductDao productDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDaoTest(ProductDao productDao, JdbcTemplate jdbcTemplate) {
        this.productDao = productDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into product (name, price, image_url) values ('치킨', 10000, 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('샐러드', 20000, 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80')");
        jdbcTemplate.update("insert into product (name, price, image_url) values ('피자', 13000, 'https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80')");
    }

    @Test
    void findAllByIds() {
        List<Product> products = productDao.findAllByIds(List.of(1L, 2L));

        assertAll(
                () -> assertThat(products.size()).isEqualTo(2),
                () -> assertThat(products.get(0).getId()).isEqualTo(1L),
                () -> assertThat(products.get(1).getId()).isEqualTo(2L)
        );

    }
}
