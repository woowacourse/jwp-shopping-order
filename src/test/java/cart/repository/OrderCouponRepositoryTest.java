package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.domain.Order;
import cart.domain.Product;
import cart.domain.coupon.Coupon;
import cart.domain.repository.CouponRepository;
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

    @Test
    @DisplayName("주문시 사용한 쿠폰을 저장한다.")
    void saveOrderCoupon() {
        Member member = new Member(1L, "a@a", "123");
        Coupon coupon = couponRepository.findAllCoupons().get(0);
        Order order = new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))), coupon);
        couponRepository.publishUserCoupon(member, 1L);
        Long orderId = orderRepository.saveOrder(order);
        assertDoesNotThrow(() -> orderCouponRepository.saveOrderCoupon(orderId, order));
    }

    @Test
    void deleteOrderCoupon() {
        Member member = new Member(1L, "a@a", "123");
        Coupon coupon = couponRepository.findAllCoupons().get(0);
        Order order = new Order(member, List.of(new CartItem(member, new Product("오션", 10000, "ocean.com"))), coupon);
        couponRepository.publishUserCoupon(member, 1L);
        Long orderId = orderRepository.saveOrder(order);
        assertDoesNotThrow(() -> orderCouponRepository.deleteOrderCoupon(orderId));
    }
}
