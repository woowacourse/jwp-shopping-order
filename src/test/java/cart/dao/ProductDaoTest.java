package cart.dao;

import static cart.fixture.ProductFixture.CHICKEN_NO_ID;
import static cart.fixture.ProductFixture.PIZZA_NO_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.entity.ProductEntity;
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
        Long actual = productDao.save(ProductEntity.from(CHICKEN_NO_ID));

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 단일_상품을_조회한다() {
        // given
        ProductEntity expected = ProductEntity.from(CHICKEN_NO_ID);
        Long given = productDao.save(expected);

        // when
        Optional<ProductEntity> actual = productDao.findById(given);

        // then
        assertThat(actual).isPresent();
        상품_검증(actual.get(), expected);
    }

    @Test
    void 존재하지_않는_상품_조회시_빈값() {
        // when
        Optional<ProductEntity> actual = productDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_상품을_조회한다() {
        // given
        ProductEntity expectedChicken = ProductEntity.from(CHICKEN_NO_ID);
        ProductEntity expectedPizza = ProductEntity.from(PIZZA_NO_ID);
        productDao.save(expectedChicken);
        productDao.save(expectedPizza);

        // when
        List<ProductEntity> products = productDao.findAll();

        // then
        상품_검증(products.get(0), expectedChicken);
        상품_검증(products.get(1), expectedPizza);
    }

    @Test
    void 여러_아이디를_전달받아_해당하는_모든_상품을_반환한다() {
        // given
        ProductEntity expectedChicken = ProductEntity.from(CHICKEN_NO_ID);
        ProductEntity expectedPizza = ProductEntity.from(PIZZA_NO_ID);
        Long chickenId = productDao.save(expectedChicken);
        Long pizzaId = productDao.save(expectedPizza);
        Product baseball = new Product("야구공", 3000, "www.baseball.com");
        productDao.save(ProductEntity.from(baseball));
        List<Long> ids = List.of(chickenId, pizzaId);

        // when
        List<ProductEntity> actual = productDao.findAllByIds(ids);

        // then
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> 상품_검증(actual.get(0), expectedChicken),
                () -> 상품_검증(actual.get(1), expectedPizza)
        );
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long CHICKEN_NO_ID_ID = productDao.save(ProductEntity.from(CHICKEN_NO_ID));

        // when
        productDao.deleteById(CHICKEN_NO_ID_ID);

        // then
        assertThat(productDao.findById(CHICKEN_NO_ID_ID)).isEmpty();
    }

    @Test
    void 상품을_변경한다() {
        // given
        Long id = productDao.save(ProductEntity.from(CHICKEN_NO_ID));
        ProductEntity expected = new ProductEntity(id, PIZZA_NO_ID.getName(), PIZZA_NO_ID.getPrice(), PIZZA_NO_ID.getImageUrl());
        // when
        productDao.update(
                expected);

        // then
        ProductEntity actual = productDao.findById(id).get();
        상품_검증(actual, expected);
    }

    private void 상품_검증(ProductEntity actualProduct, ProductEntity expectedProduct) {
        assertAll(
                () -> assertThat(actualProduct.getId()).isPositive(),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualProduct.getImageUrl()).isEqualTo(expectedProduct.getImageUrl())
        );
    }
}


