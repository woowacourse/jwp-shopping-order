package cart.application;

import cart.domain.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 목록을 가져온다.")
    void getAllProductsTest() {
        // given
        when(productRepository.getAllProducts()).thenReturn(List.of(PRODUCT1, PRODUCT2, PRODUCT3));
        List<ProductResponse> expectResponse = List.of(PRODUCT_RESPONSE1, PRODUCT_RESPONSE2, PRODUCT_RESPONSE3);

        // when
        List<ProductResponse> response = productService.getAllProducts();

        // then
        assertThat(response).isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("ID에 해당하는 Product를 가져온다.")
    void getProductByIdTest() {
        // given
        Product product = PRODUCT1;
        Long idToFind = product.getId();
        when(productRepository.getProductById(idToFind)).thenReturn(product);
        ProductResponse expectResponse = PRODUCT_RESPONSE1;

        // when
        ProductResponse response = productService.getProductById(idToFind);

        // then
        assertThat(response).isEqualTo(expectResponse);
    }

    @Test
    @DisplayName("상품을 추가한다.")
    void createProductTest() {
        // given
        ProductRequest newProductRequest = NEW_PRODUCT_REQUEST;
        Product newProductToCreate = NEW_PRODUCT_TO_INSERT;
        Product productAfterCreate = NEW_PRODUCT;
        when(productRepository.createProduct(newProductToCreate)).thenReturn(productAfterCreate.getId());

        // when
        Long newProductId = productService.createProduct(newProductRequest);

        // then
        assertThat(newProductId).isEqualTo(productAfterCreate.getId());
    }

    @Test
    @DisplayName("상품 정보를 수정한다.")
    void updateProductTest() {
        // given
        ProductRequest updateProduct1Request = UPDATE_PRODUCT1_REQUEST;
        Product updateProduct = UPDATE_PRODUCT1;
        Long updateProductId = updateProduct.getId();
        doNothing().when(productRepository).updateProduct(updateProductId, updateProduct);

        // when, then
        assertThatNoException().isThrownBy(() -> productService.updateProduct(updateProductId, updateProduct1Request));
    }

    @Test
    @DisplayName("상품을 삭제한다.")
    void deleteProductTest() {
        // given
        Product productToDelete = PRODUCT1;
        Long productIdToDelete = productToDelete.getId();
        doNothing().when(productRepository).deleteProduct(productIdToDelete);

        // when, then
        assertThatNoException().isThrownBy(() -> productService.deleteProduct(productIdToDelete));
    }
}