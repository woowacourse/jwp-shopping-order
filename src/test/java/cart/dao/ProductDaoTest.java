package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.entity.ProductEntity;
import cart.fixture.ProductEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class ProductDaoTest {

    private ProductDao productDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void insert() {
        final ProductEntity result = productDao.insert(ProductEntityFixture.CHICKEN);
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getName()).isEqualTo("치킨"),
                () -> assertThat(result.getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getStock()).isEqualTo(10)
        );
    }

    @Sql("classpath:deleteAll.sql")
    @Test
    void findAll() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Long pizzaId = productDao.insert(ProductEntityFixture.PIZZA).getId();
        final List<ProductEntity> result = productDao.findAll();
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(chickenId),
                () -> assertThat(result.get(0).getName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get(0).getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get(0).getStock()).isEqualTo(10),
                () -> assertThat(result.get(1).getId()).isEqualTo(pizzaId),
                () -> assertThat(result.get(1).getName()).isEqualTo("피자"),
                () -> assertThat(result.get(1).getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get(1).getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get(1).getStock()).isEqualTo(10)
        );
    }

    @DisplayName("데이터가 없을 때 빈 리스트를 반환한다.")
    @Sql("classpath:deleteAll.sql")
    @Test
    void findAllWhenEmpty() {
        final List<ProductEntity> result = productDao.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void findById() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Optional<ProductEntity> result = productDao.findById(chickenId);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get().getName()).isEqualTo("치킨"),
                () -> assertThat(result.get().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get().getStock()).isEqualTo(10)
        );
    }

    @DisplayName("존재하지 않는 상품을 조회하면 Optional.empty 를 반환한다.")
    @Test
    void findByNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<ProductEntity> result = productDao.findById(chickenId);
        assertThat(result).isEmpty();
    }

    @Test
    void update() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Optional<ProductEntity> result = productDao.update(new ProductEntity(chickenId, ProductEntityFixture.PIZZA));
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get().getName()).isEqualTo("피자"),
                () -> assertThat(result.get().getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get().getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get().getStock()).isEqualTo(10)
        );
    }

    @DisplayName("존재하지 않는 상품을 업데이트하면 Optional.empty 를 반환한다.")
    @Test
    void updateWithNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<ProductEntity> result = productDao.update(new ProductEntity(chickenId, ProductEntityFixture.PIZZA));
        assertThat(result).isEmpty();
    }

    @Test
    void deleteById() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<ProductEntity> result = productDao.findById(chickenId);
        assertThat(result).isEmpty();
    }

    @DisplayName("존재하지 않는 id 에 대해 삭제해도 예외를 발생시키지 않는다.")
    @Test
    void deleteByNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        assertThatCode(() -> productDao.deleteById(chickenId))
                .doesNotThrowAnyException();
    }
}
