package cart.service.product;

import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.dto.sale.SaleProductRequest;
import cart.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void find_all_products() {
        // given
        Product product = createProduct();
        List<Product> products = List.of(product);
        given(productRepository.findAllProducts()).willReturn(products);

        // when
        List<ProductResponse> result = productService.findAllProducts();

        // then
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(products.get(0).getId()),
                () -> assertThat(result.get(0).getName()).isEqualTo(products.get(0).getName())
        );
    }

    @DisplayName("상품 한 개를 조회한다.")
    @Test
    void find_product_by_id() {
        // given
        Long id = 1L;
        Product product = createProduct();
        given(productRepository.findProductById(id)).willReturn(product);

        // when
        ProductResponse result = productService.findProductById(1L);

        // then
        assertThat(result.getId()).isEqualTo(id);
    }

    @DisplayName("상품을 업데이트한다.")
    @Test
    void update_product() {
        // given
        Long id = 1L;
        ProductRequest req = new ProductRequest("치킨", 10000, "img");

        // when
        productService.updateProduct(id, req);

        // then
        verify(productRepository).updateProduct(any(), any(Product.class));
    }

    @DisplayName("상품을 삭제한다.")
    @Test
    void delete_product() {
        // given
        Long id = 1L;

        // when
        productService.deleteProduct(id);

        // then
        verify(productRepository).deleteProduct(id);
    }

    @DisplayName("상품에 세일을 적용한다.")
    @Test
    void apply_sale_on_product() {
        // given
        Long id = 1L;
        SaleProductRequest req = new SaleProductRequest(10);
        Product product = createProduct();
        given(productRepository.findProductById(id)).willReturn(product);

        // when
        long resultId = productService.applySale(id, req);

        // then
        assertThat(product.isOnSale()).isTrue();
    }

    @DisplayName("상품에 세일을 없앤다.")
    @Test
    void un_apply_sale_on_product() {
        // given
        Long id = 1L;
        Product product = createProduct();
        given(productRepository.findProductById(id)).willReturn(product);

        // when
        productService.unapplySale(id);

        // then
        assertThat(product.isOnSale()).isFalse();
    }
}
