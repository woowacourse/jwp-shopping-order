package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import cart.dao.CartItemDao;
import cart.dao.MemberDao;
import cart.dao.ProductDao;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderItem;
import cart.domain.price.FixedPercentPointPolicy;
import cart.domain.price.PointPolicy;
import cart.domain.product.Product;
import cart.dto.request.OrderItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.OrderDetailResponse;
import cart.exception.NumberRangeException;
import cart.exception.PointNotEnoughException;
import cart.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductDao productDao;

    @Mock
    CartItemDao cartItemDao;

    @Mock
    MemberDao memberDao;

    @Spy
    PointPolicy pointPolicy = new FixedPercentPointPolicy(10);

    @Test
    void 회원의_주문이_생성되어야_한다() {
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(1L, 3),
                new OrderItemRequest(2L, 5)
        ), 500L);
        Member member = new Member(1L, "a@a.com", "123456", 1000);
        Product productA = new Product(1L, "상품A", 1000, "http://image.com/image.png");
        Product productB = new Product(2L, "상품B", 2000, "http://image.com/image.png");
        Order order = new Order(1L, member, List.of(
                new OrderItem(1L, productA, 3, 3000),
                new OrderItem(2L, productB, 5, 10000)
        ), 500, LocalDateTime.now());
        given(productDao.findById(1L))
                .willReturn(Optional.of(productA));
        given(productDao.findById(2L))
                .willReturn(Optional.of(productB));
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);
        given(orderRepository.findById(1L))
                .willReturn(order);
        given(memberDao.findById(1L))
                .willReturn(Optional.of(member));

        Long orderId = orderService.createOrder(orderRequest, 1L);
        OrderDetailResponse orderDetailResponse = orderService.findOrderById(orderId, 1L);

        assertAll(
                () -> assertThat(orderDetailResponse.getOrderId()).isEqualTo(orderId),
                () -> assertThat(orderDetailResponse.getTotalPrice()).isEqualTo(13000),
                () -> assertThat(orderDetailResponse.getSpendPoint()).isEqualTo(500),
                () -> assertThat(orderDetailResponse.getSpendPrice()).isEqualTo(12500)
        );
    }

    @Test
    void 주문시_포인트를_사용하면_사용한_포인트는_차감되고_결제_금액의_일부가_포인트로_전환된다() {
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(1L, 3),
                new OrderItemRequest(2L, 5)
        ), 1000L);
        Member member = new Member(1L, "a@a.com", "123456", 1000);
        Product productA = new Product(1L, "상품A", 1000, "http://image.com/image.png");
        Product productB = new Product(2L, "상품B", 2000, "http://image.com/image.png");
        Order order = new Order(1L, member, List.of(
                new OrderItem(1L, productA, 3, 3000),
                new OrderItem(2L, productB, 5, 10000)
        ), 1000, LocalDateTime.now());
        given(productDao.findById(1L))
                .willReturn(Optional.of(productA));
        given(productDao.findById(2L))
                .willReturn(Optional.of(productB));
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);
        given(memberDao.findById(1L))
                .willReturn(Optional.of(member));

        orderService.createOrder(orderRequest, 1L);

        assertThat(member.getPoint().getAmount()).isEqualTo(1200);
    }

    @Test
    void 주문시_가지고_있는_포인트보다_더_많이_사용하려고_하면_예외가_발생한다() {
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(1L, 3)
        ), 1001L);
        Member member = new Member(1L, "a@a.com", "123456", 1000);
        Product productA = new Product(1L, "상품A", 2000, "http://image.com/image.png");
        given(productDao.findById(1L))
                .willReturn(Optional.of(productA));
        given(memberDao.findById(1L))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> orderService.createOrder(orderRequest, 1L))
                .isInstanceOf(PointNotEnoughException.class)
                .hasMessage("포인트가 부족합니다.");
    }

    @Test
    void 주문시_결제_금액보다_더_많은_포인트를_사용하면_예외가_발생한다() {
        OrderRequest orderRequest = new OrderRequest(List.of(
                new OrderItemRequest(1L, 3)
        ), 3001L);
        Member member = new Member(1L, "a@a.com", "123456", 10000);
        Product productA = new Product(1L, "상품A", 1000, "http://image.com/image.png");
        given(productDao.findById(1L))
                .willReturn(Optional.of(productA));
        given(memberDao.findById(1L))
                .willReturn(Optional.of(member));

        assertThatThrownBy(() -> orderService.createOrder(orderRequest, 1L))
                .isInstanceOf(NumberRangeException.class)
                .hasMessage("포인트는 총 주문 가격보다 클 수 없습니다.");
    }
}
