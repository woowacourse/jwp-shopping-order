package cart.application.service;

import cart.application.repository.product.ProductRepository;
import cart.application.service.product.ProductReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static cart.fixture.ProductFixture.통구이;
import static cart.fixture.ProductFixture.꼬리요리;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductReadServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductReadService productReadService;

    @Test
    @DisplayName("정상적인 전체 상품 조회 테스트")
    void getAllProductsTest() {
        given(productRepository.findAll())
                .willReturn(List.of(통구이, 꼬리요리));

        assertDoesNotThrow(() -> productReadService.getAllProducts());
    }

    @Test
    @DisplayName("정상적인 특정 상품 조회 테스트")
    void getProductByIdTest() {
        given(productRepository.findById(any()))
                .willReturn(Optional.of(꼬리요리));

        assertDoesNotThrow(() -> productReadService.getProductById(any()));
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외 처리 테스트")
    void getProductByIdExceptionTest() {
        given(productRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> productReadService.getProductById(any()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("일치하는 상품이 없습니다.");
    }

}
