package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.product.Product;
import cart.exception.ProductException;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("getAllProducts 메서드는 모든 상품을 조회한다.")
    void getAllProducts() {
        Product productA = new Product("치킨", 10000, "http://chicken.com");
        Product productB = new Product("샐러드", 20000, "http://salad.com");
        Product productC = new Product("피자", 13000, "http://pizza.com");
        Long productIdA = productRepository.createProduct(productA);
        Long productIdB = productRepository.createProduct(productB);
        Long productIdC = productRepository.createProduct(productC);

        List<Product> result = productRepository.getAllProducts();

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getId()).isEqualTo(productIdA),
                () -> assertThat(result.get(0)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(productA),
                () -> assertThat(result.get(1).getId()).isEqualTo(productIdB),
                () -> assertThat(result.get(1)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(productB),
                () -> assertThat(result.get(2).getId()).isEqualTo(productIdC),
                () -> assertThat(result.get(2)).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(productC)
        );
    }

    @Nested
    @DisplayName("getProductById 메서드는 ")
    class GetProductById {

        @Test
        @DisplayName("ID의 상품이 존재하지 않으면 예외를 던진다.")
        void emptyProduct() {
            assertThatThrownBy(() -> productRepository.getProductById(-1L))
                    .isInstanceOf(ProductException.class)
                    .hasMessage("해당 상품이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("ID의 상품이 존재하면 상품을 반환한다.")
        void getProduct() {
            Product product = new Product("치킨", 10000, "http://chicken.com");
            Long savedProductId = productRepository.createProduct(product);

            Product result = productRepository.getProductById(savedProductId);

            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(savedProductId),
                    () -> assertThat(result).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(product)
            );
        }
    }

    @Test
    @DisplayName("updateProduct 메서드는 상품 정보를 수정한다.")
    void updateProduct() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);
        Product updateProduct = new Product(savedProductId, "피자", 13000, "http://pizza.com");

        productRepository.updateProduct(updateProduct);

        Product result = productRepository.getProductById(savedProductId);
        assertThat(result).usingRecursiveComparison().isEqualTo(updateProduct);
    }

    @Test
    @DisplayName("deleteProduct 메서드는 상품을 삭제한다.")
    void deleteProduct() {
        Product product = new Product("치킨", 10000, "http://chicken.com");
        Long savedProductId = productRepository.createProduct(product);

        productRepository.deleteProduct(savedProductId);

        List<Product> result = productRepository.getAllProducts();
        assertThat(result).isEmpty();
    }
}
