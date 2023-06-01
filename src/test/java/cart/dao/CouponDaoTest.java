package cart.dao;

import cart.dao.entity.CouponEntity;
import cart.dao.entity.CouponTypeCouponEntity;
import cart.dao.entity.CouponTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class CouponDaoTest {

    private CouponDao couponDao;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void init() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE", PreparedStatement::execute);
        couponDao = new CouponDao(jdbcTemplate, dataSource);
    }

    @Test
    void 쿠폰을_발급한다() {
        // given
        final CouponEntity couponEntity = new CouponEntity(false, 1L, 1L);

        // when
        final Long saveId = couponDao.issue(couponEntity);

        // then
        assertThat(saveId).isNotNull();
    }

    @Test
    void 회원_아이디로_쿠폰을_조회한다() {
        // when
        final List<CouponTypeCouponEntity> results = couponDao.findByMemberId(1L);

        // then
        assertThat(results).hasSize(3);
    }

    @Test
    void 쿠폰의_상태를_변경한다() {
        // when
        couponDao.changeStatus(1L, 1L, true);

        // then
        final List<CouponTypeCouponEntity> results = couponDao.findByMemberId(1L);
        assertAll(
                () -> assertThat(results).hasSize(3),
                () -> assertThat(results.get(0).getUsageStatus()).isTrue()
        );
    }

    @Test
    void 전체_쿠폰_조회한다() {
        // when
        final List<CouponTypeEntity> couponTypeEntities = couponDao.findAll();

        // then
        assertAll(
                () -> assertThat(couponTypeEntities).hasSize(4),
                () -> assertThat(couponTypeEntities.stream()
                        .map(CouponTypeEntity::getDiscountAmount)
                        .collect(toList())).containsExactly(1000, 3000, 5000, 10000)
        );
    }

    @Test
    void 쿠폰을_회원아이디와_쿠폰아이디로_조회한다() {
        // when
        final CouponTypeCouponEntity couponTypeCouponEntity = couponDao.findByCouponIdAndMemberId(1L, 1L).get();

        // then
        assertThat(couponTypeCouponEntity.getCouponId()).isNotNull();
    }

    @Test
    void 쿠폰을_삭제한다() {
        // given
        final CouponTypeCouponEntity couponTypeCouponEntity = couponDao.findByCouponIdAndMemberId(1L, 1L).get();

        // when
        couponDao.deleteCoupon(couponTypeCouponEntity.getCouponId());

        // then
        assertThat(couponDao.findByCouponIdAndMemberId(1L, 1L)).isNotPresent();
    }
}
