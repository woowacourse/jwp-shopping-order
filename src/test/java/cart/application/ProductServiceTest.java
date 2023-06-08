package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cart.domain.Product;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.exception.ProductException.ProductNotExistException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @DisplayName("모든 상품을 조회한다.")
    @Test
    void getAllProducts() {
        //given
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        when(productRepository.getAllProducts()).thenReturn(List.of(product));

        //when
        final List<ProductResponse> allProducts = productService.getAllProducts();

        //then
        assertAll(
            () -> assertThat(allProducts).hasSize(1),
            () -> assertThat(allProducts.get(0)).usingRecursiveComparison().isEqualTo(product)
        );
    }

    @DisplayName("해당 ID의 상품을 조회한다.")
    @Test
    void getProductById() {
        //given
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        when(productRepository.getProductById(any())).thenReturn(Optional.of(product));

        //when
        final ProductResponse findProduct = productService.getProductById(1L);

        //then
        assertThat(findProduct).usingRecursiveComparison().isEqualTo(product);
    }

    @DisplayName("조회하려는 ID의 상품이 없을 경우 예외를 발생한다.")
    @Test
    void getProductById_notExist() {
        //given
        when(productRepository.getProductById(any())).thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> productService.getProductById(1L))
            .isInstanceOf(ProductNotExistException.class);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        //given
        final ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        final ProductRequest productRequest = new ProductRequest("product", 1000, "imageUrl");
        final Product product = new Product("product", 1000, "imageUrl");
        when(productRepository.createProduct(any())).thenReturn(product);

        //when
        productService.createProduct(productRequest);

        //then
        verify(productRepository).createProduct(productArgumentCaptor.capture());
        final Product captured = productArgumentCaptor.getValue();
        assertThat(captured).usingRecursiveComparison().isEqualTo(product);
    }

    @DisplayName("상품을 업데이트한다.")
    @Test
    void updateProduct() {
        //given
        final ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        final ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        final ProductRequest productRequest = new ProductRequest("product", 1000, "imageUrl");
        final Product product = new Product("product", 1000, "imageUrl");
        doNothing().when(productRepository).updateProduct(any(), any());

        //when
        productService.updateProduct(1L, productRequest);

        //then
        verify(productRepository).updateProduct(longArgumentCaptor.capture(), productArgumentCaptor.capture());
        final Long capturedId = longArgumentCaptor.getValue();
        final Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(1L);
        assertThat(capturedProduct).usingRecursiveComparison().isEqualTo(product);
    }

    @DisplayName("상품을 삭제한다.")
    @Test
    void deleteProduct() {
        //given
        final ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(productRepository).deleteProduct(any());

        //when
        productService.deleteProduct(1L);

        //then
        verify(productRepository).deleteProduct(longArgumentCaptor.capture());
        final Long capturedLong = longArgumentCaptor.getValue();
        assertThat(capturedLong).isEqualTo(1L);
    }
}
