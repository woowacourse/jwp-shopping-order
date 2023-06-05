package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.MemberCouponEntity;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberCouponDao memberCouponDao;
    private Long memberId;
    private Long couponId;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
        final MemberDao memberDao = new MemberDao(jdbcTemplate);
        final CouponDao couponDao = new CouponDao(jdbcTemplate);
        memberId = memberDao.findById(1L).orElseThrow().getId();
        couponId = couponDao.findById(1L).orElseThrow().getId();
    }

    @Test
    @DisplayName("사용자의 쿠폰을 수정할 수 있다.")
    void testCreate() {
        // given
        memberCouponDao.create(couponId, memberId);
        final MemberCouponEntity updatedMemberCoupon = MemberCouponEntity.of(1L, true, memberId, couponId);

        // when
        memberCouponDao.update(updatedMemberCoupon);

        // then
        final MemberCouponEntity result = memberCouponDao.findByCouponIdAndMemberId(couponId, memberId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(result.isUsed()).isTrue();
    }
}
