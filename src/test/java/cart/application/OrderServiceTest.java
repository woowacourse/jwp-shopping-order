package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;

import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.Quantity;
import cart.domain.member.Email;
import cart.domain.member.Member;
import cart.domain.member.Password;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.Orders;
import cart.domain.product.ImageUrl;
import cart.domain.product.Name;
import cart.domain.product.Price;
import cart.domain.product.Product;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.repository.CartRepository;
import cart.repository.OrderRepository;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, cartRepository);
    }

    @Test
    void 장바구니_목록을_받아_주문이_들어가고_장바구니에서_해당_목록을_삭제한다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("image1.com"), new Price(1000));
        final Product product2 = new Product(1L, new Name("상품2"), new ImageUrl("image2.com"), new Price(2000));

        final Cart cart = new Cart(List.of(
                new CartItem(1L, new Quantity(4), product1, member),
                new CartItem(2L, new Quantity(3), product2, member)
        ));

        final OrderRequest request = new OrderRequest(List.of(1L, 2L));
        given(cartRepository.findByIds(request.getCartItemIds())).willReturn(cart);
        given(orderRepository.save(any())).willReturn(1L);

        // when
        final Long orderId = orderService.addOrder(member, request);

        // then
        InOrder inOrder = inOrder(orderRepository, cartRepository);
        inOrder.verify(orderRepository).save(any(Order.class));
        inOrder.verify(cartRepository).deleteCart(cart);
        assertThat(orderId).isEqualTo(1L);
    }

    @Test
    void 고객은_주문번호로_주문내역을_찾을_수_있다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final Long orderId = 1L;
        final Product product = new Product(1L, new Name("상품1"), new ImageUrl("image1.com"), new Price(1000));
        final List<OrderItem> orderItems = List.of(new OrderItem(1L, new Quantity(5), product));
        final Order order = new Order(1L, member, new Timestamp(System.currentTimeMillis()), orderItems);

        given(orderRepository.findByOrderId(orderId)).willReturn(order);

        // when
        final OrderResponse orderResponse = orderService.findByOrderId(member, 1L);

        // then
        assertThat(orderResponse).usingRecursiveComparison().isEqualTo(OrderResponse.from(order));
    }

    @Test
    void 고객이_요청한_주문번호로_찾은_주문이_고객의_것이_아니면_예외를_발생한다() {
        // given
        final Member member1 = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final Member member2 = new Member(2L, new Email("b@b.com"), new Password("1234"));
        final Long orderId = 1L;
        final Product product = new Product(1L, new Name("상품1"), new ImageUrl("image1.com"), new Price(1000));
        final List<OrderItem> orderItems = List.of(new OrderItem(1L, new Quantity(5), product));
        final Order order = new Order(1L, member2, new Timestamp(System.currentTimeMillis()), orderItems);

        given(orderRepository.findByOrderId(orderId)).willReturn(order);

        // expect
        assertThatThrownBy(() -> orderService.findByOrderId(member1, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 멤버의 주문이 아닙니다.");
    }

    @Test
    void 고객의_전체_주문_내역을_조회할_수_있다() {
        // given
        final Member member = new Member(1L, new Email("a@a.com"), new Password("1234"));
        final Long orderId1 = 1L;
        final Long orderId2 = 2L;

        final Product product1 = new Product(1L, new Name("상품1"), new ImageUrl("image1.com"), new Price(1000));
        final Product product2 = new Product(2L, new Name("상품2"), new ImageUrl("image2.com"), new Price(1000));

        final List<OrderItem> orderItems1 = List.of(new OrderItem(1L, new Quantity(5), product1));
        final List<OrderItem> orderItems2 = List.of(new OrderItem(1L, new Quantity(5), product1));

        final Order order1 = new Order(1L, member, new Timestamp(System.currentTimeMillis()), orderItems1);
        final Order order2 = new Order(2L, member, new Timestamp(System.currentTimeMillis()), orderItems2);

        final Orders orders = new Orders(List.of(order1, order2));
        given(orderRepository.findByMember(member)).willReturn(orders);

        // when
        final List<OrderResponse> ordersResponse = orderService.findMemberOrders(member);

        // then
        assertThat(ordersResponse).usingRecursiveComparison().isEqualTo(
                List.of(OrderResponse.from(order1), OrderResponse.from(order2))
        );
    }
}
