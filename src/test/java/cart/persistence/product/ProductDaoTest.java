package cart.persistence.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    @DisplayName("상품을 올바르게 생성한다.")
    void createProductTest() {
        final ProductEntity productEntity = new ProductEntity("레오배변패드", 10000, "https://www.google.com/aclk?sa=l&ai=DChcSEwichomlqpz_AhUWMGAKHVkwBDgYABABGgJ0bQ&sig=AOD64_0741-6emp177CIBjUFFPDDyQIwwA&adurl&ctype=5&ved=2ahUKEwjEzIClqpz_AhXVEIgKHRt9DlAQvhd6BAgBEEw");
        assertDoesNotThrow(() -> productDao.createProduct(productEntity));
    }
}
