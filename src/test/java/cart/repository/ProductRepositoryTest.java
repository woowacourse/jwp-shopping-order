package cart.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;


@JdbcTest
class ProductRepositoryTest extends RepositoryTest {
    private final ProductRepository productRepository;

    @Autowired
    private ProductRepositoryTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.productRepository = new ProductRepository(productDao, ordersCartItemDao);
    }

    @Test
    void findProductIdQuantityWithOrdersId() {
        Assertions.assertThat(productRepository.findProductIdQuantityWithOrdersId(1L)).isEqualTo(10000);
    }
}
