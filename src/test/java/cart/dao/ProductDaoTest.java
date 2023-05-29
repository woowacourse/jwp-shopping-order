package cart.dao;

import static cart.fixture.ProductFixture.CHICKEN_NO_ID;
import static cart.fixture.ProductFixture.PIZZA_NO_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductDao 은(는)")
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
    void 상품을_추가한다() {
        // when
        Long actual = productDao.save(CHICKEN_NO_ID);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        Long given = productDao.save(CHICKEN_NO_ID);

        // when
        Optional<Product> actual = productDao.findById(given);

        // then
        assertThat(actual).isPresent();
        상품_검증(actual.get(), CHICKEN_NO_ID);
    }

    @Test
    void 존재하지_않는_상품_조회시_빈값() {
        // when
        Optional<Product> actual = productDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        productDao.save(CHICKEN_NO_ID);
        productDao.save(PIZZA_NO_ID);

        // when
        List<Product> products = productDao.findAll();

        // then
        상품_검증(products.get(0), CHICKEN_NO_ID);
        상품_검증(products.get(1), PIZZA_NO_ID);
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long CHICKEN_NO_ID_ID = productDao.save(CHICKEN_NO_ID);

        // when
        productDao.deleteById(CHICKEN_NO_ID_ID);

        // then
        assertThat(productDao.findById(CHICKEN_NO_ID_ID)).isEmpty();
    }

    @Test
    void 상품을_변경한다() {
        // given
        Long id = productDao.save(CHICKEN_NO_ID);

        // when
        productDao.updateById(id, PIZZA_NO_ID);

        // then
        Product actual = productDao.findById(id).get();
        상품_검증(actual, PIZZA_NO_ID);
    }

    private void 상품_검증(Product actualProduct, Product expectedProduct) {
        assertAll(
                () -> assertThat(actualProduct.getId()).isPositive(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl())
        );
    }
}


