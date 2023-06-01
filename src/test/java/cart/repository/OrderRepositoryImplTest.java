package cart.repository;

import static cart.fixture.CartItemsFixture.CART_ITEMS_1;
import static cart.fixture.OrderFixture.ORDER_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.dao.PaymentDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(OrderRepositoryImpl.class)
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private MemberDao memberDao;

    @MockBean
    private OrderDao orderDao;

    @MockBean
    private PaymentDao paymentDao;

    @MockBean
    private OrderItemDao orderItemDao;

    @MockBean
    private CartItemDao cartItemDao;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        willReturn(1L).given(orderDao).save(any());
        willReturn(1L).given(paymentDao).save(any(), anyLong(), anyLong());
        willDoNothing().given(orderItemDao).saveAll(any(), anyLong());
        willDoNothing().given(cartItemDao).deleteAll(any());

        // when
        Long orderId = orderRepository.createOrder(ORDER_1, CART_ITEMS_1);

        // then
        assertThat(orderId).isEqualTo(1L);
    }
}
