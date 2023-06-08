package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Product;
import cart.domain.delivery.DeliveryPolicy;
import cart.domain.discount.DiscountPolicy;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.order.OrderItems;
import cart.domain.order.OrderPrice;
import cart.domain.respository.cartitem.CartItemRepository;
import cart.domain.respository.member.MemberRepository;
import cart.domain.respository.order.OrderRepository;
import cart.domain.respository.orderitem.OrderItemRepository;
import cart.domain.respository.product.ProductRepository;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderResponse;
import cart.dto.response.OrdersResponse;
import cart.exception.MemberNotExistException;
import cart.exception.ProductException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.orderService = new OrderService(orderRepository, memberRepository, productRepository, cartItemRepository
            , orderItemRepository, new TestDiscount(), new TestDelivery());
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        //given
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Member member = new Member(1L, "email", "password");
        final Order persistedOrder = Order.persisted(1L, member,
            new OrderItems(List.of(OrderItem.notPersisted(product, 10))),
            OrderPrice.of(1000L, new TestDiscount(), new TestDelivery()), LocalDateTime.now());

        when(memberRepository.getMemberById(any())).thenReturn(Optional.of(member));
        when(productRepository.getProductById(any())).thenReturn(Optional.of(product));
        when(cartItemRepository.findByMemberId(any()))
            .thenReturn(List.of(new CartItem(1L, 10, product, member)));
        when(orderRepository.insert(any(), any())).thenReturn(persistedOrder);

        //when
        final OrderResponse orderResponse = orderService.createOrder(1L,
            new OrderRequest(List.of(new OrderItemRequest(1L, 10)), LocalDateTime.now()));

        //then
        assertAll(
            () -> assertThat(orderResponse.getOrderId()).isEqualTo(1L),
            () -> assertThat(orderResponse.getItems()).hasSize(1),
            () -> assertThat(orderResponse.getProductPrice()).isEqualTo(10000L),
            () -> assertThat(orderResponse.getDiscountPrice()).isEqualTo(0L),
            () -> assertThat(orderResponse.getDeliveryFee()).isEqualTo(0L),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(10000L)
        );
    }

    @DisplayName("주문을 생성 시 멤버가 존재하지 않을 경우 오류를 던진다.")
    @Test
    void createOrder_notExistMember() {
        //given
        when(memberRepository.getMemberById(any())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(1L,
                new OrderRequest(List.of(new OrderItemRequest(1L, 10)), LocalDateTime.now())))
            .isInstanceOf(MemberNotExistException.class);
    }

    @DisplayName("주문을 생성시 상품이 존재하지 않는 경우 오류를 던진다.")
    @Test
    void createOrder_notExistProduct() {
        //given
        final Member member = new Member(1L, "email", "password");

        when(memberRepository.getMemberById(any())).thenReturn(Optional.of(member));
        when(productRepository.getProductById(any())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderService.createOrder(1L,
                new OrderRequest(List.of(new OrderItemRequest(1L, 10)), LocalDateTime.now())))
            .isInstanceOf(ProductException.ProductNotExistException.class);
    }

    @DisplayName("해당 ID의 주문을 조회한다.")
    @Test
    void getOrderById() {
        //given
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Member member = new Member(1L, "email", "password");
        final Order persistedOrder = Order.persisted(1L, member,
            new OrderItems(List.of(OrderItem.notPersisted(product, 10))),
            OrderPrice.of(10000L, new TestDiscount(), new TestDelivery()), LocalDateTime.now());

        when(orderRepository.findByOrderId(any())).thenReturn(persistedOrder);

        //when
        final OrderResponse orderResponse = orderService.getOrderById(1L);

        //then
        assertAll(
            () -> assertThat(orderResponse.getOrderId()).isEqualTo(1L),
            () -> assertThat(orderResponse.getItems()).hasSize(1),
            () -> assertThat(orderResponse.getProductPrice()).isEqualTo(10000L),
            () -> assertThat(orderResponse.getDiscountPrice()).isEqualTo(0L),
            () -> assertThat(orderResponse.getDeliveryFee()).isEqualTo(0L),
            () -> assertThat(orderResponse.getTotalPrice()).isEqualTo(10000L)
        );
    }

    @DisplayName("해당 멤버의 모든 주문을 조회한다.")
    @Test
    void getOrderByMemberId() {
        //given
        final Product product = new Product(1L, "product", 1000, "imageUrl");
        final Member member = new Member(1L, "email", "password");
        final Order persistedOrder = Order.persisted(1L, member,
            new OrderItems(List.of(OrderItem.notPersisted(product, 10))),
            OrderPrice.of(10000L, new TestDiscount(), new TestDelivery()), LocalDateTime.now());

        when(orderRepository.findAllByMemberId(any())).thenReturn(List.of(persistedOrder));

        //when
        final OrdersResponse ordersResponse = orderService.getOrderByMemberId(1L);

        //then
        assertAll(
            () -> assertThat(ordersResponse.getOrders()).hasSize(1),
            () -> assertThat(ordersResponse.getOrders().get(0).getOrderId()).isEqualTo(1L),
            () -> assertThat(ordersResponse.getOrders().get(0).getItems()).hasSize(1),
            () -> assertThat(ordersResponse.getOrders().get(0).getProductPrice()).isEqualTo(10000L),
            () -> assertThat(ordersResponse.getOrders().get(0).getDiscountPrice()).isEqualTo(0L),
            () -> assertThat(ordersResponse.getOrders().get(0).getDeliveryFee()).isEqualTo(0L),
            () -> assertThat(ordersResponse.getOrders().get(0).getTotalPrice()).isEqualTo(10000L)
        );
    }

    private static class TestDiscount implements DiscountPolicy {

        @Override
        public Long calculate(final Long price) {
            return 0L;
        }
    }

    private static class TestDelivery implements DeliveryPolicy {

        @Override
        public Long getDeliveryFee(final Long productPrice) {
            return 0L;
        }
    }
}
