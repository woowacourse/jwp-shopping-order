package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.domain.ProductStock;
import cart.domain.Stock;
import cart.entity.ProductEntity;
import cart.fixture.ProductEntityFixture;
import cart.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class ProductRepositoryTest {

    private ProductDao productDao;
    private ProductRepository productRepository;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate) {
        productDao = new ProductDao(jdbcTemplate);
        productRepository = new ProductRepository(productDao);
    }

    @Test
    void createProduct() {
        final ProductStock result = productRepository.createProduct(ProductFixture.CHICKEN, new Stock(10));
        assertAll(
                () -> assertThat(result.getProduct().getId()).isPositive(),
                () -> assertThat(result.getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getStock().getValue()).isEqualTo(10)
        );
    }

    @Sql("classpath:deleteAll.sql")
    @Test
    void getAllProductStocks() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Long pizzaId = productDao.insert(ProductEntityFixture.PIZZA).getId();
        final List<ProductStock> result = productRepository.getAllProductStocks();
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getProduct().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get(0).getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get(0).getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get(0).getStock().getValue()).isEqualTo(10),
                () -> assertThat(result.get(1).getProduct().getId()).isEqualTo(pizzaId),
                () -> assertThat(result.get(1).getProduct().getName()).isEqualTo("피자"),
                () -> assertThat(result.get(1).getProduct().getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get(1).getProduct().getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get(1).getStock().getValue()).isEqualTo(10)
        );
    }

    @DisplayName("데이터가 없을 때 빈 리스트를 반환한다.")
    @Sql("classpath:deleteAll.sql")
    @Test
    void getAllProductStocksWhenEmpty() {
        final List<ProductStock> result = productRepository.getAllProductStocks();
        assertThat(result).isEmpty();
    }

    @Test
    void getProductStockById() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Optional<ProductStock> result = productRepository.getProductStockById(chickenId);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getProduct().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get().getProduct().getName()).isEqualTo("치킨"),
                () -> assertThat(result.get().getProduct().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get().getProduct().getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get().getStock().getValue()).isEqualTo(10)
        );
    }

    @DisplayName("존재하지 않는 상품재고를 조회하면 Optional.empty 를 반환한다.")
    @Test
    void getProductStockByNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<ProductStock> result = productRepository.getProductStockById(chickenId);
        assertThat(result).isEmpty();
    }

    @Test
    void getProductById() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Optional<Product> result = productRepository.getProductById(chickenId);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get().getName()).isEqualTo("치킨"),
                () -> assertThat(result.get().getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get().getImageUrl()).isEqualTo("http://example.com/chicken.jpg")
        );
    }

    @DisplayName("존재하지 않는 상품을 조회하면 Optional.empty 를 반환한다.")
    @Test
    void getProductByNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<Product> result = productRepository.getProductById(chickenId);
        assertThat(result).isEmpty();
    }

    @Test
    void updateProductStock() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        final Optional<ProductStock> result = productRepository.updateProductStock(
                new Product(chickenId, "피자", 15_000, "http://example.com/pizza.jpg"),
                new Stock(15)
        );
        assertAll(
                () -> assertThat(result.isPresent()),
                () -> assertThat(result.get().getProduct().getId()).isEqualTo(chickenId),
                () -> assertThat(result.get().getProduct().getName()).isEqualTo("피자"),
                () -> assertThat(result.get().getProduct().getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get().getProduct().getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get().getStock().getValue()).isEqualTo(15)
        );
    }

    @DisplayName("존재하지 않는 상품을 업데이트하면 Optional.empty 를 반환한다.")
    @Test
    void updateProductStockWithNoExistId() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productDao.deleteById(chickenId);
        final Optional<ProductStock> result = productRepository.updateProductStock(
                new Product(chickenId, "피자", 15_000, "http://example.com/pizza.jpg"),
                new Stock(15)
        );
        assertThat(result).isEmpty();
    }

    @Test
    void deleteProductById() {
        final Long chickenId = productDao.insert(ProductEntityFixture.CHICKEN).getId();
        productRepository.deleteProductById(chickenId);
        final Optional<ProductEntity> result = productDao.findById(chickenId);
        assertThat(result).isEmpty();
    }
}
