package cart.step2.coupon.persist;

import cart.step2.coupon.domain.CouponEntity;
import cart.step2.coupon.exception.CouponNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class CouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @DisplayName("쿠폰 사용 상태를 N으로 변경한다.")
    @Test
    void updateUsageStatus() {
        // given
        final String sql = "UPDATE coupon " +
                "SET usage_status = 'Y' " +
                "WHERE member_id = ? " +
                "AND id = ?";
        final Long memberId = 1L;
        final Long couponId = 4L;
        jdbcTemplate.update(sql, memberId, couponId);

        // when
        couponDao.updateUsageStatus(memberId, couponId);
        CouponEntity couponEntity = couponDao.findById(4L)
                .orElseThrow(() -> CouponNotFoundException.THROW);

        // then
        assertThat(couponEntity.getUsageStatus()).isEqualTo("N");
    }

    @DisplayName("couponId를 받아 단건 조회한다.")
    @Test
    void findById() {
        // given
        final Long couponId = 1L;

        // when
        CouponEntity couponEntity = couponDao.findById(couponId)
                .orElseThrow(() -> CouponNotFoundException.THROW);

        // then
        Assertions.assertAll(
                () -> assertThat(couponEntity.getId()).isEqualTo(1L),
                () -> assertThat(couponEntity.getMemberId()).isEqualTo(1L),
                () -> assertThat(couponEntity.getCouponTypeId()).isEqualTo(1L),
                () -> assertThat(couponEntity.getUsageStatus()).isEqualTo("N")
        );
    }

    @DisplayName("모든 쿠폰들을 조회한다.")
    @Test
    void findAll() {
        // given
        final Long memberId = 1L;

        // when
        List<CouponEntity> couponEntities = couponDao.findAll(memberId);

        // then
        Assertions.assertAll(
                () -> assertThat(couponEntities).extracting(CouponEntity::getMemberId)
                        .contains(1L),
                () -> assertThat(couponEntities).extracting(CouponEntity::getCouponTypeId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(couponEntities).extracting(CouponEntity::getId)
                        .contains(1L, 2L, 3L, 4L),
                () -> assertThat(couponEntities).extracting(CouponEntity::getUsageStatus)
                        .contains("N")
        );
    }

    @DisplayName("couponId를 받아 삭제한다.")
    @Test
    void deleteById() {
        // given
        final Long memberId = 1L;
        final Long couponId = 4L;

        // when
        couponDao.deleteById(couponId);
        List<CouponEntity> couponEntities = couponDao.findAll(memberId);

        // then
        assertThat(couponEntities).hasSize(3);
    }

}
