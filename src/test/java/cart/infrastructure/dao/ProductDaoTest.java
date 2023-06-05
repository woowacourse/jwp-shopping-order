package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Product;
import cart.domain.vo.Amount;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
    @DisplayName("상품을 생성할 수 있다.")
    void testCreate() {
        // given
        final Product product = new Product("망고", Amount.of(1_000), "mango.png");
        final ProductEntity productEntity = ProductEntity.from(product);

        // when
        final Long id = productDao.create(productEntity);

        // then
        assertThat(productDao.findById(id).isPresent()).isTrue();
    }

    @Test
    @DisplayName("전체 상품 목록을 조회할 수 있다.")
    void testFindAll() {
        // given
        final Product product1 = new Product("망고", Amount.of(1_000), "mango.png");
        final Product product2 = new Product("터틀", Amount.of(2_000), "turtle.png");
        final ProductEntity productEntity1 = ProductEntity.from(product1);
        final ProductEntity productEntity2 = ProductEntity.from(product2);
        productDao.create(productEntity1);
        productDao.create(productEntity2);

        // when
        final List<ProductEntity> productEntities = productDao.findAll();

        // then
        assertAll(
                () -> assertThat(productEntities).hasSize(2),
                () -> assertThat(productEntities)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(productEntity1, productEntity2))
        );
    }

    @Test
    @DisplayName("상품을 업데이트 할 수 있다.")
    void testUpdate() {
        // given
        final Product product = new Product("망고", Amount.of(1_000), "mango.png");
        final ProductEntity productEntity = ProductEntity.from(product);
        final Long id = productDao.create(productEntity);

        // when
        final Product updatedProduct = new Product("망고", Amount.of(2_000), "updated.png");
        final ProductEntity updatedProductEntity = ProductEntity.from(updatedProduct);
        productDao.update(id, updatedProductEntity);

        // then
        final ProductEntity actual = productDao.findById(id).orElseThrow(NoSuchElementException::new);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(updatedProductEntity);
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다.")
    void testDelete() {
        // given
        final Product product = new Product("망고", Amount.of(1_000), "mango.png");
        final ProductEntity productEntity = ProductEntity.from(product);
        final Long id = productDao.create(productEntity);

        // when
        productDao.deleteById(id);

        // then
        assertThat(productDao.findById(id).isPresent()).isFalse();
    }
}
