package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.product.Product;
import cart.repository.ProductRepository;
import cart.test.ServiceTest;
import cart.ui.controller.dto.request.ProductRequest;
import cart.ui.controller.dto.response.ProductResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("getAllProducts 메서드는 모든 상품 정보를 조회한다.")
    void getAllProducts() {
        Product productA = new Product("치킨", 10000, "http://chicken.com");
        Product productB = new Product("피자", 13000, "http://pizza.com");
        Long productIdA = productRepository.createProduct(productA);
        Long productIdB = productRepository.createProduct(productB);

        List<ProductResponse> result = productService.getAllProducts();

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isEqualTo(productIdA),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(ProductResponse.from(productA)),
                () -> assertThat(result.get(1).getId()).isEqualTo(productIdB),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(ProductResponse.from(productB))
        );

    }

    @Test
    @DisplayName("getProductById 메서드 ID에 해당하는 상품 정보를 조회한다.")
    void getProductById() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);

        ProductResponse result = productService.getProductById(1L);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(savedProductId),
                () -> assertThat(result).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(ProductResponse.from(product))
        );
    }

    @Test
    @DisplayName("updatedProduct 메서드는 상품을 업데이트한다.")
    void updateProduct() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);
        ProductRequest request = new ProductRequest("피자", 13000, "http://pizza.com");

        productService.updateProduct(savedProductId, request);

        ProductResponse result = productService.getProductById(savedProductId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(savedProductId),
                () -> assertThat(result.getName()).isEqualTo(request.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(request.getPrice()),
                () -> assertThat(result.getImageUrl()).isEqualTo(request.getImageUrl())
        );
    }

    @Test
    @DisplayName("deleteProduct 메서드는 상품을 삭제한다.")
    void deleteProduct() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);

        productService.deleteProduct(savedProductId);

        List<ProductResponse> result = productService.getAllProducts();
        assertThat(result).isEmpty();
    }
}
