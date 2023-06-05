package cart.repository;

import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.TWO_CHICKEN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.dao.CartItemDao;
import cart.exception.CartItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryTest {

    CartItemDao cartItemDao;
    CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        cartItemDao = mock(CartItemDao.class);
        cartItemRepository = new CartItemRepository(cartItemDao);
    }

    @Test
    @DisplayName("save는 이미 장바구니에 저장한 상품인 cartItem을 전달하면 예외가 발생한다.")
    void saveFailTest() {
        given(cartItemDao.isExistBy(anyLong(), anyLong())).willReturn(true);

        assertThatThrownBy(() -> cartItemRepository.save(MEMBER_A, TWO_CHICKEN))
                .isInstanceOf(CartItemException.AlreadyExist.class)
                .hasMessageContaining("의 장바구니에 이미 ");
    }
}
