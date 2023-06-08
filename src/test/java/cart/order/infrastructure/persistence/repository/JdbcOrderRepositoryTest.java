package cart.order.infrastructure.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.infrastructure.persistence.dao.OrderDao;
import cart.order.infrastructure.persistence.dao.OrderItemDao;
import cart.order.infrastructure.persistence.entity.OrderEntity;
import cart.order.infrastructure.persistence.entity.OrderItemEntity;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("JdbcOrderRepository 은(는)")
class JdbcOrderRepositoryTest {

    @InjectMocks
    private JdbcOrderRepository jdbcOrderRepository;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderItemDao orderItemDao;

    @Test
    void 주문을_저장한다() {
        // given
        Order order = new Order(1L,
                new OrderItem(10, 1L, "말랑", 1000, 1000, "image"),
                new OrderItem(20, 2L, "코코닥", 2000, 1000, "image")
        );
        given(orderDao.save(any()))
                .willReturn(1L);

        // when
        Long id = jdbcOrderRepository.save(order);

        // then
        assertThat(id).isEqualTo(1L);
        then(orderItemDao).should(times(1))
                .saveAll(any());
    }

    @Test
    void 특정_회원의_주문을_조회한다() {
        // given
        OrderEntity orderEntity1 = new OrderEntity(1L, 1L);
        OrderItemEntity orderItemEntity1 = new OrderItemEntity(1L, 10, 1L, "말랑", 1000, 1000, "image", 1L);
        OrderItemEntity orderItemEntity2 = new OrderItemEntity(2L, 20, 2L, "코코닥", 2000, 1000, "image", 1L);
        OrderEntity orderEntity2 = new OrderEntity(2L, 1L);
        OrderItemEntity orderItemEntity3 = new OrderItemEntity(3L, 10, 1L, "말랑", 1000, 1000, "image", 2L);
        OrderItemEntity orderItemEntity4 = new OrderItemEntity(4L, 20, 2L, "코코닥", 2000, 1000, "image", 2L);

        given(orderDao.findAllByMemberId(1L))
                .willReturn(List.of(orderEntity1, orderEntity2));
        given(orderItemDao.findAllByOrderId(1L))
                .willReturn(List.of(orderItemEntity1, orderItemEntity2));
        given(orderItemDao.findAllByOrderId(2L))
                .willReturn(List.of(orderItemEntity3, orderItemEntity4));

        // when
        List<Order> orders = jdbcOrderRepository.findAllByMemberId(1L);

        // then
        Order order1 = new Order(1L, 1L,
                new OrderItem(1L, 10, 1L, "말랑", 1000, 1000, "image"),
                new OrderItem(2L, 20, 2L, "코코닥", 2000, 1000, "image")
        );
        Order order2 = new Order(2L, 1L,
                new OrderItem(3L, 10, 1L, "말랑", 1000, 1000, "image"),
                new OrderItem(4L, 20, 2L, "코코닥", 2000, 1000, "image")
        );
        List<Order> expected = List.of(order1, order2);
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
