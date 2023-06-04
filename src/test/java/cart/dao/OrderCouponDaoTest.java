package cart.dao;

import cart.entity.MemberCouponEntity;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
class OrderCouponDaoTest {

    private OrderCouponDao orderCouponDao;
    private MemberCouponDao memberCouponDao;
    private OrderDao orderDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
        orderDao = new OrderDao(jdbcTemplate);
    }

    @Test
    @DisplayName("주문시 사용한 쿠폰을 저장한다.")
    void saveOrderCoupon() {
        Long userCouponId = memberCouponDao.save(new MemberCouponEntity(1L, 1L, true));
        Long orderId = orderDao.save(new OrderEntity(1L, 1000, 1000));

        orderCouponDao.save(orderId, userCouponId);

        assertThat(orderCouponDao.checkByOrderId(orderId)).isTrue();
    }

    @Test
    @DisplayName("주문시 사용한 쿠폰을 삭제한다.")
    void deleteOrderCoupon() {
        Long userCouponId = memberCouponDao.save(new MemberCouponEntity(1L, 1L, true));
        Long orderId = orderDao.save(new OrderEntity(1L, 1000, 1000));
        orderCouponDao.save(orderId, userCouponId);

        orderCouponDao.deleteByOrderId(orderId);

        assertThat(orderCouponDao.checkByOrderId(orderId)).isFalse();
    }

    @Test
    @DisplayName("주문 id로 사용한 쿠폰을 찾을 수 있다.")
    void findByOrderId() {
        Long userCouponId = memberCouponDao.save(new MemberCouponEntity(1L, 1L, true));
        Long orderId = orderDao.save(new OrderEntity(1L, 1000, 1000));
        orderCouponDao.save(orderId, userCouponId);

        assertDoesNotThrow(() -> orderCouponDao.findIdByOrderId(orderId));
    }

    @Test
    @DisplayName("주문 id로 주문 시 사용한 쿠폰 유무를 확인할 수 있다.")
    void checkOrderCouponByOrderId() {
        Long userCouponId = memberCouponDao.save(new MemberCouponEntity(1L, 1L, true));
        Long orderId = orderDao.save(new OrderEntity(1L, 1000, 1000));
        orderCouponDao.save(orderId, userCouponId);

        assertThat(orderCouponDao.checkByOrderId(orderId)).isTrue();
    }
}
