package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.persistence.dao.dto.MemberCouponDto;
import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;
import java.util.List;
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
        final MemberEntity 져니 = new MemberEntity("journey", "password");
        final Long 저장된_져니_아이디 = memberDao.insert(져니);

        final CouponEntity 신규_가입_축하_쿠폰 = new CouponEntity("신규 가입 축하 쿠폰", 20, 10, LocalDateTime.now().plusDays(365));
        final Long 저장된_신규_가입_축하_쿠폰_아이디 = couponDao.insert(신규_가입_축하_쿠폰);

        final LocalDateTime 쿠폰_발급_시간 = LocalDateTime.now();
        final LocalDateTime 쿠폰_만료_시간 = 쿠폰_발급_시간.plusDays(10);
        final MemberCouponEntity 사용자_쿠폰_저장_엔티티 = new MemberCouponEntity(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디, 쿠폰_발급_시간,
            쿠폰_만료_시간, false);

        // when
        memberCouponDao.insert(사용자_쿠폰_저장_엔티티);

        // then
        final List<MemberCouponDto> coupons = memberDao.findMyCouponsByName(져니.getName());
        assertThat(coupons).hasSize(1);
        assertThat(coupons)
            .extracting(MemberCouponDto::getMemberId, MemberCouponDto::getMemberName,
                MemberCouponDto::getMemberPassword, MemberCouponDto::getCouponId, MemberCouponDto::getCouponName,
                MemberCouponDto::getCouponPeriod, MemberCouponDto::getDiscountRate, MemberCouponDto::isUsed)
            .containsExactly(
                tuple(저장된_져니_아이디, "journey", "password", 저장된_신규_가입_축하_쿠폰_아이디, "신규 가입 축하 쿠폰", 10,
                    20, false));
    }
}
