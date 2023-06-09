package cart.repository;

import cart.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBProductRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new DBProductRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 상품을 가져온다.")
    void getAllProductsTest() {
        // given
        List<Product> allProducts = List.of(PRODUCT1, PRODUCT2, PRODUCT3);

        // when
        List<Product> findAllProducts = productRepository.getAllProducts();

        // then
        assertThat(findAllProducts).isEqualTo(allProducts);
    }

    @Test
    @DisplayName("ID에 해당하는 상품을 가져온다.")
    void getProductByIdTest() {
        // given
        Product product = PRODUCT1;
        Long product1Id = product.getId();

        // when
        Product findProduct = productRepository.getProductById(product1Id);

        // then
        assertThat(findProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("상품을 저장한다.")
    void createProductTest() {
        // given
        Product newProductToInsert = NEW_PRODUCT_TO_INSERT;
        Product newProductAfterCreate = NEW_PRODUCT;

        // when
        productRepository.createProduct(newProductToInsert);

        // then
        assertThat(productRepository.getAllProducts()).contains(newProductAfterCreate);
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProductTest() {
        // given
        Product updateProduct = UPDATE_PRODUCT1;
        Long updateProductId = updateProduct.getId();

        // when
        productRepository.updateProduct(updateProductId, updateProduct);

        // then
        assertThat(productRepository.getProductById(updateProductId)).isEqualTo(updateProduct);
    }

    @Test
    @DisplayName("ID에 해당하는 상품을 삭제한다.")
    void deleteProductTest() {
        // given
        Product productToDelete = PRODUCT1;
        Long deleteProductId = productToDelete.getId();
        int productsCountBeforeDelete = productRepository.getAllProducts().size();
        int productsCountAfterDelete = productsCountBeforeDelete - 1;

        // when
        productRepository.deleteProduct(deleteProductId);

        // then
        assertThat(productRepository.getAllProducts()).hasSize(productsCountAfterDelete);
    }
}