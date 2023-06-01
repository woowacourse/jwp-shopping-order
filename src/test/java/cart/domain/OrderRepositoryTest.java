package cart.domain;

import cart.Fixture;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    @InjectMocks
    private OrderRepository orderRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderItemDao orderItemDao;

    @DisplayName("주문 저장")
    @Test
    void saveOrder() {
        // given
        given(orderDao.insert(any(Order.class))).willReturn(1L);
        given(orderItemDao.insert(anyLong(), any(OrderItem.class))).willReturn(anyLong());

        // when & then
        assertThat(orderRepository.saveOrder(Fixture.order1)).isEqualTo(1L);
        verify(orderDao, times(1)).insert(any(Order.class));
        verify(orderItemDao, times(Fixture.order1.getOrderItems().size())).insert(anyLong(), any(OrderItem.class));
    }
}
