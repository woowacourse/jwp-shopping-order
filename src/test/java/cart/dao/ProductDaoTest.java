package cart.dao;

import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static cart.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new JdbcTemplateProductDao(jdbcTemplate);
    }

    @Test
    void 상품을_추가하고_조회한다() {
        // given, when
        Long productId = productDao.createProduct(치킨);
        Product newProduct = new Product(
                productId,
                치킨.getName(),
                치킨.getPrice(),
                치킨.getImageUrl(),
                치킨.getStock());

        // then
        assertThat(productDao.findProductById(productId).get())
                .isEqualTo(newProduct);
    }

    @Test
    void 없는_상품을_조회하면_빈_옵셔널을_반환한다() {
        // given
        Long productId = productDao.createProduct(치킨);

        // when
        Optional<Product> product = productDao.findProductById(productId + 1);

        // then
        assertThat(product.isEmpty()).isTrue();
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        productDao.createProduct(치킨);
        productDao.createProduct(피자);
        productDao.createProduct(샐러드);

        // when
        List<Product> products = productDao.findAllProducts();

        // then
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void 전체_상품을_조회할_때_상품이_없으면_빈_리스트를_반환한다() {
        // given, when
        List<Product> products = productDao.findAllProducts();

        // then
        assertThat(products).isEmpty();
    }

    @Test
    void 상품을_업데이트한다() {
        // given
        Long productId = productDao.createProduct(치킨);

        // when
        productDao.updateProduct(productId, 피자);

        // then
        assertThat(productDao.findProductById(productId).get().getName())
                .isEqualTo(피자.getName());
    }

    @Test
    void 상품을_삭제한다() {
        // given
        Long productId = productDao.createProduct(치킨);

        // when
        productDao.deleteProduct(productId);

        // then
        assertThat(productDao.findProductById(productId).isEmpty())
                .isTrue();
    }
}
