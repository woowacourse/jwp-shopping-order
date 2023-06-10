package cart.dao;

import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@JdbcTest
class ProductDaoTest {

    private static final Product ADDED_PRODUCT = new Product("쌀국수", 9000, "pho_image", 5.0, false);

    private ProductDao productDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        productDao = new ProductDao(jdbcTemplate);
    }

    @DisplayName("getProductById 테스트")
    @Test
    void getProductByIdTest() {
        Long productId = productDao.createProduct(ADDED_PRODUCT);
        Product product = productDao.getProductById(productId);

        assertSoftly(softly -> {
            softly.assertThat(product.getId()).isEqualTo(productId);
            softly.assertThat(product.getName()).isEqualTo(ADDED_PRODUCT.getName());
            softly.assertThat(product.getPrice()).isEqualTo(ADDED_PRODUCT.getPrice());
            softly.assertThat(product.getImageUrl()).isEqualTo(ADDED_PRODUCT.getImageUrl());
            softly.assertThat(product.getPointRatio()).isEqualTo(ADDED_PRODUCT.getPointRatio());
            softly.assertThat(product.getPointAvailable()).isEqualTo(ADDED_PRODUCT.getPointAvailable());
        });
    }

    //
    @DisplayName("getAllProducts 테스트")
    @Test
    void getAllProductsTest() {
        // data.sql 파일에서 3개의 product를 사전에 추가
        List<Product> products = productDao.getAllProducts();
        assertThat(products).hasSize(3);
    }

    @DisplayName("updateProduct 테스트")
    @Test
    void updateProductTest() {
        Long productId = productDao.createProduct(ADDED_PRODUCT);
        Product beforeProduct = productDao.getProductById(productId);

        int affectedRow = productDao.updateProduct(productId, new Product("쌀국수", 12000, "pho_image", 3.3, true));
        Product afterProduct = productDao.getProductById(productId);

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isEqualTo(1);
            softly.assertThat(beforeProduct.getName()).isEqualTo(ADDED_PRODUCT.getName());
            softly.assertThat(afterProduct.getName()).isEqualTo("쌀국수");
            softly.assertThat(beforeProduct.getPrice()).isEqualTo(ADDED_PRODUCT.getPrice());
            softly.assertThat(afterProduct.getPrice()).isEqualTo(12000);
            softly.assertThat(beforeProduct.getImageUrl()).isEqualTo(ADDED_PRODUCT.getImageUrl());
            softly.assertThat(afterProduct.getImageUrl()).isEqualTo("pho_image");
            softly.assertThat(beforeProduct.getPointRatio()).isEqualTo(ADDED_PRODUCT.getPointRatio());
            softly.assertThat(afterProduct.getPointRatio()).isEqualTo(3.3);
            softly.assertThat(beforeProduct.getPointAvailable()).isEqualTo(ADDED_PRODUCT.getPointAvailable());
            softly.assertThat(afterProduct.getPointAvailable()).isEqualTo(true);
        });
    }

    @DisplayName("deleteProduct 테스트")
    @Test
    void deleteProductTest() {
        Long productId = productDao.createProduct(ADDED_PRODUCT);
        int affectedRow = productDao.deleteProduct(productId);

        assertSoftly(softly -> {
            softly.assertThat(affectedRow).isEqualTo(1);
            softly.assertThatThrownBy(() -> productDao.getProductById(productId))
                    .isInstanceOf(DataAccessException.class);
        });
    }
}
