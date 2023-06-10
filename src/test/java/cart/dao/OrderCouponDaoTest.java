package cart.dao;

import cart.domain.coupon.RateStrategy;
import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.fixture.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.stream.Collectors;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private OrderItemDao orderItemDao;
    private OrderDao orderDao;
    private OrderCouponDao orderCouponDao;
    private MemberCouponDao memberCouponDao;
    private Long orderId;
    private Long orderItemId;
    private Long memberCouponId;

    @BeforeEach
    void setUp() {
        orderDao = new OrderDao(jdbcTemplate);
        orderItemDao = new OrderItemDao(jdbcTemplate);
        orderCouponDao = new OrderCouponDao(jdbcTemplate);
        memberCouponDao = new MemberCouponDao(jdbcTemplate);

        orderId = orderDao.create(1L, 3000);
        orderItemId = orderItemDao.create(orderId, Fixture.치킨, 10);
        memberCouponId = memberCouponDao.create(1L, 1L);
    }

    @Test
    void create() {
        orderCouponDao.create(orderItemId, memberCouponId);
        List<MemberCouponEntity> memberCoupons = orderCouponDao.findByOrderItemId(orderItemId);

        List<CouponEntity> coupons = memberCoupons.stream().map(MemberCouponEntity::getCoupon).collect(Collectors.toList());
        Assertions.assertThat(coupons)
                .contains(new CouponEntity(1L, "오픈 기념 쿠폰", new RateStrategy().getStrategyName().name(), 10));
    }
}
