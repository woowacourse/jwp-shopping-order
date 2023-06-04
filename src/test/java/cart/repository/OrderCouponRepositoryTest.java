package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
import cart.domain.repository.MemberCouponRepository;
import cart.domain.repository.OrderCouponRepository;
import cart.domain.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class OrderCouponRepositoryTest {
    @Autowired
    private OrderCouponRepository orderCouponRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Test
    @DisplayName("주문시 사용한 쿠폰을 저장한다.")
    void saveOrderCoupon() {
        Member member = new Member(1L, "a@a", "123");
        Coupon coupon = couponRepository.findAll().get(0);
        Long memberCouponId = couponRepository.save(member, coupon.getId());
        Coupon memberCoupon = memberCouponRepository.findAvailableCouponByIdAndMemberId(member, memberCouponId);
        Order order = new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))), memberCoupon);
        Long orderId = orderRepository.save(order);
        assertDoesNotThrow(() -> orderCouponRepository.save(orderId, order));
    }

    @Test
    void deleteOrderCoupon() {
        Member member = new Member(1L, "a@a", "123");
        Coupon coupon = couponRepository.findAll().get(0);
        Long memberCouponId = couponRepository.save(member, coupon.getId());
        Coupon memberCoupon = memberCouponRepository.findAvailableCouponByIdAndMemberId(member, memberCouponId);
        Order order = new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))), memberCoupon);
        Long orderId = orderRepository.save(order);
        assertDoesNotThrow(() -> orderCouponRepository.deleteByOrderId(orderId));
    }
}
