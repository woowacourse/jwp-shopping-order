package cart.application;

import static cart.domain.fixture.OrderFixture.member;
import static cart.domain.fixture.OrderFixture.order;
import static cart.exception.OrderException.EmptyItemInput;
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
        Order expected = order;

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
    @DisplayName("CartItem 아이디 리스트가 비어있을 때 예외처리한다.")
    void createOrderAndSave_fail() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> orderService.createDraftOrder(member, List.of()))
                .isInstanceOf(EmptyItemInput.class);
    }

}