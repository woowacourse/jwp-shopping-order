package cart.service;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture.쿠폰_발급;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.OrderItemFixture.상품_18900원_1개_주문;
import static cart.fixture.OrderItemFixture.상품_28900원_1개_주문;
import static cart.fixture.OrderItemFixture.상품_8900원_1개_주문;
import static cart.fixture.ProductFixture.상품_28900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.cart.CartItem;
import cart.domain.cart.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.dto.order.OrderItemResponse;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSaveRequest;
import cart.repository.CartItemRepository;
import cart.repository.CouponRepository;
import cart.repository.MemberRepository;
import cart.repository.OrderRepository;
import cart.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 상품을_주문한다() {
        // given
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_28900원);
        final Member member = memberRepository.save(사용자1);
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final Coupon coupon = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));
        final OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                List.of(cartItem1.getId(), cartItem2.getId()),
                coupon.getId()
        );

        // when
        final Long result = orderService.save(orderSaveRequest, member.getId());

        // then
        assertAll(
                () -> assertThat(orderRepository.findAllByMemberId(member.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member.getId())).hasSize(0),
                () -> assertThat(cartItemRepository.findAllByMemberId(member.getId())).hasSize(0),
                () -> assertThat(result).isPositive()
        );
    }

    @Test
    void 주문_전체_조회() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));
        coupon.use();
        couponRepository.update(coupon);
        final Order order1 = orderRepository.save(
                Order.of(coupon, member.getId(), List.of(상품_8900원_1개_주문, 상품_28900원_1개_주문))
        );
        final Order order2 = orderRepository.save(
                Order.of(Coupon.EMPTY, member.getId(), List.of(상품_18900원_1개_주문))
        );

        // when
        final List<OrderResponse> result = orderService.findAll(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(List.of(
                new OrderResponse(order1.getId(), 37800L, 2000L, 3000L, List.of(
                        new OrderItemResponse(null, "pizza1", 8900L, "pizza1.png", 1),
                        new OrderItemResponse(null, "pizza3", 28900L, "pizza3.png", 1)
                )),
                new OrderResponse(order2.getId(), 18900L, 0L, 3000L, List.of(
                        new OrderItemResponse(null, "pizza2", 18900L, "pizza2.png", 1)
                ))
        ));
    }

    @Test
    void 주문_단일_조회() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Order order = orderRepository.save(
                Order.of(Coupon.EMPTY, member.getId(), List.of(상품_8900원_1개_주문))
        );

        // when
        final OrderResponse result = orderService.findById(order.getId(), member.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(
                new OrderResponse(order.getId(), 8900L, 0L, 3000L, List.of(
                        new OrderItemResponse(null, "pizza1", 8900L, "pizza1.png", 1)
                ))
        );
    }
}
