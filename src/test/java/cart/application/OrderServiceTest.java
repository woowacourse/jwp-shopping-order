package cart.application;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.*;
import cart.dto.*;
import cart.exception.OrderException;
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
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private CartItemDao cartItemDao;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PointRepository pointRepository;

    private OrderService orderService;
    private Member member;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(productDao, cartItemDao, orderRepository, pointRepository,
                new OrderPage(10), new OrderPointAccumulationPolicy(new OrderPointExpirePolicy()));
        member = new Member(1L, "kong", "123");
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

        OrderPageResponse orderPageResponse = orderService.findOrders(member, 1);

        assertAll(
                () -> assertThat(orderPageResponse.getTotalPages()).isEqualTo(1),
                () -> assertThat(orderPageResponse.getCurrentPage()).isEqualTo(1),
                () -> assertThat(orderPageResponse.getContents().size()).isEqualTo(2)
        );
    }

    @DisplayName("주문을 할 수 있다. - 포인트를 사용하지 않는 경우")
    @Test
    void saveOrder_success_1() {
        List<OrderItem> orderItems = List.of(new OrderItem(product1, 3, 30000),
                new OrderItem(product2, 2, 40000));

        Point point1 = Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30));
        Point point2 = Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31));
        Point point3 = Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31));

        Order order = new Order(new Points(new ArrayList<>()), orderItems);

        when(pointRepository.findUsablePointsByMemberId(1L)).thenReturn(
                new Points(List.of(point1, point2, point3)));
        when(productDao.findAllByIds(List.of(1L, 2L))).thenReturn(List.of(product1, product2));
        when(orderRepository.save(1L, order)).thenReturn(1L);

        assertThatCode(() ->
                orderService.saveOrder(member, new OrderRequest(0,
                        List.of(new ProductOrderRequest(1L, 3),
                                new ProductOrderRequest(2L, 2)))))
                .doesNotThrowAnyException();
    }

    @DisplayName("주문을 할 수 있다. - 포인트를 사용하는 경우")
    @Test
    void saveOrder_success_2() {
        List<OrderItem> orderItems = List.of(new OrderItem(product1, 3, 30000),
                new OrderItem(product2, 2, 40000));

        Point point1 = Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30));
        Point point2 = Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31));
        Point point3 = Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31));

        Order order = new Order(new Points(List.of(point3, point1)), orderItems);

        when(pointRepository.findUsablePointsByMemberId(1L)).thenReturn(
                new Points(List.of(point1, point2, point3)));
        when(productDao.findAllByIds(List.of(1L, 2L))).thenReturn(List.of(product1, product2));
        when(orderRepository.save(1L, order)).thenReturn(1L);

        assertThatCode(() ->
                        orderService.saveOrder(member, new OrderRequest(4000,
                                List.of(new ProductOrderRequest(1L, 3),
                                        new ProductOrderRequest(2L, 2)))))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용 가능한 포인트를 초과하는 경우 포인트를 사용할 수 없다.")
    @Test
    void saveOrder_fail() {
        Point point1 = Point.of(1L, 1000, "테스트", LocalDate.of(2023, 3, 5), LocalDate.of(2023, 6, 30));
        Point point2 = Point.of(2L, 2000, "테스트", LocalDate.of(2023, 5, 5), LocalDate.of(2023, 8, 31));
        Point point3 = Point.of(3L, 3000, "테스트", LocalDate.of(2023, 2, 5), LocalDate.of(2023, 5, 31));

        when(pointRepository.findUsablePointsByMemberId(1L)).thenReturn(new Points(List.of(point1, point2, point3)));

        assertThatThrownBy(() -> orderService.saveOrder(member, new OrderRequest(7000,
                List.of(new ProductOrderRequest(1L, 3),
                        new ProductOrderRequest(2L, 2)))))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining("사용가능한 포인트보다 더 많은 포인트를 사용할 수 없습니다.");
    }

    @DisplayName("주문 번호를 전달했을 때 해당 주문에 대한 상세한 정보를 전달받을 수 있다.")
    @Test
    void findDetailOrder() {
        List<OrderItem> orderItems = List.of(new OrderItem(product1, 3, 30000),
                new OrderItem(product2, 2, 40000));
        Point usedPoint = Point.of(10000, "구매 포인트", LocalDate.now());
        Point savedPoint = Point.from(4800);
        Points points = new Points(List.of(usedPoint));
        Order order = new Order(1L, OrderStatus.PENDING, points, orderItems, LocalDate.now());

        when(orderRepository.findOrder(1L, 1L)).thenReturn(order);
        when(pointRepository.findBy(1L, 1L)).thenReturn(savedPoint);

        ProductOrderResponse productOrderResponse1 = new ProductOrderResponse(3, ProductResponse.of(product1));
        ProductOrderResponse productOrderResponse2 = new ProductOrderResponse(2, ProductResponse.of(product2));
        List<ProductOrderResponse> products = new ArrayList<>(List.of(productOrderResponse1, productOrderResponse2));
        DetailOrderResponse expected = new DetailOrderResponse(1L, LocalDate.now(), 60000, usedPoint.getValue(), savedPoint.getValue(), products);

        DetailOrderResponse detailOrder = orderService.findDetailOrder(member, 1L);

        assertThat(detailOrder).isEqualTo(expected);
    }
}
