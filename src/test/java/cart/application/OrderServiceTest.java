package cart.application;

import static cart.domain.fixture.OrderFixture.member;
import static cart.domain.fixture.OrderFixture.orderWithoutId;
import static cart.exception.OrderException.EmptyItemInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.domain.Order;
import cart.repository.CartItemFakeRepository;
import cart.repository.CartItemRepository;
import cart.repository.OrderFakeRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    public static final List<Long> CART_ITEM_IDS = List.of(
            1L, 2L, 3L
    );
    private OrderService orderService;

    private OrderRepository orderRepository;

    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderFakeRepository();

        cartItemRepository = new CartItemFakeRepository();
        orderService = new OrderService(orderRepository, cartItemRepository);
    }

    @Test
    @DisplayName("CartItem 아이디 리스트로 Order 객체를 생성한다.")
    void createOrder() {
        //given

        //when
        Order order = orderService.createDraftOrder(member, CART_ITEM_IDS);

        //then
        assertSoftly(softly -> softly.assertThat(order).isNotNull()
        );
    }

    @Test
    @DisplayName("CartItem 아이디 리스트로 Order 객체를 생성하고 저장한다.")
    void createOrderAndSave() {
        //given
        Order expected = orderWithoutId;

        //when
        Long createdOrderId = orderService.createOrderAndSave(member, CART_ITEM_IDS);

        //then
        assertSoftly(softly -> {
            Optional<Order> createdOrder = orderRepository.findById(createdOrderId);
            softly.assertThat(createdOrder).isNotEmpty();
            softly.assertThat(createdOrder.get()).usingRecursiveComparison().ignoringFields("id", "orderTime")
                    .isEqualTo(expected);
            CART_ITEM_IDS.forEach(cartItemId -> softly.assertThat(cartItemRepository.findById(cartItemId)).isEmpty());
        });
    }

    @Test
    @DisplayName("Order 아이디로 Order를 반환한다.")
    void retrieveOrderById() {
        //given
        Long orderId = orderService.createOrderAndSave(member, CART_ITEM_IDS);
        //when
        Order order = orderService.retrieveOrderById(orderId);
        //then
        assertThat(order.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("모든 Order를 반환한다.")
    void retrieveAllOrders() {
        //given
        Long order1 = orderService.createOrderAndSave(member, List.of(1L, 2L));
        Long order2 = orderService.createOrderAndSave(member, List.of(3L));
        List<Long> orderIds = List.of(order1, order2);
        //when
        List<Order> orders = orderService.retrieveAllOrders();
        //then
        orders.forEach(order -> assertThat(order.getId()).isIn(orderIds));
    }

    @Test
    @DisplayName("CartItem 아이디 리스트가 비어있을 때 예외처리한다.")
    void createOrderAndSave_fail() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> orderService.createDraftOrder(member, List.of()))
                .isInstanceOf(EmptyItemInput.class);
    }

}