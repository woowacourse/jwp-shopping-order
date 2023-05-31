package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(value = {MemberCouponDao.class, MemberDao.class, CouponDao.class})
class MemberCouponDaoTest extends DaoTest {

    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CouponDao couponDao;

    @Test
    @DisplayName("사용자의 쿠폰 정보를 저장한다.")
    void insert() {
        // given
        final Long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();

        // when
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.now();
        final LocalDateTime 쿠폰_만료_시간 = 쿠폰_발급_시간.plusDays(10);
        final MemberCouponEntity 사용자_쿠폰_저장_엔티티 = new MemberCouponEntity(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디,
            쿠폰_발급_시간, 쿠폰_만료_시간, false);
        memberCouponDao.insert(사용자_쿠폰_저장_엔티티);

        // then
        final List<MemberCouponDto> coupons = memberCouponDao.findMyCouponsByName("journey");
        assertThat(coupons).hasSize(1);
        assertThat(coupons)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate, MemberCouponDto::isUsed)
            .containsExactly(
                tuple(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 10,
                    20, false));
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
        final MemberEntity 져니 = new MemberEntity("journey", "password");
        final Long 저장된_져니_아이디 = memberDao.insert(져니);
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
        final MemberEntity 져니 = new MemberEntity("journey", "password");
        final Long 저장된_져니_아이디 = memberDao.insert(져니);
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
                MemberCouponDto::isUsed)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                    10, 20, false);
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
    @DisplayName("쿠폰 정보를 포함한 사용자 정보를 조회한다.")
    void findMyCouponsByName() {
        // given
        final long 저장된_져니_아이디 = 져니_저장();
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = 신규_가입_쿠폰_저장();
        져니_쿠폰_저장(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디);

        // when
        final List<MemberCouponDto> coupons = memberCouponDao.findMyCouponsByName("journey");

        // then
        assertThat(coupons).hasSize(1);
        assertThat(coupons)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate,
                MemberCouponDto::isUsed)
            .containsExactly(
                tuple(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                    10, 20, false));
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
                MemberCouponDto::isUsed)
            .containsExactly(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰",
                10, 20, true);
    }

    private Long 져니_저장() {
        final MemberEntity 져니 = new MemberEntity("journey", "password");
        final Long 저장된_져니_아이디 = memberDao.insert(져니);
        return 저장된_져니_아이디;
    }

    private Long 신규_가입_쿠폰_저장() {
        final LocalDateTime now = LocalDateTime.now();
        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 10, now.plusDays(10));
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);
        return 저장된_신규_가입_축하_쿠폰_아이디;
    }

    private void 져니_쿠폰_저장(final long 저장된_져니_아이디, final Long 저장된_신규_가입_축하_쿠폰_아이디) {
        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.now();
        final LocalDateTime 쿠폰_만료_시간 = 쿠폰_발급_시간.plusDays(10);
        final MemberCouponEntity 사용자_쿠폰_저장_엔티티 = new MemberCouponEntity(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디,
            쿠폰_발급_시간, 쿠폰_만료_시간, false);
        memberCouponDao.insert(사용자_쿠폰_저장_엔티티);
    }
}
