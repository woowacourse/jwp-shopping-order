package cart.application;

import cart.domain.product.Product;
import cart.dto.request.ProductRequest;
import cart.dto.response.ProductResponse;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static cart.fixture.ProductFixture.지구;
import static cart.fixture.ProductFixture.화성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 모두_조회한다() {
        List<Product> products = List.of(지구, 화성);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductResponse> response = productService.getAllProducts();

        assertThat(response).hasSize(2);
    }

    @Test
    void 아이디로_조회한다() {
        when(productRepository.findById(1L)).thenReturn(지구);

        ProductResponse response = productService.getProductById(1L);

        assertThat(response.getName()).isEqualTo("지구");
    }

    @Test
    void 저장한다() {
        ProductRequest request = new ProductRequest("저장", 1000, "주소");

        productService.createProduct(request);

        verify(productRepository, times(1)).create(any(Product.class));
    }

    @Test
    void 수정한다() {
        Product 수정지구 = new Product(지구.getId(), 지구.getName(), 1200, 지구.getImageUrl());
        when(productRepository.findById(1L)).thenReturn(지구);

        ProductRequest request = new ProductRequest(
                수정지구.getName(),
                수정지구.getPrice(),
                수정지구.getImageUrl()
        );
        productService.updateProduct(1L, request);


        verify(productRepository, times(1)).update(수정지구);
    }

    @Test
    void 삭제한다() {
        when(productRepository.findById(1L)).thenReturn(지구);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(지구);
    }
}
