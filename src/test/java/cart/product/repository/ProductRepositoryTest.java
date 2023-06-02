package cart.product.repository;

import cart.cartitem.dao.CartItemDao;
import cart.init.DBInit;
import cart.product.dao.ProductDao;
import cart.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static cart.product.domain.ProductTest.PRODUCT_FIRST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductRepositoryTest extends DBInit {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        final ProductDao productDao = new ProductDao(jdbcTemplate);
        final CartItemDao cartItemDao = new CartItemDao(jdbcTemplate);
        productRepository = new ProductRepository(productDao, cartItemDao);
    }

    @Test
    void productId로_물품을_조회한다() {
        // given
        final long productId = 1L;

        // when
        final Product product = productRepository.getProductById(productId);

        // then
        assertAll(
                () -> assertThat(product.getId()).isOne(),
                () -> assertThat(product.getName()).isEqualTo("치킨")
        );
    }

    @Test
    void 모든_물품을_조회한다() {
        // when
        final List<Product> products = productRepository.getAllProducts();

        // then
        assertThat(products).hasSize(3);
    }

    @Test
    void 물품을_추가한다() {
        // when
        final Long productId = productRepository.createProduct(PRODUCT_FIRST);

        // then
        assertThat(productId).isEqualTo(4L);
    }

    @Test
    void 물품을_수정한다() {
        // given
        final long productId = 2L;
        final Product product = new Product(productId, "사과", 10L, "aa", 10.0, true);

        // when
        productRepository.updateProduct(product);

        // then
        assertThat(productRepository.getProductById(productId).getName()).isEqualTo("사과");
    }

    @Test
    void 물품을_삭제한다() {
        // when
        productRepository.removeProduct(1L);

        // then
        assertThat(productRepository.getAllProducts()).hasSize(2);
    }
}
