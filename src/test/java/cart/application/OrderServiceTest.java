package cart.application;

import static cart.domain.fixture.MemberFixture.memberWithId;
import static cart.exception.OrderException.EmptyItemInput;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.OrderItem;
import cart.exception.CartItemException;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    private List<Long> CART_ITEM_IDS;

    @BeforeEach
    void setUp() {
        final List<CartItem> cartItems = this.cartItemRepository.findByMember(memberWithId);
        this.CART_ITEM_IDS = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
    }

    @Test
    @DisplayName("CartItem 아이디 리스트로 Order 객체를 생성한다.")
    void createOrder() {
        //given
        //when
        final Order order = this.orderService.createDraftOrder(memberWithId, this.CART_ITEM_IDS);

        //then
        assertSoftly(softly -> softly.assertThat(order).isNotNull()
        );
    }

    @Test
    @DisplayName("CartItem 아이디 리스트로 Order 객체를 생성하고 저장한다.")
    void createOrderAndSave() {
        //given
        final List<OrderItem> orderItems = this.CART_ITEM_IDS.stream().map(this.cartItemRepository::findById)
                .map(cartItem -> OrderItem.from(cartItem.orElseThrow())).collect(Collectors.toList());
        final Order expected = new Order(memberWithId, orderItems);
        //when
        final Long createdOrderId = this.orderService.createOrderAndSave(memberWithId, this.CART_ITEM_IDS);

        //then
        assertSoftly(softly -> {
            final Optional<Order> createdOrder = this.orderRepository.findById(createdOrderId);
            softly.assertThat(createdOrder).isNotEmpty();
            softly.assertThat(createdOrder.get()).usingRecursiveComparison().ignoringFields("id", "orderTime")
                    .isEqualTo(expected);
            this.CART_ITEM_IDS.forEach(
                    cartItemId -> softly.assertThat(this.cartItemRepository.findById(cartItemId)).isEmpty());
        });
    }

    @Test
    @DisplayName("Order 아이디로 Order를 반환한다.")
    void retrieveOrderById() {
        //given
        final Long orderId = this.orderService.createOrderAndSave(memberWithId, this.CART_ITEM_IDS);
        //when
        final Order order = this.orderService.retrieveOrderById(orderId);
        //then
        assertThat(order.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("모든 Order를 반환한다.")
    void retrieveAllOrders() {
        //given
        final Long order1 = this.orderService.createOrderAndSave(memberWithId, List.of(1L));
        final Long order2 = this.orderService.createOrderAndSave(memberWithId, List.of(2L));
        final List<Long> orderIds = List.of(order1, order2);
        //when
        final List<Order> orders = this.orderService.retrieveMemberOrders(memberWithId);
        //then
        orders.forEach(order -> assertThat(order.getId()).isIn(orderIds));
    }

    @Test
    @DisplayName("CartItem 아이디 리스트가 비어있을 때 예외처리한다.")
    void createOrderAndSave_fail() {
        //given
        //when
        //then
        Assertions.assertThatThrownBy(() -> this.orderService.createDraftOrder(memberWithId, List.of()))
                .isInstanceOf(EmptyItemInput.class);
    }

    @Test
    @DisplayName("요청 회원의 CartItem이 아닐 때 예외처리한다.")
    void createOrderAndSave_fail_() {
        //given
        final Member unauthorizedMember = new Member(10000000L, "iam@othermember.com", "1234");
        final CartItem cartItem = this.cartItemRepository.findById(this.CART_ITEM_IDS.get(0)).orElseThrow();
        //when
        //then
        assertThatThrownBy(() -> this.orderService.createOrderAndSave(unauthorizedMember, List.of(cartItem.getId())))
                .isInstanceOf(CartItemException.IllegalMember.class);
    }
}