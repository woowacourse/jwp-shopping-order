package cart.application;

import cart.Fixture;
import cart.dao.CartItemDao;
import cart.domain.Order;
import cart.domain.OrderRepository;
import cart.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemDao cartItemDao;

    @DisplayName("주문을 처리하고 저장한다")
    @Test
    void add() {
        // given
        final OrderRequest orderRequest = new OrderRequest(0, List.of(1L));
        given(cartItemDao.findByMemberId(anyLong())).willReturn(List.of(Fixture.cartItem1));
        given(orderRepository.saveOrder(any(Order.class))).willReturn(1L);
        doNothing().when(cartItemDao).deleteById(anyLong());

        // when & then
        assertThat(orderService.add(Fixture.memberA, orderRequest)).isEqualTo(1L);
    }
}
