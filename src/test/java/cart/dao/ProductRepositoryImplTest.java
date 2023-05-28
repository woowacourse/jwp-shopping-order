package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import cart.domain.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class ProductRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductRepository productRepositoryImpl;

    @BeforeEach
    void setUp() {
        productRepositoryImpl = new ProductRepositoryImpl(
                new ProductDao(jdbcTemplate)
        );
    }

    @Test
    void 상품_저장_테스트() {
        final Product product = new Product("testProductA", 3_000, "testImageUrl");

        productRepositoryImpl.saveProduct(product);

        final List<Product> allProducts = productRepositoryImpl.findAllProducts();
        assertThat(allProducts).hasSize(1);
        assertThat(allProducts.get(0).getName()).isEqualTo("testProductA");
    }

    @Test
    void 전체_상품_조회_테스트() {
        final Product productA = new Product("testProductA", 3_000, "testImageUrlA");
        final Product productB = new Product("testProductB", 2_000, "testImageUrlB");
        productRepositoryImpl.saveProduct(productA);
        productRepositoryImpl.saveProduct(productB);

        final List<Product> allProducts = productRepositoryImpl.findAllProducts();

        assertThat(allProducts).hasSize(2);
    }

    @Test
    void 식별자로_상품_조회_테스트() {
        final String testProductName = "testProductA";
        final Product productA = new Product(testProductName, 3_000, "testImageUrlA");
        final Long saveProductId = productRepositoryImpl.saveProduct(productA);

        final Product productById = productRepositoryImpl.findProductById(saveProductId);

        assertThat(productById.getName()).isEqualTo(testProductName);
    }

    @Test
    void 상품_수정_테스트() {
        final Product productA = new Product("testProductA", 3_000, "testImageUrlA");
        final Long saveProductId = productRepositoryImpl.saveProduct(productA);
        final String expectedName = "newProductName";
        final Product newProduct = new Product(expectedName, 2_000, "newTestImageUrl");

        productRepositoryImpl.updateProduct(saveProductId, newProduct);

        final Product product = productRepositoryImpl.findProductById(saveProductId);
        assertThat(product.getName()).isEqualTo(expectedName);
    }

    @Test
    void 상품_삭제_테스트() {
        final Product productA = new Product("testProductA", 3_000, "testImageUrlA");
        final Long saveProductId = productRepositoryImpl.saveProduct(productA);

        productRepositoryImpl.deleteProduct(saveProductId);

        final List<Product> allProducts = productRepositoryImpl.findAllProducts();
        assertThat(allProducts).isEmpty();
    }
}
