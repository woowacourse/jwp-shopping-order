package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.dao.entity.CouponEntity;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CouponDaoTest {

    private final CouponDao couponDao;
    private List<CouponEntity> coupons;

    @Autowired
    public CouponDaoTest(final JdbcTemplate jdbcTemplate) {
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        // TODO 쿠폰 발급 기능 넣으면, 여기서 쿠폰 넣어주기?
        coupons = couponDao.findAll();
    }

    @DisplayName("쿠폰 아이디로 쿠폰을 조회한다.")
    @Test
    void findById() {
        // given
        final CouponEntity coupon = coupons.get(0);
        final Long couponId = coupon.getId();

        // when, then
        couponDao.findById(couponId)
                .ifPresentOrElse(
                        found -> assertThat(found.getId()).isEqualTo(couponId),
                        () -> Assertions.fail("coupon not exist; couponId=" + couponId));
    }

    // TODO findByIdForMember

    @DisplayName("특정 회원의 모든 쿠폰을 조회한다.")
    @Test
    void findByMember() {
        // given
        final CouponEntity coupon = coupons.get(0);
        final Long couponId = coupon.getId();

        // when
        final long memberId = coupon.getMemberId();
        final List<CouponEntity> found = couponDao.findByMember(memberId);

        // then
        for (final CouponEntity foundCoupon : found) {
            assertThat(foundCoupon.getMemberId()).isEqualTo(memberId);
        }
    }

    @DisplayName("쿠폰의 상태를 변경한다.")
    @Test
    void updateStatus() {
        // given
        final CouponEntity coupon = coupons.get(0);
        final Long couponId = coupon.getId();

        // when
        couponDao.updateStatus(new CouponEntity(
                couponId,
                coupon.getMemberId(),
                coupon.getCouponTypeId(),
                true));

        // then
        couponDao.findById(couponId)
                .ifPresentOrElse(
                        found -> assertThat(found.isUsed()).isTrue(),
                        () -> Assertions.fail("coupon not exist; couponId=" + couponId));
    }
}
