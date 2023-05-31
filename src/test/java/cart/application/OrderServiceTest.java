package cart.application;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Member;
import cart.dto.request.OrderRequestDto;
import cart.exception.CartItemNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartItemDao cartItemDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("존재하지 않는 카트 아이템에 대한 요청을 보내면 예외가 발생한다.")
    @Test
    void order_invalid_notExistCartItem() {
        //given
        given(cartItemDao.findByMemberId(anyLong()))
                .willReturn(List.of(
                        new CartItem(1L, 1, null, null),
                        new CartItem(2L, 2, null, null)
                ));

        final Member member = new Member(1L, "email", "password");
        final OrderRequestDto orderRequestDto = new OrderRequestDto(List.of(1, 3), 1000, 1);

        //when, then
        assertThatThrownBy(() -> orderService.order(member, orderRequestDto))
                .isInstanceOf(CartItemNotFoundException.class);
    }
}
