package cart.product.application;

import cart.product.domain.Product;
import cart.product.domain.ProductTest;
import cart.product.dto.ProductRequest;
import cart.product.dto.ProductResponse;
import cart.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void 모든_상품을_조회한다() {
        // given
        given(productRepository.getAllProducts()).willReturn(List.of(PRODUCT_FIRST, PRODUCT_SECOND));

        // when
        final List<ProductResponse> products = productService.getAllProducts();

        // then
        assertThat(products).hasSize(2);
    }

    @Test
    void productId로_물품을_조회한다() {
        // given
        given(productRepository.getProductById(1L)).willReturn(PRODUCT_FIRST);

        // when
        final ProductResponse productResponse = productService.getProductById(1L);

        // then
        assertThat(productResponse.getId()).isOne();
    }

    @Test
    void 물품을_추가한다() {
        // given
        final ProductRequest productRequest = new ProductRequest("사과", 1000L, "aa", 10.0, true);
        final Product product = new Product(
                productRequest.getName(),
                productRequest.getPrice(),
                productRequest.getImageUrl(),
                productRequest.getPointRatio(),
                productRequest.getPointAvailable()
        );
        given(productRepository.createProduct(product)).willReturn(1L);

        // when
        final Long productId = productService.createProduct(productRequest);

        // then
        assertThat(productId).isOne();
    }

    @Test
    void 물품을_수정한다() {
        // given
        final ProductRequest productRequest = new ProductRequest("사과", 1000L, "aa", 10.0, true);

        // expect
        assertThatNoException()
                .isThrownBy(() -> productService.updateProduct(1L, productRequest));
    }

    @Test
    void 물품을_삭제한다() {
        // expect
        assertThatNoException()
                .isThrownBy(() -> productService.deleteProduct(1L));
    }
}
