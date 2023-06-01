package cart.application;

import cart.dao.CartItemDao;
import cart.dao.OrderDao;
import cart.domain.*;
import cart.dto.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartItemDao cartItemDao;
    @Mock
    private OrderDao orderDao;

    @DisplayName("주문할 수 있다")
    @Test
    void order() {
        //given
        final Member member = new Member(1L, "abc@naver.com", "123", MemberGrade.GOLD);
        final OrderRequest orderRequest = new OrderRequest(List.of(1L));
        final Product product = new Product(1L, "예비군", 200000, "image");
        final CartItem cartItem = new CartItem(1L, 3, product, member);

        final Order order = new Order(1L, member, 160000, List.of(cartItem));

        when(cartItemDao.findAllByIds(List.of(1L))).thenReturn(List.of(cartItem));
        when(orderDao.save(any())).thenReturn(order);

        //when
        orderService.save(member, orderRequest);

        //then
        verify(orderDao, times(1)).save(any(Order.class));
    }
}
