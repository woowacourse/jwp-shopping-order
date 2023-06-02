package cart.domain;

import cart.Fixture;
import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.entity.OrderEntity;
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

    @DisplayName("주문 ID로 조회")
    @Test
    void findById() {
        // given
        final OrderEntity orderEntity = new OrderEntity(1L, 1L, 1000, 900, 100, 100, "2023-05-29 08:55:03");
        given(orderDao.findById(1L)).willReturn(orderEntity);
        given(orderItemDao.findByOrderId(1L)).willReturn(List.of(Fixture.orderItem1));

        // when
        final Order actual = orderRepository.findById(1L);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(Fixture.order1);
        verify(orderDao, times(1)).findById(anyLong());
        verify(orderItemDao, times(1)).findByOrderId(anyLong());
    }

    @DisplayName("주문 내역 조회")
    @Test
    void findPageByIndex() {
        // given
        final List<OrderEntity> orderEntities = List.of(new OrderEntity(1L, 1L, 1000, 900, 100, 100, "2023-05-29 08:55:03"));
        given(orderDao.findByIndexRange(anyLong(), anyLong())).willReturn(orderEntities);
        given(orderItemDao.findByOrderId(anyLong())).willReturn(List.of(Fixture.orderItem1));

        // when
        final List<Order> actual = orderRepository.findPageByIndex(1L, 0L);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(List.of(Fixture.order1));
        verify(orderDao, times(1)).findByIndexRange(anyLong(), anyLong());
        verify(orderItemDao, times(orderEntities.size())).findByOrderId(anyLong());

    }
}
