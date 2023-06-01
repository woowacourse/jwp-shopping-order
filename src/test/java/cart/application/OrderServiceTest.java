package cart.application;

import static cart.domain.fixture.OrderFixture.member;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import cart.domain.Order;
import cart.repository.CartItemFakeRepository;
import cart.repository.CartItemRepository;
import cart.repository.OrderFakeRepository;
import cart.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

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
        List<Long> cartItemIds = List.of(
                1L, 2L, 3L
        );

        //when
        Order order = orderService.createDraftOrder(member, cartItemIds);

        //then
        assertSoftly(softly -> {
                    softly.assertThat(order).isNotNull();
                }
        );
    }

}