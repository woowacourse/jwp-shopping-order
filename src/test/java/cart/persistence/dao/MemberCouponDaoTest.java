package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.MemberCouponEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(value = {MemberCouponDao.class})
class MemberCouponDaoTest extends DaoTestHelper {

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    @DisplayName("사용자의 쿠폰 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();

        // when
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.of(2023, 6, 1, 13, 0, 0);
        final LocalDateTime 쿠폰_만료_시간 = 쿠폰_발급_시간.plusDays(10);
        final MemberCouponEntity 사용자_쿠폰_저장_엔티티 = new MemberCouponEntity(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디,
            쿠폰_발급_시간, 쿠폰_만료_시간, false);
        memberCouponDao.insert(사용자_쿠폰_저장_엔티티);

        // then
        final MemberCouponDto coupon = memberCouponDao.findByMemberIdAndCouponId(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디).get();
        assertThat(coupon)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate, MemberCouponDto::isUsed,
                MemberCouponDto::getExpiredAt, MemberCouponDto::getIssuedAt)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 10,
                20, false, 쿠폰_만료_시간, 쿠폰_발급_시간);
    }

    @Test
    @DisplayName("해당 회원이 쿠폰을 이미 발급받았다면 true를 반환한다.")
    void existByMemberIdAndCouponId_true() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final boolean result = memberCouponDao.existByMemberIdAndCouponId(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("해당 회원이 쿠폰을 발급받지 않았다면 false를 반환한다.")
    void existByMemberIdAndCouponId_false() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();

        // when
        final boolean result = memberCouponDao.existByMemberIdAndCouponId(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("사용자 아이디와 쿠폰 아이디로 사용자의 쿠폰이 존재하면 반환한다.")
    void findByMemberIdAndCouponId_success() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final MemberCouponDto memberCouponDto = memberCouponDao.findByMemberIdAndCouponId(저장된_져니_아이디,
            저장된_신규_가입_축하_쿠폰_아이디).get();

        // then
        assertThat(memberCouponDto)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed, MemberCouponDto::getExpiredAt, MemberCouponDto::getIssuedAt)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                10, 20, false, LocalDateTime.of(2023, 6, 1, 13, 0, 0).plusDays(10),
                LocalDateTime.of(2023, 6, 1, 13, 0, 0));
    }

    @Test
    @DisplayName("사용자 아이디와 쿠폰 아이디로 사용자의 쿠폰이 존재하지 않으면 빈 값을 반환한다.")
    void findByMemberIdAndCouponId_empty() {
        // when
        final Optional<MemberCouponDto> memberCouponDto = memberCouponDao.findByMemberIdAndCouponId(1L, 1L);

        // then
        assertThat(memberCouponDto)
            .isEmpty();
    }

    @Test
    @DisplayName("사용자 이름으로 사용자의 쿠폰 정보를 조회한다.")
    void findMyCouponsByName() {
        // given
        final long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        final Long 저장된_행운의_쿠폰_아이디 = 행운의_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_행운의_쿠폰_아이디);

        // when
        final List<MemberCouponDto> coupons = memberCouponDao.findMyCouponsByName("journey");

        // then
        assertThat(coupons).hasSize(2);
        assertThat(coupons)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed, MemberCouponDto::getExpiredAt, MemberCouponDto::getIssuedAt)
            .containsExactly(
                tuple(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                    10, 20, false, LocalDateTime.of(2023, 6, 1, 13, 0, 0).plusDays(10),
                    LocalDateTime.of(2023, 6, 1, 13, 0, 0)),
                tuple(저장된_져니_아이디, "journey", "password", 저장된_행운의_쿠폰_아이디, "행운의 쿠폰",
                    1, 10, false, LocalDateTime.of(2023, 6, 1, 13, 0, 0).plusDays(10),
                    LocalDateTime.of(2023, 6, 1, 13, 0, 0))
            );
    }

    @Test
    @DisplayName("쿠폰의 사용 정보를 1로 (사용 완료) 업데이트한다.")
    void updateUsed() {
        // given
        final long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final int updatedCount = memberCouponDao.updateUsed(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // then
        final MemberCouponDto memberCouponDto = memberCouponDao.findByMemberIdAndCouponId(저장된_져니_아이디,
            저장된_신규_가입_축하_쿠폰_아이디).get();

        assertThat(updatedCount)
            .isSameAs(1);
        assertThat(memberCouponDto)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed, MemberCouponDto::getExpiredAt, MemberCouponDto::getIssuedAt)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                10, 20, true, LocalDateTime.of(2023, 6, 1, 13, 0, 0).plusDays(10),
                LocalDateTime.of(2023, 6, 1, 13, 0, 0));
    }

    @Test
    @DisplayName("쿠폰의 사용 정보를 0으로 (사용하지 않음) 업데이트한다.")
    void updateNotUsed() {
        // given
        final long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);
        memberCouponDao.updateUsed(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final int updatedCount = memberCouponDao.updateNotUsed(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // then
        final MemberCouponDto memberCouponDto = memberCouponDao.findByMemberIdAndCouponId(저장된_져니_아이디,
            저장된_신규_가입_축하_쿠폰_아이디).get();

        assertThat(updatedCount)
            .isSameAs(1);
        assertThat(memberCouponDto)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed, MemberCouponDto::getExpiredAt, MemberCouponDto::getIssuedAt)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                10, 20, false, LocalDateTime.of(2023, 6, 1, 13, 0, 0).plusDays(10),
                LocalDateTime.of(2023, 6, 1, 13, 0, 0));
    }
}
