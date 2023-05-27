package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberCouponDao;
import cart.domain.CartItem;
import cart.domain.Item;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.common.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeliveryFeeDiscountPolicy;
import cart.domain.coupon.NoneDiscountCondition;
import cart.entity.MemberCouponEntity;
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
    private MemberCouponDao memberCouponDao;

    @Test
    void 주문을_저장한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Product product1 = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product product2 = productRepository.save(new Product("고양이", "cat.jpg", 1000000L));
        final Item cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final Item cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Coupon coupon = couponRepository.save(new Coupon(
                1L,
                "배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new NoneDiscountCondition()
        ));
        memberCouponDao.insert(new MemberCouponEntity(coupon.getId(), member.getId(), false));
        final Order order = new Order(coupon, member.getId(), List.of(cartItem1, cartItem2));

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
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Product product1 = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Product product2 = productRepository.save(new Product("고양이", "cat.jpg", 1000000L));
        final Item cartItem1 = cartItemRepository.save(new CartItem(member, product1));
        final Item cartItem2 = cartItemRepository.save(new CartItem(member, product2));
        final Coupon coupon = couponRepository.save(new Coupon(
                1L,
                "배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new NoneDiscountCondition()
        ));
        final Order order1 = orderRepository.save(new Order(coupon, member.getId(), List.of(cartItem1, cartItem2)));
        final Order order2 = orderRepository.save(new Order(coupon, member.getId(), List.of(cartItem1, cartItem2)));

        // when
        final List<Order> result = orderRepository.findAllByMemberId(member.getId());

        // then
        assertThat(result).extracting(Order::getId).isEqualTo(List.of(order1.getId(), order2.getId()));
    }

    @Test
    void 주문을_단일_조회한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Product product = productRepository.save(new Product("허브티", "tea.jpg", 1000L));
        final Item cartItem = cartItemRepository.save(new CartItem(member, product));
        final Coupon coupon = couponRepository.save(new Coupon(
                1L,
                "배달비 할인 쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new NoneDiscountCondition()
        ));
        final Order order = orderRepository.save(new Order(coupon, member.getId(), List.of(cartItem)));

        // when
        final Order result = orderRepository.findById(order.getId(), member.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.calculateTotalPrice()).isEqualTo(Money.from(1000L)),
                () -> assertThat(result.getId()).isEqualTo(order.getId())
        );
    }
}
