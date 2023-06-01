package cart.application;

import cart.Fixture;
import cart.dao.MemberDao;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderRepository;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private MemberDao memberDao;

    @DisplayName("주문을 처리하고 저장한다")
    @Test
    void add() {
        // given
        final OrderRequest orderRequest = new OrderRequest(0, List.of(1L));
        given(cartItemService.findSelectedCartItems(any(Member.class), any())).willReturn(List.of(Fixture.cartItem1));
        given(orderRepository.saveOrder(any(Order.class))).willReturn(1L);
        doNothing().when(cartItemService).remove(any(Member.class), anyLong());
        doNothing().when(memberDao).updateMemberPoint(any(Member.class));

        // when & then
        assertThat(orderService.add(Fixture.memberA, orderRequest)).isEqualTo(1L);
        verify(orderRepository, times(1)).saveOrder(any(Order.class));
        verify(cartItemService, times(orderRequest.getCartItemIds().size())).remove(any(Member.class), anyLong());
        verify(memberDao, times(1)).updateMemberPoint(any(Member.class));
    }

    @DisplayName("ID로 주문을 조회한다")
    @Test
    void findById() {
        // given
        given(orderRepository.findById(1L)).willReturn(Fixture.order1);

        // when
        final OrderResponse actual = orderService.findById(Fixture.memberA, 1L);

        // then
        assertThat(actual.getId()).isEqualTo(Fixture.order1.getId());
        verify(orderRepository, times(1)).findById(anyLong());
    }
}
