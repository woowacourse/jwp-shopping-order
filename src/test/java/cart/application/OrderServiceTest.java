package cart.application;

import cart.domain.*;
import cart.dto.OrderPageResponse;
import cart.repository.OrderRepository;
import cart.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static cart.ProductFixture.product1;
import static cart.ProductFixture.product2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PointRepository pointRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, pointRepository, new OrderPage(10));
    }

    @DisplayName("한 유저가 주문한 정보에 대해 한 페이지당 10개의 주문을 반환받을 수 있다.")
    @Test
    void findOrders_1() {
        List<OrderItem> orderItems = List.of(new OrderItem(product1, 3, 30000),
                new OrderItem(product2, 2, 40000));
        Points points = new Points(List.of(Point.of(10000, "구매 포인트", LocalDate.now())));
        Order order1 = new Order(1L, OrderStatus.PENDING, points, orderItems, LocalDate.now());
        Order order2 = new Order(1L, OrderStatus.PENDING, new Points(new ArrayList<>()), orderItems, LocalDate.now());

        when(orderRepository.findAllByMemberId(1L)).thenReturn(List.of(order1, order2));

        OrderPageResponse orderPageResponse = orderService.findOrders(new Member(1L, "kong", "123"), 1);

        assertAll(
                () -> assertThat(orderPageResponse.getTotalPages()).isEqualTo(1),
                () -> assertThat(orderPageResponse.getCurrentPage()).isEqualTo(1),
                () -> assertThat(orderPageResponse.getContents().size()).isEqualTo(2)
        );
    }
}
