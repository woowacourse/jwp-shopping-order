package cart.persistence.dao;

import static cart.fixture.MemberCouponFixture.만료된_멤버쿠폰_엔티티;
import static cart.fixture.MemberCouponFixture.사용된_멤버쿠폰_엔티티;
import static cart.fixture.MemberCouponFixture.유효한_멤버쿠폰_엔티티;
import static cart.fixture.MemberFixture.멤버_test1_엔티티;
import static cart.fixture.MemberFixture.멤버_test2_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.fixture.CouponFixture.금액_10000원이상_1000원할인;
import cart.persistence.dto.MemberCouponDetailDTO;
import cart.persistence.dto.MemberCouponDetailWithUserDTO;
import cart.persistence.entity.MemberCouponEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class MemberCouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;

    private CouponDao couponDao;

    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        couponDao = new CouponDao(jdbcTemplate);
        memberCouponDao = new MemberCouponDao(jdbcTemplate);
    }

    @Test
    void 멤버쿠폰을_생성_및_조회한다() {
        // given
        long memberId = memberDao.create(멤버_test1_엔티티);
        long couponId = couponDao.create(금액_10000원이상_1000원할인.ENTITY);
        MemberCouponEntity memberCoupon = 유효한_멤버쿠폰_엔티티(memberId, couponId);

        // when
        long id = memberCouponDao.create(memberCoupon);
        Optional<MemberCouponDetailWithUserDTO> result = memberCouponDao.findById(id);

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getCouponEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(금액_10000원이상_1000원할인.ENTITY),
                () -> assertThat(result.get().getMemberEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(멤버_test1_엔티티),
                () -> assertThat(result.get().getMemberCouponEntity())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(memberCoupon)
        );
    }

    @Test
    void 자신의_유효한_멤버쿠폰만_조회한다() {
        // given
        long memberId = memberDao.create(멤버_test1_엔티티);
        long otherMemberId = memberDao.create(멤버_test2_엔티티);
        long couponId = couponDao.create(금액_10000원이상_1000원할인.ENTITY);
        MemberCouponEntity validCoupon = 유효한_멤버쿠폰_엔티티(memberId, couponId);
        MemberCouponEntity otherCoupon = 유효한_멤버쿠폰_엔티티(otherMemberId, couponId);
        MemberCouponEntity expiredCoupon = 만료된_멤버쿠폰_엔티티(memberId, couponId);
        MemberCouponEntity usedCoupon = 사용된_멤버쿠폰_엔티티(memberId, couponId);

        long validCouponId = memberCouponDao.create(validCoupon);
        memberCouponDao.create(otherCoupon);
        memberCouponDao.create(expiredCoupon);
        memberCouponDao.create(usedCoupon);

        // when
        List<MemberCouponDetailDTO> result = memberCouponDao.findValidByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(result).hasSize(1),
                () -> assertThat(result.get(0).getMemberCouponEntity().getId()).isEqualTo(validCouponId)
        );
    }
}
