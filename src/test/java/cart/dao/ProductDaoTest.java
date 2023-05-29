package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import cart.repository.dao.ProductDao;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @Test
    void createProduct() {
        // given
        final Product product = new Product("product", 1_000, "www.image.png");

        // when
        final Long saveId = productDao.createProduct(product);

        // then
        assertThat(saveId).isNotNull();
    }

    @Test
    void getProductById() {
        // given
        final Product product = new Product("product", 1_000, "www.image.png");
        final Long saveId = productDao.createProduct(product);

        // when
        final Product findProduct = productDao.getProductById(saveId);

        // then
        assertThat(findProduct.getName()).isEqualTo(product.getName());
        assertThat(findProduct.isDeleted()).isFalse();
    }

    @Test
    void deleteProduct() {
        // given
        final Product product = new Product("product", 1_000, "www.image.png");
        final Long saveId = productDao.createProduct(product);
        productDao.deleteProduct(saveId);

        // when
        final Product findProduct = productDao.getProductById(saveId);

        // then
        assertThat(findProduct.isDeleted()).isTrue();
    }

    @Test
    void getProductByIds() {
        // given
        final List<Long> ids = List.of(1L, 3L);

        // when
        final List<Product> findProducts = productDao.getProductByIds(ids);

        // then
        assertThat(findProducts).map(Product::getId)
                .isEqualTo(ids);
    }
}
