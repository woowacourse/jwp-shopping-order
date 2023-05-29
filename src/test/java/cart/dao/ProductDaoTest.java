package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
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
    void 상품_저장_테스트() {
        final String testProductName = "testProductA";
        final Product product = new Product(testProductName, 3_000, "testImageUrl");

        productDao.saveProduct(product);

        final List<Product> allProducts = productDao.findAllProducts();
        assertThat(allProducts).hasSize(1);
        assertThat(allProducts.get(0).getName()).isEqualTo(testProductName);
    }

    @Test
    void 상품_식별자_조회_테스트() {
        final String testProductName = "testProductA";
        final Product product = new Product(testProductName, 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(product);

        final Optional<Product> productById = productDao.findProductById(savedProductId);

        assertThat(productById).isNotEmpty();
        assertThat(productById.get().getName()).isEqualTo(testProductName);
    }

    @Test
    void 상품_정보_수정_테스트() {
        final String beforeName = "testProductA";
        final String afterName = "testProductB";
        final Product beforeProduct = new Product(beforeName, 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(beforeProduct);
        final Product afterProduct = new Product(afterName, beforeProduct.getPrice(),
                beforeProduct.getImageUrl());

        productDao.updateProduct(savedProductId, afterProduct);

        final Optional<Product> updatedProduct = productDao.findProductById(savedProductId);

        assertThat(updatedProduct).isNotEmpty();
        assertThat(updatedProduct.get().getName()).isEqualTo(afterName);
    }

    @Test
    void 상품_삭제_테스트() {
        final Product product = new Product("testProductA", 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(product);
        assertThat(productDao.findAllProducts()).hasSize(1);

        productDao.deleteProduct(savedProductId);

        assertThat(productDao.findAllProducts()).isEmpty();
    }

    @Test
    void 상품_선택_조회_테스트() {
        final Product productA = new Product("testProductA", 3_000, "testImageUrl");
        final Product productB = new Product("testProductB", 2_000, "testImageUrl");
        final Product productC = new Product("testProductC", 1_000, "testImageUrl");
        final Long savedProductAId = productDao.saveProduct(productA);
        productDao.saveProduct(productB);
        final Long savedProductCId = productDao.saveProduct(productC);

        final List<Product> productsById = productDao.findByIds(List.of(savedProductAId, savedProductCId));

        assertThat(productsById).hasSize(2);
        assertThat(productsById).extracting("name").doesNotContain("testProductB");
    }
}
