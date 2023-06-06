package cart.repository;

import cart.domain.CartItem;
import cart.domain.Money;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.domain.order.Order;
import cart.test.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RepositoryTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    private Member member;
    private Product product1;
    private Product product2;
    private Product product3;
    private CartItem cartItem1;
    private CartItem cartItem2;
    private CartItem cartItem3;
    private Coupon coupon;
    private MemberCoupon memberCoupon;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("pizza1@pizza.com", "password1"));
        product1 = productRepository.save(new Product("치즈피자1", "1.jpg", new Money(8900L)));
        product2 = productRepository.save(new Product("치즈피자2", "2.jpg", new Money(9900L)));
        product3 = productRepository.save(new Product("치즈피자3", "3.jpg", new Money(10900L)));
        cartItem1 = cartItemRepository.save(new CartItem(member.getId(), product1));
        cartItem2 = cartItemRepository.save(new CartItem(member.getId(), product2));
        cartItem3 = cartItemRepository.save(new CartItem(member.getId(), product3));
        coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), BigDecimal.valueOf(3000L), new Money(30000L)));
        memberCoupon = new MemberCoupon(member, coupon, false);
    }

    @Test
    void 주문을_저장한다() {
        // given
        final Order order = Order.createFromCartItems(List.of(cartItem1, cartItem2), new Money(3000L), memberCoupon, member.getId());

        // when
        final Order save = orderRepository.save(order);

        // then
        final Order result = orderRepository.findById(save.getId()).get();
        assertThat(result).isEqualTo(save);
    }

    @Test
    void 주문을_조회한다() {
        // given
        final Order order = Order.createFromCartItems(List.of(cartItem1, cartItem2), new Money(3000L), memberCoupon, member.getId());
        final Order save = orderRepository.save(order);

        // when
        final Order result = orderRepository.findById(save.getId()).get();

        // then
        assertThat(result).isEqualTo(save);
    }

    @Test
    void 사용자의_주문을_조회한다() {
        // given
        final Order order = Order.createFromCartItems(List.of(cartItem1, cartItem2), new Money(3000L), memberCoupon, member.getId());
        final Order save = orderRepository.save(order);

        // when
        final List<Order> result = orderRepository.findByMemberId(member.getId());

        // then
        assertAll(
                () -> assertThat(result.size()).isEqualTo(1),
                () -> assertThat(result.get(0)).isEqualTo(save)
        );
    }
}