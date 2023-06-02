package cart.product.dao;

import cart.init.DBInit;
import cart.product.domain.Product;
import cart.product.repository.ProductEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductDaoTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void 모든_상품을_조회한다() {
        // when
        final List<ProductEntity> products = productDao.getAllProducts();

        // then
        assertThat(products).hasSize(3);
    }

    @Test
    void productId로_물품을_조회한다() {
        // given
        final long productId = 2L;

        // when
        final ProductEntity productEntity = productDao.getProductById(productId);

        // then
        assertAll(
                () -> assertThat(productEntity.getId()).isEqualTo(2L),
                () -> assertThat(productEntity.getName()).isEqualTo("샐러드"),
                () -> assertThat(productEntity.getImageUrl()).isEqualTo("https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                () -> assertThat(productEntity.getPrice()).isEqualTo(20000L),
                () -> assertThat(productEntity.getPointRatio()).isEqualTo(10.0)
        );
    }

    @Test
    void 물품을_저장한다() {
        // given
        final ProductEntity productEntity = new ProductEntity(null, "바나나", 1000L, "aa", 10.0, true);

        // when
        final Long productId = productDao.createProduct(productEntity);

        // then
        assertThat(productId).isEqualTo(4L);
    }

    @Test
    void 물품을_수정한다() {
        // given
        final long productId = 1L;
        final ProductEntity productEntity = new ProductEntity(productId, "바나나", 1000L, "aa", 10.0, true);

        // when
        productDao.updateProduct(productEntity);

        // then
        final ProductEntity resultProductEntity = productDao.getProductById(productId);
        assertAll(
                () -> assertThat(resultProductEntity.getId()).isOne(),
                () -> assertThat(resultProductEntity.getName()).isEqualTo("바나나")
        );
    }

    @Test
    void 물품을_삭제한다() {
        // given
        final long productId = 1L;

        // expect
        assertAll(
                () -> assertThatNoException()
                        .isThrownBy(() -> productDao.deleteProduct(productId)),
                () -> assertThat(productDao.getAllProducts()).hasSize(2)
        );
    }
}
