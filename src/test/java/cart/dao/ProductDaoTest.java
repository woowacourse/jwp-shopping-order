package cart.dao;

import cart.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.common.fixture.DomainFixture.PRODUCT_CHICKEN;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql(value = "classpath:test_truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Sql("classpath:testData.sql")
    @Test
    void 전체_상품을_id순으로_start부터_size만큼_조회한다() {
        //when
        final List<Product> allProducts = productDao.getAllProductsBy(10, 10);

        //then
        assertThat(allProducts).isEqualTo(List.of(
                new Product(11L, "11", 1000, "image.jpeg"),
                new Product(12L, "12", 1000, "image.jpeg"),
                new Product(13L, "13", 1000, "image.jpeg"),
                new Product(14L, "14", 1000, "image.jpeg"),
                new Product(15L, "15", 1000, "image.jpeg"),
                new Product(16L, "16", 1000, "image.jpeg"),
                new Product(17L, "17", 1000, "image.jpeg"),
                new Product(18L, "18", 1000, "image.jpeg"),
                new Product(19L, "19", 1000, "image.jpeg"),
                new Product(20L, "20", 1000, "image.jpeg")
        ));
    }

    @Test
    void 상품의_개수를_센다() {
        //given
        productDao.createProduct(PRODUCT_CHICKEN);

        //when
        final Integer integer = productDao.countAllProduct();

        //then
        assertThat(integer).isEqualTo(1);
    }

    @Test
    void 상품의_개수를_셀_때_상품의_개수가_없다면_0을_반환한다() {
        //when
        final Integer integer = productDao.countAllProduct();

        //then
        assertThat(integer).isEqualTo(0);
    }
}
