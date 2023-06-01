package cart.step2.order.persist;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.step2.order.domain.Order;
import cart.step2.order.domain.OrderEntity;
import cart.step2.order.domain.OrderItem;
import cart.step2.order.domain.OrderItemEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryMapperTest {

    @InjectMocks
    private OrderRepositoryMapper orderRepositoryMapper;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Mock
    private ProductDao productDao;

    @DisplayName("memberId와 일치하는 order를 모두 조회하고 연관있는 객체들을 주입한다.")
    @Test
    void findAllByMemberId() {
        // given
        Long memberId = 1L;
        OrderEntity orderEntity = OrderEntity.of(1L, 100, 1L, memberId, LocalDateTime.now());
        OrderItemEntity orderItemEntity = OrderItemEntity.of(1L, 1L, 1L, 3);
        Product product = new Product("MOCK PRODUCT", 1000, "BEBE MOCK IMAGE");

        when(orderDao.findAllByMemberId(memberId)).thenReturn(Collections.singletonList(orderEntity));
        when(orderItemDao.findByOrderId(orderEntity.getId())).thenReturn(Collections.singletonList(orderItemEntity));
        when(productDao.getProductById(orderItemEntity.getProductId())).thenReturn(product);

        // when
        List<Order> orders = orderRepositoryMapper.findAllByMemberId(memberId);

        // then
        assertThat(orders).hasSize(1);

        Order order = orders.get(0);
        Assertions.assertAll(
                () -> assertThat(order.getId()).isEqualTo(orderEntity.getId()),
                () -> assertThat(order.getPrice()).isEqualTo(orderEntity.getPrice()),
                () -> assertThat(order.getCouponId()).isEqualTo(orderEntity.getCouponId()),
                () -> assertThat(order.getMemberId()).isEqualTo(orderEntity.getMemberId()),
                () -> assertThat(order.getDate()).isEqualTo(orderEntity.getDate())
        );

        List<OrderItem> orderItems = order.getOrderItems();
        OrderItem orderItem = orderItems.get(0);
        Assertions.assertAll(
                () -> assertThat(orderItems).hasSize(1),
                () -> assertThat(orderItem.getId()).isEqualTo(orderItemEntity.getId()),
                () -> assertThat(orderItem.getProduct()).isEqualTo(product),
                () -> assertThat(orderItem.getQuantity()).isEqualTo(orderItemEntity.getQuantity())
        );

        verify(orderDao, times(1)).findAllByMemberId(memberId);
        verify(orderItemDao, times(1)).findByOrderId(orderEntity.getId());
        verify(productDao, times(1)).getProductById(orderItemEntity.getProductId());
    }

}
