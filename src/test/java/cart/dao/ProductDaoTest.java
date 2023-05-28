package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
        final ProductEntity productEntity = new ProductEntity(testProductName, 3_000, "testImageUrl");

        productDao.saveProduct(productEntity);

        final List<ProductEntity> allProducts = productDao.findAllProducts();
        assertThat(allProducts).hasSize(1);
        assertThat(allProducts.get(0).getName()).isEqualTo(testProductName);
    }

    @Test
    void 상품_식별자_조회_테스트() {
        final String testProductName = "testProductA";
        final ProductEntity productEntity = new ProductEntity(testProductName, 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(productEntity);

        final ProductEntity productById = productDao.findProductById(savedProductId);

        assertThat(productById.getName()).isEqualTo(testProductName);
    }

    @Test
    void 상품_정보_수정_테스트() {
        final String beforeName = "testProductA";
        final String afterName = "testProductB";
        final ProductEntity beforeProduct = new ProductEntity(beforeName, 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(beforeProduct);
        final ProductEntity afterProduct = new ProductEntity(afterName, beforeProduct.getPrice(),
                beforeProduct.getImageUrl());

        productDao.updateProduct(savedProductId, afterProduct);

        final ProductEntity updatedProduct = productDao.findProductById(savedProductId);
        assertThat(updatedProduct.getName()).isEqualTo(afterName);
    }

    @Test
    void 상품_삭제_테스트() {
        final ProductEntity productEntity = new ProductEntity("testProductA", 3_000, "testImageUrl");
        final Long savedProductId = productDao.saveProduct(productEntity);
        assertThat(productDao.findAllProducts()).hasSize(1);

        productDao.deleteProduct(savedProductId);

        assertThat(productDao.findAllProducts()).isEmpty();
    }
}
