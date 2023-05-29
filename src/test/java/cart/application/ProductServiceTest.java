package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import cart.dto.ProductStockResponse;
import cart.exception.ProductNotFoundException;
import cart.fixture.ProductRequestFixture;
import cart.fixture.ProductStockFixture;
import cart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void createProduct() {
        given(productRepository.createProduct(any(), any())).willReturn(ProductStockFixture.CHICKEN);
        final ProductStockResponse result = productService.createProduct(ProductRequestFixture.CHICKEN);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("치킨"),
                () -> assertThat(result.getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getStock()).isEqualTo(10)
        );
    }

    @Test
    void getAllProductStockResponses() {
        given(productRepository.getAllProductStocks()).willReturn(List.of(
                ProductStockFixture.CHICKEN,
                ProductStockFixture.PIZZA
        ));
        final List<ProductStockResponse> result = productService.getAllProductStockResponses();
        assertAll(
                () -> assertThat(result.get(0).getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getName()).isEqualTo("치킨"),
                () -> assertThat(result.get(0).getPrice()).isEqualTo(10_000),
                () -> assertThat(result.get(0).getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.get(0).getStock()).isEqualTo(10),
                () -> assertThat(result.get(1).getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getName()).isEqualTo("피자"),
                () -> assertThat(result.get(1).getPrice()).isEqualTo(15_000),
                () -> assertThat(result.get(1).getImageUrl()).isEqualTo("http://example.com/pizza.jpg"),
                () -> assertThat(result.get(1).getStock()).isEqualTo(10)
        );
    }

    @DisplayName("데이터가 없을 때 빈 리스트를 반환한다.")
    @Test
    void getAllProductStockResponsesWhenEmpty() {
        given(productRepository.getAllProductStocks()).willReturn(List.of());
        final List<ProductStockResponse> result = productService.getAllProductStockResponses();
        assertThat(result).isEmpty();
    }

    @Test
    void getProductStockResponseById() {
        given(productRepository.getProductStockById(anyLong())).willReturn(Optional.of(ProductStockFixture.CHICKEN));
        final ProductStockResponse result = productService.getProductStockResponseById(1L);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("치킨"),
                () -> assertThat(result.getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getStock()).isEqualTo(10)
        );
    }

    @DisplayName("상품이 없으면 예외를 발생시킨다.")
    @Test
    void getProductStockResponseByNoExistId() {
        given(productRepository.getProductStockById(anyLong())).willReturn(Optional.empty());
        assertThatThrownBy(() -> productService.getProductStockResponseById(1L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("id: 1 에 해당하는 상품을 찾을 수 없습니다.");
    }

    @Test
    void updateProduct() {
        given(productRepository.updateProductStock(any(), any())).willReturn(Optional.of(ProductStockFixture.CHICKEN));
        final ProductStockResponse result = productService.updateProduct(1L, ProductRequestFixture.CHICKEN);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo("치킨"),
                () -> assertThat(result.getPrice()).isEqualTo(10_000),
                () -> assertThat(result.getImageUrl()).isEqualTo("http://example.com/chicken.jpg"),
                () -> assertThat(result.getStock()).isEqualTo(10)
        );
    }

    @DisplayName("업데이트 할 상품이 없으면 예외를 발생시킨다.")
    @Test
    void updateProductWithNoExistId() {
        given(productRepository.updateProductStock(any(), any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> productService.updateProduct(1L, ProductRequestFixture.CHICKEN))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("id: 1 에 해당하는 상품을 찾을 수 없습니다.");
    }

    @Test
    void deleteProduct() {
        doNothing().when(productRepository).deleteProductById(anyLong());
        assertThatCode(() -> productService.deleteProduct(1L))
                .doesNotThrowAnyException();
    }
}
