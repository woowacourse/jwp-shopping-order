package cart.repository;

import static cart.fixture.CouponFixture.배달비_3천원_할인_쿠폰;
import static cart.fixture.CouponFixture.쿠폰_발급;
import static cart.fixture.MemberFixture.사용자1;
import static cart.fixture.OrderItemFixture.상품_28900원_1개_주문;
import static cart.fixture.OrderItemFixture.상품_8900원_1개_주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.VO.Money;
import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
import cart.domain.order.Order;
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
    private CouponRepository couponRepository;

    @Test
    void 주문을_저장한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Order order = Order.of(Coupon.EMPTY, member.getId(), List.of(상품_8900원_1개_주문, 상품_28900원_1개_주문));

        // when
        orderRepository.save(order);

        // then
        assertAll(
                () -> assertThat(orderRepository.findAllByMemberId(member.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByUsedAndMemberId(false, member.getId())).isEmpty()
        );
    }

    @Test
    void 사용자의_주문을_전부_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Order order1 = orderRepository.save(
                Order.of(Coupon.EMPTY, member.getId(), List.of(상품_8900원_1개_주문, 상품_28900원_1개_주문)));
        final Order order2 = orderRepository.save(
                Order.of(Coupon.EMPTY, member.getId(), List.of(상품_8900원_1개_주문, 상품_28900원_1개_주문)));

        // when
        final List<Order> result = orderRepository.findAllByMemberId(member.getId());

        // then
        assertThat(result).extracting(Order::getId).isEqualTo(List.of(order1.getId(), order2.getId()));
    }

    @Test
    void 주문을_단일_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon = couponRepository.save(쿠폰_발급(배달비_3천원_할인_쿠폰, member.getId()));
        final Order order = orderRepository.save(Order.of(coupon, member.getId(), List.of(상품_8900원_1개_주문)));

        // when
        final Order result = orderRepository.findById(order.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.calculateTotalPrice()).isEqualTo(Money.from(8900L)),
                () -> assertThat(result.getId()).isEqualTo(order.getId())
        );
    }
}
