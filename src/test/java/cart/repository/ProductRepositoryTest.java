package cart.repository;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.SALAD;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.dao.ProductDao;
import cart.exception.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    ProductDao productDao;
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productDao = mock(ProductDao.class);
        productRepository = new ProductRepository(productDao);
    }

    @Test
    @DisplayName("update는 존재하지 않는 상품 ID를 전달하면 예외가 발생한다.")
    void updateFailWithNotExistProductId() {
        given(productDao.isExistBy(anyLong())).willReturn(false);

        assertThatThrownBy(() -> productRepository.update(CHICKEN.getId(), SALAD))
                .isInstanceOf(ProductException.NotFound.class)
                .hasMessageContaining("해당 상품을 찾을 수 없습니다 : ");
    }
}
