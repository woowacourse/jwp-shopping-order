package cart.repository;

import static cart.fixture.DomainFixture.CHICKEN;
import static cart.fixture.DomainFixture.MEMBER_A;
import static cart.fixture.DomainFixture.PIZZA;
import static cart.fixture.ValueFixture.DEFAULT_SIZE;
import static cart.fixture.ValueFixture.LAST_ID_OF_FIRST_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import cart.dao.OrderDao;
import cart.dao.OrderItemDao;
import cart.domain.Money;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.entity.OrderEntity;
import cart.entity.OrderItemEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryTest {

    OrderDao orderDao;
    OrderItemDao orderItemDao;
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        orderItemDao = mock(OrderItemDao.class);
        orderRepository = new OrderRepository(orderDao, orderItemDao);
    }

    @Test
    @DisplayName("save는 order를 저장하면 해당 주문을 저장하고 id를 반환한다.")
    void saveSuccessTest() {
        long orderId = 1L;
        given(orderDao.save(any(OrderEntity.class))).willReturn(orderId);


        List<OrderItem> orderItems = List.of(new OrderItem(CHICKEN, 2), new OrderItem(PIZZA, 1));
        Money earnedPoints = new Money(3_000);
        Money usedPoints = new Money(1_000);

        Order order = Order.of(
                1L,
                orderItems,
                MEMBER_A.getId(),
                usedPoints,
                earnedPoints,
                LocalDateTime.now()
        );

        Order actual = orderRepository.save(MEMBER_A, order);

        assertThat(actual.getId()).isEqualTo(orderId);
    }

    @Nested
    @DisplayName("주문 조회 테스트")
    class SelectOrderTest extends SelectOrderFixture {

        OrderEntity orderEntity;
        OrderItemEntity orderItemEntity;

        @BeforeEach
        void setUp() {
            orderEntity = new OrderEntity(orderId, MEMBER_A.getId(), earnedPoints, usedPoints, totalPrice, payPrice,
                    LocalDateTime.now());

            orderItemEntity = new OrderItemEntity(1L, orderId, productId, productName, productPrice,
                    productImageUrl, quantity);
        }

        @Test
        @DisplayName("findById는 조회할 주문 ID를 전달하면 그에 맞는 Order를 반환한다.")
        void findByIdSuccessTest() {
            given(orderDao.findByOrderId(anyLong())).willReturn(Optional.of(orderEntity));
            given(orderItemDao.findByOrderId(anyLong())).willReturn(List.of(orderItemEntity));

            Optional<Order> actual = orderRepository.findByOrderId(orderId);

            assertAll(
                    () -> assertThat(actual).isPresent(),
                    () -> assertThat(actual.get().getId()).isEqualTo(orderId),
                    () -> assertThat(actual.get().calculateTotalPrice()).isEqualTo(new Money(totalPrice)),
                    () -> assertThat(actual.get().calculatePayPrice()).isEqualTo(new Money(payPrice)),
                    () -> assertThat(actual.get().getEarnedPoints()).isEqualTo(new Money(earnedPoints)),
                    () -> assertThat(actual.get().getUsedPoints()).isEqualTo(new Money(usedPoints)),
                    () -> assertThat(actual.get().getOrderItems()).hasSize(1),
                    () -> assertThat(actual.get().getOrderItems().get(0).getProduct().getId()).isEqualTo(productId),
                    () -> assertThat(actual.get().getOrderItems().get(0).getProduct().getName()).isEqualTo(productName),
                    () -> assertThat(actual.get().getOrderItems().get(0).getProduct().getPrice()).isEqualTo(productPrice),
                    () -> assertThat(actual.get().getOrderItems().get(0).getProduct().getImageUrl()).isEqualTo(productImageUrl),
                    () -> assertThat(actual.get().getOrderItems().get(0).getQuantity()).isEqualTo(quantity)
            );
        }

        @Test
        @DisplayName("findByMemberIdAndLastOrderIdAndSize는 호출하면 해당 회원의 모든 Order를 반환한다.")
        void findByMemberIdAndLastOrderIdAndSizeSuccessTest() {
            given(orderDao.findByMemberIdAndLastOrderIdAndSize(anyLong(), anyLong(), anyInt()))
                    .willReturn(List.of(orderEntity));
            given(orderItemDao.findByOrderId(anyLong())).willReturn(List.of(orderItemEntity));

            List<Order> actual = orderRepository.findByMemberAndLastOrderIdAndSize(MEMBER_A, LAST_ID_OF_FIRST_PAGE,
                    DEFAULT_SIZE);

            assertAll(
                    () -> assertThat(actual).hasSize(1),
                    () -> assertThat(actual.get(0).getId()).isEqualTo(orderId),
                    () -> assertThat(actual.get(0).calculateTotalPrice().getValue()).isEqualTo(totalPrice),
                    () -> assertThat(actual.get(0).calculatePayPrice().getValue()).isEqualTo(payPrice),
                    () -> assertThat(actual.get(0).getEarnedPoints().getValue()).isEqualTo(earnedPoints),
                    () -> assertThat(actual.get(0).getUsedPoints().getValue()).isEqualTo(usedPoints),
                    () -> assertThat(actual.get(0).getOrderDate()).isNotNull(),
                    () -> assertThat(actual.get(0).getOrderItems()).hasSize(1),
                    () -> assertThat(actual.get(0).getOrderItems().get(0).getProduct().getId()).isEqualTo(productId),
                    () -> assertThat(actual.get(0).getOrderItems().get(0).getProduct().getName()).isEqualTo(productName),
                    () -> assertThat(actual.get(0).getOrderItems().get(0).getProduct().getPrice()).isEqualTo(productPrice),
                    () -> assertThat(actual.get(0).getOrderItems().get(0).getProduct().getImageUrl()).isEqualTo(productImageUrl),
                    () -> assertThat(actual.get(0).getOrderItems().get(0).getQuantity()).isEqualTo(quantity)
               );
        }
    }

    private static class SelectOrderFixture {

        long orderId = 1L;
        int earnedPoints = 3_000;
        int usedPoints = 1_000;
        int totalPrice = 30_000;
        int payPrice = 29_000;
        long productId = 1L;
        String productName = CHICKEN.getName();
        int productPrice = 10_000;
        String productImageUrl = CHICKEN.getImageUrl();
        int quantity = 3;
    }
}
