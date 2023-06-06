package shop.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.CouponEntity;
import shop.persistence.entity.MemberEntity;
import shop.persistence.entity.OrderCouponEntity;
import shop.persistence.entity.OrderEntity;
import shop.persistence.entity.detail.OrderCouponDetail;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import({OrderCouponDao.class, OrderDao.class, CouponDao.class, MemberDao.class})
class OrderCouponDaoTest extends DaoTest {
    private static Long couponId;
    private static Long orderId;

    @Autowired
    private OrderCouponDao orderCouponDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        couponId = couponDao.insert(Data.coupon);
        Long memberId = memberDao.insertMember(Data.member);

        OrderEntity orderEntity = new OrderEntity(memberId, 10000L,
                0L, 3000, LocalDateTime.now());
        orderId = orderDao.insert(orderEntity);
    }

    @DisplayName("주문에 사용된 쿠폰을 저장할 수 있다.")
    @Test
    void insertOrderCouponTest() {
        //given
        OrderCouponEntity orderCoupon = new OrderCouponEntity(orderId, couponId);

        //when
        orderCouponDao.insert(orderCoupon);

        //then
        OrderCouponDetail orderCouponDetail = orderCouponDao.findCouponByOrderId(orderId).get();

        assertThat(orderCouponDetail.getName()).isEqualTo(Data.coupon.getName());
        assertThat(orderCouponDetail.getDiscountRate()).isEqualTo(Data.coupon.getDiscountRate());
    }

    private static class Data {
        static final MemberEntity member = new MemberEntity("쥬니", "1234");
        static final CouponEntity coupon = new CouponEntity("테스트용 쿠폰", 80,
                365, LocalDateTime.now().plusDays(1));
    }

}
