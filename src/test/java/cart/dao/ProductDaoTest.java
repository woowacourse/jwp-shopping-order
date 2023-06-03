package cart.dao;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.Product;

@JdbcTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 여러_id로_product들을_찾는다() {
        // given
        List<Product> givenProducts = productDao.getAllProducts();
        List<Long> productIds = givenProducts.stream()
            .map(Product::getId)
            .collect(toList());

        // when
        List<Product> foundProducts = productDao.findProductsByIds(productIds);

        // then
        assertThat(foundProducts.size()).isEqualTo(givenProducts.size());
    }
}
