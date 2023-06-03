package cart.repository;

import static cart.fixture.CouponFixture.배달비_3천원_할인_쿠폰;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.ProductFixture.상품_28900원;
import static cart.fixture.ProductFixture.상품_8900원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.VO.Money;
import cart.domain.cart.CartItem;
import cart.domain.cart.MemberCoupon;
import cart.domain.cart.Order;
import cart.domain.cart.Product;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Test
    void 주문을_저장한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_28900원);
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final Order order = Order.of(null, member.getId(), List.of(cartItem1, cartItem2));

        // when
        orderRepository.save(order);

        // then
        assertAll(
                () -> assertThat(orderRepository.findAllByMemberId(member.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByMemberId(member.getId())).isEmpty()
        );
    }

    @Test
    void 사용자의_주문을_전부_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product1 = productRepository.save(상품_8900원);
        final Product product2 = productRepository.save(상품_28900원);
        final CartItem cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        final CartItem cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        final Order order1 = orderRepository.save(Order.of(null, member.getId(), List.of(cartItem1, cartItem2)));
        final Order order2 = orderRepository.save(Order.of(null, member.getId(), List.of(cartItem1, cartItem2)));

        // when
        final List<Order> result = orderRepository.findAllByMemberId(member.getId());

        // then
        assertThat(result).extracting(Order::getId).isEqualTo(List.of(order1.getId(), order2.getId()));
    }

    @Test
    void 주문을_단일_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Product product = productRepository.save(상품_8900원);
        final CartItem cartItem = cartItemRepository.save(new CartItem(member.getId(), product));
        final Coupon coupon = couponRepository.save(배달비_3천원_할인_쿠폰);
        final MemberCoupon memberCoupon = memberCouponRepository.save(new MemberCoupon(member.getId(), coupon));
        final Order order = orderRepository.save(Order.of(memberCoupon, member.getId(), List.of(cartItem)));

        // when
        final Order result = orderRepository.findById(order.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.calculateTotalPrice()).isEqualTo(Money.from(8900L)),
                () -> assertThat(result.getId()).isEqualTo(order.getId())
        );
    }
}
