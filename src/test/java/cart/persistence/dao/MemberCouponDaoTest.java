package cart.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.persistence.entity.CouponEntity;
import cart.persistence.entity.MemberCouponEntity;
import cart.persistence.entity.MemberEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Import(value = {MemberCouponDao.class, MemberDao.class, CouponDao.class})
class MemberCouponDaoTest extends DaoTest {

    @Autowired
    private MemberCouponDao memberCouponDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
            쿠폰_만료_시간);

        // when
        memberCouponDao.insert(사용자_쿠폰_저장_엔티티);

        // then
        final MemberCouponEntity 저장된_사용자_쿠폰_엔티티 = findMemberCouponByMemberId(저장된_져니_아이디);
        assertThat(저장된_사용자_쿠폰_엔티티)
            .extracting(MemberCouponEntity::getMemberId, MemberCouponEntity::getCouponId,
                MemberCouponEntity::getIssuedDate, MemberCouponEntity::getExpiredDate)
            .containsExactly(저장된_져니_아이디, 저장된_신규_가입_축하_쿠폰_아이디, 쿠폰_발급_시간, 쿠폰_만료_시간);
    }

    private MemberCouponEntity findMemberCouponByMemberId(final Long 저장된_져니_아이디) {
        final String sql = "SELECT * FROM member_coupon WHERE member_id = ?";
        final MemberCouponEntity 저장된_사용자_쿠폰_엔티티 = jdbcTemplate.queryForObject(sql,
            (rs, count) -> new MemberCouponEntity(
                rs.getLong("member_id"),
                rs.getLong("coupon_id"),
                rs.getTimestamp("issued_date").toLocalDateTime(),
                rs.getTimestamp("expired_date").toLocalDateTime()), 저장된_져니_아이디);
        return 저장된_사용자_쿠폰_엔티티;
    }
}
