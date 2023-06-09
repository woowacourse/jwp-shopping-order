package shop.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import shop.persistence.entity.MemberCouponEntity;
import shop.persistence.entity.detail.MemberCouponDetail;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({MemberDao.class, CouponDao.class, MemberCouponDao.class})
class MemberCouponDaoTest extends DaoTest {
    private static Long memberId;
    private static Long testCouponId;
    private static Long freeCouponId;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CouponDao couponDao;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @BeforeEach
    void setUp() {
        memberId = memberDao.insertMember(DaoTestFixture.member);
        testCouponId = couponDao.insert(DaoTestFixture.testCoupon);
        freeCouponId = couponDao.insert(DaoTestFixture.freeCoupon);
    }

    @DisplayName("회원의 쿠폰을 저장할 수 있다.")
    @Test
    void insertMemberCouponTest() {
        //given
        MemberCouponEntity memberCoupon = new MemberCouponEntity(memberId, testCouponId,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);

        //when
        memberCouponDao.insert(memberCoupon);

        //then
        MemberCouponDetail findMemberCoupon = memberCouponDao.findByMemberIdAndCouponId(memberId, testCouponId);

        assertThat(findMemberCoupon.getCouponName()).isEqualTo(DaoTestFixture.testCoupon.getName());
        assertThat(findMemberCoupon.getMemberName()).isEqualTo(DaoTestFixture.member.getName());
    }

    @DisplayName("회원의 모든 쿠폰을 조회할 수 있다.")
    @Test
    void findAllMemberCouponTest() {
        MemberCouponEntity memberCoupon1 = new MemberCouponEntity(memberId, testCouponId,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);
        MemberCouponEntity memberCoupon2 = new MemberCouponEntity(memberId, freeCouponId,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3), false);

        //when
        memberCouponDao.insert(memberCoupon1);
        memberCouponDao.insert(memberCoupon2);

        //then
        List<MemberCouponDetail> findMemberCoupons = memberCouponDao.findAllByMemberId(memberId);

        assertThat(findMemberCoupons.size()).isEqualTo(2);
        assertThat(findMemberCoupons).extractingResultOf("getCouponName")
                .containsExactly(DaoTestFixture.testCoupon.getName(), DaoTestFixture.freeCoupon.getName());
        assertThat(findMemberCoupons).extractingResultOf("getMemberName")
                .containsExactly(DaoTestFixture.member.getName(), DaoTestFixture.member.getName());
    }
}
