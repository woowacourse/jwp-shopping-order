package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import cart.domain.Product;
import cart.dto.ProductRequest;
import cart.dto.ProductResponse;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품을 생성한다.")
    void createProduct() {
        //given
        given(productRepository.save(any(Product.class))).willReturn(1L);
        final ProductRequest request = new ProductRequest("치킨", 30000, "example.com/chicken");

        //when
        final Long id = productService.createProduct(request);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void findAllProducts() {
        //given
        final List<Product> expected = List.of(
                new Product(1L, "치킨", 30000, "example.com/chicken"),
                new Product(2L, "피자", 20000, "example.com/pizza")
        );
        given(productRepository.findAll()).willReturn(expected);

        //when
        final List<ProductResponse> actual = productService.findAllProducts();

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("상품의 아이디로 상품을 조회한다.")
    void findProductById() {
        //given
        final Product expected = new Product(1L, "치킨", 30000, "example.com/chicken");
        given(productRepository.findById(eq(1L))).willReturn(expected);

        //when
        final ProductResponse actual = productService.findProductById(1L);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("상품 정보를 수정한다")
    void updateProduct() {
        //given
        final ProductRequest request = new ProductRequest("치킨", 30000, "example.com/chicken");
        final Product expected = new Product(1L, "치킨", 30000, "example.com/chicken");
        willDoNothing().given(productRepository).update(eq(expected));

        //when
        //then
        assertThatNoException().isThrownBy(() -> productService.updateProduct(1L, request));
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProduct() {
        //given
        willDoNothing().given(productRepository).deleteById(eq(1L));

        //when
        //then
        assertThatNoException().isThrownBy(() -> productService.deleteProduct(1L));
    }
}
