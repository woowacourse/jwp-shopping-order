package cart.application.service;

import cart.application.repository.ProductRepository;
import cart.ui.product.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductWriteServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductWriteService productWriteService;

    @Test
    @DisplayName("상품을 정상적으로 저장한다.")
    void createProductTest() {
        given(productRepository.createProduct(any()))
                .willReturn(1L);

        assertDoesNotThrow(() -> productWriteService.createProduct(new ProductRequest("레오배변패드", 10000, "https://www.google.com/aclk?sa=l&ai=DChcSEwichomlqpz_AhUWMGAKHVkwBDgYABABGgJ0bQ&sig=AOD64_0741-6emp177CIBjUFFPDDyQIwwA&adurl&ctype=5&ved=2ahUKEwjEzIClqpz_AhXVEIgKHRt9DlAQvhd6BAgBEEw")));
    }

}
