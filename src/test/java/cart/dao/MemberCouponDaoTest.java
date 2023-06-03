package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CouponEntity;
import cart.entity.MemberCouponEntity;
import cart.entity.MemberEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberCouponDao memberCouponDao;

    private Long couponId;
    private Long memberId;

    @BeforeEach
    void setUp() {
        memberCouponDao = new MemberCouponDao(jdbcTemplate);

        CouponDao couponDao = new CouponDao(jdbcTemplate);
        couponId = couponDao.save(new CouponEntity("쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.ZERO));

        MemberDao memberDao = new MemberDao(jdbcTemplate);
        memberId = memberDao.save(new MemberEntity("email@email.com", "password"));
    }

    @Test
    void 멤버_쿠폰을_저장한다() {
        // given
        MemberCouponEntity memberCoupon = new MemberCouponEntity(memberId, couponId, LocalDate.of(3000, 6, 16));

        // when
        Long id = memberCouponDao.save(memberCoupon);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 멤버_쿠폰을_id로_조회한다() {
        // given
        MemberCouponEntity memberCoupon = new MemberCouponEntity(memberId, couponId, LocalDate.of(3000, 6, 16));
        Long id = memberCouponDao.save(memberCoupon);

        // when
        Optional<MemberCouponEntity> memberCouponEntity = memberCouponDao.findById(id);

        // then
        assertThat(memberCouponEntity.get()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(memberCoupon);
    }

    @Test
    void 멤버_쿠폰을_삭제한다() {
        // given
        MemberCouponEntity memberCoupon = new MemberCouponEntity(memberId, couponId, LocalDate.of(3000, 6, 16));
        Long id = memberCouponDao.save(memberCoupon);

        // when
        memberCouponDao.deleteById(id);

        // then
        Optional<MemberCouponEntity> memberCouponEntity = memberCouponDao.findById(id);
        assertThat(memberCouponEntity).isEmpty();
    }

    @Test
    void 멤버로_멤버_쿠폰을_조회한다() {
        // given
        MemberCouponEntity memberCoupon1 = new MemberCouponEntity(memberId, couponId, LocalDate.of(3000, 6, 16));
        MemberCouponEntity memberCoupon2 = new MemberCouponEntity(memberId, couponId, LocalDate.of(3000, 6, 8));
        memberCouponDao.save(memberCoupon1);
        memberCouponDao.save(memberCoupon2);

        // when
        List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByMemberId(memberId);

        // then
        assertThat(memberCouponEntities).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(memberCoupon1, memberCoupon2));
    }
}
