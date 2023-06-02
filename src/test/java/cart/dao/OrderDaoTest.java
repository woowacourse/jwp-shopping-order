package cart.dao;

import cart.entity.CouponEntity;
import cart.entity.MemberEntity;
import cart.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class OrderDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private OrderDao orderDao;

    private CouponDao couponDao;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.memberDao = new MemberDao(jdbcTemplate);
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    void 주문을_저장한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", BigDecimal.valueOf(1000L), BigDecimal.valueOf(10000L));
        final CouponEntity coupon = couponDao.insert(couponEntity);

        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), coupon.getId(), member.getId());

        // when
        final OrderEntity order = orderDao.insert(orderEntity);

        // then
        final OrderEntity result = orderDao.findById(order.getId()).get();
        assertAll(
                () -> assertThat(result.getDeliveryFee()).isEqualTo(order.getDeliveryFee()),
                () -> assertThat(result.getMemberCouponId()).isEqualTo(order.getMemberCouponId()),
                () -> assertThat(result.getMemberId()).isEqualTo(order.getMemberId())
        );
    }

    @Test
    void 아이디에_해당하는_주문을_조회한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", BigDecimal.valueOf(1000L), BigDecimal.valueOf(10000L));
        final CouponEntity coupon = couponDao.insert(couponEntity);

        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), coupon.getId(), member.getId());
        final OrderEntity order = orderDao.insert(orderEntity);

        // when
        final OrderEntity result = orderDao.findById(order.getId()).get();

        // then
        assertAll(
                () -> assertThat(result.getDeliveryFee()).isEqualTo(order.getDeliveryFee()),
                () -> assertThat(result.getMemberCouponId()).isEqualTo(order.getMemberCouponId()),
                () -> assertThat(result.getMemberId()).isEqualTo(order.getMemberId())
        );
    }

    @Test
    void 주문을_수정한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", BigDecimal.valueOf(1000L), BigDecimal.valueOf(10000L));
        final CouponEntity coupon = couponDao.insert(couponEntity);

        final MemberEntity memberEntity = new MemberEntity("pizza@pizza.com", "password");
        final MemberEntity member = memberDao.insert(memberEntity);

        final OrderEntity orderEntity = new OrderEntity(BigDecimal.valueOf(3000L), coupon.getId(), member.getId());
        final OrderEntity order = orderDao.insert(orderEntity);
        final OrderEntity updatedOrderEntity = new OrderEntity(order.getId(), order.getDeliveryFee(), order.getMemberCouponId(), order.getMemberId());

        // when
        orderDao.update(updatedOrderEntity);

        // then
        final OrderEntity result = orderDao.findById(order.getId()).get();
        assertAll(
                () -> assertThat(result.getDeliveryFee()).isEqualTo(updatedOrderEntity.getDeliveryFee()),
                () -> assertThat(result.getMemberCouponId()).isEqualTo(updatedOrderEntity.getMemberCouponId()),
                () -> assertThat(result.getMemberId()).isEqualTo(updatedOrderEntity.getMemberId())
        );
    }

    @Test
    void 멤버_아이디에_해당하는_주문을_조회한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity("10000원 이상 1000원 할인 쿠폰", "PRICE", BigDecimal.valueOf(1000L), BigDecimal.valueOf(10000L));
        final CouponEntity coupon = couponDao.insert(couponEntity);

        final MemberEntity memberEntity1 = new MemberEntity("pizza1@pizza.com", "password");
        final MemberEntity memberEntity2 = new MemberEntity("pizza2@pizza.com", "password");
        final MemberEntity member1 = memberDao.insert(memberEntity1);
        final MemberEntity member2 = memberDao.insert(memberEntity2);

        final OrderEntity orderEntity1 = new OrderEntity(BigDecimal.valueOf(1000L), coupon.getId(), member1.getId());
        final OrderEntity orderEntity2 = new OrderEntity(BigDecimal.valueOf(2000L), coupon.getId(), member1.getId());
        final OrderEntity orderEntity3 = new OrderEntity(BigDecimal.valueOf(3000L), coupon.getId(), member2.getId());
        orderDao.insert(orderEntity1);
        orderDao.insert(orderEntity2);
        orderDao.insert(orderEntity3);

        // when
        final List<OrderEntity> result = orderDao.findByMemberId(member1.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}